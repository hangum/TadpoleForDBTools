/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.ext.editors.javascript.browserfunction;

import com.hangum.tadpold.commons.libs.core.browser.TadpoleBrowserFunction;

/**
 * editor browser function 정의 
 * 
 * @author hangum
 *
 */
public interface IJavaScriptBrowserFunction extends TadpoleBrowserFunction {
	
	
	public static final int DIRTY_CHANGED 			= 1;
//	public static final int GET_CONTENT_NAME 		= 5;
	public static final int GET_INITIAL_CONTENT 	= 10;
	
	public static final int SAVE 					= 15;
	public static final int SAVE_S					= 16;
	
	public static final int STATUS_CHANGED 			= 20;
	
	public static final int EXECUTE_QUERY 			= 25;
	public static final int EXECUTE_FORMAT 			= 35;
	
	public static final int DOWNLOAD_SQL	 		= 50;
	
	/** query history page로 이동합니다 */
	public static final int MOVE_HISTORY_PAGE		= 55;
	
	// help popup
	public static final int HELP_POPUP				= 60;
	
	public static final int SET_FOCUS 				= 999;
    
    public static final String EDITOR_SERVICE_MAP = "editorService";
    public static final String EDITOR_SERVICE_HANDLER = "editorServiceHandler";
    
    /** 자바 스크립트 초기화 */
    public static final String JAVA_SCRIPT_INIT_EMBEDDED_EDITOR = "initEmbeddedEditor()";
    
    /** init container */
    public static final String JAVA_SCRIPT_GET_INITCONTAINER = EDITOR_SERVICE_MAP + ".getInitialContent()";
    
    /** 저장 */
    public static final String JAVA_SCRIPT_SAVE_FUNCTION = "return " + EDITOR_SERVICE_MAP + ".save()";
    
    /** 쿼리 */
    public static final String JAVA_SCRIPT_EXECUTE_QUERY_FUNCTION = EDITOR_SERVICE_MAP + ".executeQuery()";
    
    /** 쿼리 포멧 */
    public static final String JAVA_SCRIPT_EXECUTE_FORMAT_FUNCTION = "return " + EDITOR_SERVICE_MAP + ".executeFormat()";
    
    /** 에디터에 기존 텍스트를 없애고 새롭게 데이터를 채웁니다 */
    public static final String JAVA_SCRIPT_RE_NEW = "return " + EDITOR_SERVICE_MAP + ".reNew()";
    
    /** DOWNLOAD SQL */
    public static final String JAVA_DOWNLOAD_SQL = EDITOR_SERVICE_MAP + ".downloadSQL()";
    
    /** TEXT FOCUS */
    public static final String JAVA_SCRIPT_SET_FOCUS_FUNCTION = EDITOR_SERVICE_MAP + ".setTextFocus()";
    
}
