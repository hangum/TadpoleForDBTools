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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.ace.editor.core.utils.TadpoleEditorUtils;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.session.manager.SessionManager;

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

	/** session에서 사용자 정보를 가져다 놓습니다.
	 * No context available outside of the request service lifecycle.
	 */
	/**
	 * 사용자 email
	 */
	private final String userEMail 		= SessionManager.getEMAIL();
	
	/**
	 * 사용자 seq
	 */
	private final int userSeq 			= SessionManager.getUserSeq();
	protected String strRoleType 		= "";
//	protected final int intUserSeq 		= SessionManager.getSeq();
	
	/** 현재 에디터에서 처리해야하는 디비 정보. */
	protected UserDBDAO userDB;
	
	public EditorExtension() {
		super();
	}
	
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public String getUserEMail() {
		return userEMail;
	}
	
	public int getUserSeq() {
		return userSeq;
	}
	

	/**
	 * find editor extension
	 * 
	 * eg) mysql, pgsql
	 * @return
	 */
	protected String findEditorExt() {
		String ext = EditorDefine.EXT_DEFAULT;
		if(DBDefine.MYSQL_DEFAULT == userDB.getDBDefine() || DBDefine.MARIADB_DEFAULT == userDB.getDBDefine()) {
			ext = EditorDefine.EXT_MYSQL;
		} else if(DBDefine.POSTGRE_DEFAULT == userDB.getDBDefine()) {
			ext = EditorDefine.EXT_PGSQL;
		} else if(DBDefine.SQLite_DEFAULT == userDB.getDBDefine()) {
			ext = EditorDefine.EXT_SQLite;
//		테이블명이 올바로 표시되지 않는 오류로 sql 확장자로 처리 할수 있도록 수정합니다. 	
//		} else if(DBDefine.MSSQL_8_LE_DEFAULT == userDB.getDBDefine() || DBDefine.MSSQL_DEFAULT == userDB.getDBDefine()) {
//			ext = EditorDefine.EXT_MSSQL;
		}
		return ext;
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
			StringBuffer strOption = new StringBuffer();
			for(String arg : args) {
				strOption.append(arg + ", \t");
			}
			logger.debug("\t ### send command is : [command] " + command + ", \n [args]" + strOption.toString());
		}
		
		try {
//			logger.debug("===[start]==========================================================================>");
//			logger.debug(TadpoleEditorUtils.makeGrantArgs(args)[0]);
//			logger.debug("===[end]==========================================================================>");
			
			browserQueryEditor.evaluate(String.format(command, TadpoleEditorUtils.makeGrantArgs(args)));
		} catch(Exception e) {
			logger.error(RequestInfoUtils.requestInfo("browser evaluate [ " + command + " ]\r\n", getUserEMail()), e); //$NON-NLS-1$ //$NON-NLS-2$
		}
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
			if(!StringUtils.endsWith(StringUtils.trimToEmpty(strText), PublicTadpoleDefine.SQL_DELIMITER)) {
				strText += PublicTadpoleDefine.SQL_DELIMITER;
			}
			
			browserEvaluate(EditorFunctionService.APPEND_TEXT, strText);
		} catch(Exception ee){
			logger.error("query text" , ee); //$NON-NLS-1$
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
			logger.error(RequestInfoUtils.requestInfo("browser evaluate [ " + command + " ]\r\n", getUserEMail()), e); //$NON-NLS-1$ //$NON-NLS-2$
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
//			logger.error("set focous", e);
		}
	}
	
	public void setUserType(String userRoleType) {
		strRoleType = userRoleType;
	}
	
	/**
	 * 사용자 role_type을 리턴한다.
	 * 
	 * @return
	 */
	public String getUserType() {
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
