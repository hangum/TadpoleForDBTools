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

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.dialogs.help.RDBShortcutHelpDialog;
import com.hangum.tadpole.ace.editor.core.texteditor.EditorExtension;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.texteditor.function.IEditorFunction;
import com.hangum.tadpole.commons.dialogs.fileupload.SingleFileuploadDialog;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.commons.util.ShortcutPrefixUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.bill.UserBillEditorInput;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.sql.dialog.save.ResourceSaveDialog;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.dialog.db.UserDBGroupDialog;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.SQLToStringDialog;
import com.hangum.tadpole.rdb.core.dialog.restfulapi.MainSQLEditorAPIServiceDialog;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultMainComposite;
import com.hangum.tadpole.rdb.core.editors.main.function.MainEditorBrowserFunctionService;
import com.hangum.tadpole.rdb.core.editors.main.utils.ExtMakeContentAssistUtil;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.IMainEditorExtension;
import com.hangum.tadpole.rdb.core.extensionpoint.handler.MainEditorContributionsHandler;
import com.hangum.tadpole.rdb.core.util.DialogUtil;
import com.hangum.tadpole.rdb.core.util.EditorUtils;
import com.hangum.tadpole.rdb.core.viewers.connections.DBIconsUtils;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;
import com.hangum.tadpole.session.manager.SessionManager;
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
	
	/** auto save를 위해 마지막 콘텐츠 를 남겨 놓는다. */
	private String strLastContent = "";
	
	/** toolbar auto commit */
	private ToolItem tiAutoCommit = null, tiAutoCommitCommit = null, tiAutoCommitRollback = null;

	/** result tab */
	protected ResultMainComposite resultMainComposite;

	/** edior가 초기화 될때 처음 로드 되어야 하는 String. */
	protected String initDefaultEditorStr = ""; //$NON-NLS-1$
	
	/** 현재 editor가 열린 상태. 즉 table, view, index 등의 상태. */
	protected OBJECT_TYPE dbAction;
	
	/** resource 정보. */
	protected UserDBResourceDAO dBResource;
	/** 자동 저장 auto save */
	protected UserDBResourceDAO dBResourceAuto;
	
	/** save mode */
	protected boolean isDirty = false;
	
	/** short cut prefix */
	protected static final String STR_SHORT_CUT_PREFIX = ShortcutPrefixUtils.getCtrlShortcut();
	
	protected SashForm sashFormExtension;
	protected IMainEditorExtension[] compMainExtions;
	
	public MainEditor() {
		super();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		MainEditorInput qei = (MainEditorInput)input;
		userDB = qei.getUserDB();
		initDefaultEditorStr = qei.getDefaultStr();
		strLastContent = qei.getDefaultStr();
		dbAction = qei.getDbAction();

		String strPartName = qei.getName();
		dBResource = qei.getResourceDAO();
		if(dBResource != null) {
			strPartName = dBResource.getName();
		} else {
			// 기본 저장된 쿼리가 있는지 가져온다.
			try {
				dBResourceAuto = TadpoleSystem_UserDBResource.getDefaultDBResourceData(userDB);
				if(dBResourceAuto != null) {
					if(!StringUtils.isEmpty(dBResourceAuto.getDataString())) initDefaultEditorStr = dBResourceAuto.getDataString() + Messages.get().AutoRecoverMsg + initDefaultEditorStr;
				}
			} catch(Exception e) {
				logger.error("Get default resource", e);
			}
		}
		strLastContent = initDefaultEditorStr;

		strRoleType = userDB.getRole_id();
		super.setUserType(strRoleType);
		
		setSite(site);
		setInput(input);
		setPartName(strPartName);
		
		setTitleImage(DBIconsUtils.getEditorImage(getUserDB()));
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
		GridLayout gl_compositeEditor = new GridLayout(3, false);
		gl_compositeEditor.verticalSpacing = 0;
		gl_compositeEditor.horizontalSpacing = 0;
		gl_compositeEditor.marginHeight = 0;
		gl_compositeEditor.marginWidth = 0;
		compositeEditor.setLayout(gl_compositeEditor);
		
		ToolBar toolBar = new ToolBar(compositeEditor, SWT.NONE | SWT.FLAT | SWT.RIGHT);
		final ToolItem tltmConnectURL = new ToolItem(toolBar, SWT.NONE);
		tltmConnectURL.setToolTipText(Messages.get().DatabaseInformation);
		tltmConnectURL.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/connect.png")); //$NON-NLS-1$
		tltmConnectURL.setText(userDB.getDisplay_name());
		
		tltmConnectURL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBInformationDialog dialog = new DBInformationDialog(getSite().getShell(), userDB);
				dialog.open();
				setFocus();
			}
		});
		final ToolItem tltmSelectDB = new ToolItem(toolBar, SWT.NONE);
		tltmSelectDB.setToolTipText(Messages.get().SelectOthersDB);
		tltmSelectDB.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/arrow_down.png")); //$NON-NLS-1$
		tltmSelectDB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!isAutoCommit()) {
					MessageDialog.openWarning(getSite().getShell(), Messages.get().Warning, Messages.get().PleaseEndedTransaction);
				} else {
					
					UserDBGroupDialog dialog = new UserDBGroupDialog(getSite().getShell(), userDB);
					if(Dialog.OK == dialog.open()) {
						UserDBDAO selectedUserDB = dialog.getUserDB();
						if(selectedUserDB != null) {
							userDB = selectedUserDB;
							
							try {
								TadpoleObjectQuery.getTableList(userDB);
							} catch (Exception e1) {
								logger.error("get table list", e1);
							}
							
							tltmConnectURL.setText(userDB.getDisplay_name());
						}
					}
				}
				
				setFocus();
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		// fileupload 
		ToolItem tltmOpen = new ToolItem(toolBar, SWT.NONE);
		tltmOpen.setToolTipText(Messages.get().MainEditor_35);
		tltmOpen.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/file-open.png")); //$NON-NLS-1$
		tltmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SingleFileuploadDialog dialog = new SingleFileuploadDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.get().MainEditor_36);
				if(Dialog.OK == dialog.open()) {
//					if(logger.isDebugEnabled()) logger.debug("============> " +  dialog.getStrTxtFile()); //$NON-NLS-1$
					appendText(dialog.getStrTxtFile());
				}
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
		tltmExecute.setToolTipText(String.format(Messages.get().MainEditor_tltmExecute_toolTipText_1, STR_SHORT_CUT_PREFIX));
		tltmExecute.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/play.png")); //$NON-NLS-1$
		tltmExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.GET_SELECTED_TEXT, PublicTadpoleDefine.SQL_DELIMITER);
				
				EditorDefine.EXECUTE_TYPE executeType = EditorDefine.EXECUTE_TYPE.NONE;
				if( Boolean.parseBoolean( browserEvaluateToStr(EditorFunctionService.IS_BLOCK_TEXT) ) ) {
					executeType = EditorDefine.EXECUTE_TYPE.BLOCK;
				}
				
				RequestQuery reqQuery = new RequestQuery(userDB, strQuery, dbAction, EditorDefine.QUERY_MODE.QUERY, executeType, isAutoCommit());
				executeCommand(reqQuery);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		if(SQLUtil.isSELECTEditor(dbAction)) {
			ToolItem tltmExecuteAll = new ToolItem(toolBar, SWT.NONE);
			tltmExecuteAll.setToolTipText(Messages.get().MainEditor_tltmExecuteAll_text);
			tltmExecuteAll.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql-query-all.png")); //$NON-NLS-1$
			tltmExecuteAll.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
					
					RequestQuery reqQuery = new RequestQuery(userDB, strQuery, dbAction, EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.ALL, isAutoCommit());
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
				String strQuery = browserEvaluateToStr(EditorFunctionService.GET_SELECTED_TEXT, PublicTadpoleDefine.SQL_DELIMITER); //$NON-NLS-1$
				
				RequestQuery reqQuery = new RequestQuery(userDB, strQuery, dbAction, EditorDefine.QUERY_MODE.EXPLAIN_PLAN, EditorDefine.EXECUTE_TYPE.NONE, isAutoCommit());
				executeCommand(reqQuery);
				
			}
		});
		tltmExplainPlanctrl.setToolTipText(String.format(Messages.get().MainEditor_3, STR_SHORT_CUT_PREFIX));
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
		tltmSort.setToolTipText(String.format(Messages.get().MainEditor_4, STR_SHORT_CUT_PREFIX));
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmSQLToApplication = new ToolItem(toolBar, SWT.NONE);
		tltmSQLToApplication.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/sql_to_applications.png")); //$NON-NLS-1$
		tltmSQLToApplication.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
				
				SQLToStringDialog dialog = new SQLToStringDialog(null, getUserDB(), strQuery);
				dialog.open();
				setFocus();
			}
		});
	    tltmSQLToApplication.setToolTipText(Messages.get().MainEditor_40);
	    new ToolItem(toolBar, SWT.SEPARATOR);
		
//		ToolItem tltmDownload = new ToolItem(toolBar, SWT.NONE);
//		tltmDownload.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/download_query.png")); //$NON-NLS-1$
//		tltmDownload.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if(!MessageDialog.openConfirm(null, Messages.get().MainEditor_38, Messages.get().MainEditor_39)) return;
//		
//				try {
//					String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
//					compResult.downloadExtFile(getUserDB().getDisplay_name()+".sql", strQuery); //$NON-NLS-1$
//				} catch(Exception ee) {
//					logger.error("Download SQL", ee); //$NON-NLS-1$
//				}
//			}
//		});
//		tltmDownload.setToolTipText(Messages.get().MainEditor_42);
//		new ToolItem(toolBar, SWT.SEPARATOR);
		
		tiAutoCommit = new ToolItem(toolBar, SWT.CHECK);
		tiAutoCommit.setSelection(false);
		tiAutoCommit.setText(Messages.get().MainEditor_41);
		tiAutoCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initAutoCommitAction(false, true);
			}
		});
		
		tiAutoCommitCommit = new ToolItem(toolBar, SWT.NONE);
		tiAutoCommitCommit.setSelection(false);
		tiAutoCommitCommit.setText(Messages.get().Commit);
		tiAutoCommitCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(logger.isDebugEnabled()) logger.debug("[set commit][user id]" + getUserEMail() + "[user id]" + userDB); //$NON-NLS-1$ //$NON-NLS-2$
				
				TadpoleSQLTransactionManager.commit(getUserEMail(), userDB);
			}
		});
		
		tiAutoCommitRollback = new ToolItem(toolBar, SWT.NONE);
		tiAutoCommitRollback.setSelection(false);
		tiAutoCommitRollback.setText(Messages.get().Rollback);
		tiAutoCommitRollback.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(logger.isDebugEnabled()) logger.debug("[set rollback][user id]" + getUserEMail() + "[user id]" + userDB); //$NON-NLS-1$ //$NON-NLS-2$
				
				TadpoleSQLTransactionManager.rollback(getUserEMail(), userDB);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		// api
		ToolItem tltmAPI = new ToolItem(toolBar, SWT.NONE);
		tltmAPI.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/restful_api.png")); //$NON-NLS-1$
		tltmAPI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strQuery = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
				
				MainSQLEditorAPIServiceDialog dialog = new MainSQLEditorAPIServiceDialog(getSite().getShell(), userDB, strQuery);
				dialog.open();
				
				setFocus();
			}
		});
		tltmAPI.setToolTipText(Messages.get().MainEditor_51);
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
		tltmHelp.setToolTipText(String.format(Messages.get().MainEditor_27, STR_SHORT_CUT_PREFIX));
		new ToolItem(toolBar, SWT.SEPARATOR);
	    ////// tool bar end ///////////////////////////////////////////////////////////////////////////////////
		
	    ////// orion editor start /////////////////////////////////////////////////////////////////////////////
	    browserQueryEditor = new Browser(compositeEditor, SWT.BORDER);
	    browserQueryEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
	    addBrowserService();
	    
	    resultMainComposite = new ResultMainComposite(sashForm, SWT.BORDER);
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.horizontalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 0;
	    resultMainComposite.setLayout(gl_compositeResult);
	    resultMainComposite.setMainEditor(this);
		
		sashForm.setWeights(new int[] {63, 37});
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
				} else if(event.getProperty() == PreferenceDefine.EDITOR_CHANGE_EVENT) {
					final String varTheme 		= PublicTadpoleDefine.getMapTheme().get(GetPreferenceGeneral.getEditorTheme());
				    final String varFontSize 	= GetPreferenceGeneral.getEditorFontSize();
				    final String varIsWrap 		= ""+GetPreferenceGeneral.getEditorIsWarp();
				    final String varWarpLimit 	= GetPreferenceGeneral.getEditorWarpLimitValue();
				    final String varIsShowGutter = ""+GetPreferenceGeneral.getEditorShowGutter();
				    
				    browserEvaluate(IEditorFunction.CHANGE_EDITOR_STYLE, 
							varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter
						);
				}
			} //
		}); // end property change
	
	}
	
	public Browser getBrowserQueryEditor() {
		return browserQueryEditor;
	}
	
	/**
	 * browser handler
	 */
	protected void addBrowserService() {
		browserQueryEditor.setUrl(REAL_DB_URL);
	    	
//	    final String strConstList = findDefaultKeyword();
		// 기존 리소스를 가져왔으면 auto save mode 는 false
	    final String varAutoSave 	= dBResource != null?"fasle":""+GetPreferenceGeneral.getEditorAutoSave();
	    
	    final String varTheme 		= PublicTadpoleDefine.getMapTheme().get(GetPreferenceGeneral.getEditorTheme());
	    final String varFontSize 	= GetPreferenceGeneral.getEditorFontSize();
	    final String varIsWrap 		= ""+GetPreferenceGeneral.getEditorIsWarp();
	    final String varWarpLimit 	= GetPreferenceGeneral.getEditorWarpLimitValue();
	    final String varIsShowGutter = ""+GetPreferenceGeneral.getEditorShowGutter();
	    registerBrowserFunctions();
	    
		browserQueryEditor.addProgressListener(new ProgressListener() {
			@Override
			public void completed( ProgressEvent event ) {
				try {
					browserEvaluate(IEditorFunction.RDB_INITIALIZE, 
							findEditorExt(), dbAction.toString(), getInitDefaultEditorStr(),
							varAutoSave, varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter
							); //$NON-NLS-1$
				} catch(Exception ee) {
					logger.error("rdb editor initialize ", ee); //$NON-NLS-1$
				}
			}
			public void changed( ProgressEvent event ) {}			
		});
	}

	/**
	 * getContentAssist
	 * 
	 * @param strQuery
	 * @param intPosition
	 * @return
	 */
	public String getContentAssist(String strQuery, int intPosition) {
		if("".equals(StringUtils.trimToEmpty(strQuery))) return "";
		
		if(logger.isDebugEnabled()) {
			logger.debug("-[start block] -------------------------------------------------------------");
			logger.debug("\t[strQuery]" + strQuery );
			logger.debug("\t[intPosition]" + intPosition );
		}
		
		String newContentAssist = "";
		try {
			ExtMakeContentAssistUtil constAssistUtil = new ExtMakeContentAssistUtil();
			newContentAssist = constAssistUtil.makeContentAssist(getUserDB(), strQuery, intPosition);
		} catch(Exception e) {
			logger.error("Content assist", e);
		} 
		if(logger.isDebugEnabled()) logger.debug("-[end block] -------------------------------------------------------------");
		
		return newContentAssist==null?"":newContentAssist;
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
		
		// 과거에 실행했던 쿼리 정보 가져오기.
//		resultMainComposite.initMainComposite();
		
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
					if(MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().MainEditor_47)) {
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
	
	/**
	 * execute query
	 * 
	 * @param reqQuery
	 */
	public void executeCommand(final RequestQuery reqQuery) {
		// 요청쿼리가 없다면 무시합니다. 
		if(StringUtils.isEmpty(reqQuery.getSql())) return;
		
		// do not execute query
		if(System.currentTimeMillis() > SessionManager.getServiceEnd().getTime()) {
			if(ApplicationArgumentUtils.isOnlineServer()) {
				if(MessageDialog.openConfirm(null, Messages.get().Information, Messages.get().MainEditorServiceEndGoPay)) {
					UserBillEditorInput mei = new UserBillEditorInput();
					
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mei, "com.hangum.tadpole.bill.core.editor.user");
					} catch (PartInitException e) {
						logger.error("open editor", e); //$NON-NLS-1$
						Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
						ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Bill page open", errStatus); //$NON-NLS-1$
					}
				}
			} else {
				MessageDialog.openInformation(null, Messages.get().Information, Messages.get().MainEditorServiceEnd);
			}
			return;
		}
		
		String strCheckSQL = SQLUtil.removeCommentAndOthers(userDB, reqQuery.getSql());
		if(StringUtils.startsWithIgnoreCase(strCheckSQL, "desc ")) {
			String strObject = StringUtils.removeStartIgnoreCase(strCheckSQL, "desc ");
			DialogUtil.popupDMLDialog(getUserDB(), strObject);
		} else {
			resultMainComposite.executeCommand(reqQuery);
		}

		// google analytic
		AnalyticCaller.track(MainEditor.ID, "executeCommand"); //$NON-NLS-1$
	}
	/**
	 * auto commit 
	 * @return
	 */
	public boolean isAutoCommit() {
		if(tiAutoCommit == null) return true;
		return !tiAutoCommit.getSelection();
	}
	
	@Override
	public void setFocus() {
		setOrionTextFocus();
		EditorUtils.selectConnectionManager(getUserDB());
	}
	
	/**
	 * new resource name
	 * 
	 * @return
	 */
	private UserDBResourceDAO getResouceName(UserDBResourceDAO initDBResource, String strContentData) {
		ResourceSaveDialog rsDialog = new ResourceSaveDialog(null, initDBResource, userDB, PublicTadpoleDefine.RESOURCE_TYPE.SQL, strContentData);
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
				UserDBResourceDAO newDBResource = getResouceName(null, strContentData);
				if(newDBResource == null) return false;

				isSaved = saveResourceData(newDBResource, strContentData);
			// 업데이트 일때.
			} else {
				isSaved = updateResourceDate(strContentData);
			}

			this.strLastContent = strContentData;

			// auto save 항목을 지운다.
			if(dBResourceAuto != null) {
				TadpoleSystem_UserDBResource.updateResourceAuto(dBResourceAuto, "");
			}
			
		} catch(Exception e) {
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
	
	/**
	 * call auto save
	 * 
	 * @param strContentData
	 * @return
	 */
	public boolean calledDoAutoSave(String strContentData) {
		if(logger.isDebugEnabled()) logger.debug("====== called auto save ==========" + strContentData);
		// 내용이 공백이거나 이전 내용과 같으면 저장하지 않는다.
		if("".equals(StringUtils.trimToEmpty(strContentData))) return true;
		if(StringUtils.trimToEmpty(strLastContent).equals(StringUtils.trimToEmpty(strContentData))) return true;

		boolean isSaved = false;
		try {
			isSaved = updateAutoResourceDate(strContentData);
			strLastContent = strContentData;
		} catch(SWTException e) {
			logger.error(RequestInfoUtils.requestInfo("doAutoSave exception", getUserEMail()), e); //$NON-NLS-1$
		} finally {
//			browserEvaluate(IEditorFunction.SET_FOCUS);
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
		
		// 저장을 호출합니다.
		try {
			String strEditorAllText = browserEvaluateToStr(EditorFunctionService.ALL_TEXT);
			
			// 신규 저장일때는 리소스타입, 이름, 코멘를 입력받습니다.
			UserDBResourceDAO newDBResource = getResouceName(dBResource, strEditorAllText);
			if(newDBResource == null) return;
			
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
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
			
			return false;
		}
	}
	
	/**
	 * auto update save 
	 * @param newContents
	 * @return
	 */
	private boolean updateAutoResourceDate(String newContents) {
		if(dBResource != null) return true;
		
		// table, view만 auto save 된다.
		if(dbAction == PublicTadpoleDefine.OBJECT_TYPE.TABLES | 
				dbAction == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
				
			if(logger.isDebugEnabled()) logger.debug("====> called updateAutoResourceDate ");
			try {
				dBResourceAuto = TadpoleSystem_UserDBResource.updateAutoResourceDate(getUserDB(), dBResourceAuto, dBResource, newContents);
				if(dBResource != null) {
					setDirty(false);
				}
				return true;
			} catch (Exception e) {
				logger.error("Autosave exception", e);
				// igoner error message
				
				return false;
			}
		}
		
		return true;
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
			PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.SAVE_FILE, String.format("%s:%s", dBResource.getDb_seq(), System.currentTimeMillis())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
		} catch (Exception e) {
			logger.error("save data", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
			
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
//		getSite().getPage().removePartListener(partListener);
		super.dispose();
	}
	
	/**
	 * 에디터의 성격을 정의 합니다.  
	 * 
	 * @return
	 */
	public OBJECT_TYPE getDbAction() {
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
		editorService = new MainEditorBrowserFunctionService(userDB, browserQueryEditor, EditorFunctionService.EDITOR_SERVICE_HANDLER, this);
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
//				MessageDialog.openError(null, Messages.get().Error, "Download fail. \n" + event.getException().getMessage());
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