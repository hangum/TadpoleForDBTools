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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure;

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
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectExecuteProcedureAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.OracleObjectCompileAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ProcedureFunctionComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;

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

	private CTabItem tbtmProcedures;
	
	private TableViewer procedureTableViewer;
	private ProcedureFunctionComparator procedureComparator;
	private List<ProcedureFunctionDAO> showProcedure;
	private ProcedureFunctionViewFilter procedureFilter;

	private ObjectCreatAction creatAction_Procedure;
	private ObjectDropAction dropAction_Procedure;
	private ObjectRefreshAction refreshAction_Procedure;
	private AbstractObjectSelectAction viewDDLAction;
	private AbstractObjectSelectAction executeAction_Procedure;
	private AbstractObjectSelectAction objectCompileAction;

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
		tbtmProcedures = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmProcedures.setText(Messages.get().Procedures);
		tbtmProcedures.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES.name());

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

		// SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		procedureTableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableList = procedureTableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);
		procedureTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection iss = (IStructuredSelection) event.getSelection();
				if(!iss.isEmpty()) {
					ObjectExecuteProcedureAction action = new ObjectExecuteProcedureAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), OBJECT_TYPE.PROCEDURES, Messages.get().Refresh);
					action.run(iss, getUserDB(), OBJECT_TYPE.PROCEDURES);
				}	// end iss.isempty
			}
		});

		procedureComparator = new ProcedureFunctionComparator();
		procedureTableViewer.setSorter(procedureComparator);
		procedureComparator.setColumn(0);

		createProcedureFunctionColumn(procedureTableViewer, procedureComparator);

		procedureTableViewer.setLabelProvider(new ProcedureFunctionLabelProvicer());
		procedureTableViewer.setContentProvider(new ArrayContentProvider());

		procedureFilter = new ProcedureFunctionViewFilter();
		procedureTableViewer.addFilter(procedureFilter);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		createMenu();
	}

	private void createMenu() {
		if(getUserDB() == null) return;
		creatAction_Procedure = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES, Messages.get().TadpoleProcedureComposite_1);
		dropAction_Procedure = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES, Messages.get().TadpoleProcedureComposite_3);
		refreshAction_Procedure = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES, Messages.get().Refresh);

		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES, Messages.get().ViewDDL);

		executeAction_Procedure = new ObjectExecuteProcedureAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES, Messages.get().TadpoleProcedureComposite_6);
		objectCompileAction = new OracleObjectCompileAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES, Messages.get().TadpoleProcedureComposite_7);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		if (PermissionChecker.isShow(getUserRoleType(), userDB)) {
			if(!isDDLLock()) {
				menuMgr.add(creatAction_Procedure);
				menuMgr.add(dropAction_Procedure);
				menuMgr.add(new Separator());
			}
		}
		menuMgr.add(refreshAction_Procedure);

		menuMgr.add(new Separator());
		menuMgr.add(viewDDLAction);
		if (userDB.getDBDefine() != DBDefine.ALTIBASE_DEFAULT) { 
			menuMgr.add(new Separator());
			menuMgr.add(executeAction_Procedure);
		}
		if (userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT){
			menuMgr.add(new Separator());
			menuMgr.add(objectCompileAction);
		}

		procedureTableViewer.getTable().setMenu(menuMgr.createContextMenu(procedureTableViewer.getTable()));
		getSite().registerContextMenu(menuMgr, procedureTableViewer);
	}

	/**
	 * viewer filter
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		procedureFilter.setSearchText(textSearch);
		procedureTableViewer.refresh();
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if (showProcedure != null) showProcedure.clear();
		
		procedureTableViewer.setInput(showProcedure);
		procedureTableViewer.refresh();

		if(getUserDB() == null) return;
		creatAction_Procedure.setUserDB(getUserDB());
		dropAction_Procedure.setUserDB(getUserDB());
		refreshAction_Procedure.setUserDB(getUserDB());
		executeAction_Procedure.setUserDB(getUserDB());
		objectCompileAction.setUserDB(getUserDB());

		viewDDLAction.setUserDB(getUserDB());
	}

	/**
	 * procedure 정보를 최신으로 갱신 합니다.
	 * @param strObjectName 
	 */
	public void refreshProcedure(final UserDBDAO userDB, boolean boolRefresh, String strObjectName) {
		if (!boolRefresh) {
			if (showProcedure != null) return;
		}
		
		this.userDB = userDB;

		try {
			showProcedure = DBSystemSchema.getProcedure(userDB);
			procedureTableViewer.setInput(showProcedure);
			procedureTableViewer.refresh();
			
			TableUtil.packTable(procedureTableViewer.getTable());
			
			// select tabitem
			getTabFolderObject().setSelection(tbtmProcedures);

			selectDataOfTable(strObjectName);
		} catch (Exception e) {
			logger.error("showProcedure refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ExplorerViewer_71, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * get tableviewer
	 * 
	 * @return
	 */
	public TableViewer getTableViewer() {
		return procedureTableViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		procedureFilter.setSearchText(searchText);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		if(creatAction_Procedure != null) creatAction_Procedure.dispose();
		if(dropAction_Procedure != null) dropAction_Procedure.dispose();
		if(refreshAction_Procedure != null) refreshAction_Procedure.dispose();
		if(viewDDLAction != null) viewDDLAction.dispose();
		if(executeAction_Procedure != null) executeAction_Procedure.dispose();
		if(objectCompileAction != null) objectCompileAction.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableViewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<showProcedure.size(); i++) {
			ProcedureFunctionDAO tableDao = (ProcedureFunctionDAO)getTableViewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, tableDao.getName())) {
				getTableViewer().setSelection(new StructuredSelection(getTableViewer().getElementAt(i)), true);
				break;
			}
		}		
	}
}