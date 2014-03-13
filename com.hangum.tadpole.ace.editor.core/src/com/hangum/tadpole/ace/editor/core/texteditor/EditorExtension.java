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
package com.hangum.tadpole.ace.editor.core.texteditor;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.utils.TadpoleEditorUtils;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;

/**
 * Editor의 확장을 정의한다.
 * 
 * @author hangum
 *
 */
public abstract class EditorExtension extends EditorPart implements IEditorExtension {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EditorExtension.class);

	///[browser editor]/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected Browser browserQueryEditor;
	/** browser.browserFunction의 서비스 헨들러 */
	protected EditorFunctionService editorService;

//	/** session에서 사용자 정보를 가져다 놓습니다.
//	 * No context available outside of the request service lifecycle.
//	 */
	protected final String strUserEMail = SessionManager.getEMAIL();
	protected String strRoleType = "";
	protected final int intUserSeq = SessionManager.getSeq();
	
	/** 쿼리 결과에 리미트 쿼리 한계를 가져오게 합니다. */
	protected int queryResultCount 	= GetPreferenceGeneral.getQueryResultCount();
	/** 쿼리 결과를 page당 처리 하는 카운트 */
	protected int queryPageCount 		= GetPreferenceGeneral.getPageCount();
	/** oracle plan table 이름 */
	protected String planTableName 	= GetPreferenceGeneral.getPlanTableName();
////	/** export delimit */
////	protected String EXPORT_DEMILITER = GetPreferenceGeneral.getExportDelimit().equalsIgnoreCase("tab")?"	":GetPreferenceGeneral.getExportDelimit() + " "; //$NON-NLS-1$ //$NON-NLS-2$
	/** 결과 컬럼이 숫자이면 ,를 찍을 것인지 */
	protected boolean isResultComma = GetPreferenceGeneral.getISRDBNumberIsComma();
	
//	protected String QUERY_DELIMITER = ";";
	
	/** 현재 에디터에서 처리해야하는 디비 정보. */
	protected UserDBDAO userDB;
	
	public EditorExtension() {
		super();
	}
	
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	/**
	 * command
	 * 
	 * @param command
	 */
	public void browserEvaluate(String command) {
		browserEvaluate(command, "");
	}
	/** 
	 * browser function call
	 * 
	 *  @param command brower command
	 *  @param args command argument
	 */
	public void browserEvaluate(String command, String ... args) {
		if(logger.isDebugEnabled()) {
			logger.debug("\t ### send command is : [command] " + command + ", [args]" + args);
		}
		
		try {
			browserQueryEditor.evaluate(String.format(command, TadpoleEditorUtils.makeGrantArgs(args)));
		} catch(Exception e) {
			logger.error(RequestInfoUtils.requestInfo("browser evaluate [ " + command + " ]\r\n", strUserEMail), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	public String browserEvaluateToStr(String command) {
		return browserEvaluateToStr(command, "");
	}
	
	/**
	 * 
	 * @param command
	 * @param args
	 * @return
	 */
	public String browserEvaluateToStr(String command, String ... args) {
		if(logger.isDebugEnabled()) {
			logger.debug("\t ### send command is : " + command);
		}
		
		try {
			Object ret = browserQueryEditor.evaluate(String.format(command, TadpoleEditorUtils.makeGrantArgs(args)));
			if(ret != null) return ret.toString();
		} catch(Exception e) {
			logger.error(RequestInfoUtils.requestInfo("browser evaluate [ " + command + " ]\r\n", strUserEMail), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return "";
	}
	
	/**
	 * orion text setfocus
	 */
	public void setOrionTextFocus() {
		try {
			browserQueryEditor.evaluate(EditorFunctionService.SET_FOCUS);
		} catch(Exception e) {
			// ignore exception
		}
	}
	
	/**
	 * 사용자 role_type을 리턴한다.
	 * 
	 * @return
	 */
	protected String getUserType() {
		return strRoleType;
	}
	
	/**
	 * register browser function
	 */
	protected abstract void registerBrowserFunctions();
	
	/**
	 * unregister browser function
	 */
	private void unregisterBrowserFunctions() {
		if(editorService != null && editorService instanceof BrowserFunction) {
			editorService.dispose();
		}
	}
	
	@Override
	public void dispose() {
		unregisterBrowserFunctions();
		super.dispose();
	}

}
