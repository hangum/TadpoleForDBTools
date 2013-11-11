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
package com.hangum.tadpole.rdb.core.editors.sessionlist;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.sessionlist.composite.mysql.MySQLSessionListLabelProvider;
import com.hangum.tadpole.rdb.core.editors.sessionlist.composite.mysql.MySQLSessionListTableCompare;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.sql.dao.mysql.SessionListDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

/**
 * DDB Session list editor
 * 
 * mysql : http://dev.mysql.com/doc/refman/5.1/en/show-processlist.html
 * 
 * @since 2013.04.01
 * @author hangum
 *
 */
public class SessionListEditor extends EditorPart {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SessionListEditor.class);

	public static final String ID = "com.hangum.tadpole.rdb.core.editor.sessionlist";
	
	protected final int user_seq = SessionManager.getSeq();
	
	private UserDBDAO userDB;
	
	private TableViewer tableViewerSessionList;
	private ObjectComparator comparator;
	private Text textQuery;

	public SessionListEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		SessionListEditorInput qei = (SessionListEditorInput)input;
		this.userDB = qei.getUserDB();
		setPartName(qei.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 0;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginHeight = 0;
		gl_parent.marginWidth = 0;
		parent.setLayout(gl_parent);
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(1, false);
		gl_compositeHead.marginHeight = 0;
		gl_compositeHead.horizontalSpacing = 0;
		gl_compositeHead.marginWidth = 0;
		compositeHead.setLayout(gl_compositeHead);
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setToolTipText("Refresh");
		tltmRefresh.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/refresh.png")); //$NON-NLS-1$
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initSessionListData();
			}
		});
		
		final ToolItem tltmKillProcess = new ToolItem(toolBar, SWT.NONE);
		tltmKillProcess.setToolTipText("Kill Process");
		tltmKillProcess.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/stop_process.png")); //$NON-NLS-1$
		tltmKillProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				killProcess();
			}
		});
		tltmKillProcess.setEnabled(false);
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeBody = new Composite(sashForm, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		
		tableViewerSessionList = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewerSessionList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(tableViewerSessionList.getSelection().isEmpty()) return;
				
				tltmKillProcess.setEnabled(true);
				
				StructuredSelection ss = (StructuredSelection)tableViewerSessionList.getSelection();
				SessionListDAO sl = (SessionListDAO)ss.getFirstElement();
				if(null != sl.getInfo()) {
					textQuery.setText(sl.getInfo());
					textQuery.setFocus();
				} else {
					textQuery.setText("");
				}
			}
		});
		Table tableSessionList = tableViewerSessionList.getTable();
		tableSessionList.setHeaderVisible(true);
		tableSessionList.setLinesVisible(true);
		tableSessionList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group compositeQuery = new Group(sashForm, SWT.NONE);
		compositeQuery.setLayout(new GridLayout(1, false));
		compositeQuery.setText("Query");
		
		textQuery = new Text(compositeQuery, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comparator = new MySQLSessionListTableCompare();
		tableViewerSessionList.setSorter(comparator);
		
		createColumn();
		
		tableViewerSessionList.setContentProvider(new ArrayContentProvider());
		tableViewerSessionList.setLabelProvider(new MySQLSessionListLabelProvider());
		
		sashForm.setWeights(new int[] {7, 3});
		
		// init data
		initSessionListData();
	}
	
	/**
	 * kill process
	 */
	private void killProcess() {
		StructuredSelection ss = (StructuredSelection)tableViewerSessionList.getSelection();
		SessionListDAO sl = (SessionListDAO)ss.getFirstElement();
		
		if(!MessageDialog.openConfirm(null, "Confirm", "Do you want kill process?")) return;
		
		String sqlText = "kill " + sl.getId(); 
//		java.sql.Connection javaConn = null;		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			client.queryForObject("killProcess", sl.getId());
//			javaConn = client.getDataSource().getConnection();
//			
//			Statement stmt = javaConn.createStatement();
//			boolean boolResult = stmt.execute( sqlText );
			
			initSessionListData();
		} catch(Exception e) {
			logger.error("killprocess exception", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
			
		} finally {
//			try { javaConn.close(); } catch(Exception e){}
			
//			if(DB_Define.YES_NO.YES.toString().equals(userDB.getIs_profile())) {
//				try {
//					TadpoleSystem_UserDBResource.saveResource(user_seq, userDB, DB_Define.RESOURCE_TYPE.USER_EXECUTE_QUERY, Messages.MainEditor_31 + System.currentTimeMillis(), sqlText);
//				} catch(Exception e) {
//					logger.error("SAVE killprocess log", e);
//				}
//			}
		}	
	}
	
	/**
	 * editor init data
	 */
	private void initSessionListData() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			
			int getSessionGrant = (Integer) sqlClient.queryForObject("getSessionGrant");
			if (0 >= getSessionGrant){
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "In order to display a list of the session , you want to manage, and requires a 'ALTER SYSTEM' authority.", null); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", "You do not have permission to 'ALTER SYSTEM'", errStatus); //$NON-NLS-1$
				return;
			}
			
			try {
				int getSessionView = (Integer) sqlClient.queryForObject("getSessionView");
			}catch (Exception e) {
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", "You do not have permission to viewing 'V $ SESSION'", errStatus); //$NON-NLS-1$
				return;
			}
			
			int version = (Integer) sqlClient.queryForObject("getVersion");
			List<?> listSessionList = null;
			if (version <= 9){
				listSessionList = sqlClient.queryForList("sessionList_9");
			}else{
				listSessionList = sqlClient.queryForList("sessionList");
			}
			tableViewerSessionList.setInput(listSessionList);
			tableViewerSessionList.refresh();
		} catch (Exception e) {
			logger.error("initialize session list", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * create column
	 */
	private void createColumn() {
		String[] name = {"PID", "User", "Host", "Database", "Command", "Time", "State", "Info"};
		int[] size = {70, 70, 150, 70, 70, 100, 150, 200};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tableViewerSessionList, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tableViewerSessionList, comparator, tableColumn.getColumn(), i));
		}
	}
	
	/**
	 * table sorter
	 * 
	 * @param comparator
	 * @param viewer
	 * @param column
	 * @param index
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewer viewer, final ObjectComparator comparator, final TableColumn column, final int index) {
		SelectionAdapter adapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				viewer.getTable().setSortDirection(comparator.getDirection());
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		
		return adapter;
	}

	@Override
	public void setFocus() {
		tableViewerSessionList.getTable().setFocus();
	}

}

