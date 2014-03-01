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
package com.hangum.tadpole.ace.editor.core.define;

/**
 * Editor Define class
 * 
 * @author hangum
 *
 */
public class EditorDefine {
	/**
	 * 에디터에서 사용할 확장자로 sql을 지정. 
	 */
	public static String EXT_SQL = "ace/mode/sql";
	
	/**
	 * 에디터에서 사용할 확장자로 json을 지정.
	 */
	public static String EXT_JSON = "ace/mode/json";
	
	/** query editor에서 실행 모드 */
	public static enum QUERY_MODE {QUERY, EXPLAIN_PLAN};
	
    /** 에디터의 커서 포인트 */
    public enum EXECUTE_TYPE {ALL, BLOCK, NONE};

	/** sql을 각 언어로 컨버팅 해줍니다 */
	public static enum SQL_TO_APPLICATION {Java_StringBuffer, PHP};

	/** tab index name */
	public enum RESULT_TAB_NAME {RESULT_SET, SQL_RECALL, TADPOLE_MESSAGE};

}
