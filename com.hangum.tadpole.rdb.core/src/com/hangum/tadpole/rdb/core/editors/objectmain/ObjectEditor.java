/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.objectmain;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.texteditor.function.IEditorFunction;
import com.hangum.tadpole.commons.dialogs.fileupload.SingleFileuploadDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.AbstractTadpoleManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.DBSystemSchema;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;
import com.hangum.tadpole.engine.sql.util.ObjectCompileUtil;
import com.hangum.tadpole.engine.utils.EditorDefine;
import com.hangum.tadpole.engine.utils.EditorDefine.EXECUTE_TYPE;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultMainComposite;
import com.hangum.tadpole.rdb.core.util.GrantCheckerUtils;
import com.hangum.tadpole.rdb.core.viewers.connections.DBIconsUtils;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.ResourceManager;

/**
 * Object Editor
 * 
 * @author hangum
 *
 */
public class ObjectEditor extends MainEditor {
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.main.procedure"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(ObjectEditor.class);
	
	public ObjectEditor() {
		super();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		ObjectEditorInput qei = (ObjectEditorInput)input;
		userDB = qei.getUserDB();
		initDefaultEditorStr = qei.getDefaultStr();
		dbAction = qei.getDbAction();
		strRoleType = userDB.getRole_id();
		
		String strPartName = "";
		if("".equals(qei.getObjectName())) strPartName = qei.getName(); //$NON-NLS-1$
		else strPartName = String.format("%s (%s)", qei.getName(), qei.getObjectName()); //$NON-NLS-1$

		setSite(site);
		setInput(input);
		setPartName(strPartName);
		setTitleImage(DBIconsUtils.getProcedureImage(getUserDB()));
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
					if(logger.isDebugEnabled()) logger.debug("============> " +  dialog.getStrTxtFile()); //$NON-NLS-1$
					appendText(dialog.getStrTxtFile());
				}
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmCompile = new ToolItem(toolBar, SWT.NONE);
		tltmCompile.setToolTipText(String.format(Messages.get().Compile, STR_SHORT_CUT_PREFIX));
		tltmCompile.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/compile.png")); //$NON-NLS-1$
		tltmCompile.addSelectionListener(new SelectionAdapter() {
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
		
		// mysql group 이면 스키마 목록이 보이도록 합니다.
		if(getUserDB().getDBGroup() == DBGroupDefine.MYSQL_GROUP | userDB.getDBGroup() == DBGroupDefine.ORACLE_GROUP | userDB.getDBGroup() == DBGroupDefine.POSTGRE_GROUP) {
			ToolItem sep = new ToolItem(toolBar, SWT.SEPARATOR);
			
			comboSchema = new Combo(toolBar, SWT.READ_ONLY);
			comboSchema.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					final String strSchema = comboSchema.getText();
					userDB.setSchema(strSchema);
					
					// 기존에 설정되어 있는 테이블 목록등을 삭제한다.
					userDB.setTableListSeparator(null);
					userDB.setViewListSeparator(null);
					userDB.setFunctionLisstSeparator(null);
					
					Connection conn = null;
					try {
						conn = TadpoleSQLManager.getConnection(userDB);
						AbstractTadpoleManager.changeSchema(userDB, conn);
					} catch(Exception e3) {
						logger.error("** initialize connection", e3);
					} finally {
						try { if(conn != null) conn.close(); } catch(Exception ee) {}
					}
					
					//오브젝트 익스플로어가 같은 스키마 일경우 스키마가 변경되도록.
					getSite().getShell().getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							try {
								// 오브젝트 탐색기가 열려 있으면 탐색기의 스키마 이름을 변경해 줍니다.
								IViewReference[] iViewReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
								for (IViewReference iViewReference : iViewReferences) {
									if(ExplorerViewer.ID.equals(iViewReference.getId())) {
										ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ExplorerViewer.ID);
										ev.changeSchema(userDB, strSchema);
										
										break;
									}
								}
							} catch (PartInitException e) {
								logger.error("ExplorerView show", e); //$NON-NLS-1$
							}
						}
						
					});
				}
			});
			
			//
			// 스키마리스트가 없는 경우 스키마 리스트를 가지고 넣는다.
			//
			if(userDB.getSchemas().isEmpty()) {
				try {
					for (Object object : DBSystemSchema.getSchemas(userDB)) {
						HashMap<String, String> mapData = (HashMap)object;
						comboSchema.add(mapData.get("SCHEMA"));
						userDB.addSchema(mapData.get("SCHEMA"));
					}
					comboSchema.select(0);
					userDB.setSchema(comboSchema.getText());
					
				} catch(Exception e) {
					logger.error("get schema list " + e.getMessage());
				}
			} else {
			
				for (String schema : userDB.getSchemas()) {
					comboSchema.add(schema);
				}
				if("".equals(userDB.getSchema())) {
					comboSchema.select(0);
					userDB.setSchema(comboSchema.getText());
				}
			}
			comboSchema.setVisibleItemCount(userDB.getSchemas().size());
			comboSchema.setText(userDB.getSchema());
			comboSchema.pack();
			new ToolItem(toolBar, SWT.SEPARATOR);
			
			sep.setWidth(comboSchema.getSize().x);
		    sep.setControl(comboSchema);
		    toolBar.pack();
		}
		
//		ToolItem tltmExecute = new ToolItem(toolBar, SWT.NONE);
//		tltmExecute.setToolTipText(Messages.get().ObjectEditor_5);
//		tltmExecute.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/play.png")); //$NON-NLS-1$
//		tltmExecute.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//
//			}
//		});
//		new ToolItem(toolBar, SWT.SEPARATOR);
//		
//		ToolItem tltmHelp = new ToolItem(toolBar, SWT.NONE);
//		tltmHelp.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/about.png")); //$NON-NLS-1$
//		tltmHelp.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				RDBShortcutHelpDialog dialog = new RDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
//				dialog.open();
//				
//				setFocus();
//			}
//		});
//		tltmHelp.setToolTipText(String.format(Messages.get().MainEditor_27, STR_SHORT_CUT_PREFIX));
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
		
		// change editor style
		PlatformUI.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {

				if(event.getProperty() == PreferenceDefine.EDITOR_CHANGE_EVENT) {
					final String varTheme 		= PublicTadpoleDefine.getMapTheme().get(GetPreferenceGeneral.getEditorTheme());
				    final String varFontSize 	= GetPreferenceGeneral.getEditorFontSize();
				    final String varIsWrap 		= ""+GetPreferenceGeneral.getEditorIsWarp(); //$NON-NLS-1$
				    final String varWarpLimit 	= GetPreferenceGeneral.getEditorWarpLimitValue();
				    final String varIsShowGutter = ""+GetPreferenceGeneral.getEditorShowGutter(); //$NON-NLS-1$
				    
				    browserEvaluate(IEditorFunction.CHANGE_EDITOR_STYLE, 
							varTheme, varFontSize, varIsWrap, varWarpLimit, varIsShowGutter
						);
				}
			} //
		}); // end property change
			
	}
	/**
	 * initialize editor
	 */
	private void initEditor() {
		// 초기 연결 커넥션을 초기화 합니다.
		if (DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			Connection conn = null;
			try {
				conn = TadpoleSQLManager.getConnection(userDB);
				AbstractTadpoleManager.changeSchema(userDB, conn);
			} catch(Exception e3) {
				logger.error("** initialize connection", e3);
			} finally {
				try { if(conn != null) conn.close(); } catch(Exception ee) {}
			}
		}
				
		// google analytic
		AnalyticCaller.track(MainEditor.ID, userDB.getDbms_type());
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
			MessageDialog.openInformation(null, CommonMessages.get().Information, Messages.get().MainEditorServiceEnd);
			return;
		}

		if(reqQuery.getExecuteType() == EXECUTE_TYPE.BLOCK) {
			resultMainComposite.executeCommand(reqQuery);
		} else {
			try {
				if(!GrantCheckerUtils.ifExecuteQuery(getUserDB(), reqQuery)) return;
			} catch (Exception e1) {
				logger.error("if execute query?", e1);
				return;
			}
			
//			if(!MessageDialog.openConfirm(null, CommonMessages.get().Confirm, Messages.get().ObjectEditor_3)) {
//				setOrionTextFocus();
//				return;
//			}
			
			RequestResultDAO reqResultDAO = new RequestResultDAO();
			try {
				ExecuteDDLCommand.executSQL(userDB, reqResultDAO, reqQuery.getOriginalSql()); //$NON-NLS-1$

			} catch(Exception e) {
				logger.error("execute ddl", e); //$NON-NLS-1$
				reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.name());
				reqResultDAO.setMesssage(e.getMessage());
			} finally {
				if(PublicTadpoleDefine.SUCCESS_FAIL.F.name().equals(reqResultDAO.getResult())) {
					afterProcess(reqQuery, reqResultDAO, ""); //$NON-NLS-1$
					
					if(DBGroupDefine.MYSQL_GROUP == getUserDB().getDBGroup()) {
						mysqlAfterFailProcess(reqResultDAO, reqQuery);
					} else if(DBGroupDefine.MSSQL_GROUP == getUserDB().getDBGroup()) {
						mssqlAfterFailProcess(reqResultDAO, reqQuery);
					}
					
				} else {
					String retMsg = ObjectCompileUtil.validateObject(userDB, reqQuery.getSqlDDLType(), reqQuery.getSqlObjectName());

					if(!"".equals(retMsg)) { //$NON-NLS-1$
						reqResultDAO.setMesssage(retMsg);
						
						afterProcess(reqQuery, reqResultDAO, Messages.get().ObjectEditorCompileError);
					} else {
						//DBMS_OUTOUT.PUT_LINE을 이용해 출력했던 내용이 있으면 그대로 표시하기 위해서 주석처리.
						//reqResultDAO.setMesssage("");
						afterProcess(reqQuery, reqResultDAO, Messages.get().ObjectEditor_2);
					}
					
				}

				setDirty(false);
				browserEvaluate(IEditorFunction.SAVE_DATA);
				setOrionTextFocus();
			}
		}

		// google analytic
		AnalyticCaller.track(ObjectEditor.ID, "executeCommandObject"); //$NON-NLS-1$
	}

	/**
	 * after process
	 * 
	 * @param reqQuery
	 * @param reqResultDAO
	 * @param title
	 */
	private void afterProcess(RequestQuery reqQuery, RequestResultDAO reqResultDAO, String title) {
		resultMainComposite.getCompositeQueryHistory().afterQueryInit(reqResultDAO);
		resultMainComposite.resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);
		
		if(PublicTadpoleDefine.SUCCESS_FAIL.S.name().equals(reqResultDAO.getResult())) {
			resultMainComposite.refreshInfoMessageView(reqQuery, String.format("%s %s", title, reqResultDAO.getMesssage())); //$NON-NLS-1$
			refreshExplorerView(getUserDB(), reqQuery);
		} else {
			resultMainComposite.refreshErrorMessageView(reqQuery, reqResultDAO.getException(), String.format("%s %s", title, reqResultDAO.getMesssage())); //$NON-NLS-1$			
		}
	}
	
	/**
	 * CREATE, DROP, ALTER 문이 실행되어 ExplorerViewer view를 리프레쉬합니다.
	 * 
	 * @param userDB
	 * @param reqQuery
	 */
	protected void refreshExplorerView(final UserDBDAO userDB, final RequestQuery reqQuery) {
		resultMainComposite.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ExplorerViewer.ID);
					ev.refreshCurrentTab(userDB, reqQuery);
				} catch (PartInitException e) {
					logger.error("ExplorerView show", e); //$NON-NLS-1$
				}
			}
			
		});
	}
	
	/**
	 * mysql 처리를 합니다.
	 * 
	 * @param reqResultDAO
	 * @param reqQuery
	 * @param e
	 */
	private void mysqlAfterFailProcess(RequestResultDAO reqResultDAO, RequestQuery reqQuery) {
		if(reqResultDAO.getException() == null) return;
		
		String strSQLState = "";
		int intSQLErrorCode = -1;
		Throwable cause = reqResultDAO.getException();;
		if (cause instanceof SQLException) {
			try {
				SQLException sqlException = (SQLException)cause;
				strSQLState = sqlException.getSQLState();
				intSQLErrorCode =sqlException.getErrorCode();
				
				if(logger.isDebugEnabled()) logger.debug("==========> SQLState : " + strSQLState + "\t SQLErrorCode : " + intSQLErrorCode);
			} catch(Exception e) {
				logger.error("SQLException to striing", e); //$NON-NLS-1$
			}
		}
		
		if(strSQLState.equals("42000") && intSQLErrorCode == 1304) { //$NON-NLS-1$
			
			String cmd = String.format("DROP %s %s", reqQuery.getSqlDDLType(), reqQuery.getSqlObjectName()); //$NON-NLS-1$
			if(MessageDialog.openConfirm(null, CommonMessages.get().Confirm, String.format(Messages.get().ObjectEditor_13, reqQuery.getSqlObjectName()))) {
				RequestResultDAO reqReResultDAO = new RequestResultDAO();
				try {
					ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, cmd); //$NON-NLS-1$
					afterProcess(reqQuery, reqReResultDAO, Messages.get().ObjectEditor_2);
					
					ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, reqQuery.getOriginalSql()); //$NON-NLS-1$
					afterProcess(reqQuery, reqReResultDAO, Messages.get().ObjectEditor_2);
				} catch(Exception ee) {
					afterProcess(reqQuery, reqResultDAO, ""); //$NON-NLS-1$
				}
			}
//		1064는 컴파일 에러.
//		} else if(strSQLState.equals("42000") && intSQLErrorCode == 1064) { //$NON-NLS-1$
		}
	}
	
	/**
	 * mssql after process
	 * @param reqResultDAO
	 * @param reqQuery
	 */
	private void mssqlAfterFailProcess(RequestResultDAO reqResultDAO, RequestQuery reqQuery) {
		if(reqResultDAO.getException() == null) return;
		String strSQLState = "";
		int intSQLErrorCode = -1;
		Throwable cause = reqResultDAO.getException();;
		if (cause instanceof SQLException) {
			try {
				SQLException sqlException = (SQLException)cause;
				strSQLState = sqlException.getSQLState();
				intSQLErrorCode =sqlException.getErrorCode();
				
				if(logger.isDebugEnabled()) logger.debug("==========> SQLState : " + strSQLState + "\t SQLErrorCode : " + intSQLErrorCode);
			} catch(Exception e) {
				logger.error("SQLException to striing", e); //$NON-NLS-1$
			}
		}
		
		if(strSQLState.equals("S0001") && intSQLErrorCode == 2714) { //$NON-NLS-1$
			String cmd = String.format("DROP %s %s", reqQuery.getSqlDDLType(), reqQuery.getSqlObjectName()); //$NON-NLS-1$
			if(MessageDialog.openConfirm(null, CommonMessages.get().Confirm, String.format(Messages.get().ObjectEditor_13, reqQuery.getSqlObjectName()))) {
				RequestResultDAO reqReResultDAO = new RequestResultDAO();
				try {
					ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, cmd); //$NON-NLS-1$
					afterProcess(reqQuery, reqReResultDAO, Messages.get().ObjectEditor_2);
					
					ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, reqQuery.getOriginalSql()); //$NON-NLS-1$
					afterProcess(reqQuery, reqReResultDAO, Messages.get().ObjectEditor_2);
				} catch(Exception ee) {
					afterProcess(reqQuery, reqResultDAO, ""); //$NON-NLS-1$
				}
			}
		}
		
	}
}