/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilrir - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.java;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJavaDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectAlterAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.OracleObjectCompileAction;
import com.hangum.tadpole.rdb.core.dialog.java.CreateJavaDialog;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Oracle java composite
 * 
 * @author nilriri
 * 
 */
public class TadpoleJavaComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleJavaComposite.class);

	private CTabItem tbtmJava;

	// table info
	private TableViewer javaListViewer;
	private List<OracleJavaDAO> showJava = new ArrayList<OracleJavaDAO>();
	private DefaultTableColumnFilter javaFilter;

	// column info
	private TableViewer javaColumnViewer;

	private ObjectCreatAction creatAction_Java;
	private ObjectAlterAction alterAction_Java;
	private AbstractObjectAction dropAction_Java;
	private AbstractObjectAction refreshAction_Java;

	private OracleObjectCompileAction compileAction;

	/**
	 * Create the composite.
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public TadpoleJavaComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);

		createWidget(tabFolderObject);
	}

	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmJava = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmJava.setText("Java");
		tbtmJava.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.JAVA.name());

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmJava.setControl(compositeTables);
		GridLayout gl_compositeTables = new GridLayout(1, false);
		gl_compositeTables.verticalSpacing = 2;
		gl_compositeTables.horizontalSpacing = 2;
		gl_compositeTables.marginHeight = 2;
		gl_compositeTables.marginWidth = 2;
		compositeTables.setLayout(gl_compositeTables);
		compositeTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		SashForm sashForm = new SashForm(compositeTables, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		javaListViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		javaListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (null != is) {
						OracleJavaDAO javaDao = (OracleJavaDAO) is.getFirstElement();
						CreateJavaDialog epd = new CreateJavaDialog(null, userDB, javaDao);
						epd.open();
						refreshJava(userDB, true, "");
					}
				} catch (Exception e) {
					logger.error("Open detail information view!", e);
				}
			}
		});

		Table tableTableList = javaListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		createJavaMenu();
		createJavaListColumns();

		javaListViewer.setInput(showJava);
		javaListViewer.refresh();

		javaFilter = new DefaultTableColumnFilter();
		javaListViewer.addFilter(javaFilter);
	}

	private void createJavaListColumns() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
		new TableViewColumnDefine("OBJECT_NAME", "Name", 120, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("SCHEMA_NAME", "Owner", 80, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("OBJECT_TYPE", "Type", 80, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("CREATED", "Created", 80, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("LAST_DDL_TIME", "Lst DDL", 80, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("STATUS", "Status", 80, SWT.LEFT) // //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(javaListViewer, tableColumnDef);

		javaListViewer.setContentProvider(new ArrayContentProvider());
		javaListViewer.setLabelProvider(new DefaultLabelProvider(javaListViewer));

	}

	/**
	 * create Table menu
	 */
	private void createJavaMenu() {
		if (getUserDB() == null) return;

		creatAction_Java = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JAVA, Messages.get().CreateJava);
		alterAction_Java = new ObjectAlterAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JAVA, Messages.get().ChangeJava);
		dropAction_Java = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JAVA, 	Messages.get().DropJava); //$NON-NLS-1$
		refreshAction_Java = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JAVA, Messages.get().Refresh); //$NON-NLS-1$
		compileAction = new OracleObjectCompileAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JAVA, Messages.get().Compilejava);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "Java"); //$NON-NLS-1$ //$NON-NLS-2$
		menuMgr.add(refreshAction_Java);
		menuMgr.add(new Separator());
		if (!isDDLLock()) {
			menuMgr.add(creatAction_Java);
			menuMgr.add(alterAction_Java);
			menuMgr.add(dropAction_Java);
			menuMgr.add(new Separator());
		}
		menuMgr.add(new Separator());
		menuMgr.add(compileAction);

		javaListViewer.getTable().setMenu(menuMgr.createContextMenu(javaListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, javaListViewer);
	}

	/**
	 * 정보를 최신으로 리프레쉬합니다.
	 * 
	 * @param strObjectName
	 */
	public void refreshJava(final UserDBDAO selectUserDb, final boolean boolRefresh, final String strObjectName) {
		if (!boolRefresh) if (selectUserDb == null) return;
		this.userDB = selectUserDb;
		
		try {
			QueryUtils.executeQuery(userDB, "select 1 from javasnm where 1=0 ", 0, 1);
		} catch (Exception e) {
			MessageDialog.openInformation(getShell(), Messages.get().Information, Messages.get().doesnotSupportJavaObject);
			return;
		}

		Job job = new Job(Messages.get().MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Connect database", IProgressMonitor.UNKNOWN); //$NON-NLS-1$

				try {
					showJava = getJavaList(userDB);
					for (OracleJavaDAO dao : showJava) {
						dao.setSysName(dao.getObjectName() + "");
					}

				} catch (Exception e) {
					logger.error("Java Referesh", e); //$NON-NLS-1$

					return new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
				}

				return Status.OK_STATUS;
			}
		};

		job.addJobChangeListener(new JobChangeAdapter() {

			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event;

				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (jobEvent.getResult().isOK()) {
							javaListViewer.setInput(showJava);
							javaListViewer.refresh();
							TableUtil.packTable(javaListViewer.getTable());

							// select tabitem
							getTabFolderObject().setSelection(tbtmJava);

							selectDataOfTable(strObjectName);
						} else {
							if (showJava != null) showJava.clear();
							javaListViewer.setInput(showJava);
							javaListViewer.refresh();
							TableUtil.packTable(javaListViewer.getTable());

							MessageDialog.openError(getShell(), Messages.get().Error, jobEvent.getResult().getMessage());
						}
					}
				}); // end display.asyncExec
			} // end done

		}); // end job

		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}

	/**
	 * 보여 주어야할 목록을 정의합니다.
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static List<OracleJavaDAO> getJavaList(final UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("getJavaList", userDB.getSchema()); //$NON-NLS-1$
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if (getUserDB() == null)
			return;

		creatAction_Java.setUserDB(getUserDB());
		alterAction_Java.setUserDB(getUserDB());
		dropAction_Java.setUserDB(getUserDB());
		refreshAction_Java.setUserDB(getUserDB());
		compileAction.setUserDB(getUserDB());
	}

	/**
	 * get javaViewer
	 * 
	 * @return
	 */
	public TableViewer getTableviewer() {
		return javaListViewer;
	}

	/**
	 * get java column viewer
	 * 
	 * @return
	 */
	public TableViewer getJobsColumnViewer() {
		return javaColumnViewer;
	}

	/**
	 * initialize filter text
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		javaFilter.setSearchString(textSearch);
		javaListViewer.refresh();
	}

	@Override
	public void dispose() {
		super.dispose();
		if (creatAction_Java != null)
			creatAction_Java.dispose();
		if (alterAction_Java != null)
			alterAction_Java.dispose();
		if (dropAction_Java != null)
			dropAction_Java.dispose();
		if (refreshAction_Java != null)
			refreshAction_Java.dispose();
		if (compileAction != null)
			compileAction.dispose();
	}

	@Override
	public void setSearchText(String searchText) {
		javaFilter.setSearchString(searchText);
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if ("".equals(strObjectName) || strObjectName == null)
			return;

		getTableviewer().getTable().setFocus();

		// find select object and viewer select
		for (int i = 0; i < this.showJava.size(); i++) {
			OracleJavaDAO javaDao = (OracleJavaDAO) getTableviewer().getElementAt(i);
			if (StringUtils.equalsIgnoreCase(strObjectName, javaDao.getObjectName() + "")) {
				getTableviewer().setSelection(new StructuredSelection(getTableviewer().getElementAt(i)), true);
				break;
			}
		}
	}
}
