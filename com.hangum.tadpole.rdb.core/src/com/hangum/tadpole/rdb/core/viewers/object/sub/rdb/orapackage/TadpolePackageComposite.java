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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.orapackage;

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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.executer.ProcedureExecuterManager;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectExecuteProcedureAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.OracleObjectCompileAction;
import com.hangum.tadpole.rdb.core.dialog.procedure.ExecuteProcedureDialog;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ProcedureFunctionComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure.ProcedureFunctionLabelProvicer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure.ProcedureFunctionViewFilter;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure.TadpoleProcedureComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB procedure composite
 * 
 * @author hangum
 * 
 */
public class TadpolePackageComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleProcedureComposite.class);

	private CTabItem tbtmPackage;
	/** select table name */
	private String selectPackageName = ""; //$NON-NLS-1$

	private TableViewer packageTableViewer;
	private ProcedureFunctionComparator packageComparator;
	private List<ProcedureFunctionDAO> showPackage;
	private ProcedureFunctionViewFilter packageFilter;

	private ObjectCreatAction creatAction_Package;
	private ObjectDropAction dropAction_Package;
	private ObjectRefreshAction refreshAction_Package;
	private GenerateViewDDLAction viewDDLAction;
	private ObjectExecuteProcedureAction executeAction_Package;
	private OracleObjectCompileAction objectCompileAction;

	// column info
	private TableViewer packageProcFuncViewer;
	private ObjectComparator packageProcFuncComparator;
	private List showPackageProcFuncColumns;

	/**
	 * procedure
	 * 
	 * @param site
	 * @param tabFolderObject
	 * @param userDB
	 */
	public TadpolePackageComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}

	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmPackage = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmPackage.setText(Messages.get().Package);
		tbtmPackage.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.PACKAGES.name());

		Composite compositePackages = new Composite(tabFolderObject, SWT.NONE);
		tbtmPackage.setControl(compositePackages);
		GridLayout gl_compositePackages = new GridLayout(1, false);
		gl_compositePackages.verticalSpacing = 2;
		gl_compositePackages.horizontalSpacing = 2;
		gl_compositePackages.marginHeight = 2;
		gl_compositePackages.marginWidth = 2;
		compositePackages.setLayout(gl_compositePackages);

		SashForm sashForm = new SashForm(compositePackages, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		packageTableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableList = packageTableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);
		
		packageTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {

				// 인덱스 디테일한 정보를 확인할동안은 블럭으로 만들어 놓습니다.
				if (userDB.getDBDefine() == DBDefine.SQLite_DEFAULT ||
						userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT ||
						userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT
				)  return;
				
				if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDB.getIs_showtables())) return;
				
				// 테이블의 컬럼 목록을 출력합니다.
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					Object packageDAO = is.getFirstElement();

					if (packageDAO != null) {
						ProcedureFunctionDAO oraclePackage = (ProcedureFunctionDAO) packageDAO;

						if (selectPackageName.equals(oraclePackage.getName())) return;
						selectPackageName = oraclePackage.getName();

						SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
						
						showPackageProcFuncColumns = sqlClient.queryForList("packageBodyList", selectPackageName); //$NON-NLS-1$

					} else
						showPackageProcFuncColumns = null;

					packageProcFuncViewer.setInput(showPackageProcFuncColumns);
					packageProcFuncViewer.refresh();
					TableUtil.packTable(packageProcFuncViewer.getTable());

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});		

		packageComparator = new ProcedureFunctionComparator();
		packageTableViewer.setSorter(packageComparator);
		packageComparator.setColumn(0);

		createProcedureFunctionColumn(packageTableViewer, packageComparator);

		packageTableViewer.setLabelProvider(new ProcedureFunctionLabelProvicer());
		packageTableViewer.setContentProvider(new ArrayContentProvider());

		packageFilter = new ProcedureFunctionViewFilter();
		packageTableViewer.addFilter(packageFilter);

		// columns
		packageProcFuncViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableColumn = packageProcFuncViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);
		
		packageProcFuncComparator = new DefaultComparator();
		packageProcFuncViewer.setSorter(packageProcFuncComparator);

		createProcedureFunctionListColumne(packageProcFuncViewer);

		packageProcFuncViewer.setContentProvider(new ArrayContentProvider());
		packageProcFuncViewer.setLabelProvider(new PackageProcFuncLabelprovider());

		
		sashForm.setWeights(new int[] { 1, 1 });

		// creat action
		createMenu();
	}

	private void createMenu() {
		if(getUserDB() == null) return;
		
		creatAction_Package = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PACKAGES, Messages.get().TadpolePackageComposite_3);
		dropAction_Package = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PACKAGES, Messages.get().TadpolePackageComposite_4);
		refreshAction_Package = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PACKAGES, Messages.get().Refresh);

		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PACKAGES, Messages.get().ViewDDL);
		executeAction_Package = new ObjectExecuteProcedureAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PACKAGES , Messages.get().TadpolePackageComposite_7);
		objectCompileAction = new OracleObjectCompileAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.PACKAGES, Messages.get().TadpolePackageComposite_8);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		if (PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
			if(!isDDLLock()) {
				menuMgr.add(creatAction_Package);
				menuMgr.add(dropAction_Package);
				menuMgr.add(new Separator());
			}
			menuMgr.add(refreshAction_Package);

			menuMgr.add(new Separator());
			menuMgr.add(viewDDLAction);
		}

		if (getUserDB().getDBDefine() == DBDefine.ORACLE_DEFAULT || getUserDB().getDBDefine() == DBDefine.TIBERO_DEFAULT){
			menuMgr.add(new Separator());
			menuMgr.add(objectCompileAction);
		}
		packageTableViewer.getTable().setMenu(menuMgr.createContextMenu(packageTableViewer.getTable()));
		getSite().registerContextMenu(menuMgr, packageTableViewer);
		
		// package procedure/function list sub menu
		final MenuManager subMenuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		subMenuMgr.add(executeAction_Package);

		packageProcFuncViewer.getTable().setMenu(subMenuMgr.createContextMenu(packageProcFuncViewer.getTable()));
		getSite().registerContextMenu(subMenuMgr, packageProcFuncViewer);
	}

	/**
	 * selection adapter
	 * 
	 * @param packageProcFuncColumn
	 * @param i
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewerColumn packageProcFuncColumn, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				packageProcFuncComparator.setColumn(index);
				
				packageProcFuncViewer.getTable().setSortDirection(packageProcFuncComparator.getDirection());
				packageProcFuncViewer.getTable().setSortColumn(packageProcFuncColumn.getColumn());
				
				packageProcFuncViewer.refresh();
			}
		};
		
		return selectionAdapter;
	}

	/**
	 * package procedure function list
	 */
	protected void createProcedureFunctionListColumne(final TableViewer tv) {
		String[] name = {Messages.get().Type, Messages.get().Name, Messages.get().Overload};
		int[] size = {120, 300, 120};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn packageProcFuncColumn = new TableViewerColumn(tv, SWT.LEFT);
			packageProcFuncColumn.getColumn().setText(name[i]);
			packageProcFuncColumn.getColumn().setWidth(size[i]);
			packageProcFuncColumn.getColumn().addSelectionListener(getSelectionAdapter(packageProcFuncColumn, i));
		}
		
		packageProcFuncViewer.addDoubleClickListener(new IDoubleClickListener() {
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
	}

	/**
	 * viewer filter
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		packageFilter.setSearchText(textSearch);
		packageTableViewer.refresh();
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if (showPackage != null) showPackage.clear();
		packageTableViewer.setInput(showPackage);
		packageTableViewer.refresh();

		if(getUserDB() == null) return;
		creatAction_Package.setUserDB(getUserDB());
		dropAction_Package.setUserDB(getUserDB());
		refreshAction_Package.setUserDB(getUserDB());
		executeAction_Package.setUserDB(getUserDB());

		viewDDLAction.setUserDB(getUserDB());
		objectCompileAction.setUserDB(getUserDB());
	}

	/**
	 * procedure 정보를 최신으로 갱신 합니다.
	 * @param strObjectName 
	 */
	public void refreshPackage(final UserDBDAO userDB, boolean boolRefresh, String strObjectName) {
		if (!boolRefresh) if (showPackage != null) return;
		this.userDB = userDB;

		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showPackage = sqlClient.queryForList("packageList", userDB.getDb()); //$NON-NLS-1$

			packageTableViewer.setInput(showPackage);
			packageTableViewer.refresh();
			
			TableUtil.packTable(packageTableViewer.getTable());
			
			// select tabitem
			getTabFolderObject().setSelection(tbtmPackage);
			selectDataOfTable(strObjectName);
		} catch (Exception e) {
			logger.error("showPackage refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ExplorerViewer_71, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * get tableviewer
	 * 
	 * @return
	 */
	public TableViewer getPackageTableViewer() {
		return packageTableViewer;
	}

	public TableViewer getProcFuncTableViewer() {
		return this.packageProcFuncViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		packageFilter.setSearchText(searchText);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		if(creatAction_Package != null) creatAction_Package.dispose();
		if(dropAction_Package != null) dropAction_Package.dispose();
		if(refreshAction_Package != null) refreshAction_Package.dispose();
		if(viewDDLAction != null) viewDDLAction.dispose();
		if(executeAction_Package != null) executeAction_Package.dispose();
		if(objectCompileAction != null) objectCompileAction.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getProcFuncTableViewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<showPackage.size(); i++) {
			ProcedureFunctionDAO tableDao = (ProcedureFunctionDAO)getProcFuncTableViewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, tableDao.getName())) {
				getProcFuncTableViewer().setSelection(new StructuredSelection(getProcFuncTableViewer().getElementAt(i)), true);
				break;
			}
		}
	}	
}