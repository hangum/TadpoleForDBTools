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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
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
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.dialogs.help.RDBShortcutHelpDialog;
import com.hangum.tadpole.ace.editor.core.texteditor.EditorExtension;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.texteditor.function.IEditorFunction;
import com.hangum.tadpole.commons.dialogs.fileupload.SingleFileuploadDialog;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.commons.util.ShortcutPrefixUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.sql.dialog.save.ResourceSaveDialog;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.dialog.export.SQLToStringDialog;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultMainComposite;
import com.hangum.tadpole.rdb.core.editors.main.function.MainEditorBrowserFunctionService;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.utils.UserPreference;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.IMainEditorExtension;
import com.hangum.tadpole.rdb.core.extensionpoint.handler.MainEditorContributionsHandler;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TadpoleTableComposite;
import com.hangum.tadpole.sql.format.SQLFormater;
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
	private ResultMainComposite resultMainComposite;

	/** edior가 초기화 될때 처음 로드 되어야 하는 String. */
	protected String initDefaultEditorStr = ""; //$NON-NLS-1$
	
	/** 현재 editor가 열린 상태. 즉 table, view, index 등의 상태. */
	protected DB_ACTION dbAction;
	
	/** resource 정보. */
	protected UserDBResourceDAO dBResource;
	
	/** save mode */
	private boolean isDirty = false;
	
	/** db table list */
	private Map<String, TableDAO> mapTableList = new HashMap<String, TableDAO>();

	private SashForm sashFormExtension;
	private IMainEditorExtension[] compMainExtions;
	
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
		dbAction = qei.getDbAction();

		dBResource = qei.getResourceDAO();
		if(dBResource == null) setPartName(qei.getName());
		else  setPartName(dBResource.getName());

		strRoleType = userDB.getRole_id();//SessionManager.getRoleType(userDB);
		super.setUserType(strRoleType);
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
		
		// 에디터 확장을 위한 기본 베이스 위젲을 설정합니다.
		sashFormExtension = new SashForm(parent, SWT.NONE);
		sashFormExtension.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				
		SashForm sashForm = new SashForm(sashFormExtension, SWT.VERTICAL);
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
		ToolItem tltmConnectURL = new ToolItem(toolBar, SWT.NONE);
		tltmConnectURL.setToolTipText("Connection Name"); //$NON-NLS-1$
		tltmConnectURL.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/connect.png")); //$NON-NLS-1$
		tltmConnectURL.setText(userDB.getDisplay_name());
		
		tltmConnectURL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBInformationDialog dialog = new DBInformationDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB);
				dialog.open();
				setFocus();
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		// fileupload 
		ToolItem tltmOpen = new ToolItem(toolBar, SWT.NONE);
		tltmOpen.setToolTipText("Open a file");
		tltmOpen.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/file-open.png")); //$NON-NLS-1$
		tltmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SingleFileuploadDialog dialog = new SingleFileuploadDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "SQL File Open");
				if(Dialog.OK == dialog.open()) {
					if(logger.isDebugEnabled()) logger.debug("============> " +  dialog.getStrTxtFile());
					appendText(dialog.getStrTxtFile());
				}
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
		tltmExecute.setToolTipText(String.format(Messages.MainEditor_tltmExecute_toolTipText_1, ShortcutPrefixUtils.getCtrlShortcut()));
		tltmExecute.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql-query.png")); //$NON-NLS-1$
		tltmExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.SELECTED_TEXT, UserPreference.QUERY_DELIMITER);
				
				EditorDefine.EXECUTE_TYPE executeType = EditorDefine.EXECUTE_TYPE.NONE;
				if( Boolean.parseBoolean( browserEvaluateToStr(EditorFunctionService.IS_BLOCK_TEXT) ) ) {
					executeType = EditorDefine.EXECUTE_TYPE.BLOCK;
				}
				
				RequestQuery reqQuery = new RequestQuery(strQuery, dbAction, EditorDefine.QUERY_MODE.QUERY, executeType, isAutoCommit());
				executeCommand(reqQuery);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		if(SQLUtil.isSELECTEditor(dbAction)) {
			ToolItem tltmExecuteAll = new ToolItem(toolBar, SWT.NONE);
			tltmExecuteAll.setToolTipText(Messages.MainEditor_tltmExecuteAll_text);
			tltmExecuteAll.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql-query-all.png")); //$NON-NLS-1$
			tltmExecuteAll.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
					
					RequestQuery reqQuery = new RequestQuery(strQuery, dbAction, EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.ALL, isAutoCommit());
					executeCommand(reqQuery);
				}
			});
			new ToolItem(toolBar, SWT.SEPARATOR);
		}
	
		ToolItem tltmExplainPlanctrl = new ToolItem(toolBar, SWT.NONE);
		tltmExplainPlanctrl.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/execute_plan.png")); //$NON-NLS-1$
		tltmExplainPlanctrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.SELECTED_TEXT, UserPreference.QUERY_DELIMITER); //$NON-NLS-1$
				
				RequestQuery reqQuery = new RequestQuery(strQuery, dbAction, EditorDefine.QUERY_MODE.EXPLAIN_PLAN, EditorDefine.EXECUTE_TYPE.NONE, isAutoCommit());
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
	    tltmSQLToApplication.setToolTipText("SQL statement to Application code"); //$NON-NLS-1$
	    new ToolItem(toolBar, SWT.SEPARATOR);
		
//		ToolItem tltmDownload = new ToolItem(toolBar, SWT.NONE);
//		tltmDownload.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/download_query.png")); //$NON-NLS-1$
//		tltmDownload.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if(!MessageDialog.openConfirm(null, Messages.MainEditor_38, Messages.MainEditor_39)) return;
//		
//				try {
//					String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
//					compResult.downloadExtFile(getUserDB().getDisplay_name()+".sql", strQuery); //$NON-NLS-1$
//				} catch(Exception ee) {
//					logger.error("Download SQL", ee); //$NON-NLS-1$
//				}
//			}
//		});
//		tltmDownload.setToolTipText(Messages.MainEditor_42);
//		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tiAutoCommit = new ToolItem(toolBar, SWT.CHECK);
		tiAutoCommit.setSelection(false);
		tiAutoCommit.setText("Transaction start"); //$NON-NLS-1$
		tiAutoCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initAutoCommitAction(false, true);
			}
		});
		
		tiAutoCommitCommit = new ToolItem(toolBar, SWT.NONE);
		tiAutoCommitCommit.setSelection(false);
		tiAutoCommitCommit.setText("Commit"); //$NON-NLS-1$
		tiAutoCommitCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(logger.isDebugEnabled()) logger.debug("[set commit][user id]" + getUserEMail() + "[user id]" + userDB); //$NON-NLS-1$ //$NON-NLS-2$
				
				TadpoleSQLTransactionManager.commit(getUserEMail(), userDB);
			}
		});
		
		tiAutoCommitRollback = new ToolItem(toolBar, SWT.NONE);
		tiAutoCommitRollback.setSelection(false);
		tiAutoCommitRollback.setText("Rollback"); //$NON-NLS-1$
		tiAutoCommitRollback.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(logger.isDebugEnabled()) logger.debug("[set rollback][user id]" + getUserEMail() + "[user id]" + userDB); //$NON-NLS-1$ //$NON-NLS-2$
				
				TadpoleSQLTransactionManager.rollback(getUserEMail(), userDB);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		// semicolon
		ToolItem tltmSemicolon = new ToolItem(toolBar, SWT.NONE);
		tltmSemicolon.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/Semicolon.png")); //$NON-NLS-1$
		tltmSemicolon.setToolTipText(Messages.MainEditor_49);
		
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
	    
	    resultMainComposite = new ResultMainComposite(sashForm, SWT.BORDER);
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.horizontalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 0;
	    resultMainComposite.setLayout(gl_compositeResult);
	    resultMainComposite.setMainEditor(this);
		
		sashForm.setWeights(new int[] {65, 35});
		initEditor();
		
		// 올챙이 확장에 관한 코드를 넣습니다. =================================================================== 
		MainEditorContributionsHandler editorExtension = new MainEditorContributionsHandler();
		compMainExtions = editorExtension.evaluateCreateWidgetContribs(userDB);
		int intSashCnt = 1;
		for (IMainEditorExtension aMainEditorExtension : compMainExtions) {
			
			if(aMainEditorExtension.isEnableExtension()) {
				intSashCnt++;
				Composite compExt = new Composite(sashFormExtension, SWT.BORDER);
				GridLayout gl_compositeExt = new GridLayout(1, false);
				gl_compositeExt.verticalSpacing = 0;
				gl_compositeExt.horizontalSpacing = 0;
				gl_compositeExt.marginHeight = 0;
				gl_compositeExt.marginWidth = 0;
				compExt.setLayout(gl_compositeExt);
	
				aMainEditorExtension.createPartControl(compExt, this);
			}
		}
		
		if(intSashCnt >= 2) {
			sashFormExtension.setWeights(new int[] {100, 0});
		}
		// 올챙이 확장에 관한 코드를 넣습니다. ===================================================================
		
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
								tiAutoCommitCommit.setEnabled(false);
								tiAutoCommitRollback.setEnabled(false);
							} else {
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
			if(!StringUtils.endsWith(strText, PublicTadpoleDefine.SQL_DELIMITER)) {
				strText += PublicTadpoleDefine.SQL_DELIMITER;
			}
			
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
					browserEvaluate(IEditorFunction.INITIALIZE, findEditorExt(), dbAction.toString(), getAssistTableList(), getInitDefaultEditorStr()); //$NON-NLS-1$
				} catch(Exception ee) {
					logger.error("rdb editor initialize ", ee); //$NON-NLS-1$
				}
			}
			public void changed( ProgressEvent event ) {}			
		});
	}
	
	/**
	 * find editor extension
	 * 
	 * eg) mysql, pgsql
	 * @return
	 */
	private String findEditorExt() {
		String ext = EditorDefine.EXT_DEFAULT;
		if(DBDefine.MYSQL_DEFAULT == userDB.getDBDefine() || DBDefine.MARIADB_DEFAULT == userDB.getDBDefine()) {
			ext = EditorDefine.EXT_MYSQL;
		} else if(DBDefine.POSTGRE_DEFAULT == userDB.getDBDefine()) {
			ext = EditorDefine.EXT_PGSQL;
		} else if(DBDefine.SQLite_DEFAULT == userDB.getDBDefine()) {
			ext = EditorDefine.EXT_SQLite;
		}
		
		return ext;
	}
	
//	/**
//	 * List of assist table column name
//	 * 
//	 * @param tableName
//	 * @return
//	 */
//	public String getAssistColumnList(String tableName) {
//		String strColumnlist = ""; //$NON-NLS-1$
//		
//		try {
//			TableDAO table = mapTableList.get(tableName);
//			
//			List<TableColumnDAO> showTableColumns = TadpoleObjectQuery.makeShowTableColumns(userDB, table);
//			for (TableColumnDAO tableDao : showTableColumns) {
//				strColumnlist += tableDao.getSysName() + "|"; //$NON-NLS-1$
//			}
//			strColumnlist = StringUtils.removeEnd(strColumnlist, "|"); //$NON-NLS-1$
//		} catch(Exception e) {
//			logger.error("MainEditor get the table column list", e); //$NON-NLS-1$
//		}
//		
//		return strColumnlist;
//	}
//	
	/**
	 * List of assist table name 
	 * 
	 * @return
	 */
	private String getAssistTableList() {
		String strTablelist = ""; //$NON-NLS-1$
		
		try {
			List<TableDAO> showTables = TadpoleTableComposite.getTableList(getUserDB());
			for (TableDAO tableDao : showTables) {
				strTablelist += tableDao.getSysName() + "|"; //$NON-NLS-1$
				mapTableList.put(tableDao.getSysName(), tableDao);
			}
			strTablelist = StringUtils.removeEnd(strTablelist, "|"); //$NON-NLS-1$
			
		} catch(Exception e) {
			logger.error("MainEditor get the table list", e); //$NON-NLS-1$
		}
		
		return strTablelist;
	}

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
		
		// 
		resultMainComposite.initMainComposite();
		
		// google analytic
		AnalyticCaller.track(MainEditor.ID, userDB.getDbms_type());
	}
	
	/**
	 * start sql transaction;
	 */
	public void beginTransaction() {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if(tiAutoCommit.isEnabled()) {
					tiAutoCommit.setEnabled(true);
					tiAutoCommit.setSelection(true);
					tiAutoCommitCommit.setEnabled(true);
					tiAutoCommitRollback.setEnabled(true);
				}
			}
		});
	}
	
	/**
	 * init auto commit button
	 * 
	 * @param isFirst
	 * @param isRiseEvent
	 */
	private void initAutoCommitAction(boolean isFirst, boolean isRiseEvent) {
		if(isAutoCommit()) {
			tiAutoCommitCommit.setEnabled(false);
			tiAutoCommitRollback.setEnabled(false);
			
			if(!isFirst) {
				if(TadpoleSQLTransactionManager.isInstance(getUserEMail(), userDB)) {
					if(MessageDialog.openConfirm(null, Messages.MainEditor_30, Messages.MainEditor_47)) {
						TadpoleSQLTransactionManager.commit(getUserEMail(), userDB);
					} else {
						TadpoleSQLTransactionManager.rollback(getUserEMail(), userDB);
					}
				}
			}
		} else {
			tiAutoCommitCommit.setEnabled(true);
			tiAutoCommitRollback.setEnabled(true);
		}
		
		if(isRiseEvent) {
			// auto commit의 실행버튼을 동일한 db를 열고 있는 에디터에서 공유합니다.
			PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.AUTOCOMMIT_USE, userDB.getSeq() + "||" + tiAutoCommit.getSelection() + "||" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
	
	public void executeCommand(final RequestQuery reqQuery) {
		resultMainComposite.executeCommand(reqQuery);

		// google analytic
		AnalyticCaller.track(MainEditor.ID, "executeCommand"); //$NON-NLS-1$
	}
	/**
	 * auto commit 
	 * @return
	 */
	public boolean isAutoCommit() {
		return !tiAutoCommit.getSelection();
	}
	
	@Override
	public void setFocus() {
		setOrionTextFocus();
	}
	
	/**
	 * new file name
	 * @return
	 */
	private UserDBResourceDAO getFileName(UserDBResourceDAO initDBResource) {
		ResourceSaveDialog rsDialog = new ResourceSaveDialog(null, initDBResource, userDB, PublicTadpoleDefine.RESOURCE_TYPE.SQL);
		if(rsDialog.open() == Window.OK) {
			return rsDialog.getRetResourceDao();
		} else {
			return null;
		}
	}
	
	/**
	 * 데이터를 저장합니다.
	 * 
	 * @param strContentData
	 * @return
	 */
	public boolean calledDoSave(String strContentData) {
		boolean isSaved = false;
		
		try {
			// 신규 저장일때는 리소스타입, 이름, 코멘를 입력받습니다.
			if(dBResource == null) {
				UserDBResourceDAO newDBResource = getFileName(null);
				if(newDBResource == null) return false;

				isSaved = saveResourceData(newDBResource, strContentData);
			// 업데이트 일때.
			} else {
				isSaved = updateResourceDate(strContentData);
			}
			
		} catch(SWTException e) {
			logger.error(RequestInfoUtils.requestInfo("doSave exception", getUserEMail()), e); //$NON-NLS-1$
		} finally {
			if(isSaved) {
				setDirty(false);
				browserEvaluate(IEditorFunction.SAVE_DATA);	
			}
			
			browserEvaluate(IEditorFunction.SET_FOCUS);
		}
		
		return isSaved;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		String strEditorAllText = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
		calledDoSave(strEditorAllText);
	}
	
	@Override
	public void doSaveAs() {
		boolean isSaved = false;
		
		// 신규 저장일때는 리소스타입, 이름, 코멘를 입력받습니다.
		UserDBResourceDAO newDBResource = getFileName(dBResource);
		if(newDBResource == null) return;
		
		// 저장을 호출합니다.
		try {
			String strEditorAllText = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
			isSaved = saveResourceData(newDBResource, strEditorAllText);
		} catch(SWTException e) {
			logger.error(RequestInfoUtils.requestInfo("doSave exception", getUserEMail()), e); //$NON-NLS-1$
		} finally {
			if(isSaved) {
				setDirty(false);
				browserEvaluate(IEditorFunction.SAVE_DATA);	
			}
			
			browserEvaluate(IEditorFunction.SET_FOCUS);
		}
	}

	/**
	 * 데이터를 수정합니다.
	 * 
	 * @param newContents
	 * @return
	 */
	private boolean updateResourceDate(String newContents) {
		try {
			TadpoleSystem_UserDBResource.updateResource(dBResource, newContents);
			return true;
		} catch (Exception e) {
			logger.error("update file", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.MainEditor_19, errStatus); //$NON-NLS-1$
			
			return false;
		}
	}
	
	/**
	 * save data
	 * 
	 * @param newDBResource 저장 하려는 리소스
	 * @param newContents
	 * @return
	 */
	private boolean saveResourceData(UserDBResourceDAO newDBResource, String newContents) {

		try {
			// db 저장
			dBResource = TadpoleSystem_UserDBResource.saveResource(userDB, newDBResource, newContents);
			dBResource.setParent(userDB);
			
			// title 수정
			setPartName(dBResource.getName());
			
			// tree 갱신
			PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.SAVE_FILE, ""+dBResource.getDb_seq() + ":" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
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
	
	/**
	 * 에디터의 성격을 정의 합니다.  
	 * 
	 * @return
	 */
	public DB_ACTION getDbAction() {
		return dbAction;
	}
	
	/**
	 * get Resource
	 * 
	 * @return
	 */
	public UserDBResourceDAO getdBResource() {
		return dBResource;
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
	
//	/**
//	 * 저장 이벤트 
//	 * 
//	 * @return
//	 */
//	private String startUploadReceiver() {
//		receiver = new DiskFileUploadReceiver();
//		final FileUploadHandler uploadHandler = new FileUploadHandler(receiver);
//		uploadHandler.addUploadListener(new FileUploadListener() {
//
//			public void uploadProgress(FileUploadEvent event) {
//			}
//
//			public void uploadFailed(FileUploadEvent event) {
////				MessageDi( "upload failed: " + event.getException() ); //$NON-NLS-1$
//				MessageDialog.openError(null, "Error", "Download fail. \n" + event.getException().getMessage());
//			}
//
//			public void uploadFinished(FileUploadEvent event) {
//				for( FileDetails file : event.getFileDetails() ) {
////					addToLog( "uploaded : " + file.getFileName() );
//					System.out.println("downloaded. " + file.getFileName());
//
//				}
//			}			
//		});
//		
//		return uploadHandler.getUploadUrl();
//	}

	public IMainEditorExtension[] getMainEditorExtions() {
		return compMainExtions;
	}
	
	public SashForm getSashFormExtension() {
		return sashFormExtension;
	}
}