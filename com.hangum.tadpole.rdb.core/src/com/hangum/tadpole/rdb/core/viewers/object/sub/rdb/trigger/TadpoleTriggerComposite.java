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
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.OracleObjectCompileAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TriggerComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.sql.dao.mysql.TriggerDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;
import com.hangum.tadpole.sql.util.tables.TableUtil;
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
	
	private TableViewer triggerTableViewer;
	private TriggerComparator triggerComparator;
	private List<TriggerDAO> showTrigger;
	private TriggerViewFilter triggerFilter;

	private ObjectCreatAction creatAction_Trigger;
	private ObjectDeleteAction deleteAction_Trigger;
	private ObjectRefreshAction refreshAction_Trigger;
	private GenerateViewDDLAction viewDDLAction;
	private OracleObjectCompileAction objectCompileAction;

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
		triggerTableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableList = triggerTableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		triggerComparator = new TriggerComparator();
		triggerTableViewer.setSorter(triggerComparator);
		triggerComparator.setColumn(0);

		createTriggerColumn(triggerTableViewer, triggerComparator);

		triggerTableViewer.setLabelProvider(new TriggerLabelProvicer());
		triggerTableViewer.setContentProvider(new ArrayContentProvider());

		triggerFilter = new TriggerViewFilter();
		triggerTableViewer.addFilter(triggerFilter);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		createMenu();
	}
	
	private void createMenu() {
		creatAction_Trigger = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$
		deleteAction_Trigger = new ObjectDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$
		refreshAction_Trigger = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$
		
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TRIGGERS, "View"); //$NON-NLS-1$
		objectCompileAction = new OracleObjectCompileAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
					manager.add(creatAction_Trigger);
					manager.add(deleteAction_Trigger);
				}
					
				manager.add(refreshAction_Trigger);
				
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(viewDDLAction);
				if (DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT){
					manager.add(objectCompileAction);
				}
				
			}
		});

		triggerTableViewer.getTable().setMenu(menuMgr.createContextMenu(triggerTableViewer.getTable()));
		getSite().registerContextMenu(menuMgr, triggerTableViewer);
	}

	/**
	 * trigger filter text
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		triggerFilter.setSearchText(textSearch);
		triggerTableViewer.refresh();
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if (showTrigger != null) showTrigger.clear();
		triggerTableViewer.setInput(showTrigger);
		triggerTableViewer.refresh();

		creatAction_Trigger.setUserDB(getUserDB());
		deleteAction_Trigger.setUserDB(getUserDB());
		refreshAction_Trigger.setUserDB(getUserDB());
		
		viewDDLAction.setUserDB(getUserDB());
		objectCompileAction.setUserDB(getUserDB());
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

			triggerTableViewer.setInput(showTrigger);
			triggerTableViewer.refresh();
			
			TableUtil.packTable(triggerTableViewer.getTable());

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
		return triggerTableViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		triggerFilter.setSearchText(searchText);		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		creatAction_Trigger.dispose();
		deleteAction_Trigger.dispose();
		refreshAction_Trigger.dispose();
		viewDDLAction.dispose();	
		objectCompileAction.dispose();
	}
}
