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

/**
 * editor browser function 정의 
 * 
 * @author hangum
 *
 */
public interface IEditorFunction  {
	
	public static final int DIRTY_CHANGED 			= 1;
	public static final int CONTENT_ASSIST			= 5;
	
	public static final int SAVE 					= 15;
	public static final int AUTO_SAVE				= 16;
	
	public static final int EXECUTE_QUERY 			= 25;
	public static final int EXECUTE_PLAN 			= 30;
	public static final int EXECUTE_FORMAT 			= 35;
	
	public static final int F4_DML_OPEN				= 40;
	public static final int GENERATE_SELECT			= 45;
	
	/** query history page로 이동합니다 */
	public static final int MOVE_HISTORY_PAGE		= 55;
	
	// help popup
	public static final int HELP_POPUP				= 60;
    
    public static final String EDITOR_SERVICE_MAP = "editorService";
    public static final String EDITOR_SERVICE_HANDLER = "AceEditorBrowserHandler";
    
    /**
     * RDB 에디터를 초기화 합니다. 
     * 
     * @param 확장자
     * @param editor type
     * @param 추가하려는 키워드
     * @param 초기텍스트
     */
    public static final String RDB_INITIALIZE = EDITOR_SERVICE_MAP + ".RDBinitEditor('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";
    
    /**
     * MONGODB 에디터를 초기화 합니다. 
     * 
     * @param 확장자
     * @param editor type
     * @param 추가하려는 키워드
     * @param 초기텍스트
     */
    public static final String MONGO_INITIALIZE = EDITOR_SERVICE_MAP + ".MONGODBinitEditor('%s', '%s', '%s', '%s', '%s', '%s', '%s');";
    
    /**
     * Change editor style
     */
    public static final String CHANGE_EDITOR_STYLE = EDITOR_SERVICE_MAP + ".changeEditorStyle('%s', '%s', '%s', '%s', '%s');";
    
    /** define theme */
    public static final String SET_THEME = EDITOR_SERVICE_MAP + ".setTheme('%s');";

	/** define fontsize */
    public static final String SET_FONT_SIZE = EDITOR_SERVICE_MAP + ".setFontSize('%s');";
	
	/** define wrap */
    public static final String SET_IS_WARP = EDITOR_SERVICE_MAP + ".setWrap('%s', '%s');";
	
    /** 쿼리 - 2014.3.1 (hangum) */
    public static final String GET_SELECTED_TEXT = "return " + EDITOR_SERVICE_MAP + ".getSelectedText('%s');";
    
    /** set selected text */
    public static final String SET_SELECTED_TEXT = EDITOR_SERVICE_MAP + ".setSelectedText();";
    
    /** block text */
    public static final String IS_BLOCK_TEXT = "return " + EDITOR_SERVICE_MAP + ".isBlockText();";
    
    /** 쿼리 창의 모든 쿼리 - 2014.3.1 (hangum) */
    public static final String ALL_TEXT = "return " + EDITOR_SERVICE_MAP + ".getAllText();";
    
    /** 쿼리 추가 - 2014.3.1(hangum) */
    public static final String APPEND_TEXT = EDITOR_SERVICE_MAP + ".addText('%s');";
    
    /** 현재 커서 포인트에 쿼리 추가 - 2014.3.1(hangum) */
    public static final String INSERT_TEXT = EDITOR_SERVICE_MAP + ".insertText('%s');";
    
    /** 에디터에 기존 텍스트를 없애고 새롭게 데이터를 채웁니다 */
    public static final String RE_NEW_TEXT = EDITOR_SERVICE_MAP + ".reNewText('%s');";
    
    /** TEXT FOCUS */
    public static final String SET_FOCUS = EDITOR_SERVICE_MAP + ".setFocus();";
    
    /**
     * 데이터 저장.
     */
    public static final String SAVE_DATA = EDITOR_SERVICE_MAP + ".saveData();";
//    /** 자바 호출이 끝났음을 자바 스크립트에게 알려줍니다 */
//    public static final String EXECUTE_DONE = EDITOR_SERVICE_MAP + ".executeFlag();";
    
}
