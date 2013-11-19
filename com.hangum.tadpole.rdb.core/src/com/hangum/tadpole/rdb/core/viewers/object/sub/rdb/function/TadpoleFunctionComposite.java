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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.function;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectExecuteProcedureAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.OracleObjectCompileAction;
import com.hangum.tadpole.rdb.core.dialog.procedure.ExecuteProcedureDialog;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ProcedureFunctionComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure.ProcedureFunctionLabelProvicer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure.ProcedureFunctionViewFilter;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;
import com.hangum.tadpole.sql.util.executer.ProcedureExecuterManager;
import com.hangum.tadpole.sql.util.tables.TableUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Function composite
 * 
 * @author hangum
 *
 */
public class TadpoleFunctionComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleFunctionComposite.class);
	
	private TableViewer functionTableViewer;
	private ProcedureFunctionComparator functionComparator;
	private List<ProcedureFunctionDAO> showFunction;
	private ProcedureFunctionViewFilter functionFilter;

	private ObjectCreatAction creatAction_Function;
	private ObjectDeleteAction deleteAction_Function;
	private ObjectRefreshAction refreshAction_Function;
	private GenerateViewDDLAction viewDDLAction;
	private ObjectExecuteProcedureAction executeAction_Procedure;
	private OracleObjectCompileAction objectCompileAction;
	
	/**
	 * function composite
	 * 
	 * @param site
	 * @param tabFolderObject
	 * @param userDB
	 */
	public TadpoleFunctionComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {
		CTabItem  tbtmFunctions = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmFunctions.setText("Functions");; //$NON-NLS-1$

		Composite compositeIndexes = new Composite(tabFolderObject, SWT.NONE);
		tbtmFunctions.setControl(compositeIndexes);
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
		functionTableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableList = functionTableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		functionComparator = new ProcedureFunctionComparator();
		functionTableViewer.setSorter(functionComparator);
		functionComparator.setColumn(0);

		createProcedureFunctionColumn(functionTableViewer, functionComparator);

		functionTableViewer.setLabelProvider(new ProcedureFunctionLabelProvicer());
		functionTableViewer.setContentProvider(new ArrayContentProvider());
		
		functionTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection iss = (IStructuredSelection) event.getSelection();
				if(!iss.isEmpty()) {
					ProcedureFunctionDAO procedureDAO = (ProcedureFunctionDAO)iss.getFirstElement();
					
					ProcedureExecuterManager pm = new ProcedureExecuterManager(getUserDB(), procedureDAO);
					if(pm.isExecuted(procedureDAO, getUserDB())) {
						ExecuteProcedureDialog epd = new ExecuteProcedureDialog(null, getUserDB(), procedureDAO);
						epd.open();
					}
				}	// end iss.isempty
			}
		});


		functionFilter = new ProcedureFunctionViewFilter();
		functionTableViewer.addFilter(functionFilter);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		createMenu();
	}
	
	private void createMenu() {
		creatAction_Function = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.FUNCTIONS, "Function"); //$NON-NLS-1$
		deleteAction_Function = new ObjectDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.FUNCTIONS, "Function"); //$NON-NLS-1$
		refreshAction_Function = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.FUNCTIONS, "Function"); //$NON-NLS-1$
	
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.FUNCTIONS, "View"); //$NON-NLS-1$

		executeAction_Procedure = new ObjectExecuteProcedureAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.FUNCTIONS, "Function"); //$NON-NLS-1$
		objectCompileAction = new OracleObjectCompileAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.FUNCTIONS, "Function"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
					manager.add(creatAction_Function);
					manager.add(deleteAction_Function);
				}
				manager.add(refreshAction_Function);
				
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(viewDDLAction);
				manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				manager.add(executeAction_Procedure);
				if (DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT){
					manager.add(objectCompileAction);
				}
			}
		});

		functionTableViewer.getTable().setMenu(menuMgr.createContextMenu(functionTableViewer.getTable()));
		getSite().registerContextMenu(menuMgr, functionTableViewer);
	}

	/**
	 * text filter
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		functionFilter.setSearchText(textSearch);
		functionTableViewer.refresh();
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if (showFunction != null) showFunction.clear();
		functionTableViewer.setInput(showFunction);
		functionTableViewer.refresh();

		creatAction_Function.setUserDB(getUserDB());
		deleteAction_Function.setUserDB(getUserDB());
		refreshAction_Function.setUserDB(getUserDB());
		
		viewDDLAction.setUserDB(getUserDB());

		executeAction_Procedure.setUserDB(getUserDB());
		objectCompileAction.setUserDB(getUserDB());
	}
	
	/**
	 * function 정보를 최신으로 갱신 합니다.
	 */
	public void refreshFunction(final UserDBDAO userDB, boolean boolRefresh) {
		if(!boolRefresh) if(showFunction != null) return;
		this.userDB = userDB;
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showFunction = sqlClient.queryForList("functionList", userDB.getDb()); //$NON-NLS-1$

			functionTableViewer.setInput(showFunction);
			functionTableViewer.refresh();
			
			TableUtil.packTable(functionTableViewer.getTable());

		} catch (Exception e) {
			logger.error("showFunction refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_81, errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * tableviewer
	 * 
	 * @return
	 */
	public TableViewer getTableviewer() {
		return functionTableViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		functionFilter.setSearchText(searchText);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		creatAction_Function.dispose();
		deleteAction_Function.dispose();
		refreshAction_Function.dispose();
		viewDDLAction.dispose();
		executeAction_Procedure.dispose();
		objectCompileAction.dispose();
	}

}
