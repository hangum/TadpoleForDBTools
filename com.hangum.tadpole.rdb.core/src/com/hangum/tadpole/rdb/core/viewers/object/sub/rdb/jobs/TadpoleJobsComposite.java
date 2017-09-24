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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.jobs;

import java.sql.SQLException;
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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJobDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectAlterAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectExplorerSelectionToEditorAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Oracle database job composite
 * 
 * @author nilriri
 * 
 */
public class TadpoleJobsComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleJobsComposite.class);

	private CTabItem tbtmJobs;

	// table info
	private TableViewer jobsListViewer;
	private List<OracleJobDAO> showJobs = new ArrayList<OracleJobDAO>();
	private DefaultTableColumnFilter jobsFilter;

	// column info
	private TableViewer jobsColumnViewer;
	
	private ObjectCreatAction creatAction_Jobs;
	private ObjectAlterAction alterAction_Jobs;
	private AbstractObjectAction dropAction_Jobs;
	private AbstractObjectAction refreshAction_Jobs;

	/**
	 * Create the composite.
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public TadpoleJobsComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);

		createWidget(tabFolderObject);
	}

	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmJobs = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmJobs.setText("Jobs");
		tbtmJobs.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.JOBS.name());

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmJobs.setControl(compositeTables);
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
		jobsListViewer = new TableViewer(sashForm, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		jobsListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (null != is) {
						OracleJobDAO jobsDAO = (OracleJobDAO) is.getFirstElement();
						FindEditorAndWriteQueryUtil.run(userDB, "CALL SYS.DBMS_JOB.RUN('"+jobsDAO.getJob()+"');" , PublicTadpoleDefine.OBJECT_TYPE.JOBS);
					}
				} catch (Exception e) {
					logger.error("create jobs", e);
				}
			}
		});

		Table tableTableList = jobsListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		createJobsMenu();
		createJobsListColumns();

		jobsListViewer.setInput(showJobs);
		jobsListViewer.refresh();

		jobsFilter = new DefaultTableColumnFilter();
		jobsListViewer.addFilter(jobsFilter);
	}

	private void createJobsListColumns() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
				  new TableViewColumnDefine("JOB", "ID", 40, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("SCHEMA_NAME", "Schema", 80, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("WHAT", "What", 120, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("LAST_DATE", "Last", 120, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("NEXT_DATE", "Next", 120, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("INTERVAL", "Interval", 80, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("PRIV_USER", "Privilege", 80, SWT.LEFT) // //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(jobsListViewer, tableColumnDef);

		jobsListViewer.setContentProvider(new ArrayContentProvider());
		jobsListViewer.setLabelProvider(new DefaultLabelProvider(jobsListViewer));

	}

	/**
	 * create Table menu
	 */
	private void createJobsMenu() {
		if(getUserDB() == null) return;
		
		creatAction_Jobs = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JOBS, 	Messages.get().CreateJob);
		alterAction_Jobs = new ObjectAlterAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JOBS, 	Messages.get().ChangeJob);
		dropAction_Jobs = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JOBS, 	Messages.get().RemoveJob); //$NON-NLS-1$
		refreshAction_Jobs = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.JOBS, CommonMessages.get().Refresh); //$NON-NLS-1$
		// object copy to query editor
		objectSelectionToEditorAction = new ObjectExplorerSelectionToEditorAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES);
				
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "Jobs"); //$NON-NLS-1$ //$NON-NLS-2$
		if(!isDDLLock()) {
			menuMgr.add(creatAction_Jobs);
			menuMgr.add(alterAction_Jobs);
			menuMgr.add(dropAction_Jobs);
			menuMgr.add(new Separator());
		}
		menuMgr.add(refreshAction_Jobs);
		menuMgr.add(new Separator());
		
		menuMgr.add(new Separator());
		menuMgr.add(objectSelectionToEditorAction);

		jobsListViewer.getTable().setMenu(menuMgr.createContextMenu(jobsListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, jobsListViewer);
	}

	/**
	 * 정보를 최신으로 리프레쉬합니다.
	 * @param strObjectName 
	 */
	public void refreshJobs(final UserDBDAO selectUserDb, final boolean boolRefresh, final String strObjectName) {
		if (!boolRefresh) if(!showJobs.isEmpty()) return;
		this.userDB = selectUserDb;

		showJobs = (List<OracleJobDAO>)userDB.getDBObject(OBJECT_TYPE.JOBS, userDB.getDefaultSchemanName());
		if(!(showJobs == null || showJobs.isEmpty())) {
			jobsListViewer.setInput(showJobs);
			jobsListViewer.refresh();
			TableUtil.packTable(jobsListViewer.getTable());

			// select tabitem
			getTabFolderObject().setSelection(tbtmJobs);
			
			selectDataOfTable(strObjectName);
		} else {
			final String MSG_LoadingData = CommonMessages.get().LoadingData;
			Job job = new Job(Messages.get().MainEditor_45) {
				@Override
				public IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(MSG_LoadingData, IProgressMonitor.UNKNOWN); //$NON-NLS-1$
	
					try {
						showJobs = getJobsList(userDB);
						
						for(OracleJobDAO dao : showJobs) {
							dao.setSysName( dao.getJob() + "" );
						}
						
						// set push of cache
						userDB.setDBObject(OBJECT_TYPE.JOBS, userDB.getDefaultSchemanName(), showJobs);
					} catch (Exception e) {
						logger.error("Jobs Referesh", e); //$NON-NLS-1$
	
						return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
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
								jobsListViewer.setInput(showJobs);
								jobsListViewer.refresh();
								TableUtil.packTable(jobsListViewer.getTable());
	
								// select tabitem
								getTabFolderObject().setSelection(tbtmJobs);
								
								selectDataOfTable(strObjectName);
							} else {
								if (showJobs != null)
									showJobs.clear();
								jobsListViewer.setInput(showJobs);
								jobsListViewer.refresh();
								TableUtil.packTable(jobsListViewer.getTable());
	
								Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult().getException()); //$NON-NLS-1$
								ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, Messages.get().ExplorerViewer_86, errStatus); //$NON-NLS-1$
							}
						}
					}); // end display.asyncExec
				} // end done
	
			}); // end job
	
			job.setName(userDB.getDisplay_name());
			job.setUser(true);
			job.schedule();
		}
	}

	/**
	 * 보여 주어야할 목록을 정의합니다.
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List<OracleJobDAO> getJobsList(final UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);

		List<OracleJobDAO> listDBAResult = new ArrayList<OracleJobDAO>();		
		List<OracleJobDAO> listUserResult = sqlClient.queryForList("getJobList", userDB.getSchema()); //$NON-NLS-1$
		
		// DBA권한이 없을경우 발생하는 Exception은 무시한다.
		try{
			listDBAResult = sqlClient.queryForList("getDBAJobList", userDB.getSchema()); //$NON-NLS-1$
		}catch(SQLException e){
			// ignore exception
		}
		
		// 사용자 권한에 따라서 조회되는 job object목록이 중복되므로 id를 기준으로 중복을 제거한다.
		for (OracleJobDAO oracleJobDAO : listUserResult) {
			if(!listDBAResult.contains(oracleJobDAO)) {
				listDBAResult.add(oracleJobDAO);
			}
		}
		return listDBAResult;
		
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if(getUserDB() == null) return; 
		
		creatAction_Jobs.setUserDB(getUserDB());
		alterAction_Jobs.setUserDB(getUserDB());
		dropAction_Jobs.setUserDB(getUserDB());
		refreshAction_Jobs.setUserDB(getUserDB());
	}

	/**
	 * get jobsViewer
	 * 
	 * @return
	 */
	public TableViewer getTableviewer() {
		return jobsListViewer;
	}

	/**
	 * get jobs column viewer
	 * 
	 * @return
	 */
	public TableViewer getJobsColumnViewer() {
		return jobsColumnViewer;
	}

	/**
	 * initialize filter text
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		jobsFilter.setSearchString(textSearch);
		jobsListViewer.refresh();
		TableUtil.packTable(jobsListViewer.getTable());
	}

	@Override
	public void dispose() {
		super.dispose();	
		if(creatAction_Jobs != null) creatAction_Jobs.dispose();
		if(alterAction_Jobs != null) alterAction_Jobs.dispose();
		if(dropAction_Jobs != null) dropAction_Jobs.dispose();
		if(refreshAction_Jobs != null) refreshAction_Jobs.dispose();
	}

	@Override
	public void setSearchText(String searchText) {
		jobsFilter.setSearchString(searchText);
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableviewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i< this.showJobs.size(); i++) {
			OracleJobDAO jobsDao = (OracleJobDAO)getTableviewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, jobsDao.getJob()+"" )) {
				getTableviewer().setSelection(new StructuredSelection(getTableviewer().getElementAt(i)), true);
				break;
			}
		}
	}

	public void clearList() {
		// TODO Auto-generated method stub
		if(showJobs != null) this.showJobs.clear();
	}
}
