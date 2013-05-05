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
package com.hangum.tadpole.editor.core.widgets.editor;

/**
 * OrionHub Editor Browser function 
 * 
 * @author hangum
 *
 */
public interface IJavaScriptBrowserFunction {
	
	/** 
	 * Delimiter used on OrionHub Editor
	 * 
	 * ex)
	 * 		CursorPosition-tadpole-del-editorText
	 */
	public static final String CARET_QUERY_DELIMIT 	= "-tadpole-del-";
	
	/** editor service map */
    public static final String EDITOR_SERVICE_MAP 	= "editorService";
    
    /** browser editor service name */
    public static final String EDITOR_SERVICE_HANDLER = "editorServiceHandler";
    
	//////////////////////// Called Javascript to java /////////////////////////////////////////
		/** editor change */
		public static final int DIRTY_CHANGED 			= 1;
		
		/** editor initial content */
		public static final int GET_INITIAL_CONTENT 	= 10;
		
		/** save */
		public static final int SAVE 					= 15;
		
		/** status change */
		public static final int STATUS_CHANGED 			= 20;
		
		public static final int APPEND_QUERY_TEXT 		= 40;
		public static final int RE_NEW_TEXT				= 41;
		
//		/** help popup */
//		public static final int HELP_POPUP				= 60;

		/** set editor focus */
		public static final int SET_FOCUS 				= 999;
	//////////////////////// Called JavaScript to java /////////////////////////////////////////
    
	//////////////////////// Called java to JavaScript /////////////////////////////////////////
	    /** default java script initialize */
	    public static final String JAVA_SCRIPT_INIT_EMBEDDED_EDITOR 	= "initEmbeddedEditor()";
	    
	    /** first set content */
	    public static final String JAVA_SCRIPT_GET_INITCONTAINER 		= EDITOR_SERVICE_MAP + ".getInitialContent()";
	    
	    /** save */
	    public static final String JAVA_SCRIPT_SAVE_FUNCTION 			= "return " + EDITOR_SERVICE_MAP + ".save()";
	    
	    /** 쿼리 추가 */
	    public static final String JAVA_SCRIPT_APPEND_QUERY_TEXT = "return " + EDITOR_SERVICE_MAP + ".appendQueryText()";
	    
	    /** 에디터에 기존 텍스트를 없애고 새롭게 데이터를 채웁니다 */
	    public static final String JAVA_SCRIPT_RE_NEW_TEXT = "return " + EDITOR_SERVICE_MAP + ".reNewText()";
	    
	    /** TEXT FOCUS */
	    public static final String JAVA_SCRIPT_SET_FOCUS_FUNCTION = EDITOR_SERVICE_MAP + ".setTextFocus()";
	    
	//////////////////////// Called java to javascript /////////////////////////////////////////
    
}
