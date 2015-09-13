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
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.SessionListDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.sessionlist.composite.mysql.MySQLSessionListLabelProvider;
import com.hangum.tadpole.rdb.core.editors.sessionlist.composite.mysql.MySQLSessionListTableCompare;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.session.manager.SessionManager;
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

	public static final String ID = "com.hangum.tadpole.rdb.core.editor.sessionlist"; //$NON-NLS-1$
	
	protected final int user_seq = SessionManager.getUserSeq();
	
	private boolean isThread = true;
	private final ServerPushSession pushSession = new ServerPushSession();

	// set initialize button
	private ToolItem tltmStart;
	private ToolItem tltmStop;
	private boolean isNotRefreshUi = true;
	
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
		
		tltmStart = new ToolItem(toolBar, SWT.NONE);
		tltmStart.setToolTipText(Messages.SessionListEditor_1);
		tltmStart.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/start.png")); //$NON-NLS-1$
		tltmStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isNotRefreshUi = true;
				
				tltmStart.setEnabled(false);
				tltmStop.setEnabled(true);
			}
		});
		tltmStart.setEnabled(false);
		
		tltmStop = new ToolItem(toolBar, SWT.NONE);
		tltmStop.setToolTipText(Messages.SessionListEditor_2);
		tltmStop.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/stop.png")); //$NON-NLS-1$
		tltmStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isNotRefreshUi = false;
				
				tltmStart.setEnabled(true);
				tltmStop.setEnabled(false);
			} 
		});
		
		final ToolItem tltmKillProcess = new ToolItem(toolBar, SWT.NONE);
		tltmKillProcess.setToolTipText(Messages.SessionListEditor_3);
		tltmKillProcess.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/kill_process.png")); //$NON-NLS-1$
		tltmKillProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				killProcess();
			}
		});
		tltmKillProcess.setEnabled(false);
		
		ToolItem tltmSecondsRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmSecondsRefresh.setEnabled(false);
		tltmSecondsRefresh.setSelection(true);
		tltmSecondsRefresh.setText(Messages.SessionListEditor_4);
		
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
					textQuery.setText(""); //$NON-NLS-1$
				}
			}
		});
		Table tableSessionList = tableViewerSessionList.getTable();
		tableSessionList.setHeaderVisible(true);
		tableSessionList.setLinesVisible(true);
		tableSessionList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group compositeQuery = new Group(sashForm, SWT.NONE);
		compositeQuery.setLayout(new GridLayout(1, false));
		compositeQuery.setText(Messages.SessionListEditor_6);
		
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
		
		callbackui();
	}
	
	private void callbackui() {
		pushSession.start();
		Thread thread = new Thread(startUIThread());
		thread.setDaemon(true);
		thread.start();
	}
	
	private Runnable startUIThread() {
		final String email = SessionManager.getEMAIL();
		final Display display = PlatformUI.getWorkbench().getDisplay();// tvError.getTable().getDisplay();

		Runnable bgRunnable = new Runnable() {
			@Override
			public void run() {

				while(isThread) {
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							
							if(isNotRefreshUi) initSessionListData();
						}
					});	
						
					// 20 seconds
					try{ Thread.sleep(1000 * 5); } catch(Exception e) {}
				}
			};
		};

		return bgRunnable;
	}
	
	/**
	 * kill process
	 */
	private void killProcess() {
		StructuredSelection ss = (StructuredSelection)tableViewerSessionList.getSelection();
		SessionListDAO sl = (SessionListDAO)ss.getFirstElement();
		
		if(!MessageDialog.openConfirm(null, Messages.SessionListEditor_7, Messages.SessionListEditor_8)) return;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			if (DBDefine.getDBDefine(userDB) == DBDefine.POSTGRE_DEFAULT) {
				client.queryForObject("killProcess", Integer.parseInt(sl.getId())); //$NON-NLS-1$
			} else {
				client.queryForObject("killProcess", sl.getId()); //$NON-NLS-1$
			}
			
			initSessionListData();
		} catch(Exception e) {
			logger.error("killprocess exception", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
		}	
	}
	
	/**
	 * editor init data
	 */
	private void initSessionListData() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);

			List<?> listSessionList = null;
			if (DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT) {

				int getSessionGrant = (Integer) sqlClient.queryForObject("getSessionGrant"); //$NON-NLS-1$
				if (0 >= getSessionGrant){
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "In order to display a list of the session , you want to manage, and requires a authority.", null); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.SessionListEditor_13, errStatus); //$NON-NLS-1$
					return;
				}
				
				try {
					int getSessionView = (Integer) sqlClient.queryForObject("getSessionView"); //$NON-NLS-1$
				}catch (Exception e) {
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.SessionListEditor_15, errStatus); //$NON-NLS-1$
					return;
				}
				
				int version = (Integer) sqlClient.queryForObject("getVersion");				 //$NON-NLS-1$
				if (version <= 9){
					listSessionList = sqlClient.queryForList("sessionList_9"); //$NON-NLS-1$
				}else{
					listSessionList = sqlClient.queryForList("sessionList"); //$NON-NLS-1$
				}
			}else{
				listSessionList = sqlClient.queryForList("sessionList"); //$NON-NLS-1$
			}
			
			tableViewerSessionList.setInput(listSessionList);
			tableViewerSessionList.refresh();
		} catch (Exception e) {
			logger.error("initialize session list", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
		}
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
	}
	
	/**
	 * create column
	 */
	private void createColumn() {
		String[] name = {Messages.SessionListEditor_21, Messages.SessionListEditor_22, Messages.SessionListEditor_23, Messages.SessionListEditor_24, Messages.SessionListEditor_25, Messages.SessionListEditor_26, Messages.SessionListEditor_27, Messages.SessionListEditor_28};
		int[] size = {70, 70, 150, 70, 70, 100, 50, 200};

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

	@Override
	public void dispose() {
		super.dispose();
		
		isThread = false;
		pushSession.stop();
	}
}

