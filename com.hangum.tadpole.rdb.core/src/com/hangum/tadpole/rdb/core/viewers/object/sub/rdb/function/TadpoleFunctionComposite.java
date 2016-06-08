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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.DBSystemSchema;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectExecuteProcedureAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.OracleObjectCompileAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ProcedureFunctionComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure.ProcedureFunctionLabelProvicer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure.ProcedureFunctionViewFilter;

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
	
	private CTabItem  tbtmFunctions;
	private TableViewer functionTableViewer;
	private ProcedureFunctionComparator functionComparator;
	private List<ProcedureFunctionDAO> showFunction;
	private ProcedureFunctionViewFilter functionFilter;

	private ObjectCreatAction creatAction_Function;
	private ObjectDropAction dropAction_Function;
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
		tbtmFunctions = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmFunctions.setText(Messages.get().Functions);
		tbtmFunctions.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS.name());

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
					ObjectExecuteProcedureAction action = new ObjectExecuteProcedureAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), OBJECT_TYPE.FUNCTIONS, Messages.get().TadpoleFunctionComposite_4);
					action.run(iss, getUserDB(), OBJECT_TYPE.FUNCTIONS);
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
		if(getUserDB() == null) return;
		
		creatAction_Function = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS, Messages.get().TadpoleFunctionComposite_1);
		dropAction_Function = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS, Messages.get().TadpoleFunctionComposite_2);
		refreshAction_Function = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS, Messages.get().Refresh);
	
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS, Messages.get().TadpoleFunctionComposite_4);

		executeAction_Procedure = new ObjectExecuteProcedureAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS, Messages.get().TadpoleFunctionComposite_5);
		objectCompileAction = new OracleObjectCompileAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS, Messages.get().TadpoleFunctionComposite_6);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		if(PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
			if(!isDDLLock()) {
				menuMgr.add(creatAction_Function);
				menuMgr.add(dropAction_Function);
				menuMgr.add(new Separator());
			}
		}
		menuMgr.add(refreshAction_Function);
		
		menuMgr.add(new Separator());
		menuMgr.add(viewDDLAction);
		if (getUserDB().getDBDefine() != DBDefine.ALTIBASE_DEFAULT) { 
			menuMgr.add(new Separator());
			menuMgr.add(executeAction_Procedure);
		}
		
		if (getUserDB().getDBDefine() == DBDefine.ORACLE_DEFAULT || getUserDB().getDBDefine() == DBDefine.TIBERO_DEFAULT){
			menuMgr.add(new Separator());
			menuMgr.add(objectCompileAction);
		}

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

		if(getUserDB() == null) return;
		creatAction_Function.setUserDB(getUserDB());
		dropAction_Function.setUserDB(getUserDB());
		refreshAction_Function.setUserDB(getUserDB());
		
		viewDDLAction.setUserDB(getUserDB());

		executeAction_Procedure.setUserDB(getUserDB());
		objectCompileAction.setUserDB(getUserDB());
	}
	
	/**
	 * function 정보를 최신으로 갱신 합니다.
	 */
	public void refreshFunction(final UserDBDAO userDB, boolean boolRefresh, String strObjectName) {
		if(!boolRefresh) if(showFunction != null) return;
		this.userDB = userDB;
		
		try {
			showFunction = DBSystemSchema.getFunctionList(userDB);

			functionTableViewer.setInput(showFunction);
			functionTableViewer.refresh();
			
			TableUtil.packTable(functionTableViewer.getTable());
			
			// select tabitem
			getTabFolderObject().setSelection(tbtmFunctions);
			
			selectDataOfTable(strObjectName);
		} catch (Exception e) {
			logger.error("showFunction refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ExplorerViewer_81, errStatus); //$NON-NLS-1$
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
		
		if(creatAction_Function != null) creatAction_Function.dispose();
		if(dropAction_Function != null) dropAction_Function.dispose();
		if(refreshAction_Function != null) refreshAction_Function.dispose();
		if(viewDDLAction != null) viewDDLAction.dispose();
		if(executeAction_Procedure != null) executeAction_Procedure.dispose();
		if(objectCompileAction != null) objectCompileAction.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableviewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<showFunction.size(); i++) {
			ProcedureFunctionDAO tableDao = (ProcedureFunctionDAO)getTableviewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, tableDao.getName())) {
				getTableviewer().setSelection(new StructuredSelection(getTableviewer().getElementAt(i)), true);
				break;
			}
		}
	}
}
