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
package com.hangum.tadpole.rdb.core.viewers.object.sub.agens.vertex;

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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.agens.AgensVertexDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSequenceDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Agens graph vertex composite
 * 
 * @author hangum
 * 
 */
public class TadpoleVertexComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleVertexComposite.class);

	private CTabItem tbtmVertex;

	// table info
	private TableViewer vertexListViewer;
	private List<AgensVertexDAO> showVertex = new ArrayList<AgensVertexDAO>();
	private DefaultTableColumnFilter vertexFilter;

	private AbstractObjectAction refreshAction_Vertex;

	/**
	 * Create the composite.
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public TadpoleVertexComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);

		createWidget(tabFolderObject);
	}

	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmVertex = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmVertex.setText("Vertex");
		tbtmVertex.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.VERTEX.name());

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmVertex.setControl(compositeTables);
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
		vertexListViewer = new TableViewer(sashForm, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		vertexListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (null != is) {
						AgensVertexDAO vertexDAO = (AgensVertexDAO) is.getFirstElement();
						FindEditorAndWriteQueryUtil.run(userDB, "Match (n: " + vertexDAO.getSysName() + ") return n;", PublicTadpoleDefine.OBJECT_TYPE.VERTEX);
					}
				} catch (Exception e) {
					logger.error("vertex match statement", e);
				}
			}
		});

		Table tableTableList = vertexListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		createSequenceMenu();
		createSequenceListColumns();

		vertexListViewer.setInput(showVertex);
		vertexListViewer.refresh();

		vertexFilter = new DefaultTableColumnFilter();
		vertexListViewer.addFilter(vertexFilter);
	}

	/** create column */
	private void createSequenceListColumns() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
		new TableViewColumnDefine("LABNAME", CommonMessages.get().Name, 100, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("RELID", "RELID", 80, SWT.RIGHT) // //$NON-NLS-1$
				, new TableViewColumnDefine("LABOWNER", "LABOWNER", 80, SWT.RIGHT) // //$NON-NLS-1$
				, new TableViewColumnDefine("LABKIND", "LABKIND", 40, SWT.RIGHT) // //$NON-NLS-1$
				, new TableViewColumnDefine("INHRELID", "INHRELID", 40, SWT.CENTER) // //$NON-NLS-1$
				, new TableViewColumnDefine("INHPARENT", "INHPARENT", 40, SWT.RIGHT) // //$NON-NLS-1$
				, new TableViewColumnDefine("INHSEQNO", "INHSEQNO", 80, SWT.RIGHT) // //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(vertexListViewer, tableColumnDef);

		vertexListViewer.setContentProvider(new ArrayContentProvider());
		vertexListViewer.setLabelProvider(new DefaultLabelProvider(vertexListViewer));
	}

	/**
	 * create Table menu
	 */
	private void createSequenceMenu() {
		if(getUserDB() == null) return;
		
		refreshAction_Vertex = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.VERTEX, CommonMessages.get().Refresh); //$NON-NLS-1$

		final MenuManager menuMgr = new MenuManager("#PopupMenu", "Vertex"); //$NON-NLS-1$ //$NON-NLS-2$
		menuMgr.add(refreshAction_Vertex);
		vertexListViewer.getTable().setMenu(menuMgr.createContextMenu(vertexListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, vertexListViewer);
	}

	/**
	 * 정보를 최신으로 리프레쉬합니다.
	 * @param strObjectName 
	 */
	public void refreshSequence(final UserDBDAO selectUserDb, final boolean boolRefresh, final String strObjectName) {
		if (!boolRefresh)
			if(selectUserDb == null) return;
			//if (!showVertex.isEmpty()) return;
		this.userDB = selectUserDb;

		showVertex = (List<AgensVertexDAO>)selectUserDb.getDBObject(OBJECT_TYPE.VERTEX, selectUserDb.getDefaultSchemanName());
		if(!(showVertex == null || showVertex.isEmpty())) {
			vertexListViewer.setInput(showVertex);
			vertexListViewer.refresh();
			TableUtil.packTable(vertexListViewer.getTable());

			// select tabitem
			getTabFolderObject().setSelection(tbtmVertex);
			
			selectDataOfTable(strObjectName);
		} else {
			Job job = new Job(Messages.get().MainEditor_45) {
				@Override
				public IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(MSG_DataIsBeginAcquired, IProgressMonitor.UNKNOWN); //$NON-NLS-1$
	
					try {
						showVertex = getVertexList(userDB);

						// set push of cache
						userDB.setDBObject(OBJECT_TYPE.VERTEX, userDB.getDefaultSchemanName(), showVertex);
					} catch (Exception e) {
						logger.error("Vertex Referesh", e); //$NON-NLS-1$
	
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
								vertexListViewer.setInput(showVertex);
								vertexListViewer.refresh();
								TableUtil.packTable(vertexListViewer.getTable());
	
								// select tabitem
								getTabFolderObject().setSelection(tbtmVertex);
								
								selectDataOfTable(strObjectName);
							} else {
								if (showVertex != null) showVertex.clear();
								vertexListViewer.setInput(showVertex);
								vertexListViewer.refresh();
								TableUtil.packTable(vertexListViewer.getTable());
	
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
	public static List<AgensVertexDAO> getVertexList(final UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		
		List<AgensVertexDAO> vertexList = 
		
		 sqlClient.queryForList("agensVertex", userDB.getSchema()); //$NON-NLS-1$
		
		for(AgensVertexDAO td : vertexList) {
			td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getLabname() ));
		}
		
		return vertexList;
		
		
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if(getUserDB() == null) return; 
		refreshAction_Vertex.setUserDB(getUserDB());
	}

	/**
	 * get sequenceViewer
	 * 
	 * @return
	 */
	public TableViewer getTableviewer() {
		return vertexListViewer;
	}

	/**
	 * initialize filter text
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		vertexFilter.setSearchString(textSearch);
		vertexListViewer.refresh();
	}

	@Override
	public void dispose() {
		super.dispose();	
		if(refreshAction_Vertex != null) refreshAction_Vertex.dispose();
	}

	@Override
	public void setSearchText(String searchText) {
		vertexFilter.setSearchString(searchText);
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableviewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i< this.showVertex.size(); i++) {
			AgensVertexDAO sequenceDao = (AgensVertexDAO)getTableviewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, sequenceDao.getLabname())) {
				getTableviewer().setSelection(new StructuredSelection(getTableviewer().getElementAt(i)), true);
				break;
			}
		}
	}

	public void clearList() {
		// TODO Auto-generated method stub
		if(showVertex != null) this.showVertex.clear();
	}
}
