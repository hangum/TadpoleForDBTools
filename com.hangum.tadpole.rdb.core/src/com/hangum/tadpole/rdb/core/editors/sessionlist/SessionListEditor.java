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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
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
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.SessionListDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.editors.sessionlist.composite.mysql.MySQLSessionListLabelProvider;
import com.hangum.tadpole.rdb.core.editors.sessionlist.composite.mysql.MySQLSessionListTableCompare;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.SWTResourceManager;

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
	private static final Logger logger = Logger.getLogger(SessionListEditor.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.sessionlist"; //$NON-NLS-1$
	private SashForm mainSashForm;
	
	protected final int user_seq = SessionManager.getUserSeq();
	
	private boolean isThread = true;
	private final ServerPushSession pushSession = new ServerPushSession();

	// set initialize button
	private ToolItem tltmStart;
	private ToolItem tltmStop;
	private ToolItem tltmKillProcess;
	private boolean isNotRefreshUi = false;
	
	private UserDBDAO userDB;
	
	private TableViewer tableViewerSessionList;
	private ObjectComparator comparator;
	private Text textQuery;
	private TableViewer tableViewerLocks;
	private TableViewer tableViewerBlock;
	private Table tableLocks;
	private List<HashMap> showLocksList;
	private List<HashMap> showLockBlockList;
	private Table table;
	private Text textSQL;
	private Text textRefreshMil;
	private Button btnSessionLocks;
	private Button btnAllLocks;
	
	/** SESSION INTERVAL */
	private int SESSION_INTERVAL = 10;

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
		
		mainSashForm = new SashForm(parent, SWT.NONE);
		mainSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createSessionUI();
		
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT||
				//userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT||
				userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT||
				userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			createOracleExtensionUI();
			mainSashForm.setWeights(new int[] {1, 1});
		}
	}
	
	/**
	 * crate extension oracle 
	 */
	private void createOracleExtensionUI() {
		Composite compositeExtension = new Composite(mainSashForm, SWT.NONE);
		compositeExtension.setLayout(new GridLayout(1, false));
		
		SashForm sashFormExtension = new SashForm(compositeExtension, SWT.VERTICAL);
		sashFormExtension.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeExtHead = new Composite(sashFormExtension, SWT.NONE);
		compositeExtHead.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(compositeExtHead, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		btnAllLocks = new Button(composite, SWT.RADIO);
		btnAllLocks.setText("All Locks");
		btnAllLocks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshLocksList("");
			}
		});
		
		btnSessionLocks = new Button(composite, SWT.RADIO);
		btnSessionLocks.setSelection(true);
		btnSessionLocks.setText("Session Locks");
		
		tableViewerLocks = new TableViewer(compositeExtHead, SWT.BORDER | SWT.FULL_SELECTION);
		tableLocks = tableViewerLocks.getTable();
		tableLocks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tableViewerLocks.getSelection().isEmpty()) return;
				
				StructuredSelection ss = (StructuredSelection)tableViewerLocks.getSelection();
				HashMap map = (HashMap)ss.getFirstElement();
				if(null != map) {					
					if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT||userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT){
						refreshLocksBlockList((String)map.get("lock_trx_id"), "");
					}else{
						refreshLocksBlockList((String)map.get("LOCK_ID1"), (String)map.get("LOCK_ID2"));
					}
				}
				
			}
		});
		tableLocks.setHeaderVisible(true);
		tableLocks.setLinesVisible(true);
		tableLocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createLocksTableColumn();
		
		Composite compositeExtBody = new Composite(sashFormExtension, SWT.NONE);
		compositeExtBody.setLayout(new GridLayout(1, false));
		
		Label lblBlockedBlocking = new Label(compositeExtBody, SWT.NONE);
		lblBlockedBlocking.setText("Blocked && Blocking");
		
		tableViewerBlock = new TableViewer(compositeExtBody, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewerBlock.getTable();
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tableViewerBlock.getSelection().isEmpty()) return;
				
				StructuredSelection ss = (StructuredSelection)tableViewerBlock.getSelection();
				HashMap map = (HashMap)ss.getFirstElement();
				if(null != map) {
					textSQL.setText(StringUtils.trimToEmpty((String)map.get("SQL_TEXT")));
				}else{
					textSQL.setText(StringUtils.EMPTY);
				}								
			}
		});
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		textSQL = new Text(compositeExtBody, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textSQL.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		GridData gd_textSQL = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textSQL.heightHint = 80;
		textSQL.setLayoutData(gd_textSQL);
		sashFormExtension.setWeights(new int[] {1, 1});
		
		createLocksBlockTableColumn();
	}
	
	private void createLocksTableColumn() {
		TableViewColumnDefine[] tableColumnDef = null;
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT||userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT){
			this.btnAllLocks.setEnabled(true);
			this.btnSessionLocks.setEnabled(true);

			tableColumnDef = new TableViewColumnDefine[] { new TableViewColumnDefine("SID", "Session ID", 80, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("USERNAME", "User Name", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("LOCK_TYPE", "Lock Type", 80, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("MODE_HELD", "Mode Held", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("MODE_REQUESTED", "Mode Requested", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("OWNER", "Owner", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("OBJECT_TYPE", "Object Type", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("OBJECT_NAME", "Object Name", 100, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("BLOCK", "Blocking", 60, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("LOCKWAIT", "Blocked", 60, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("OSUSER", "OS User", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("MACHINE", "Machine", 100, SWT.LEFT) //$NON-NLS-1$
			};
		}else if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT||userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT){
			this.btnAllLocks.setEnabled(false);
			this.btnSessionLocks.setEnabled(false);
			
			tableColumnDef = new TableViewColumnDefine[] { new TableViewColumnDefine("lock_id", "Lock ID", 80, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("lock_trx_id", "Transaction ID", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("lock_mode", "Lock mode", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("lock_type", "Lock type", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("lock_table", "Lock table", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("lock_index", "Lock index", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("lock_space", "Lock space", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("lock_page", "Lock page", 100, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("lock_rec", "Lock rec", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("lock_data", "Lock data", 60, SWT.LEFT) //$NON-NLS-1$
			};			
		}


		ColumnHeaderCreator.createColumnHeader(tableViewerLocks, tableColumnDef);

		tableViewerLocks.setContentProvider(new ArrayContentProvider());
		tableViewerLocks.setLabelProvider(new SessionLocksLabelProvider(tableViewerLocks));
	}
	
	private void createLocksBlockTableColumn() {
		TableViewColumnDefine[] tableColumnDef = null;
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT||userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT){
			tableColumnDef = new TableViewColumnDefine[] { new TableViewColumnDefine("SID", "Session ID", 80, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("USERNAME", "User Name", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("BLOCK_TYPE", "Block Type", 80, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("STATUS", "Status", 80, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("SQL_TEXT", "SQL", 300, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("LOCK_TYPE", "Lock Type", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("MODE_HELD", "Mode Held", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("MODE_REQUESTED", "Mode Requested", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("OSUSER", "OS User", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("MACHINE", "Machine", 100, SWT.LEFT) //$NON-NLS-1$
			};
		}else if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT||userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT){
				tableColumnDef = new TableViewColumnDefine[] { new TableViewColumnDefine("trx_id", "Transaction ID", 80, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_state", "State", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_started", "Started", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_requested_lock_id", "Requested lock", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_wait_started", "Wait started", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_weight", "Weight", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_mysql_thread_id", "Thread ID", 80, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("SQL_TEXT", "SQL", 100, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_operation_state", "Operation state", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_tables_in_use", "In use", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_tables_locked", "Locked", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_lock_structs", "Structs", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_lock_memory_bytes", "Memory bytes", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_rows_locked", "Rows locked", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_rows_modified", "Rows modified", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_concurrency_tickets", "Concur..Tickets", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_isolation_level", "Isolation Level", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_unique_checks", "Unique check", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_foreign_key_checks", "ForeignKey check", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_last_foreign_key_error", "ForeignKey error", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_adaptive_hash_latched", "Hash latched", 60, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("trx_adaptive_hash_timeout", "Hash timeout", 60, SWT.LEFT) //$NON-NLS-1$
			};			
		}

		ColumnHeaderCreator.createColumnHeader(tableViewerBlock, tableColumnDef);

		tableViewerBlock.setContentProvider(new ArrayContentProvider());
		tableViewerBlock.setLabelProvider(new SessionLocksLabelProvider(tableViewerBlock));
	}
	
	public void refreshLocksList(String sid) {
		try {

			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			Map<String, String> param = new HashMap<String, String>();
			param.put("sid", StringUtils.replace(sid, ",", ""));
			showLocksList = (List<HashMap>) sqlClient.queryForList("getLockList", param); //$NON-NLS-1$

			if (showLocksList!=null){
				tableViewerLocks.setInput(showLocksList);
				tableViewerLocks.refresh();
			}
			tableViewerBlock.setInput(new ArrayList<HashMap>());
			tableViewerBlock.refresh();

		} catch (Exception e) {
			logger.error("refresh list", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
		}
	}	
	
	public void refreshLocksBlockList(String lock_id1, String lock_id2) {
		try {

			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			Map<String, String> param = new HashMap<String, String>();
			param.put("lock_id1", StringUtils.replace(lock_id1, ",", ""));
			param.put("lock_id2", StringUtils.replace(lock_id2, ",", ""));
			showLockBlockList = (List<HashMap>) sqlClient.queryForList("getLockBlockList", param); //$NON-NLS-1$

			tableViewerBlock.setInput(showLockBlockList);
			tableViewerBlock.refresh();

		} catch (Exception e) {
			logger.error("refresh list", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * 기본 세션 모니터링 화면을 조회 합니다.
	 */
	private void createSessionUI() {
		Composite compositSessionUI = new Composite(mainSashForm, SWT.NONE);
		compositSessionUI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(1, false);
		gl_compositeHead.marginHeight = 0;
		gl_compositeHead.horizontalSpacing = 0;
		gl_compositeHead.marginWidth = 0;
		compositSessionUI.setLayout(gl_compositeHead);
		
		
		Composite compositeSessionHead = new Composite(compositSessionUI, SWT.NONE);
		compositeSessionHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeSessionHead.setLayout(new GridLayout(2, false));
		
		Composite compositeSessionBody = new Composite(compositSessionUI, SWT.NONE);
		compositeSessionBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		textRefreshMil = new Text(compositeSessionHead, SWT.BORDER);
		textRefreshMil.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				validateInterval();
			}
		});
		textRefreshMil.setText("10");
		GridData gd_textRefreshMil = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_textRefreshMil.widthHint = 30;
		gd_textRefreshMil.minimumWidth = 30;
		textRefreshMil.setLayoutData(gd_textRefreshMil);
		
		ToolBar toolBar = new ToolBar(compositeSessionHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem tltmSecondsRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmSecondsRefresh.setText(Messages.get().SessionListEditor_4);
		
		tltmStart = new ToolItem(toolBar, SWT.NONE);
		tltmStart.setToolTipText(Messages.get().Start);
		tltmStart.setImage(GlobalImageUtils.getStart()); //$NON-NLS-1$
		tltmStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validateInterval()) return;
				
				isNotRefreshUi = true;
				
				tltmStart.setEnabled(false);
				tltmStop.setEnabled(true);
				
				initSessionListData();
			}
		});
		tltmStart.setEnabled(true);
		
		tltmStop = new ToolItem(toolBar, SWT.NONE);
		tltmStop.setToolTipText(Messages.get().Stop);
		tltmStop.setImage(GlobalImageUtils.getStop());
		tltmStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isNotRefreshUi = false;
				
				tltmStart.setEnabled(true);
				tltmStop.setEnabled(false);
			} 
		});
		tltmStop.setEnabled(false);
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setToolTipText(Messages.get().Refresh);
		tltmRefresh.setImage(GlobalImageUtils.getRefresh());
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initSessionListData();
			} 
		});
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tltmKillProcess = new ToolItem(toolBar, SWT.NONE);
		tltmKillProcess.setToolTipText(Messages.get().SessionListEditor_3);
		tltmKillProcess.setImage(GlobalImageUtils.getKilling());
		tltmKillProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isPossible = false;
				if(PermissionChecker.isDBAdminRole(userDB)) isPossible = true;
				else {
					if(!PermissionChecker.isProductBackup(userDB)) isPossible = true;
				}
				
				if(isPossible) {
					killProcess();
				} else {
					MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.get().Warning, Messages.get().MainEditor_21);
				}
			}
		});
		tltmKillProcess.setEnabled(false);
		compositeSessionBody.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(compositeSessionBody, SWT.VERTICAL);
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
					refreshLocksList( sl.getSID());
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
		compositeQuery.setText(Messages.get().Query);
		
		textQuery = new Text(compositeQuery, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comparator = new MySQLSessionListTableCompare();
		tableViewerSessionList.setSorter(comparator);
		
		createColumn();
		
		tableViewerSessionList.setContentProvider(new ArrayContentProvider());
		tableViewerSessionList.setLabelProvider(new MySQLSessionListLabelProvider());
		
		sashForm.setWeights(new int[] {7, 3});
		
		pushSession.start();
		Thread thread = new Thread(startUIThread());
		thread.setDaemon(true);
		thread.start();	
		
		initSessionListData();
	}
	
	/**
	 * check validate interval
	 * @return
	 */
	private boolean validateInterval() {
		String strRefreshTerm = textRefreshMil.getText();
		if(!NumberUtils.isNumber(strRefreshTerm)) {
			MessageDialog.openInformation(null, Messages.get().Information, Messages.get().SessionListEditor_GreatThan10Sec);
			return false;
		} else if(Integer.parseInt(strRefreshTerm) < 10) {
			MessageDialog.openInformation(null, Messages.get().Information, Messages.get().SessionListEditor_GreatThan10Sec);
			return false;
		}
		
		SESSION_INTERVAL = Integer.parseInt(strRefreshTerm);
		return true;
	}

	/**
	 *  start ui thread
	 *  
	 * @return
	 */
	private Runnable startUIThread() {
		final Display display = tltmStart.getDisplay();

		Runnable bgRunnable = new Runnable() {
			@Override
			public void run() {

				while(isThread) {
					display.syncExec(new Runnable() {
						@Override
						public void run() {
							if(isNotRefreshUi) initSessionListData();
						}
					});
					
					// 20 seconds
					try{ Thread.sleep(1000 * SESSION_INTERVAL); } catch(Exception e) {}
						
				}	// end while 
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
		
		if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().SessionListEditor_8)) return;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			if (userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
				client.queryForObject("killProcess", Integer.parseInt(sl.getId())); //$NON-NLS-1$
			} else if (userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT){
				 Map<String, String> parameters = new HashMap<String, String>(2);
				 parameters.put("dbname", sl.getDb());
				 parameters.put("session_id", sl.getId());
				 
				 client.queryForObject("killProcess", parameters);
			} else {
				client.queryForObject("killProcess", sl.getId()); //$NON-NLS-1$
			}
			
			initSessionListData();
		} catch(Exception e) {
			logger.error("killprocess exception", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
		}	
	}
	
	/**
	 * editor init data
	 */
	private void initSessionListData() {
		
		// 버튼을 초기화 합니다.
		tltmKillProcess.setEnabled(false);
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);

			List<?> listSessionList = null;
			if (userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
				int getSessionGrant = (Integer) sqlClient.queryForObject("getSessionGrant"); //$NON-NLS-1$
				if (0 >= getSessionGrant){
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "In order to display a list of the session , you want to manage, and requires a authority.", null); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().SessionListEditor_13, errStatus); //$NON-NLS-1$
					return;
				}
				
				try {
					int getSessionView = (Integer) sqlClient.queryForObject("getSessionView"); //$NON-NLS-1$
				}catch (Exception e) {
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().SessionListEditor_15, errStatus); //$NON-NLS-1$
					return;
				}
				
				int version = (Integer) sqlClient.queryForObject("getVersion");				 //$NON-NLS-1$
				if (version <= 9){
					listSessionList = sqlClient.queryForList("sessionList_9"); //$NON-NLS-1$
				}else{
					listSessionList = sqlClient.queryForList("sessionList"); //$NON-NLS-1$
				}
			} else if(userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT) {
				int getSessionGrant = (Integer) sqlClient.queryForObject("getSessionGrant"); //$NON-NLS-1$
				if (0 >= getSessionGrant){
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "In order to display a list of the session , you want to manage, and requires a authority.", null); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().SessionListEditor_13, errStatus); //$NON-NLS-1$
					return;
				}
				
				try {
					int getSessionView = (Integer) sqlClient.queryForObject("getSessionView"); //$NON-NLS-1$
				}catch (Exception e) {
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().SessionListEditor_15, errStatus); //$NON-NLS-1$
					return;
				}
				
				listSessionList = sqlClient.queryForList("sessionList"); //$NON-NLS-1$
			}else{
				listSessionList = sqlClient.queryForList("sessionList"); //$NON-NLS-1$
			}
			
			tableViewerSessionList.setInput(listSessionList);
			tableViewerSessionList.refresh();
		} catch (Exception e) {
			logger.error("initialize session list", e); //$NON-NLS-1$
			
			exitSession();
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
		}
	}
	
	private void exitSession() {
		isThread = true;
		isNotRefreshUi = false;
		tltmStart.setEnabled(false);
		tltmStop.setEnabled(false);
		pushSession.stop();
	}
	
	/**
	 * create column
	 */
	private void createColumn() {
		String[] name = {Messages.get().PID, Messages.get().User, Messages.get().Host, Messages.get().Database, Messages.get().Command, Messages.get().Time, Messages.get().State, Messages.get().Info};
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

