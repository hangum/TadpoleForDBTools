/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.editor.auth.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.hangum.tadpole.engine.query.dao.system.TableFilterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_TableColumnFilter;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 *  SQLAudit
 *  	Table, column filter dialog
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 1.
 *
 */
public class TableColumnFilterDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(TableColumnFilterDialog.class);
	
	private UserDBDAO userDB;
	private TableViewer tvTableFilter;
	private List<TableFilterDAO> listTableColumns = new ArrayList<TableFilterDAO>();
	
	private ToolItem tltmDelete;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public TableColumnFilterDialog(Shell parentShell, final UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText("Table, Column Filter dialog"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		ToolBar toolBarMain = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmRefresh = new ToolItem(toolBarMain, SWT.NONE);
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		tltmRefresh.setText("Refresh");
		
		ToolItem tltmAdd = new ToolItem(toolBarMain, SWT.NONE);
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddTableFilterDialog dialog = new AddTableFilterDialog(getShell(), userDB);
				if(Dialog.OK == dialog.open()) initData();
			}
		});
		tltmAdd.setText("Add");
		
		tltmDelete = new ToolItem(toolBarMain, SWT.NONE);
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteFilter();
			}
		});
		tltmDelete.setText("Delete");
		tltmDelete.setEnabled(false);
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tvTableFilter = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tvTableFilter.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				tltmDelete.setEnabled(true);
			}
		});
		Table tableTableFilter = tvTableFilter.getTable();
		tableTableFilter.setLinesVisible(true);
		tableTableFilter.setHeaderVisible(true);
		tableTableFilter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createTableFilterColumns();
		
		tvTableFilter.setContentProvider(new ArrayContentProvider());
		tvTableFilter.setLabelProvider(new TableFilterLabelProvider());
		tvTableFilter.setInput(listTableColumns);
		
		initData();

		return container;
	}
	
	/**
	 * delete filter
	 */
	private void deleteFilter() {
		IStructuredSelection iss = (IStructuredSelection)tvTableFilter.getSelection();
		if(iss.isEmpty()) return;
		
		TableFilterDAO tableFilterDao = (TableFilterDAO)iss.getFirstElement();
		if(MessageDialog.openConfirm(getShell(), "Confirm", "Do you want to delete filter?")) {
			try {
				TadpoleSystem_TableColumnFilter.deleteTableColumnFilters(tableFilterDao);
				initData();
			} catch (Exception e) {
				logger.error("delete tablefilter exception", e);
			}
		}
	}
	
	/**
	 * init data
	 */
	private void initData() {
		listTableColumns.clear();
		
		try {
			listTableColumns = TadpoleSystem_TableColumnFilter.getTableColumnFilters(userDB.getSeq());
			tvTableFilter.setInput(listTableColumns);
		} catch (Exception e) {
			logger.error("find table, filter exception", e);
		}
	}

	/**
	 * Create table filter 
	 */
	private void createTableFilterColumns() {
		String[] columns = {"Name", "Table Lock", "Column Names", "Description", "Create_time"};
		int[] columnsSize = {100, 50, 300, 200, 100};
	
		for(int i=0; i<columns.length; i++) {
			TableViewerColumn tvColName = new TableViewerColumn(tvTableFilter, SWT.NONE);
			TableColumn tbName = tvColName.getColumn();
			tbName.setWidth(columnsSize[i]);
			tbName.setText(columns[i]);
		}
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 445);
	}

}

/**
 * create table filter label provider
 */
class TableFilterLabelProvider extends LabelProvider implements ITableLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		TableFilterDAO tableFilterDao = (TableFilterDAO)element;
		
		switch(columnIndex) {
		case 0: return tableFilterDao.getTable_name();
		case 1: return tableFilterDao.getTable_lock();
		case 2: return tableFilterDao.getColumn_names();
		case 3: return tableFilterDao.getDescription();
		case 4: return tableFilterDao.getCreate_time().toLocaleString();
		}
		return "*** Please check column : not set columns ***";
	}
	
}