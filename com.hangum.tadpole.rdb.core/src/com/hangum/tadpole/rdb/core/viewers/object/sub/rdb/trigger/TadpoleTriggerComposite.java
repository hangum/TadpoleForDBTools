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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.trigger;

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
import com.hangum.tadpole.define.DB_Define;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.system.permission.PermissionChecker;
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
	public TadpoleTriggerComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {		
		CTabItem tbtmTriggers = new CTabItem(tabFolderObject, SWT.NONE);
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

		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableList = tableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		triggerComparator = new ObjectComparator();
		tableViewer.setSorter(triggerComparator);

		createTriggerColumn(tableViewer, triggerComparator);

		tableViewer.setLabelProvider(new TriggerLabelProvicer());
		tableViewer.setContentProvider(new ArrayContentProvider());
//		tableViewer.setInput(showTrigger);

		triggerFilter = new TriggerViewFilter();
		tableViewer.addFilter(triggerFilter);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		createMenu();
	}
	
	private void createMenu() {
		creatAction_Trigger = new ObjectCreatAction(getSite().getWorkbenchWindow(), DB_Define.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$
		deleteAction_Trigger = new ObjectDeleteAction(getSite().getWorkbenchWindow(), DB_Define.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$
		refreshAction_Trigger = new ObjectRefreshAction(getSite().getWorkbenchWindow(), DB_Define.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if(PermissionChecker.isShow(strUserType, userDB)) {
					manager.add(creatAction_Trigger);
					manager.add(deleteAction_Trigger);
				}
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
	public void refreshTrigger(final UserDBDAO userDB, boolean boolRefresh) {
		if(!boolRefresh) if(showTrigger != null) return;
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
