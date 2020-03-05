package com.hangum.tadpole.mongodb.core.ext.editors.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.commons.dialogs.fileupload.MainEditorFileuploadDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.utils.EditorDefine;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.mongodb.core.composite.result.MongodbResultComposite;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.db.DBInformationDialog;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.objectmain.ObjectEditor;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.ResourceManager;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.RESULT_COMP_TYPE;

/**
 * 몽고디비 메인 에디터 
 * 
 * @author hangum
 *
 */
public class MongoDBQueryEditor extends MainEditor {
	private static final Logger logger = Logger.getLogger(MongoDBQueryEditor.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.editor.mongodb.query";

	/** collection column info */
	private Map<String, String> mapCollections = new HashMap<String, String>();
	
	/** 쿼리 결과 출력 */
	private MongodbResultComposite compositeResult ;

	public MongoDBQueryEditor() {
		super();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		resultViewType = RESULT_COMP_TYPE.TABLEJSON;
		
		MongoDBQueryEditorInput qei = (MongoDBQueryEditorInput)input;
		try {
			userDB = (UserDBDAO)qei.getUserDB().clone();
		} catch(Exception e) {
			logger.error("set define default userDB", e);
		}
		
		this.initDefaultEditorStr = qei.getDefaultStr();
		this.strLastContent = qei.getDefaultStr();
		
		this.dbAction = OBJECT_TYPE.TABLES;
		
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
						initDefaultEditorStr = dBResourceAuto.getDataString() + Messages.get().AutoRecoverMsg + initDefaultEditorStr;
					}
				}
			} catch(Exception e) {
				logger.error("Get default resource", e);
			}
		}
		strLastContent = initDefaultEditorStr;
		refreshCollectionList();

		setSite(site);
		setInput(input);
		setPartName(strPartName);
	}
	
	/**
	 * refresh collection list
	 */
	private void refreshCollectionList() {
		// 만약에 컬렉션이 null이면 컬렉션 정보를 갱신해준다.
//		try {
//			this.mapCollections = MongoDBQuery.listAllCollection(userDB);
//		} catch (Exception e1) {
//			logger.error("initialize collection list", e1);
//		}
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
		
		ToolItem sep = new ToolItem(toolBar, SWT.SEPARATOR);
		
		comboSchema = new Combo(toolBar, SWT.READ_ONLY);
		comboSchema.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeSchema();
			}
		});
		
		//
		// 스키마리스트가 없는 경우 스키마 리스트를 가지고 넣는다.
		//
		if(userDB.getSchemas().isEmpty()) {
			try {
				for(String strSchema : MongoDBQuery.getSchema(userDB)) {
					comboSchema.add(strSchema);
					userDB.addSchema(strSchema);
				}
			} catch(Exception e) {
				logger.error("get schema list " + e.getMessage());
			}
		} else {
			for (String schema : userDB.getSchemas()) {
				comboSchema.add(schema);
			}
		}
		comboSchema.setVisibleItemCount(comboSchema.getItemCount() > 15 ? 15 : comboSchema.getItemCount());
		
		// 기본 스키마가 설정되어 있지 않는다면 기본 스키마를 설정한다.
		if("".equals(userDB.getSchema())) {
			userDB.setSchema(userDB.getDb());
		}
		
		comboSchema.setText(userDB.getSchema());
		comboSchema.pack();
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		sep.setWidth(comboSchema.getSize().x);
	    sep.setControl(comboSchema);
	    toolBar.pack();
		
		// fileupload 
		ToolItem tltmOpen = new ToolItem(toolBar, SWT.NONE);
		tltmOpen.setToolTipText(Messages.get().MainEditor_35);
		tltmOpen.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/file-open.png")); //$NON-NLS-1$
		tltmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MainEditorFileuploadDialog dialog = new MainEditorFileuploadDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.get().MainEditor_36);
				if(Dialog.OK == dialog.open()) {
//					if(logger.isDebugEnabled()) logger.debug("============> " +  dialog.getStrFileContent()); //$NON-NLS-1$
					if(MainEditorFileuploadDialog.ENUM_OPEN_TYPE.ADD_APPEND.name().equals(dialog.getStrComboOpenType())) {
						appendText(dialog.getStrFileContent());
					} else if(MainEditorFileuploadDialog.ENUM_OPEN_TYPE.NEW_WINDOW.name().equals(dialog.getStrComboOpenType())) {
						FindEditorAndWriteQueryUtil.run(userDB, "", dialog.getStrFileContent(), true, PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS);
					} else if(MainEditorFileuploadDialog.ENUM_OPEN_TYPE.REMOVE_AND_ADD.name().equals(dialog.getStrComboOpenType())) {
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
	    
	    compositeResult = new MongodbResultComposite(sashForm, SWT.NONE, userDB, true);
		compositeResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.horizontalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 0;
		compositeResult.setLayout(gl_compositeResult);
		
		sashForm.setWeights(new int[] {65, 35});
		initEditor();
		
	}
	
	/**
	 * change schema
	 */
	private void changeSchema() {
		final String strSchema = comboSchema.getText();
		userDB.setDefaultSchemanName(strSchema);
		
		// 기존에 설정되어 있는 테이블 목록등을 삭제한다.
		userDB.setTableListSeparator(null);
		userDB.setViewListSeparator(null);
		userDB.setFunctionLisstSeparator(null);
		
		try {

			//오브젝트 익스플로어가 같은 스키마 일경우 스키마가 변경되도록.
			getSite().getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						// 오브젝트 탐색기가 열려 있으면 탐색기의 스키마 이름을 변경해 줍니다.
						ExplorerViewer ev = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ExplorerViewer.ID);
						ev.changeSchema(userDB, strSchema);
						
					} catch (PartInitException e) {
						logger.error("ExplorerView show", e); //$NON-NLS-1$
					}
				}
				
			});
		} catch(Exception ee) {
			logger.error("change connection schema", ee);
			
			showInfoMessage("An error occurred while changing the schema.\n" + ee.getMessage());
		}
		
		refreshCollectionList();
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
		if(StringUtils.isBlank(reqQuery.getOriginalSql())) return;
		
		if(!userDB.is_isUseEnable()) {
			MessageDialog.openInformation(getSite().getShell(), CommonMessages.get().Information, CommonMessages.get().TermExpiredMsg);
			return;
		}
		
		// do not execute query
		if(System.currentTimeMillis() > SessionManager.getServiceEnd().getTime()) {
			MessageDialog.openInformation(null, CommonMessages.get().Information, Messages.get().MainEditorServiceEnd);
			return;
		}
		
		String strCheckSQL = reqQuery.getSql();
		
		// change schema test
		if(StringUtils.startsWithIgnoreCase(strCheckSQL, "use ")) {
			try {
				String strSchema = StringUtils.trim(StringUtils.removeStartIgnoreCase(strCheckSQL, "use "));
				
				int intItemOfSchema = -1;
				for(int i=0; i<comboSchema.getItemCount(); i++) {
					String strTmpSchema = comboSchema.getItem(i);
					
					if(strSchema.equalsIgnoreCase(strTmpSchema)) {
						intItemOfSchema = i;
						break;
					}
				}
				
				if(intItemOfSchema != -1) {
					userDB.setSchema(comboSchema.getItem(intItemOfSchema));
					comboSchema.setText(comboSchema.getItem(intItemOfSchema));
					changeSchema();
				}
			} catch(Exception e) {
				MessageDialog.openError(null, CommonMessages.get().Error, e.getMessage());
				setFocus();
			}
		} else {
			
			// 파라미터 쿼리이라면 파라미터 쿼리 상태로 만듭니다.
			if(!ifIsParameterQuery(reqQuery)) return;
					
			_executeQuery(reqQuery);
		}
	}
	
	/**
	 * 파라미터 쿼리인지 검사하여 쿼리를 만듭니다.
	 * @return
	 */
	private boolean ifIsParameterQuery(final RequestQuery reqQuery) {
		if(reqQuery.getExecuteType() == EditorDefine.EXECUTE_TYPE.ALL) return true;
		final DBGroupDefine dbGroup = getUserDB().getDBGroup();
		if(DBGroupDefine.HIVE_GROUP == dbGroup 
				|| DBGroupDefine.CUBRID_GROUP == dbGroup) return true; 
		
//		final Shell runShell = getSite().getShell();
//		
//		// oracle parameter
//		try {
//			OracleStyleSQLNamedParameterUtil oracleNamedParamUtil = new OracleStyleSQLNamedParameterUtil();
////			String strSQL = oracleNamedParamUtil.parse(SQLUtil.removeComment(reqQuery.getSql()));
//			String strSQL = oracleNamedParamUtil.parse(reqQuery.getSql());
//			
//			Map<Integer, String> mapIndexToName = oracleNamedParamUtil.getMapIndexToName();
//			if(!mapIndexToName.isEmpty()) {
//				
//				ParameterDialog epd = new ParameterDialog(runShell, this, PublicTadpoleDefine.PARAMETER_TYPE.ORACLE, reqQuery, getUserDB(), strSQL, mapIndexToName);
//				epd.open();
//				listParameterDialog.add(epd);
//				return false;
//			}
//		} catch(Exception e) {
//			logger.error("Oracle sytle parameter parse", e); //$NON-NLS-1$
//		}
//
//		// mybatis shap
//		GenericTokenParser mybatisShapeUtil = new GenericTokenParser("#{", "}");
//		String strSQL = mybatisShapeUtil.parse(reqQuery.getSql());
//		Map<Integer, String> mapIndexToName = mybatisShapeUtil.getMapIndexToName();
//		if(!mapIndexToName.isEmpty()) {
//			
//			ParameterDialog epd = new ParameterDialog(runShell, this, PublicTadpoleDefine.PARAMETER_TYPE.MYBATIS_SHARP, reqQuery, getUserDB(), strSQL, mapIndexToName);
//			epd.open();
//			listParameterDialog.add(epd);
//			return false;
//		}
//		
//		if(GetPreferenceGeneral.getIsMyBatisDollor()) {
//			GenericTokenParser mybatisDollarUtil = new GenericTokenParser("${", "}");
//			strSQL = mybatisDollarUtil.parse(reqQuery.getSql());
//			mapIndexToName = mybatisDollarUtil.getMapIndexToName();
//			if(!mapIndexToName.isEmpty()) {
//			
//				ParameterDialog epd = new ParameterDialog(runShell, this, PublicTadpoleDefine.PARAMETER_TYPE.MYBATIS_DOLLAR, reqQuery, getUserDB(), strSQL, mapIndexToName);
//				epd.open();
//				listParameterDialog.add(epd);
//				return false;
//			}
//		}

		return true;
	}
	
	/**
	 * 실제 쿼리를 실행한다.
	 * 
	 * @param reqQuery
	 * @return
	 */
	public boolean _executeQuery(final RequestQuery reqQuery) {
		// 사용자 설정 query timeout
		int _intPreferenceQueryTimeout = 10000;//Integer.parseInt(GetPreferenceGeneral.getMongoQueryTimeout());
		// 사용자 설정 limit 
		int _intPreferenceMaxSearchCount = GetPreferenceGeneral.getMongoDefaultMaxCount();
		
		
		final List<QueryExecuteResultDTO> listRSDao = new ArrayList<>();
		
		QueryExecuteResultDTO rsDAO = new QueryExecuteResultDTO();
		try {
			
			// execute monogodb command
			// 실제연결되 디비 스키마와 실행하는 스키마가 다를수 있습니다. 
			rsDAO = compositeResult.executeCommand(userDB, mapCollections, reqQuery, _intPreferenceQueryTimeout, _intPreferenceMaxSearchCount);
			
		} catch(Exception e) {
			logger.error("execute mongodb shell", e); //$NON-NLS-1$
			
		} finally {
		}
		
		setOrionTextFocus();

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
