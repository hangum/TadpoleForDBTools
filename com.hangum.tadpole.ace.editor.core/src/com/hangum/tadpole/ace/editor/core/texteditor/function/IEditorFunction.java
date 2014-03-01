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
package com.hangum.tadpole.ace.editor.core.texteditor.function;

import com.hangum.tadpold.commons.libs.core.browser.TadpoleBrowserFunction;

/**
 * editor browser function 정의 
 * 
 * @author hangum
 *
 */
public interface IEditorFunction extends TadpoleBrowserFunction {
	
	public static final int DIRTY_CHANGED 			= 1;
//	public static final int GET_CONTENT_NAME 		= 5;
//	public static final int GET_INITIAL_CONTENT 	= 10;
	
	public static final int SAVE 					= 15;
	public static final int SAVE_S					= 16;
	
//	public static final int STATUS_CHANGED 			= 20;
	
	public static final int EXECUTE_QUERY 			= 25;
	public static final int EXECUTE_ALL_QUERY 		= 26;
	public static final int EXECUTE_PLAN 			= 30;
	public static final int EXECUTE_FORMAT 			= 35;
	
//	public static final int APPEND_QUERY_TEXT 		= 40;
//	public static final int RE_NEW_TEXT				= 41; 
//	public static final int APPEND_QUERY_TEXT_AT_POSITION = 42;
//	public static final int SQL_TO_APPLICATION 		= 45;
//	
//	public static final int DOWNLOAD_SQL	 		= 50;
	
	/** query history page로 이동합니다 */
	public static final int MOVE_HISTORY_PAGE		= 55;
	
	// help popup
	public static final int HELP_POPUP				= 60;
	
	public static final int SET_FOCUS 				= 999;
    
    public static final String EDITOR_SERVICE_MAP = "editorService";
    public static final String EDITOR_SERVICE_HANDLER = "AceEditorBrowserHandler";
    
//    /** 자바 스크립트 초기화 */
//    public static final String JAVA_SCRIPT_INIT_EMBEDDED_EDITOR =  "initEmbeddedEditor()";
//    
//    /** init container */
//    public static final String JAVA_SCRIPT_GET_INITCONTAINER = EDITOR_SERVICE_MAP + ".getInitialContent()";
//    
//    /** 저장 */
//    public static final String JAVA_SCRIPT_SAVE_FUNCTION = "return " + EDITOR_SERVICE_MAP + ".save()";
    
    /** 쿼리 - 2014.3.1 (hangum) */
    public static final String SELECTED_TEXT = "return " + EDITOR_SERVICE_MAP + ".getSelectedText('%s')";
    
    /** 쿼리 창의 모든 쿼리 - 2014.3.1 (hangum) */
    public static final String ALL_TEXT = "return " + EDITOR_SERVICE_MAP + ".getAllText()";
    
//    /** 플랜 */
//    public static final String JAVA_SCRIPT_EXECUTE_PLAN_FUNCTION = EDITOR_SERVICE_MAP + ".executePlan()";
//    
//    /** 쿼리 포멧 */
//    public static final String JAVA_SCRIPT_EXECUTE_FORMAT_FUNCTION = "return " + EDITOR_SERVICE_MAP + ".executeFormat()";
    
    /** 쿼리 추가 - 2014.3.1(hangum) */
    public static final String APPEND_TEXT = EDITOR_SERVICE_MAP + ".addText('%s')";
    
    /** 현재 커서 포인트에 쿼리 추가 - 2014.3.1(hangum) */
    public static final String INSERT_TEXT = EDITOR_SERVICE_MAP + ".insertText('%s')";
    
    /** 에디터에 기존 텍스트를 없애고 새롭게 데이터를 채웁니다 */
    public static final String RE_NEW_TEXT = EDITOR_SERVICE_MAP + ".reNewText('%s')";
    
//    /** SQL TO APPLICATION STRING */
//    public static final String SQL_TO_APPLICATION = EDITOR_SERVICE_MAP + ".sqlToApplication()";
//    
//    /** DOWNLOAD SQL */
//    public static final String DOWNLOAD_SQL = EDITOR_SERVICE_MAP + ".downloadSQL()";
    
    /** TEXT FOCUS */
    public static final String SET_FOCUS_FUNCTION = EDITOR_SERVICE_MAP + ".setTextFocus()";
    
}
