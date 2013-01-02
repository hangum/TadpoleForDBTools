/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.index;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.dao.mongodb.MongoDBIndexDAO;
import com.hangum.tadpole.dao.mongodb.MongoDBIndexFieldDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.index.IndexesViewFilter;

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
	private List listIndexes;
	private IndexesViewFilter indexFilter;
	
	// index detail field
	private TableViewer tableColumnViewer;

//	private ObjectCreatAction creatAction_Index;
	private ObjectDeleteAction deleteAction_Index;
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
		tbtmIndex.setText("Indexes"); //$NON-NLS-1$

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

		// index table viewer
		tableViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableList = tableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);
		tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					MongoDBIndexDAO tableDAO = (MongoDBIndexDAO)is.getFirstElement();

					tableColumnViewer.setInput(tableDAO.getListIndexField());
					tableColumnViewer.refresh();

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), "Error", e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});

		indexComparator = new DefaultComparator();
		tableViewer.setSorter(indexComparator);

		createMongoDBIndexesColumn(tableViewer, indexComparator);

		tableViewer.setLabelProvider(new MongoDBIndexesLabelProvicer());
		tableViewer.setContentProvider(new ArrayContentProvider());

		indexFilter = new IndexesViewFilter();
		tableViewer.addFilter(indexFilter);

		createMenu();
		
		// index detail column
		tableColumnViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableColumn = tableColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);

		TableViewerColumn tableColumn = new TableViewerColumn(tableColumnViewer, SWT.LEFT);
		tableColumn.getColumn().setText("name");
		tableColumn.getColumn().setWidth(130);
		tableColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				MongoDBIndexFieldDAO table = (MongoDBIndexFieldDAO) element;
				return table.getName();
			}
		});
		
		tableColumn = new TableViewerColumn(tableColumnViewer, SWT.LEFT);
		tableColumn.getColumn().setText("order");
		tableColumn.getColumn().setWidth(100);
		tableColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				MongoDBIndexFieldDAO table = (MongoDBIndexFieldDAO) element;
				return table.getOrder()==1?"Ascending":"Descending";
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
		String[] name = {"Collection Name", "Index Name","Unique"};
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
//		creatAction_Index = new ObjectCreatAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.INDEXES, "Index"); //$NON-NLS-1$
		deleteAction_Index = new ObjectDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.INDEXES, "Index"); //$NON-NLS-1$
		refreshAction_Index = new ObjectRefreshAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.INDEXES, "Index"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
//				manager.add(creatAction_Index);
				manager.add(deleteAction_Index);
				manager.add(refreshAction_Index);
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(tableViewer.getTable());
		tableViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, tableViewer);

	}

	/**
	 * init action
	 */
	public void initAction() {
		if (listIndexes != null) listIndexes.clear();
		tableViewer.setInput(listIndexes);
		tableViewer.refresh();

//		creatAction_Index.setUserDB(getUserDB());
		deleteAction_Index.setUserDB(getUserDB());
		refreshAction_Index.setUserDB(getUserDB());
	}
	
	/**
	 * index 정보를 최신으로 갱신 합니다.
	 */
	public void refreshIndexes(final UserDBDAO userDB, boolean boolRefresh) {
		if(!boolRefresh) if(listIndexes != null) return;
		
		this.userDB = userDB;
		try {
			listIndexes = MongoDBQuery.listAllIndex(userDB);
			tableViewer.setInput(listIndexes);
			tableViewer.refresh();

		} catch (Exception e) {
			logger.error("index refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_1, errStatus); //$NON-NLS-1$
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
}
