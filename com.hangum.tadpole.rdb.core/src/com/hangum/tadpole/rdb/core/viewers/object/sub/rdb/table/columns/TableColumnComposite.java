/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.columns;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableColumnDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableColumnModifyAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableColumnSelectionAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableColumnComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.ColumnCommentEditorSupport;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TableColumnLabelprovider;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TadpoleTableComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * Table column
 * 
 * @author hangum
 *
 */
public class TableColumnComposite extends AbstractTableComposite {
	private static final Logger logger = Logger.getLogger(TableColumnComposite.class);
	
	private TableViewer tableColumnViewer;
	private List<TableColumnDAO> showTableColumns = new ArrayList<>();
	private ObjectComparator tableColumnComparator;
	
	private AbstractObjectAction tableColumnSelectionAction;
	private AbstractObjectAction tableColumnDeleteAction;
	private AbstractObjectAction tableColumnModifyAction;
	
	/**
	 * Create the composite.
	 * @param parentFolder
	 * @param style
	 */
	public TableColumnComposite(TadpoleTableComposite tableComposite, CTabFolder parentFolder, int style) {
		super(tableComposite, parentFolder, style);
		
		CTabItem tbtmTable = new CTabItem(parentFolder, SWT.NONE);
		tbtmTable.setText(Messages.get().Columns);
		tbtmTable.setData(Messages.get().Columns);
		
		Composite compositeColumn = new Composite(parentFolder, SWT.NONE);
		tbtmTable.setControl(compositeColumn);
		GridLayout gl_compositeTables = new GridLayout(1, false);
		gl_compositeTables.verticalSpacing = 2;
		gl_compositeTables.horizontalSpacing = 2;
		gl_compositeTables.marginHeight = 2;
		gl_compositeTables.marginWidth = 2;
		compositeColumn.setLayout(gl_compositeTables);

		SashForm sashForm = new SashForm(compositeColumn, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tableColumnViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.Move);
		tableColumnViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if (null != is) {
					TableColumnDAO tableDAO = (TableColumnDAO) is.getFirstElement();
					FindEditorAndWriteQueryUtil.runAtPosition(StringUtils.trim(tableDAO.getField()));
				}
			}
		});
		Table tableTableColumn = tableColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);
		
		tableColumnComparator = new TableColumnComparator();
		tableColumnViewer.setSorter(tableColumnComparator);

		createTableColumne();
		createTableColumnMenu();

		tableColumnViewer.setContentProvider(new ArrayContentProvider());
		tableColumnViewer.setLabelProvider(new TableColumnLabelprovider(getTableComposite().getTableListViewer(), getTableComposite().getTableDecorationExtension()));
		
		sashForm.setWeights(new int[] { 1 });
	}
	
	public List<TableColumnDAO> getShowTableColumns() {
		return showTableColumns;
	}

	/**
	 * table table column
	 */
	protected void createTableColumne() {
		String[] name 		= {Messages.get().Field, Messages.get().Type, Messages.get().Key, Messages.get().Comment, Messages.get().TadpoleTableComposite_8, Messages.get().Default, Messages.get().TadpoleTableComposite_10};
		int[] size 			= {120, 90, 100, 50, 50, 50, 50};
		
		// table column tooltip
		ColumnViewerToolTipSupport.enableFor(tableColumnViewer);
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tableColumnViewer, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tableColumn, i));
			tableColumn.getColumn().setMoveable(true);
			tableColumn.setEditingSupport(new ColumnCommentEditorSupport(getTableComposite().getTableListViewer(), tableColumnViewer, getUserDB(), i));
		}
	}
	
	/**
	 * initialize action
	 */
	public void initAction() {
		if(getUserDB() == null) return;
		
		// table column
		tableColumnSelectionAction.setUserDB(getUserDB());
		tableColumnDeleteAction.setUserDB(getUserDB());
		tableColumnModifyAction.setUserDB(getUserDB());
	}
	
	public TableViewer getTableColumnViewer() {
		return tableColumnViewer;
	}
	
	/**
	 * selection adapter
	 * 
	 * @param tableColumn
	 * @param i
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewerColumn tableColumn, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableColumnComparator.setColumn(index);
				
				tableColumnViewer.getTable().setSortDirection(tableColumnComparator.getDirection());
				tableColumnViewer.getTable().setSortColumn(tableColumn.getColumn());
				tableColumnViewer.refresh();
			}
		};
		
		return selectionAdapter;
	}
	
	/**
	 * refresh table column
	 */
	public void refreshTableColumn(TableViewer tableListViewer) {
		// 테이블의 컬럼 목록을 출력합니다.
		try {
			IStructuredSelection is = (IStructuredSelection) tableListViewer.getSelection();
			Object objDAO = is.getFirstElement();

			if (objDAO != null) {
				TableDAO tableDao = (TableDAO) objDAO;

				getTableComposite().setSelectTableName(tableDao.getName());
				showTableColumns = TadpoleObjectQuery.getTableColumns(getUserDB(), tableDao);
			} else {
				showTableColumns = new ArrayList<>();
				getTableComposite().setSelectTableName("");
			}
		} catch (Exception e) {
			logger.error("get table column", e); //$NON-NLS-1$
			
			// initialize table columns
			showTableColumns.clear();

			// show error message
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
		} finally {
			tableColumnViewer.setInput(showTableColumns);
			tableColumnComparator = new TableColumnComparator();
			tableColumnViewer.setSorter(tableColumnComparator);
			tableColumnViewer.refresh();
			TableUtil.packTable(tableColumnViewer.getTable());
		}
	}
	
	/**
	 * create table column menu
	 */
	private void createTableColumnMenu() {
		if(getUserDB() == null) return;
		
		tableColumnDeleteAction = new TableColumnDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Table"); //$NON-NLS-1$
		tableColumnSelectionAction = new TableColumnSelectionAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Table"); //$NON-NLS-1$
		tableColumnModifyAction = new TableColumnModifyAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Table"); //$NON-NLS-1$
		
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		if (getUserDB().getDBDefine() == DBDefine.MYSQL_DEFAULT || getUserDB().getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			menuMgr.add(tableColumnModifyAction);
			menuMgr.add(tableColumnDeleteAction);
			menuMgr.add(new Separator());
		}
		menuMgr.add(tableColumnSelectionAction);

		tableColumnViewer.getTable().setMenu(menuMgr.createContextMenu(tableColumnViewer.getTable()));
		getSite().registerContextMenu(menuMgr, tableColumnViewer);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		if(tableColumnSelectionAction != null) tableColumnSelectionAction.dispose();
		if(tableColumnDeleteAction != null) tableColumnDeleteAction.dispose();
		if(tableColumnModifyAction != null) tableColumnModifyAction.dispose();
	}

}
