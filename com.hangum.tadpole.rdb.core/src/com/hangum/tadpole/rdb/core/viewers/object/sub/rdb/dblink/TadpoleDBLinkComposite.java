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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.dblink;

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
import com.hangum.tadpole.commons.libs.core.message.InfoMessages;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.rdb.OracleDBLinkDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
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
 * Oracle database link composite
 * 
 * @author nilriri
 * 
 */
public class TadpoleDBLinkComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleDBLinkComposite.class);

	private CTabItem tbtmDBLink;

	// table info
	private TableViewer dbLinkListViewer;
	private List<OracleDBLinkDAO> showDBLinks = new ArrayList<OracleDBLinkDAO>();
	private DefaultTableColumnFilter dbLinkFilter;

	// column info
	private TableViewer dbLinkColumnViewer;
	
	private ObjectCreatAction creatAction_DBLink;
	private AbstractObjectAction dropAction_DBLink;
	private AbstractObjectAction refreshAction_DBLink;
	private GenerateViewDDLAction viewDDLAction;

	/**
	 * Create the composite.
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public TadpoleDBLinkComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);

		createWidget(tabFolderObject);
	}

	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmDBLink = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmDBLink.setText("DB Link");
		tbtmDBLink.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.LINK.name());

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmDBLink.setControl(compositeTables);
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
		dbLinkListViewer = new TableViewer(sashForm, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		dbLinkListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (null != is) {
						OracleDBLinkDAO dbLinkDAO = (OracleDBLinkDAO) is.getFirstElement();
						FindEditorAndWriteQueryUtil.run(userDB, "SELECT sysdate FROM DUAL@"+dbLinkDAO.getDb_link() , PublicTadpoleDefine.OBJECT_TYPE.LINK);
					}
				} catch (Exception e) {
					logger.error("create dbLink", e);
				}
			}
		});

		Table tableTableList = dbLinkListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		createDBLinkMenu();
		createDBLinkListColumns();

		dbLinkListViewer.setInput(showDBLinks);
		dbLinkListViewer.refresh();

		dbLinkFilter = new DefaultTableColumnFilter();
		dbLinkListViewer.addFilter(dbLinkFilter);
	}

	private void createDBLinkListColumns() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] {
				new TableViewColumnDefine("DB_LINK", "Link Name", 100, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("USERNAME", "User Name", 80, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("HOST", "Host", 120, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("CREATED", "Created", 80, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("SCHEMA_NAME", "Owner", 80, SWT.LEFT) // //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(dbLinkListViewer, tableColumnDef);

		dbLinkListViewer.setContentProvider(new ArrayContentProvider());
		dbLinkListViewer.setLabelProvider(new DefaultLabelProvider(dbLinkListViewer));

	}

	/**
	 * create Table menu
	 */
	private void createDBLinkMenu() {
		if(getUserDB() == null) return;
		
		creatAction_DBLink = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.LINK, Messages.get().DBLinkCreated);
		dropAction_DBLink = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.LINK, Messages.get().DBLinkDrop); //$NON-NLS-1$
		refreshAction_DBLink = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.LINK, CommonMessages.get().Refresh); //$NON-NLS-1$
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.LINK, Messages.get().ViewDDL); //$NON-NLS-1$
		
		// object copy to query editor
		objectSelectionToEditorAction = new ObjectExplorerSelectionToEditorAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "DBLink"); //$NON-NLS-1$ //$NON-NLS-2$
		if(!isDDLLock()) {
			menuMgr.add(creatAction_DBLink);
			menuMgr.add(dropAction_DBLink);
			menuMgr.add(new Separator());
		}
		menuMgr.add(refreshAction_DBLink);
		menuMgr.add(new Separator());
		
		viewDDLAction.setEnabled(true);
		menuMgr.add(viewDDLAction);
		
		menuMgr.add(new Separator());
		menuMgr.add(objectSelectionToEditorAction);

		dbLinkListViewer.getTable().setMenu(menuMgr.createContextMenu(dbLinkListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, dbLinkListViewer);
	}

	/**
	 * 정보를 최신으로 리프레쉬합니다.
	 * @param strObjectName 
	 */
	public void refreshDBLink(final UserDBDAO selectUserDb, final boolean boolRefresh, final String strObjectName) {
		if (!boolRefresh) if (!showDBLinks.isEmpty()) return;
		this.userDB = selectUserDb;

		showDBLinks = (List<OracleDBLinkDAO>)selectUserDb.getDBObject(OBJECT_TYPE.LINK, selectUserDb.getDefaultSchemanName());
		if(!(showDBLinks == null || showDBLinks.isEmpty())) {
			dbLinkListViewer.setInput(showDBLinks);
			dbLinkListViewer.refresh();
			TableUtil.packTable(dbLinkListViewer.getTable());

			// select tabitem
			getTabFolderObject().setSelection(tbtmDBLink);
			
			selectDataOfTable(strObjectName);
		} else {
			final String MSG_LoadingData = InfoMessages.get().LoadingData;
			Job job = new Job(Messages.get().MainEditor_45) {
				@Override
				public IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(MSG_LoadingData, IProgressMonitor.UNKNOWN); //$NON-NLS-1$
	
					try {
						showDBLinks = getDBLinkList(userDB);
						
						for(OracleDBLinkDAO dao : showDBLinks) {
							dao.setSysName(SQLUtil.makeIdentifierName(userDB, dao.getDb_link() ));
						}
						
						// set push of cache
						userDB.setDBObject(OBJECT_TYPE.LINK, userDB.getDefaultSchemanName(), showDBLinks);
					} catch (Exception e) {
						logger.error("DBLink Referesh", e); //$NON-NLS-1$
	
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
								dbLinkListViewer.setInput(showDBLinks);
								dbLinkListViewer.refresh();
								TableUtil.packTable(dbLinkListViewer.getTable());
	
								// select tabitem
								getTabFolderObject().setSelection(tbtmDBLink);
								
								selectDataOfTable(strObjectName);
							} else {
								if (showDBLinks != null) showDBLinks.clear();
								dbLinkListViewer.setInput(showDBLinks);
								dbLinkListViewer.refresh();
								TableUtil.packTable(dbLinkListViewer.getTable());
	
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
	public static List<OracleDBLinkDAO> getDBLinkList(final UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("dbLinkList", userDB.getSchema()); //$NON-NLS-1$
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if(getUserDB() == null) return; 
		
		creatAction_DBLink.setUserDB(getUserDB());
		dropAction_DBLink.setUserDB(getUserDB());
		refreshAction_DBLink.setUserDB(getUserDB());
		//executeAction.setUserDB(getUserDB());
		viewDDLAction.setUserDB(getUserDB());
		
		objectSelectionToEditorAction.setUserDB(getUserDB());
	}

	/**
	 * get dbLinkViewer
	 * 
	 * @return
	 */
	public TableViewer getTableviewer() {
		return dbLinkListViewer;
	}

	/**
	 * get dbLink column viewer
	 * 
	 * @return
	 */
	public TableViewer getDBLinkColumnViewer() {
		return dbLinkColumnViewer;
	}

	/**
	 * initialize filter text
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		dbLinkFilter.setSearchString(textSearch);
		dbLinkListViewer.refresh();
		TableUtil.packTable(dbLinkListViewer.getTable());
	}

	@Override
	public void dispose() {
		super.dispose();	
		if(creatAction_DBLink != null) creatAction_DBLink.dispose();
		if(dropAction_DBLink != null) dropAction_DBLink.dispose();
		if(refreshAction_DBLink != null) refreshAction_DBLink.dispose();
		if(viewDDLAction != null) viewDDLAction.dispose();
		//if(executeAction != null) executeAction.dispose();
	}

	@Override
	public void setSearchText(String searchText) {
		dbLinkFilter.setSearchString(searchText);
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableviewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i< this.showDBLinks.size(); i++) {
			OracleDBLinkDAO dbLinkDao = (OracleDBLinkDAO)getTableviewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, dbLinkDao.getDb_link() )) {
				getTableviewer().setSelection(new StructuredSelection(getTableviewer().getElementAt(i)), true);
				break;
			}
		}
	}

	public void clearList() {
		// TODO Auto-generated method stub
		if(showDBLinks != null) this.showDBLinks.clear();
	}
}
