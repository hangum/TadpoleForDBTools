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
package com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.serversidescript;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditor;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditorInput;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbSJavaScriptAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;

/**
 * MongoDB ServerSide JavaScirpt composite
 * 
 * @author hangum
 *
 */
public class TadpoleMongoDBJavaScriptComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleMongoDBJavaScriptComposite.class);
	
	private TableViewer tableViewer;
	private ObjectComparator javascriptComparator;
	private List<MongoDBServerSideJavaScriptDAO> listJavaScript;
	private MongoJavaScriptViewFilter javascriptFilter;
	

	private ObjectCreatAction creatActionJS;
	private ObjectDropAction deleteActionJS;
	private ObjectRefreshAction refreshActionJS;
	private ObjectMongodbSJavaScriptAction serverJavaScript;

	/**
	 * ServerSide JavaScirpt
	 * 
	 * @param site
	 * @param tabFolderObject
	 * @param userDB
	 */
	public TadpoleMongoDBJavaScriptComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {
		CTabItem tbtmIndex = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmIndex.setText("Javascript"); //$NON-NLS-1$
		tbtmIndex.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.JAVASCRIPT.name());

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
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					MongoDBServerSideJavaScriptDAO mDBSJSDAO = (MongoDBServerSideJavaScriptDAO)is.getFirstElement();

					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
					try {
						ServerSideJavaScriptEditorInput input = new ServerSideJavaScriptEditorInput(userDB, mDBSJSDAO);
						page.openEditor(input, ServerSideJavaScriptEditor.ID, false);
						
					} catch (PartInitException e) {
						logger.error("Mongodb javascirpt", e); //$NON-NLS-1$
						
						Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
						ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().TadpoleMongoDBJavaScriptComposite_2, errStatus); //$NON-NLS-1$
					}

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});

		javascriptComparator = new DefaultComparator();
		tableViewer.setSorter(javascriptComparator);
		javascriptComparator.setColumn(0);

		createMongoDBIndexesColumn(tableViewer, javascriptComparator);

		tableViewer.setLabelProvider(new ServerSideJavascriptLabelPrivider());
		tableViewer.setContentProvider(new ArrayContentProvider());

		javascriptFilter = new MongoJavaScriptViewFilter();
		tableViewer.addFilter(javascriptFilter);

		createMenu();
		
		sashForm.setWeights(new int[] {1});
	}
	
	/**
	 * mongodb java script column
	 * 
	 * @param tableViewer
	 * @param indexComparator
	 */
	private void createMongoDBIndexesColumn(TableViewer tv, ObjectComparator comparator) {
		String[] name = {Messages.get().Name, Messages.get().TadpoleMongoDBJavaScriptComposite_4};
		int[] size = {120, 200};

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
		
		creatActionJS = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JAVASCRIPT, Messages.get().TadpoleMongoDBJavaScriptComposite_5);
		deleteActionJS = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JAVASCRIPT, Messages.get().TadpoleMongoDBJavaScriptComposite_6);
		refreshActionJS = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JAVASCRIPT, Messages.get().Refresh);
		serverJavaScript = new ObjectMongodbSJavaScriptAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JAVASCRIPT, Messages.get().TadpoleMongoDBJavaScriptComposite_8);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.add(creatActionJS);
		menuMgr.add(deleteActionJS);
		menuMgr.add(refreshActionJS);
		
		menuMgr.add(new Separator());
		menuMgr.add(serverJavaScript);

		tableViewer.getTable().setMenu(menuMgr.createContextMenu(tableViewer.getTable()));
		getSite().registerContextMenu(menuMgr, tableViewer);
	}

	/**
	 * init action
	 */
	public void initAction() {
		if (listJavaScript != null) listJavaScript.clear();
		refreshViewer();

		if(getUserDB() == null) return;
		creatActionJS.setUserDB(getUserDB());
		deleteActionJS.setUserDB(getUserDB());
		refreshActionJS.setUserDB(getUserDB());
		
		serverJavaScript.setUserDB(getUserDB());
	}
	
	/**
	 * refresh viewer
	 */
	private void refreshViewer() {
		tableViewer.setInput(listJavaScript);
		tableViewer.refresh();
		TableUtil.packTable(tableViewer.getTable());
	}
	
	/**
	 * JavaScript 정보를 최신으로 갱신 합니다.
	 */
	public void refreshJavaScript(final UserDBDAO userDB, boolean boolRefresh) {
		if(!boolRefresh) if(listJavaScript != null) return;
		
		this.userDB = userDB;
		try {
			listJavaScript = MongoDBQuery.listAllJavaScript(userDB);
			refreshViewer();

		} catch (Exception e) {
			logger.error("javascript refresh", e); //$NON-NLS-1$
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
		javascriptFilter.setSearchText(textSearch);
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
		javascriptFilter.setSearchText(searchText);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		if(creatActionJS != null) creatActionJS.dispose();
		if(deleteActionJS != null) deleteActionJS.dispose();
		if(refreshActionJS != null) refreshActionJS.dispose();
		if(serverJavaScript != null) serverJavaScript.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
	}
}
