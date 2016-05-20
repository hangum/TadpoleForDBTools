/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.index;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBIndexDAO;
import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBIndexFieldDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;

/**
 * MongoDB indexes composite
 * 
 * @author hangum
 *
 */
public class TadpoleMongoDBIndexesComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleMongoDBIndexesComposite.class);
	
	// index
	private TableViewer tableViewer;
	private ObjectComparator indexComparator;
	private List<MongoDBIndexDAO> listIndexes;
	private MongoDBIndexesViewFilter indexFilter;
	
	// index detail field
	private TableViewer tableColumnViewer;

	private ObjectCreatAction creatAction_Index;
	private ObjectDropAction deleteAction_Index;
	private ObjectRefreshAction refreshAction_Index;

	/**
	 * indexes info
	 * 
	 * @param site
	 * @param tabFolderObject
	 * @param userDB
	 */
	public TadpoleMongoDBIndexesComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {
		CTabItem tbtmIndex = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmIndex.setText(Messages.get().TadpoleMongoDBIndexesComposite_0);
		tbtmIndex.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.INDEXES.name());

		Composite compositeIndexes = new Composite(tabFolderObject, SWT.NONE);
		tbtmIndex.setControl(compositeIndexes);
		GridLayout gl_compositeIndexes = new GridLayout(1, false);
		gl_compositeIndexes.verticalSpacing = 2;
		gl_compositeIndexes.horizontalSpacing = 2;
		gl_compositeIndexes.marginHeight = 2;
		gl_compositeIndexes.marginWidth = 2;
		compositeIndexes.setLayout(gl_compositeIndexes);

		SashForm sashForm = new SashForm(compositeIndexes, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableList = tableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);
		tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					MongoDBIndexDAO tableDAO = (MongoDBIndexDAO)is.getFirstElement();

					if(tableDAO == null) return;
					
					tableColumnViewer.setInput(tableDAO.getListIndexField());
					tableColumnViewer.refresh();
					
					TableUtil.packTable(tableColumnViewer.getTable());

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});

		indexComparator = new DefaultComparator();
		tableViewer.setSorter(indexComparator);
		indexComparator.setColumn(0);

		createMongoDBIndexesColumn(tableViewer, indexComparator);

		tableViewer.setLabelProvider(new MongoDBIndexesLabelProvicer());
		tableViewer.setContentProvider(new ArrayContentProvider());

		indexFilter = new MongoDBIndexesViewFilter();
		tableViewer.addFilter(indexFilter);

		createMenu();
		
		// index detail column
		tableColumnViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableColumn = tableColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);

		TableViewerColumn tableColumn = new TableViewerColumn(tableColumnViewer, SWT.LEFT);
		tableColumn.getColumn().setText(Messages.get().Name);
		tableColumn.getColumn().setWidth(130);
		tableColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				MongoDBIndexFieldDAO table = (MongoDBIndexFieldDAO) element;
				return table.getName();
			}
		});
		
		tableColumn = new TableViewerColumn(tableColumnViewer, SWT.LEFT);
		tableColumn.getColumn().setText(Messages.get().TadpoleMongoDBIndexesComposite_2);
		tableColumn.getColumn().setWidth(100);
		tableColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				MongoDBIndexFieldDAO table = (MongoDBIndexFieldDAO) element;
				
				return table.getOrder().equals("1")?"Ascending": //$NON-NLS-1$ //$NON-NLS-2$
					table.getOrder().equals("-1")?"Descending":"Geospatial"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		});

		tableColumnViewer.setContentProvider(new ArrayContentProvider());

		sashForm.setWeights(new int[] {7, 3});
	}

	/**
	 * mongodb index column
	 * 
	 * @param tableViewer
	 * @param indexComparator
	 */
	private void createMongoDBIndexesColumn(TableViewer tv, ObjectComparator comparator) {
		String[] name = {Messages.get().TadpoleMongoDBIndexesComposite_8, Messages.get().TadpoleMongoDBIndexesComposite_9,Messages.get().TadpoleMongoDBIndexesComposite_10};
		int[] size = {120, 70, 70};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tv, comparator, tableColumn.getColumn(), i));
		}
	}

	/**
	 * create menu
	 * 
	 */
	private void createMenu() {
		if(getUserDB() == null) return;
		creatAction_Index = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.INDEXES, Messages.get().TadpoleMongoDBIndexesComposite_11);
		deleteAction_Index = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.INDEXES, Messages.get().TadpoleMongoDBIndexesComposite_12);
		refreshAction_Index = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.INDEXES, Messages.get().Refresh);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.add(creatAction_Index);
		menuMgr.add(deleteAction_Index);
		menuMgr.add(refreshAction_Index);

		tableViewer.getTable().setMenu(menuMgr.createContextMenu(tableViewer.getTable()));
		getSite().registerContextMenu(menuMgr, tableViewer);
	}

	/**
	 * init action
	 */
	public void initAction() {
		if (listIndexes != null) listIndexes.clear();
		refreshViewer();

		if(getUserDB() == null) return;
		creatAction_Index.setUserDB(getUserDB());
		deleteAction_Index.setUserDB(getUserDB());
		refreshAction_Index.setUserDB(getUserDB());
	}
	
	/**
	 * refresh viewer
	 */
	private void refreshViewer() {
		tableViewer.setInput(listIndexes);
		tableViewer.refresh();
		TableUtil.packTable(tableViewer.getTable());
	}
	
	/**
	 * index 정보를 최신으로 갱신 합니다.
	 */
	public void refreshIndexes(final UserDBDAO userDB, boolean boolRefresh) {
		if(!boolRefresh) if(listIndexes != null) return;
		
		this.userDB = userDB;
		try {
			listIndexes = MongoDBQuery.listAllIndex(userDB);
			refreshViewer();

		} catch (Exception e) {
			logger.error("index refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ExplorerViewer_1, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * filter
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		indexFilter.setSearchText(textSearch);
		tableViewer.refresh();		
	}
	
	/**
	 * table viewer
	 * @return
	 */
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		indexFilter.setSearchText(searchText);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		if(creatAction_Index != null) creatAction_Index.dispose();
		if(deleteAction_Index != null) deleteAction_Index.dispose();
		if(refreshAction_Index != null) refreshAction_Index.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		// TODO Auto-generated method stub
		
	}
}
