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
package com.hangum.tadpole.rdb.core.viewers.object.sub.trigger;

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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
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
 * trigger composite
 * 
 * @author hangum
 *
 */
public class TadpoleTriggerComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleTriggerComposite.class);
	
	private TableViewer tableViewer;
	private ObjectComparator triggerComparator;
	private List showTrigger;
	private TriggerViewFilter triggerFilter;

	private ObjectCreatAction creatAction_Trigger;
	private ObjectDeleteAction deleteAction_Trigger;
	private ObjectRefreshAction refreshAction_Trigger;

	/**
	 * trigger composite
	 * 
	 * @param site
	 * @param tabFolderObject
	 * @param userDB
	 */
	public TadpoleTriggerComposite(IWorkbenchPartSite site, TabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		
		createTrigger(tabFolderObject);
	}
	
	/**
	 * Trigger 정의
	 */
	private void createTrigger(final TabFolder tabFolderObject) {
		TabItem tbtmTriggers = new TabItem(tabFolderObject, SWT.NONE);
		tbtmTriggers.setText("Triggers"); //$NON-NLS-1$

		Composite compositeIndexes = new Composite(tabFolderObject, SWT.NONE);
		tbtmTriggers.setControl(compositeIndexes);
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
		tableViewer.setUseHashlookup(true);
		Table tableTableList = tableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		triggerComparator = new ObjectComparator();
		tableViewer.setSorter(triggerComparator);

		createTriggerColumn(tableViewer, triggerComparator);

		tableViewer.setLabelProvider(new TriggerLabelProvicer());
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(showTrigger);

		triggerFilter = new TriggerViewFilter();
		tableViewer.addFilter(triggerFilter);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		creatAction_Trigger = new ObjectCreatAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$
		deleteAction_Trigger = new ObjectDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$
		refreshAction_Trigger = new ObjectRefreshAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(creatAction_Trigger);
				manager.add(deleteAction_Trigger);
				manager.add(refreshAction_Trigger);
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(tableViewer.getTable());
		tableViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, tableViewer);
	}

	/**
	 * trigger filter text
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		triggerFilter.setSearchText(textSearch);
		tableViewer.refresh();
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if (showTrigger != null) showTrigger.clear();
		tableViewer.setInput(showTrigger);
		tableViewer.refresh();

		creatAction_Trigger.setUserDB(getUserDB());
		deleteAction_Trigger.setUserDB(getUserDB());
		refreshAction_Trigger.setUserDB(getUserDB());
	}
	
	/**
	 * trigger 정보를 최신으로 갱신 합니다.
	 */
	public void refreshTrigger(final UserDBDAO userDB) {
		this.userDB = userDB;
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTrigger = sqlClient.queryForList("triggerList", userDB.getDb()); //$NON-NLS-1$

			tableViewer.setInput(showTrigger);
			tableViewer.refresh();

		} catch (Exception e) {
			logger.error("showTrigger refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_76, errStatus); //$NON-NLS-1$
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
		triggerFilter.setSearchText(searchText);		
	}
}
