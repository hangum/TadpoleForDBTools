/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.elasticsearch.core.editos.main;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.texteditor.function.IEditorFunction;
import com.hangum.tadpole.commons.dialogs.fileupload.SingleFileuploadDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.define.TDBResultCodeDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.utils.EditorDefine;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultMainComposite;
import com.hangum.tadpole.rdb.core.editors.objectmain.ObjectEditor;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.ResourceManager;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.RESULT_COMP_TYPE;

/**
 * Elastic Search editor
 * 
 * @author hangum
 *
 */
public class ElasticsearchEditor extends MainEditor {
	private static final Logger logger = Logger.getLogger(ElasticsearchEditor.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.elasticsearch.main";
	
	public ElasticsearchEditor() {
		super();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		resultViewType = RESULT_COMP_TYPE.TABLEJSON;
		
		ElasticsearchEditorInput qei = (ElasticsearchEditorInput)input;
		try {
			userDB = (UserDBDAO)qei.getUserDB().clone();
		} catch(Exception e) {
			logger.error("set define default userDB", e);
		}
		
		initDefaultEditorStr = qei.getDefaultStr();
		strLastContent = qei.getDefaultStr();
		dbAction = OBJECT_TYPE.TABLES;
		
		String strPartName = qei.getName();
		dBResource = qei.getResourceDAO();
		if(dBResource != null) {
			strPartName = dBResource.getName();
		} else {
			// 기본 저장된 쿼리가 있는지 가져온다.
			try {
				dBResourceAuto = TadpoleSystem_UserDBResource.getDefaultDBResourceData(userDB);
				if(dBResourceAuto != null) {
					if(!StringUtils.isEmpty(dBResourceAuto.getDataString())) {
						if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
							initDefaultEditorStr = dBResourceAuto.getDataString() + Messages.get().AutoRecoverMsg_mysql + initDefaultEditorStr;
						} else {
							initDefaultEditorStr = dBResourceAuto.getDataString() + Messages.get().AutoRecoverMsg + initDefaultEditorStr;
						}
					}
				}
			} catch(Exception e) {
				logger.error("Get default resource", e);
			}
		}
		strLastContent = initDefaultEditorStr;

		setSite(site);
		setInput(input);
		setPartName(strPartName);
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
//					if(logger.isDebugEnabled()) logger.debug("============> " +  dialog.getStrFileContent()); //$NON-NLS-1$
					if(SingleFileuploadDialog.ENUM_OPEN_TYPE.ADD_APPEND.name().equals(dialog.getStrComboOpenType())) {
						appendText(dialog.getStrFileContent());
					} else if(SingleFileuploadDialog.ENUM_OPEN_TYPE.NEW_WINDOW.name().equals(dialog.getStrComboOpenType())) {
						FindEditorAndWriteQueryUtil.run(userDB, "", dialog.getStrFileContent(), true, PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS);
					} else if(SingleFileuploadDialog.ENUM_OPEN_TYPE.REMOVE_AND_ADD.name().equals(dialog.getStrComboOpenType())) {
						try {
							browserEvaluate(EditorFunctionService.RE_NEW_TEXT, dialog.getStrFileContent());
						} catch(Exception ee) {
							logger.error("browser re_new_text error");
						}
					}
				}
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		// 결과를 텍스트로 출력할 것인지 여부
		tltmTextResultView = new ToolItem(toolBar, SWT.CHECK);
//						tltmTextResultView.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/textNote.png")); //$NON-NLS-1$
		tltmTextResultView.setText("Text");
		tltmTextResultView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tltmTextResultView.getSelection()) {
					resultViewType = RESULT_COMP_TYPE.TextJSON;
					tltmTextResultView.setSelection(true);
				} else {
					resultViewType = RESULT_COMP_TYPE.TABLEJSON;
					tltmTextResultView.setSelection(false);
				}
			}
		});
		tltmTextResultView.setToolTipText(Messages.get().TextResultView);
		tltmTextResultView.setEnabled(PublicTadpoleDefine.YES_NO.YES.name().equals(getUserDB().getIs_resource_download()));
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
				
				RequestQuery reqQuery = new RequestQuery(getConnectionid(), userDB, strQuery, dbAction, EditorDefine.QUERY_MODE.QUERY, executeType, isAutoCommit());
				executeCommand(reqQuery);
			}
		});
		new ToolItem(toolBar, SWT.SEPARATOR);
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
		
		if(!userDB.is_isUseEnable()) {
			MessageDialog.openInformation(getSite().getShell(), CommonMessages.get().Information, CommonMessages.get().TermExpiredMsg);
			return;
		}
		
		// do not execute query
		if(System.currentTimeMillis() > SessionManager.getServiceEnd().getTime()) {
			MessageDialog.openInformation(null, CommonMessages.get().Information, Messages.get().MainEditorServiceEnd);
			return;
		}
		
		// 파라미터 쿼리이라면 파라미터 쿼리 상태로 만듭니다.
//		if(!ifIsParameterQuery(reqQuery)) return;
				
		_executeQuery(reqQuery);
	}

	/**
	 * 실제 쿼리를 실행한다.
	 * 
	 * @param reqQuery
	 * @return
	 */
	public boolean _executeQuery(final RequestQuery reqQuery) {
		// 결과를 출력할 곳을 지정합니
		final int intSelectLimitCnt = GetPreferenceGeneral.getSelectLimitCount();
		
		final RequestResultDAO reqResultDAO = new RequestResultDAO();
		reqResultDAO.setStartDateExecute(new Timestamp(System.currentTimeMillis()));
		reqResultDAO.setIpAddress(reqQuery.getUserIp());
		reqResultDAO.setTdb_sql_head("");
		reqResultDAO.setSql_text(reqQuery.getSql());
		final List<QueryExecuteResultDTO> listRSDao = new ArrayList<>();
		
		QueryExecuteResultDTO queryResultDAO = new QueryExecuteResultDTO();
		final String errMsg = Messages.get().MainEditor_21;
		
		try {
//			ElasticsearchResultDataDAO eSearchDao = ElasticSearchQuery.executeQuery(errMsg, userDB, reqQuery, intSelectLimitCnt);
//			
//			if(eSearchDao.isSucceeded()) {
//				if(logger.isDebugEnabled()) logger.debug("result value ===> " + eSearchDao.getStrJson() );
//				String strResult = eSearchDao.getStrJson();
//				if(resultViewType == RESULT_COMP_TYPE.TABLEJSON) {
//					JsonElement jsonEleent = new JsonParser().parse(strResult);
//					JsonElement jsonHitsObject = jsonEleent.getAsJsonObject().get("hits");
//					
//					if(jsonHitsObject != null) {
//						JsonElement dataHits = jsonHitsObject.getAsJsonObject().get("hits");
//						strResult = dataHits.toString();
//					}
//				}
//				
//				queryResultDAO = new QueryExecuteResultDTO(getUserDB(), reqQuery.getSql(), true, strResult, intSelectLimitCnt, intSelectLimitCnt);
//			} else {
//				reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.name()); //$NON-NLS-1$
//				reqResultDAO.setTdb_result_code(eSearchDao.getResponseCode());
//				reqResultDAO.setMesssage(eSearchDao.getErrorMsg());
//			}
//		} catch(TadpoleException te) {
//			reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.name()); //$NON-NLS-1$
//			reqResultDAO.setTdb_result_code(te.getErrorCode());
//			reqResultDAO.setMesssage(te.getMessage());
//			
		} catch(Exception e) {
			logger.error("execute elasticsearch", e); //$NON-NLS-1$
			
			reqResultDAO.setResult(PublicTadpoleDefine.SUCCESS_FAIL.F.name()); //$NON-NLS-1$
			reqResultDAO.setTdb_result_code(TDBResultCodeDefine.INTERNAL_SERVER_ERROR);
			reqResultDAO.setMesssage(e.getMessage());
		} finally {
			reqResultDAO.setEndDateExecute(new Timestamp(System.currentTimeMillis()));
			reqQuery.setRequestResultDao(reqResultDAO);
			
			if(PublicTadpoleDefine.SUCCESS_FAIL.F.name().equals(reqQuery.getRequestResultDao().getResult())) {
				resultMainComposite.refreshInfoMessageView(reqQuery, Messages.get().ResultSetComposite_10 + reqQuery.getRequestResultDao().getMesssage() + PublicTadpoleDefine.LINE_SEPARATOR  + StringUtils.abbreviate(reqQuery.getOriginalSql(), 200));
				resultMainComposite.resultFolderSel(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE);				
			} else {
				List<Long> listLongHistorySeq = new ArrayList<>();
				listRSDao.add(queryResultDAO);
				
				resultMainComposite.showResultView(reqQuery, listRSDao, listLongHistorySeq);
				resultMainComposite.resultFolderSel(EditorDefine.RESULT_TAB.RESULT_SET);
			}
		}
		
		resultMainComposite.getCompositeQueryHistory().saveExecutedSQLData(reqResultDAO, listRSDao.get(0));
		
		setOrionTextFocus();

		// google analytic
		AnalyticCaller.track(ObjectEditor.ID, "executeCommandObject"); //$NON-NLS-1$
		
		return true;
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
	
}
