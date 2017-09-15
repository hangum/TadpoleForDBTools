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
package com.hangum.tadpole.rdb.core.viewers.object.sub.agens;

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

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.agens.AgensGraphPathDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.SetGraphPathAction;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Agens graph path composite
 * 
 * @author hangum
 * 
 */
public class TadpoleGraphPathComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleGraphPathComposite.class);

	private CTabItem tbtmGraphPath;
	private ExplorerViewer exViewer;

	// table info
	private TableViewer graphPathListViewer;
	private List<AgensGraphPathDAO> showGraphPath = new ArrayList<AgensGraphPathDAO>();
	private DefaultTableColumnFilter graphPathFilter;

	private AbstractObjectAction refreshAction_GraphPath;
	private AbstractObjectAction selectAction_GraphPath;

	/**
	 * Create the composite.
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public TadpoleGraphPathComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB, ExplorerViewer exViewer) {
		super(partSite, tabFolderObject, userDB);

		this.exViewer = exViewer;
		createWidget(tabFolderObject);
	}

	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmGraphPath = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmGraphPath.setText("Graph");
		tbtmGraphPath.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.GRAPHPATH.name());

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmGraphPath.setControl(compositeTables);
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
		graphPathListViewer = new TableViewer(sashForm, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		graphPathListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (null != is) {
						AgensGraphPathDAO graphPathDAO = (AgensGraphPathDAO) is.getFirstElement();
						//FindEditorAndWriteQueryUtil.run(userDB, "SET GRAPH_PATH = " + graphPathDAO.getGraphname() + " ;", PublicTadpoleDefine.OBJECT_TYPE.GRAPHPATH);
						String cmd = "SET GRAPH_PATH = " + graphPathDAO.getGraphname() + " ;";
						RequestResultDAO reqReResultDAO = new RequestResultDAO();
						ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, cmd); //$NON-NLS-1$
						
						FindEditorAndWriteQueryUtil.run(userDB, "/* select graph path is "+ graphPathDAO.getGraphname() +" */\n", PublicTadpoleDefine.OBJECT_TYPE.VERTEX);
						
						exViewer.changeSchema(graphPathDAO.getGraphname());

					}
				} catch (Exception e) {
					logger.error("select working graph_path", e);
				}
			}
		});

		Table tableTableList = graphPathListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		createRefreshMenu();
		createGraphListColumns();

		graphPathListViewer.setInput(showGraphPath);
		graphPathListViewer.refresh();

		graphPathFilter = new DefaultTableColumnFilter();
		graphPathListViewer.addFilter(graphPathFilter);
	}

	/** create column */
	private void createGraphListColumns() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
		new TableViewColumnDefine("graphname", "Graph Name", 120, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("nspid", "Namespace ID", 100, SWT.RIGHT) // //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(graphPathListViewer, tableColumnDef);

		graphPathListViewer.setContentProvider(new ArrayContentProvider());
		graphPathListViewer.setLabelProvider(new DefaultLabelProvider(graphPathListViewer));
	}

	/**
	 * create Table menu
	 */
	private void createRefreshMenu() {
		if(getUserDB() == null) return;
		
		refreshAction_GraphPath = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.GRAPHPATH, CommonMessages.get().Refresh); //$NON-NLS-1$
		selectAction_GraphPath = new SetGraphPathAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.GRAPHPATH, "Set Graph Path"); //$NON-NLS-1$
		
		
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "Graph Path"); //$NON-NLS-1$ //$NON-NLS-2$
		menuMgr.add(refreshAction_GraphPath);
		menuMgr.add(selectAction_GraphPath);
		graphPathListViewer.getTable().setMenu(menuMgr.createContextMenu(graphPathListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, graphPathListViewer);
	}

	/**
	 * 정보를 최신으로 리프레쉬합니다.
	 * @param strObjectName 
	 */
	public void refreshGraphPath(final UserDBDAO selectUserDb, final boolean boolRefresh, final String strObjectName) {
		if (!boolRefresh) 
			if(selectUserDb == null) return;
			//if (!showGraphPath.isEmpty()) return;
		this.userDB = selectUserDb;

		showGraphPath = (List<AgensGraphPathDAO>)selectUserDb.getDBObject(OBJECT_TYPE.GRAPHPATH, selectUserDb.getDefaultSchemanName());
		if(!(showGraphPath == null || showGraphPath.isEmpty())) {
			graphPathListViewer.setInput(showGraphPath);
			graphPathListViewer.refresh();
			TableUtil.packTable(graphPathListViewer.getTable());

			// select tabitem
			getTabFolderObject().setSelection(tbtmGraphPath);
			
			selectDataOfTable(strObjectName);
		} else {
			final String MSG_LoadingData = CommonMessages.get().LoadingData;
			Job job = new Job(Messages.get().MainEditor_45) {
				@Override
				public IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(MSG_LoadingData, IProgressMonitor.UNKNOWN); //$NON-NLS-1$
	
					try {
						showGraphPath = getGraphPathList(userDB);

						// set push of cache
						userDB.setDBObject(OBJECT_TYPE.GRAPHPATH, userDB.getDefaultSchemanName(), showGraphPath);
					} catch (Exception e) {
						logger.error("Graph Path Referesh", e); //$NON-NLS-1$
	
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
								graphPathListViewer.setInput(showGraphPath);
								graphPathListViewer.refresh();
								TableUtil.packTable(graphPathListViewer.getTable());
	
								// select tabitem
								getTabFolderObject().setSelection(tbtmGraphPath);
								
								selectDataOfTable(strObjectName);
							} else {
								if (showGraphPath != null) showGraphPath.clear();
								graphPathListViewer.setInput(showGraphPath);
								graphPathListViewer.refresh();
								TableUtil.packTable(graphPathListViewer.getTable());
	
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
	public static List<AgensGraphPathDAO> getGraphPathList(final UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("agensGraphPath", userDB.getSchema()); //$NON-NLS-1$
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if(getUserDB() == null) return; 
		refreshAction_GraphPath.setUserDB(getUserDB());
		selectAction_GraphPath.setUserDB(getUserDB());
	}

	/**
	 * get sequenceViewer
	 * 
	 * @return
	 */
	public TableViewer getTableviewer() {
		return graphPathListViewer;
	}

	/**
	 * initialize filter text
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		graphPathFilter.setSearchString(textSearch);
		graphPathListViewer.refresh();
	}

	@Override
	public void dispose() {
		super.dispose();	
		if(refreshAction_GraphPath != null) refreshAction_GraphPath.dispose();
		if(selectAction_GraphPath != null) selectAction_GraphPath.dispose();
	}

	@Override
	public void setSearchText(String searchText) {
		graphPathFilter.setSearchString(searchText);
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableviewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i< this.showGraphPath.size(); i++) {
			AgensGraphPathDAO graphDao = (AgensGraphPathDAO)getTableviewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, graphDao.getGraphname())) {
				getTableviewer().setSelection(new StructuredSelection(getTableviewer().getElementAt(i)), true);
				break;
			}
		}
	}

	public void clearList() {
		// TODO Auto-generated method stub
		// 모든 그래프 목록(스키마와 동일한 레벨)을 표시하므로 그대로 유지한다.
		//this.showGraphPath.clear();
	}
}
