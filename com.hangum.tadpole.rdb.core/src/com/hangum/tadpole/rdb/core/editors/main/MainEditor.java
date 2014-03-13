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
package com.hangum.tadpole.rdb.core.editors.main;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.dialogs.help.RDBShortcutHelpDialog;
import com.hangum.tadpole.ace.editor.core.texteditor.EditorExtension;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.texteditor.function.IEditorFunction;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.commons.util.ShortcutPrefixUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.dialog.export.SQLToStringDialog;
import com.hangum.tadpole.rdb.core.editors.main.composite.RDBResultComposite;
import com.hangum.tadpole.rdb.core.editors.main.function.MainEditorBrowserFunctionService;
import com.hangum.tadpole.rdb.core.util.CubridExecutePlanUtils;
import com.hangum.tadpole.rdb.core.util.OracleExecutePlanUtils;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.sql.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.sql.dialog.save.ResourceSaveDialog;
import com.hangum.tadpole.sql.format.SQLFormater;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.sql.system.permission.PermissionChecker;
import com.hangum.tadpole.sql.util.PartQueryUtil;
import com.hangum.tadpole.sql.util.ResultSetUtilDAO;
import com.hangum.tadpole.sql.util.ResultSetUtils;
import com.hangum.tadpole.sql.util.SQLUtil;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

/**
 * 쿼리 수행 및 검색 창.
 * 
 * @author hangum
 *
 */
public class MainEditor extends EditorExtension {
	/** Editor ID. */
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.main"; //$NON-NLS-1$
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(MainEditor.class);
	
	/** toolbar auto commit */
	private ToolItem tiAutoCommit = null, tiAutoCommitCommit = null, tiAutoCommitRollback = null;

	/** result tab */
	private RDBResultComposite compositeResult;

	/** edior가 초기화 될때 처음 로드 되어야 하는 String. */
	private String initDefaultEditorStr = ""; //$NON-NLS-1$
	/** resource 정보. */
	private UserDBResourceDAO dBResource;
	/** first save UserDBResource object */
	private UserDBResourceDAO userSetDBResource; //$NON-NLS-1$
	
	/** save mode */
	private boolean isDirty = false;
	
	/** 쿼리 호출 후 결과 dao */
	private ResultSetUtilDAO rsDAO = new ResultSetUtilDAO();
	    
	public MainEditor() {
		super();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		MainEditorInput qei = (MainEditorInput)input;
		userDB = qei.getUserDB();
		initDefaultEditorStr = qei.getDefaultStr();

		strRoleType = SessionManager.getRoleType(userDB.getGroup_seq());
		dBResource = qei.getResourceDAO();
		if(dBResource == null) setPartName(qei.getName());
		else  setPartName(dBResource.getName());
	}
	
	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		if(dBResource == null) return false;
		else return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginHeight = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setSashWidth(4);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		final Composite compositeEditor = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeEditor = new GridLayout(1, false);
		gl_compositeEditor.verticalSpacing = 0;
		gl_compositeEditor.horizontalSpacing = 0;
		gl_compositeEditor.marginHeight = 0;
		gl_compositeEditor.marginWidth = 0;
		compositeEditor.setLayout(gl_compositeEditor);
		
		ToolBar toolBar = new ToolBar(compositeEditor, SWT.NONE | SWT.FLAT | SWT.RIGHT);
		toolBar.setToolTipText(String.format(Messages.MainEditor_toolBar_toolTipText, ShortcutPrefixUtils.getCtrlShortcut()));
		
		ToolItem tltmConnectURL = new ToolItem(toolBar, SWT.NONE);
		tltmConnectURL.setToolTipText("Connection Information");
		tltmConnectURL.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/connect.png")); //$NON-NLS-1$
		
		if(PermissionChecker.isShow(SessionManager.getRepresentRole())) {
			if(DBDefine.getDBDefine(userDB) == DBDefine.SQLite_DEFAULT ) {
				String fileName = new File(userDB.getDb()).getName();			
				tltmConnectURL.setText(String.format(userDB.getDbms_types() + " - %s", fileName));
			} else {
				tltmConnectURL.setText(String.format(userDB.getDbms_types() + " - %s:%s", userDB.getHost(), userDB.getUsers()));
			}	
		} else {
			tltmConnectURL.setText(userDB.getDbms_types());
		}
		
		tltmConnectURL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBInformationDialog dialog = new DBInformationDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB);
				dialog.open();
				setFocus();
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
		tltmExecute.setToolTipText(String.format(Messages.MainEditor_tltmExecute_toolTipText_1, ShortcutPrefixUtils.getCtrlShortcut()));
		tltmExecute.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql-query.png")); //$NON-NLS-1$
		tltmExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.SELECTED_TEXT, SQLDefine.QUERY_DELIMITER); //$NON-NLS-1$
				
				RequestQuery reqQuery = new RequestQuery(strQuery, EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.NONE, isAutoCommit());
				executeCommand(reqQuery);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecuteAll = new ToolItem(toolBar, SWT.NONE);
		tltmExecuteAll.setToolTipText(Messages.MainEditor_tltmExecuteAll_text);
		tltmExecuteAll.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql-query-all.png")); //$NON-NLS-1$
		tltmExecuteAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
				
				RequestQuery reqQuery = new RequestQuery(strQuery, EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.ALL, isAutoCommit());
				executeCommand(reqQuery);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
	
		ToolItem tltmExplainPlanctrl = new ToolItem(toolBar, SWT.NONE);
		tltmExplainPlanctrl.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/execute_plan.png")); //$NON-NLS-1$
		tltmExplainPlanctrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.SELECTED_TEXT, SQLDefine.QUERY_DELIMITER); //$NON-NLS-1$
				
				RequestQuery reqQuery = new RequestQuery(strQuery, EditorDefine.QUERY_MODE.EXPLAIN_PLAN, EditorDefine.EXECUTE_TYPE.NONE, isAutoCommit());
				executeCommand(reqQuery);
				
			}
		});
		tltmExplainPlanctrl.setToolTipText(String.format(Messages.MainEditor_3, ShortcutPrefixUtils.getCtrlShortcut()));
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSort = new ToolItem(toolBar, SWT.NONE);
		tltmSort.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/query_format.png")); //$NON-NLS-1$
		tltmSort.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
				
				try {
					browserEvaluate(EditorFunctionService.RE_NEW_TEXT, SQLFormater.format(strQuery));
				} catch(Exception ee) {
					logger.error("sql format", ee); //$NON-NLS-1$
				}
			}
		});
		tltmSort.setToolTipText(String.format(Messages.MainEditor_4, ShortcutPrefixUtils.getCtrlShortcut()));
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSQLToApplication = new ToolItem(toolBar, SWT.NONE);
		tltmSQLToApplication.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql_to_applications.png")); //$NON-NLS-1$
		tltmSQLToApplication.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
				
				SQLToStringDialog dialog = new SQLToStringDialog(null, EditorDefine.SQL_TO_APPLICATION.Java_StringBuffer.toString(), strQuery);
				dialog.open();
				setFocus();
			}
		});
	    tltmSQLToApplication.setToolTipText("SQL statement to Application code");
	    new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmDownload = new ToolItem(toolBar, SWT.NONE);
		tltmDownload.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/download_query.png")); //$NON-NLS-1$
		tltmDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!MessageDialog.openConfirm(null, Messages.MainEditor_38, Messages.MainEditor_39)) return;
		
				try {
					String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
					compositeResult.downloadExtFile(getUserDB().getDisplay_name()+".sql", strQuery); //$NON-NLS-1$
				} catch(Exception ee) {
					logger.error("Download SQL", ee); //$NON-NLS-1$
				}
			}
		});
		tltmDownload.setToolTipText(Messages.MainEditor_42);
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tiAutoCommit = new ToolItem(toolBar, SWT.CHECK);
		tiAutoCommit.setSelection(false);
		tiAutoCommit.setText("Transaction start");
		tiAutoCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initAutoCommitAction(false, true);
			}
		});
		
		tiAutoCommitCommit = new ToolItem(toolBar, SWT.NONE);
		tiAutoCommitCommit.setSelection(false);
		tiAutoCommitCommit.setText("Commit");
		tiAutoCommitCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(logger.isDebugEnabled()) logger.debug("[set commit][user id]" + strUserEMail + "[user id]" + userDB); //$NON-NLS-1$ //$NON-NLS-2$
				
				TadpoleSQLTransactionManager.commit(strUserEMail, userDB);
			}
		});
		
		tiAutoCommitRollback = new ToolItem(toolBar, SWT.NONE);
		tiAutoCommitRollback.setSelection(false);
		tiAutoCommitRollback.setText("Rollback");
		tiAutoCommitRollback.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(logger.isDebugEnabled()) logger.debug("[set rollback][user id]" + strUserEMail + "[user id]" + userDB); //$NON-NLS-1$ //$NON-NLS-2$
				
				TadpoleSQLTransactionManager.rollback(strUserEMail, userDB);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmHelp = new ToolItem(toolBar, SWT.NONE);
		tltmHelp.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/about.png")); //$NON-NLS-1$
		tltmHelp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RDBShortcutHelpDialog dialog = new RDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
				dialog.open();
				
				setFocus();
			}
		});
		tltmHelp.setToolTipText(String.format(Messages.MainEditor_27, ShortcutPrefixUtils.getCtrlShortcut()));
	    ////// tool bar end ///////////////////////////////////////////////////////////////////////////////////
	    
	    ////// orion editor start /////////////////////////////////////////////////////////////////////////////
	    browserQueryEditor = new Browser(compositeEditor, SWT.BORDER);
	    browserQueryEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));	    
	    
	    addBrowserService();
	    
	    Composite compositeSashForm = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.horizontalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 0;
		compositeSashForm.setLayout(gl_compositeResult);
	    
	    compositeResult = new RDBResultComposite(compositeSashForm, SWT.NONE);
	    compositeResult.setLayout(gl_compositeResult);
	    compositeResult.setMainEditor(this);
		
		sashForm.setWeights(new int[] {65, 35});
		initEditor();
		
		// autocommit true 혹은 false값이 바뀌었을때..
		PlatformUI.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {

				if (event.getProperty() == PublicTadpoleDefine.AUTOCOMMIT_USE) {
					String strAutoCommit_seq = event.getNewValue().toString();
					// UserDB.seq || auto commit ture or false 
					String[] arryVal = StringUtils.split(strAutoCommit_seq, "||"); //$NON-NLS-1$
					int seq = Integer.parseInt(arryVal[0]);
					boolean boolUseAutocommit = Boolean.parseBoolean(arryVal[1]);

					if(!tiAutoCommit.isDisposed()) {
						if(seq == userDB.getSeq()) {
							tiAutoCommit.setSelection(boolUseAutocommit);
							if(!boolUseAutocommit) {
//								tiAutoCommit.setToolTipText("Transaction Enable");
								tiAutoCommitCommit.setEnabled(false);
								tiAutoCommitRollback.setEnabled(false);
							} else {
//								tiAutoCommit.setToolTipText("Transaction Disable");
								tiAutoCommitCommit.setEnabled(true);
								tiAutoCommitRollback.setEnabled(true);
							}
						}	// end tltmAutoCommit
					}	// end seq
				} // end if(event.getProperty()
			} //
		}); // end property change		
	}
	
	public Browser getBrowserQueryEditor() {
		return browserQueryEditor;
	}
	
	/**
	 * append text at position
	 * @param strText
	 */
	public void appendTextAtPosition(String strText) {
		try {
			browserEvaluate(EditorFunctionService.INSERT_TEXT, strText);
		} catch(Exception ee){
			logger.error("query text at position" , ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * append text at position
	 * 
	 * @param strText
	 */
	public void appendText(String strText) {
		try {
			browserEvaluate(EditorFunctionService.APPEND_TEXT, strText);
		} catch(Exception ee){
			logger.error("query text" , ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * browser handler
	 */
	protected void addBrowserService() {
//		if(DBOperationType.valueOf(userDB.getOperation_type()) == DBOperationType.PRODUCTION) {
	    	browserQueryEditor.setUrl(REAL_DB_URL);
//	    } else {
//	    	browserQueryEditor.setUrl(DEV_DB_URL);
//	    }
		registerBrowserFunctions();
		
		browserQueryEditor.addProgressListener(new ProgressListener() {
			@Override
			public void completed( ProgressEvent event ) {
				try {
//					content assist기능에 테이블 정보 넣는 것은 잠시 보류합니다.
					browserEvaluate(IEditorFunction.INITIALIZE, EditorDefine.EXT_SQL, "", getInitDefaultEditorStr()); //$NON-NLS-1$
				} catch(Exception ee) {
					logger.error("rdb editor initialize ", ee); //$NON-NLS-1$
				}
			}
			public void changed( ProgressEvent event ) {}			
		});
	}
	
//	/**
//	 * 에디터에서 assist창에 보여줄 목록을 가져옵니다.
//	 * 
//	 * @return
//	 */
//	private String getAssistList() {
//		String strTablelist = ""; //$NON-NLS-1$
//		
//		try {
//			List<TableDAO> showTables = null;
//			if(userDB.getDBDefine() != DBDefine.TAJO_DEFAULT) {
//				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
//				showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
//			} else {
//				showTables = new TajoConnectionManager().tableList(userDB);
//			}
//
//			for (TableDAO tableDao : showTables) {
//				strTablelist += tableDao.getName() + "|"; //$NON-NLS-1$
//			}
//			strTablelist = StringUtils.removeEnd(strTablelist, "|"); //$NON-NLS-1$
//			
//		} catch(Exception e) {
//			logger.error("MainEditor get the table list", e); //$NON-NLS-1$
//		}
//		
//		return strTablelist;
//	}

	/**
	 * initialize editor
	 */
	private void initEditor() {
		if (userDB.getDBDefine() == DBDefine.HIVE_DEFAULT || userDB.getDBDefine() == DBDefine.TAJO_DEFAULT) {
			tiAutoCommit.setEnabled(false);
		}

		if("YES".equals(userDB.getIs_autocommit())) { //$NON-NLS-1$
			tiAutoCommit.setSelection(false);
		} else {
			tiAutoCommit.setSelection(true);
		}
		
		// 기존 에디터에서 auto commit button 이 어떻게 설정 되어 있는지 가져옵니다.
		initAutoCommitAction(true, false);
	}
	
	/**
	 * init auto commit button
	 * 
	 * @param isFirst
	 * @param isRiseEvent
	 */
	private void initAutoCommitAction(boolean isFirst, boolean isRiseEvent) {
		if(isAutoCommit()) {
//			tiAutoCommit.setToolTipText("Transaction Enable"); //$NON-NLS-1$
			tiAutoCommitCommit.setEnabled(false);
			tiAutoCommitRollback.setEnabled(false);
			
			if(!isFirst) {
				if(MessageDialog.openConfirm(null, Messages.MainEditor_30, Messages.MainEditor_47)) {
					TadpoleSQLTransactionManager.commit(strUserEMail, userDB);
				} else {
					TadpoleSQLTransactionManager.rollback(strUserEMail, userDB);
				}
			}
		} else {
//			tiAutoCommit.setToolTipText("Transaction Disable"); //$NON-NLS-1$
			tiAutoCommitCommit.setEnabled(true);
			tiAutoCommitRollback.setEnabled(true);
		}
		
		if(isRiseEvent) {
			// auto commit의 실행버튼을 동일한 db를 열고 있는 에디터에서 공유합니다.
			PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.AUTOCOMMIT_USE, userDB.getSeq() + "||" + tiAutoCommit.getSelection() + "||" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
	
	/**
	 * 에디터를 실행 후에 마지막으로 실행해 주어야 하는 코드.
	 */
	private void finallyEndExecuteCommand() {
		browserEvaluate(EditorFunctionService.EXECUTE_DONE);
	}
	
	/**
	 * 쿼리를 수행합니다.
	 */
	public void executeCommand(final RequestQuery reqQuery) {
		try {
			if("".equals(reqQuery.getSql().trim())) return;
			
			// 실행해도 되는지 묻는다.
			boolean isDMLQuestion = false;
			if(reqQuery.getType() == EditorDefine.EXECUTE_TYPE.ALL) {						
				for (String strSQL : reqQuery.getOriginalSql().split(PublicTadpoleDefine.SQL_DILIMITER)) {							
					if(!SQLUtil.isStatement(strSQL)) {
						isDMLQuestion = true;
						break;
					}
				}
			} else {
				if(!SQLUtil.isStatement(reqQuery.getSql())) isDMLQuestion = true;
			}
			// 실행해도 되는지 묻는다.
			if(PublicTadpoleDefine.YES_NO.YES.toString().equals(userDB.getQuestion_dml())) {
				if(isDMLQuestion) if(!MessageDialog.openConfirm(null, "Confirm", Messages.MainEditor_56)) return; //$NON-NLS-1$
			}
		} catch(Exception e) {
			logger.error("executeCommand", e);
		} finally {
			finallyEndExecuteCommand();
		}
				
		rsDAO = new ResultSetUtilDAO();
		// 쿼리를 실행 합니다. 
		final SQLHistoryDAO sqlHistoryDAO = new SQLHistoryDAO();
		final Job jobQueryManager = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(reqQuery.getSql(), IProgressMonitor.UNKNOWN);
				
				sqlHistoryDAO.setStartDateExecute(new Date());
				sqlHistoryDAO.setIpAddress(reqQuery.getUserIp());
				try {
						
					if(reqQuery.getType() == EditorDefine.EXECUTE_TYPE.ALL) {
						
						sqlHistoryDAO.setStrSQLText(reqQuery.getOriginalSql());
						List<String> listStrExecuteQuery = new ArrayList<String>();
						for (String strSQL : reqQuery.getSql().split(PublicTadpoleDefine.SQL_DILIMITER)) {
							String strExeSQL = SQLUtil.sqlExecutable(strSQL);
							
							// execute batch update는 ddl문이 있으면 안되어서 실행할 수 있는 쿼리만 걸러 줍니다.
							if(!SQLUtil.isStatement(strExeSQL)) {
								listStrExecuteQuery.add(strExeSQL);
							} else {
								reqQuery.setSql(strExeSQL);					
							}
						}
						
						// select 이외의 쿼리 실행
						if(!listStrExecuteQuery.isEmpty()) {
							runSQLExecuteBatch(listStrExecuteQuery, reqQuery);
						}
						
						// select 문장 실행
						if(SQLUtil.isStatement(reqQuery.getSql())) { //$NON-NLS-1$
							sqlHistoryDAO.setStartDateExecute(new Date());
							sqlHistoryDAO.setStrSQLText(reqQuery.getSql());
							sqlHistoryDAO.setIpAddress(reqQuery.getUserIp());

							runSQLSelect(reqQuery);
							sqlHistoryDAO.setRows(rsDAO.getDataList().size());
						}
					} else {
						sqlHistoryDAO.setStrSQLText(reqQuery.getSql());
						
						if(SQLUtil.isStatement(reqQuery.getSql())) {
							runSQLSelect(reqQuery);
							sqlHistoryDAO.setRows(rsDAO.getDataList().size());
						} else {
							runSQLOther(reqQuery);
						}
					}
				} catch(Exception e) {
					logger.error(Messages.MainEditor_50 + reqQuery.getSql(), e);
					
					sqlHistoryDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.toString()); //$NON-NLS-1$
					sqlHistoryDAO.setMesssage(e.getMessage());
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
					
					sqlHistoryDAO.setEndDateExecute(new Date());
				}
				
				/////////////////////////////////////////////////////////////////////////////////////////
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		jobQueryManager.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							compositeResult.executeFinish(sqlHistoryDAO, rsDAO);
						} else {
							compositeResult.executeErrorProgress(jobEvent.getResult().getMessage());
						}
						
						// 쿼리 후 화면 정리 작업을 합니다.
						afterQueryInit(sqlHistoryDAO, reqQuery);
						
						// 주의) 일반적으로는 포커스가 잘 가지만, 
						// progress bar가 열렸을 경우 포커스가 잃어 버리게 되어 포커스를 주어야 합니다.
						setOrionTextFocus();
						
						// ace editor에게 작업이 끝났음을 알립니다.
						finallyEndExecuteCommand();
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		jobQueryManager.setPriority(Job.INTERACTIVE);
		jobQueryManager.setName(userDB.getDisplay_name());
		jobQueryManager.setUser(true);
		jobQueryManager.schedule();
	}
	/**
	 * 쿼리 후 실행결과를 히스토리화면과 프로파일에 저장합니다.
	 */
	private void afterQueryInit(SQLHistoryDAO sqltHistoryDao, final RequestQuery reqQuery) {
		try {
			TadpoleSystem_ExecutedSQL.saveExecuteSQUeryResource(intUserSeq, userDB, PublicTadpoleDefine.EXECUTE_SQL_TYPE.EDITOR, sqltHistoryDao);
		} catch(Exception e) {
			logger.error("save the user query", e); //$NON-NLS-1$
		}
		
		compositeResult.getListSQLHistory().add(sqltHistoryDao);
		compositeResult.getTvSQLHistory().refresh();
	}
	
	/**
	 * auto commit 
	 * @return
	 */
	public boolean isAutoCommit() {
		return !tiAutoCommit.getSelection();
	}
	
	/**
	 * transaction 쿼리인지 검사합니다.
	 * 
	 * @param query
	 * @return
	 */
	private boolean transactionQuery(String query) {
		if(StringUtils.startsWith(query, "commit")) { //$NON-NLS-1$
			TadpoleSQLTransactionManager.commit(strUserEMail, userDB);
			return true;
		}
		// 
		if(StringUtils.startsWith(query, "rollback")) { //$NON-NLS-1$
			TadpoleSQLTransactionManager.rollback(strUserEMail, userDB);
			return true;
		}
		
		return false;
	}
	
	/**
	 * select문을 실행합니다.
	 * 
	 * @param requestQuery
	 */
	private void runSQLSelect(final RequestQuery reqQuery) throws Exception {
		rsDAO = new ResultSetUtilDAO();
		if(!PermissionChecker.isExecute(getUserType(), userDB, reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		// is tajo
		if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
			rsDAO = new TajoConnectionManager().select(userDB, reqQuery.getSql(), queryResultCount, reqQuery.isAutoCommit());
			return;
		}  
		
		// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
		if(transactionQuery(reqQuery.getSql())) return;
		
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		PreparedStatement pstmt = null;
		java.sql.Statement stmt = null;
		
		try {
			if(reqQuery.isAutoCommit()) {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(strUserEMail, userDB);
			}
			
			if(reqQuery.getMode() == EditorDefine.QUERY_MODE.QUERY) {
				
				String tmpExeQuery = reqQuery.getSql();
				if(reqQuery.getSql().toUpperCase().startsWith("SELECT")) { //$NON-NLS-1$
					tmpExeQuery = PartQueryUtil.makeSelect(userDB, reqQuery.getSql(), 0, queryResultCount);
					if(logger.isDebugEnabled()) logger.debug("[SELECT] " + reqQuery.getSql()); //$NON-NLS-1$
				}
				
				pstmt = javaConn.prepareStatement(tmpExeQuery);
				//  환경설정에서 원하는 조건을 입력하였을 경우.
				rs = pstmt.executeQuery();
				
			// explain
			}  else if(reqQuery.getMode() == EditorDefine.QUERY_MODE.EXPLAIN_PLAN) {
				
				// 큐브리드 디비이면 다음과 같아야 합니다.
				if(DBDefine.getDBDefine(userDB) == DBDefine.CUBRID_DEFAULT) {
					
					rsDAO.setColumnName(CubridExecutePlanUtils.getMapColumns());
					rsDAO.setDataList(CubridExecutePlanUtils.getMakeData(CubridExecutePlanUtils.plan(userDB, reqQuery.getSql())));
					
					return;
					
				} else if(DBDefine.getDBDefine(userDB) == DBDefine.ORACLE_DEFAULT) {					
					// generation to statement id for query plan. 
					pstmt = javaConn.prepareStatement("select USERENV('SESSIONID') from dual "); //$NON-NLS-1$
					rs = pstmt.executeQuery(); 
					String statement_id = "tadpole"; //$NON-NLS-1$
					if (rs.next()) statement_id = rs.getString(1);
					
					pstmt = javaConn.prepareStatement("delete from " + planTableName + " where statement_id = '"+statement_id+"' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					pstmt.execute(); 
					
					// 플랜결과를 디비에 저장합니다.
					OracleExecutePlanUtils.plan(userDB, reqQuery.getSql(), planTableName, javaConn, pstmt, statement_id);
					// 저장된 결과를 가져와서 보여줍니다.
					pstmt = javaConn.prepareStatement("select * from " + planTableName + " where statement_id = '"+statement_id+"' "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					rs = pstmt.executeQuery(); 
				 } else if(DBDefine.MSSQL_8_LE_DEFAULT == DBDefine.getDBDefine(userDB) || DBDefine.MSSQL_DEFAULT == DBDefine.getDBDefine(userDB)) {
					 stmt = javaConn.createStatement();
					 stmt.execute(PartQueryUtil.makeExplainQuery(userDB, "ON")); //$NON-NLS-1$

					 pstmt = javaConn.prepareStatement(reqQuery.getSql());
					 rs = pstmt.executeQuery();

					 stmt.execute(PartQueryUtil.makeExplainQuery(userDB, "OFF")); //$NON-NLS-1$
				} else {
				
					pstmt = javaConn.prepareStatement(PartQueryUtil.makeExplainQuery(userDB, reqQuery.getSql()));
					rs = pstmt.executeQuery();
					
				}
			}

			//////////////////////////////////////////////////////////////////////////////////////////////////////
			rsDAO = new ResultSetUtilDAO(rs, queryResultCount, isResultComma);
			
			if(userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT || userDB.getDBDefine() == DBDefine.HIVE_DEFAULT) {
			} else {
				// 데이터셋에 추가 결과 셋이 있을경우 모두 fetch 하여 결과 그리드에 표시한다.
				while(pstmt.getMoreResults()){  
					if(logger.isDebugEnabled()) logger.debug("\n**********has more resultset1...***********"); //$NON-NLS-1$
					rsDAO.addDataAll(ResultSetUtils.getResultToList(pstmt.getResultSet(), queryResultCount, isResultComma));
				}
			}
			
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch(Exception e) {}
			try { if(stmt != null) stmt.close(); } catch(Exception e) {}
			try { if(rs != null) rs.close(); } catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
			}
		}
	}
	
	/**
	 * select문의 execute 쿼리를 수행합니다.
	 * 
	 * @param listQuery
	 * @throws Exception
	 */
	private void runSQLExecuteBatch(List<String> listQuery, final RequestQuery reqQuery) throws Exception {
		if(!PermissionChecker.isExecute(getUserType(), userDB, listQuery)) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			if(reqQuery.isAutoCommit()) {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
			} else {
				javaConn = TadpoleSQLTransactionManager.getInstance(strUserEMail, userDB);
			}
			statement = javaConn.createStatement();
			
			for (String strQuery : listQuery) {
				// 쿼리 중간에 commit이나 rollback이 있으면 어떻게 해야 하나???
				if(!transactionQuery(strQuery)) { 
					
					if(StringUtils.startsWith(strQuery.trim().toUpperCase(), "CREATE TABLE")) { //$NON-NLS-1$
						strQuery = StringUtils.replaceOnce(strQuery, "(", " ("); //$NON-NLS-1$ //$NON-NLS-2$
					}
					
				}
				statement.addBatch(strQuery);
			}
			statement.executeBatch();
		} catch(Exception e) {
			logger.error("Execute batch update", e); //$NON-NLS-1$
			throw e;
		} finally {
			try { statement.close();} catch(Exception e) {}

			if(reqQuery.isAutoCommit()) {
				try { javaConn.close(); } catch(Exception e){}
			}
		}
	}

	/**
	 * select문 이외의 쿼리를 실행합니다
	 * 
	 * @param reqQuery
	 * @exception
	 */
	private void runSQLOther(RequestQuery reqQuery) throws Exception {
		if(!PermissionChecker.isExecute(getUserType(), userDB, reqQuery.getSql())) {
			throw new Exception(Messages.MainEditor_21);
		}
		
		// is tajo
		if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
			new TajoConnectionManager().executeUpdate(userDB,reqQuery.getSql());
		} else { 
		
			// commit나 rollback 명령을 만나면 수행하고 리턴합니다.
			if(transactionQuery(reqQuery.getSql())) return;
			
			java.sql.Connection javaConn = null;
			Statement statement = null;
			try {
				if(reqQuery.isAutoCommit()) {
					SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
					javaConn = client.getDataSource().getConnection();
				} else {
					javaConn = TadpoleSQLTransactionManager.getInstance(strUserEMail, userDB);
				}
				
				statement = javaConn.createStatement();
				
				// TODO mysql일 경우 https://github.com/hangum/TadpoleForDBTools/issues/3 와 같은 문제가 있어 create table 테이블명 다음의 '(' 다음에 공백을 넣어주도록 합니다.
				if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
					final String checkSQL = reqQuery.getSql().trim().toUpperCase();
					if(StringUtils.startsWith(checkSQL, "CREATE TABLE")) { //$NON-NLS-1$
						reqQuery.setSql(StringUtils.replaceOnce(reqQuery.getSql(), "(", " (")); //$NON-NLS-1$ //$NON-NLS-2$
					}
				} else if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
					final String checkSQL = reqQuery.getSql().trim().toUpperCase();
					if(StringUtils.startsWithIgnoreCase(checkSQL, "CREATE OR") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE PROCEDURE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE FUNCTION") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE PACKAGE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "CREATE TRIGGER") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER OR") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER PROCEDURE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER FUNCTION") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER PACKAGE") || //$NON-NLS-1$
						StringUtils.startsWithIgnoreCase(checkSQL, "ALTER TRIGGER") //$NON-NLS-1$
					) { //$NON-NLS-1$
						reqQuery.setSql(reqQuery.getSql() + SQLDefine.QUERY_DELIMITER); //$NON-NLS-1$
					}
				}
				
				// hive는 executeUpdate()를 지원하지 않아서. 13.08.19-hangum
				if(userDB.getDBDefine() == DBDefine.HIVE_DEFAULT) statement.execute(reqQuery.getSql());
				else statement.executeUpdate(reqQuery.getSql());
				
			} finally {
				try { statement.close();} catch(Exception e) {}
	
				if(reqQuery.isAutoCommit()) {
					try { javaConn.close(); } catch(Exception e){}
				}
			}
		}  	// end which db
		
		// create table, drop table이면 작동하도록			
		try {
			if(!SQLUtil.isStatement(reqQuery.getSql())) refreshExplorerView();
		} catch(Exception e) {
			logger.error("CREATE, DROP, ALTER Query refersh error" + reqQuery.getSql()); //$NON-NLS-1$
		}
	}

	/**
	 * CREATE, DROP, ALTER 문이 실행되어 ExplorerViewer view를 리프레쉬합니다.
	 */
	private void refreshExplorerView() {

		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ExplorerViewer.ID);
					ev.refreshCurrentTab(userDB);
				} catch (PartInitException e) {
					logger.error("ExplorerView show", e); //$NON-NLS-1$
				}
			}
			
		});
	}

	@Override
	public void setFocus() {
		setOrionTextFocus();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		// 신규 저장일때는 리소스타입, 이름, 코멘를 입력받습니다.
		if(dBResource == null) {
			userSetDBResource = getFileName();
			if(userSetDBResource == null) return;
		}
		
		// 저장을 호출합니다.
		try {
			String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
			if(performSave(strQuery)) {
				browserEvaluate(IEditorFunction.SAVE_DATA);
			}
		} catch(SWTException e) {
			logger.error(RequestInfoUtils.requestInfo("doSave exception", strUserEMail), e); //$NON-NLS-1$
			monitor.setCanceled(true);
		}
	}
	
	@Override
	public void doSaveAs() {
		// 신규 저장일때는 리소스타입, 이름, 코멘를 입력받습니다.
		userSetDBResource = getFileName();
		if(userSetDBResource == null) return;
		
		// 저장을 호출합니다.
		try {
			String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
			if(performSave(strQuery)) {
				browserEvaluate(IEditorFunction.SAVE_DATA);
			}
		} catch(SWTException e) {
			logger.error(RequestInfoUtils.requestInfo("doSave exception", strUserEMail), e); //$NON-NLS-1$
		}
	}

	/**
	 * new file name
	 * @return
	 */
	private UserDBResourceDAO getFileName() {
		ResourceSaveDialog rsDialog = new ResourceSaveDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB, PublicTadpoleDefine.RESOURCE_TYPE.SQL);
		if(rsDialog.open() == Window.OK) {
			return rsDialog.getRetResourceDao();
		} else {
			return null;
		}
	}
	
	/**
	 * save
	 * 
	 * @param newContents
	 * @return
	 */
	public boolean performSave(String newContents) {
		boolean boolReturnVal = false;
		
		// new save
		if(dBResource == null) {
			// editor가 저장 가능 상태인지 검사합니다.
			if(!isDirty()) return false; 
			
			if(userSetDBResource == null) {
				userSetDBResource = getFileName();
				if(userSetDBResource == null) return false;
			}
			
			boolReturnVal = saveData(newContents);
			
		// save as
		} if(userSetDBResource != null) {
			boolReturnVal = saveData(newContents);
			if(boolReturnVal) userSetDBResource = null;

		// update
		} else {
			try {
				TadpoleSystem_UserDBResource.updateResource(dBResource, newContents);
				boolReturnVal = true;
				setDirty(false);
			} catch (Exception e) {
				logger.error("update file", e); //$NON-NLS-1$
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
				
				boolReturnVal = false;
			}
		}
		
		return boolReturnVal;
	}
	
	/**
	 * save data
	 * 
	 * @param newContents
	 * @return
	 */
	private boolean saveData(String newContents) {
		try {
			// db 저장
			dBResource = TadpoleSystem_UserDBResource.saveResource(userDB, userSetDBResource, newContents);
			dBResource.setParent(userDB);
			
			// title 수정
			setPartName(userSetDBResource.getName());
			
			// tree 갱신
			PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.SAVE_FILE, ""+dBResource.getDb_seq() + ":" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			setDirty(false);
			
			userSetDBResource = null;
		} catch (Exception e) {
			logger.error("save data", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
			
			return false;
		}
		
		return true;
	}
	
	/** save property dirty */
	public void setDirty(Boolean newValue) {
		if(isDirty != newValue) {
			isDirty = newValue;
			firePropertyChange(PROP_DIRTY);
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}

	public UserDBResourceDAO getdBResource() {
		return dBResource;
	}	
	public int getUserSeq() {
		return intUserSeq;
	}
	public int getQueryPageCount() {
		return queryPageCount;
	}

	@Override
	protected void registerBrowserFunctions() {
		editorService = new MainEditorBrowserFunctionService(browserQueryEditor, EditorFunctionService.EDITOR_SERVICE_HANDLER, this);
	}
	
	/**
	 * 에디터 로드할때 사용할 초기 쿼리 입니다.
	 * @return
	 */
	public String getInitDefaultEditorStr() {
		return initDefaultEditorStr;
	}
}