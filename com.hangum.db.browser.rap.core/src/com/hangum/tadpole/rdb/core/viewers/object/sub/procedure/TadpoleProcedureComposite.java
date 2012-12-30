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
package com.hangum.tadpole.rdb.core.viewers.object.sub.procedure;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
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

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB procedure composite
 * 
 * @author hangum
 *
 */
public class TadpoleProcedureComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleProcedureComposite.class);

	private TableViewer tableViewer;
	private ObjectComparator procedureComparator;
	private List showProcedure;
	private ProcedureFunctionViewFilter procedureFilter;

	private ObjectCreatAction creatAction_Procedure;
	private ObjectDeleteAction deleteAction_Procedure;
	private ObjectRefreshAction refreshAction_Procedure;

	/**
	 * procedure 
	 * 
	 * @param site
	 * @param tabFolderObject
	 * @param userDB
	 */
	public TadpoleProcedureComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {
		CTabItem tbtmProcedures = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmProcedures.setText("Procedures"); //$NON-NLS-1$

		Composite compositeIndexes = new Composite(tabFolderObject, SWT.NONE);
		tbtmProcedures.setControl(compositeIndexes);
		GridLayout gl_compositeIndexes = new GridLayout(1, false);
		gl_compositeIndexes.verticalSpacing = 2;
		gl_compositeIndexes.horizontalSpacing = 2;
		gl_compositeIndexes.marginHeight = 2;
		gl_compositeIndexes.marginWidth = 2;
		compositeIndexes.setLayout(gl_compositeIndexes);

		SashForm sashForm = new SashForm(compositeIndexes, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// procedure table viewer
		tableViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableList = tableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		procedureComparator = new ObjectComparator();
		tableViewer.setSorter(procedureComparator);

		createProcedureFunctionColumn(tableViewer, procedureComparator);

		tableViewer.setLabelProvider(new ProcedureFunctionLabelProvicer());
		tableViewer.setContentProvider(new ArrayContentProvider());
//		tableViewer.setInput(showProcedure);

		procedureFilter = new ProcedureFunctionViewFilter();
		tableViewer.addFilter(procedureFilter);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		createMenu();
	}
	
	private void createMenu() {
		creatAction_Procedure = new ObjectCreatAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.PROCEDURES, "Procedure"); //$NON-NLS-1$
		deleteAction_Procedure = new ObjectDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.PROCEDURES, "Procedure"); //$NON-NLS-1$
		refreshAction_Procedure = new ObjectRefreshAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.PROCEDURES, "Procedure"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(creatAction_Procedure);
				manager.add(deleteAction_Procedure);
				manager.add(refreshAction_Procedure);
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(tableViewer.getTable());
		tableViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, tableViewer);
	}

	/**
	 * viewer filter
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		procedureFilter.setSearchText(textSearch);
		tableViewer.refresh();
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if (showProcedure != null) showProcedure.clear();
		tableViewer.setInput(showProcedure);
		tableViewer.refresh();

		creatAction_Procedure.setUserDB(getUserDB());
		deleteAction_Procedure.setUserDB(getUserDB());
		refreshAction_Procedure.setUserDB(getUserDB());
	}
	
	/**
	 * procedure 정보를 최신으로 갱신 합니다.
	 */
	public void refreshProcedure(final UserDBDAO userDB, boolean boolRefresh) {
		if(!boolRefresh) if(showProcedure != null) return;
		this.userDB = userDB;
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showProcedure = sqlClient.queryForList("procedureList", userDB.getDb()); //$NON-NLS-1$

			tableViewer.setInput(showProcedure);
			tableViewer.refresh();

		} catch (Exception e) {
			logger.error("showProcedure refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_71, errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * get tableviewer
	 * @return
	 */
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		procedureFilter.setSearchText(searchText);
	}
}
