/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.browserfunction;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.rdb.core.dialog.editor.RDBShortcutHelpDialog;
import com.hangum.tadpole.rdb.core.dialog.export.SQLToStringDialog;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.util.browserFunction.IEditorBrowserFunction;
import com.hangum.tadpole.sql.parser.format.SQLFormater;

/**
 * query editor browser function
 * 
 * @author hangum
 *
 */
public class EditorBrowserFunctionService extends BrowserFunction implements IEditorBrowserFunction {
	private static final Logger logger = Logger.getLogger(EditorBrowserFunctionService.class);
        
	private MainEditor editor;

	public EditorBrowserFunctionService(Browser browser, String name, MainEditor editor) {
		super(browser, name);
		this.editor = editor;
	}
	
	@Override
	public Object function(Object[] arguments) {
		
		int action = Integer.parseInt(arguments[0].toString());
		
		switch (action) {
			case DIRTY_CHANGED:
				return doDirtyChanged(arguments);
				
//			case GET_CONTENT_NAME:
//				return doGetContentName(arguments);

			case GET_INITIAL_CONTENT:
				return doGetInitialContent(arguments);

			case SAVE:
				return doSave(arguments);
				
			case SAVE_S:
				return doSaveS(arguments);
				
//			case STATUS_CHANGED:
//				return doStatusChanged(arguments);
			
			case EXECUTE_QUERY:
				doExecuteQuery(arguments);
				break;
				
			case EXECUTE_ALL_QUERY:
				doExecuteAllQuery(arguments);
				break;
				
			case EXECUTE_PLAN:
				doExecutePlan(arguments);
				break;
				
			case EXECUTE_FORMAT:
				return doExecuteFormat(arguments);
				
			case APPEND_QUERY_TEXT:
				return appendQueryText(arguments);
				
			case SQL_TO_APPLICATION:
				sqlToApplication(arguments);
				break;
				
			case DOWNLOAD_SQL:
				downloadSQL(arguments);
				break;
				
			case MOVE_HISTORY_PAGE:
				moveHistoryPage();
				break;
				
			case HELP_POPUP:
				helpPopup();
				break;
				
			default:
				return null;
		}
		
		return null;
	}
	
	private Object doGetInitialContent(Object[] arguments) {
		String extension = "";
		
		if( DBDefine.getDBDefine( editor.getUserDB().getTypes() ) == DBDefine.MYSQL_DEFAULT) {
			extension = editor.getEditorInput().getName() + ".mysql";
		} else if( DBDefine.getDBDefine( editor.getUserDB().getTypes() ) == DBDefine.ORACLE_DEFAULT) {
			extension = editor.getEditorInput().getName() + ".oracle";
		} else if( DBDefine.getDBDefine( editor.getUserDB().getTypes() ) == DBDefine.MSSQL_DEFAULT) {
			extension = editor.getEditorInput().getName() + ".mssql";
		} else if( DBDefine.getDBDefine( editor.getUserDB().getTypes() ) == DBDefine.SQLite_DEFAULT) {
			extension = editor.getEditorInput().getName() + ".sqlite";
		} else if( DBDefine.getDBDefine( editor.getUserDB().getTypes() ) == DBDefine.CUBRID_DEFAULT) {
			extension = editor.getEditorInput().getName() + ".mysql";
		} else {
			extension = editor.getEditorInput().getName() + ".postgresql";
		}
		
		return extension + ":ext:" + editor.getOrionText();
	}
	
//	private Object doGetContentName(Object[] arguments) {
//		if( DBDefine.getDBDefine( editor.getUserDB().getType() ) == DBDefine.MYSQL_DEFAULT) {
//			return editor.getEditorInput().getName() + ".mysql";
//		} else if( DBDefine.getDBDefine( editor.getUserDB().getType() ) == DBDefine.ORACLE_DEFAULT) {
//			return editor.getEditorInput().getName() + ".oracle";
//		} else if( DBDefine.getDBDefine( editor.getUserDB().getType() ) == DBDefine.MSSQL_DEFAULT) {
//			return editor.getEditorInput().getName() + ".mssql";
//		} else {
//			return editor.getEditorInput().getName() + ".sqlite";
//		}
//	}
//
//	private boolean doStatusChanged(Object[] arguments) {
//		if (arguments.length != 2 || !(arguments[1] instanceof String)) {
//			return false;
//		}
//		
//		String[] position = parsePosition((String) arguments[1]);
//		editor.setPositionStatus(position[0] + CARET_QUERY_DELIMIT + position[1]);
//		return true;
//	}
//	
//	private String[] parsePosition(String message) {
//        int start = message.indexOf("Line ") + "Line ".length();
//        int end = message.indexOf(' ', start);
//        String line = message.substring(start, end);
//        start = message.indexOf("Col ") + "Col ".length();
//        end = message.indexOf(' ', start);
//        if(end == -1)
//            end = message.length();
//        String col = message.substring(start, end);
//        return (new String[] {
//            line, col
//        });
//    }

	private Object doSave(Object[] arguments) {
		boolean result = false;
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			result = editor.performSave(newContents);
		}
		
		return result;
	}
	
	private Object doSaveS(Object[] arguments) {
		boolean result = false;
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			result = editor.performSaveS(newContents);
		}
		
		return result;
	}

	private Object doDirtyChanged(Object[] arguments) {
		if (arguments.length == 2 && (arguments[1] instanceof Boolean)) {
			editor.setDirty((Boolean) arguments[1]);
		}
		
		return editor.isDirty();
	}
	
	private void doExecuteQuery(Object[] arguments) {
		
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.setOrionText(queryStruct[1]);
			editor.setOrionEditorCursorPostion(new Integer(queryStruct[0]));
			
			editor.executeCommand(Define.QUERY_MODE.DEFAULT);
		}
	}
	
	private void doExecuteAllQuery(Object[] arguments) {
		
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.setOrionText(queryStruct[1]);
			editor.setOrionEditorCursorPostion(MainEditor.ALL_QUERY_EXECUTE);
			
			editor.executeCommand(Define.QUERY_MODE.DEFAULT);
		}
	}
	
	private void doExecutePlan(Object[] arguments) {
		
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.setOrionText(queryStruct[1]);
			editor.setOrionEditorCursorPostion(new Integer(queryStruct[0]));
			
			editor.executeCommand(Define.QUERY_MODE.EXPLAIN_PLAN);
		}
	}
	
	private String doExecuteFormat(Object[] arguments) {
		String newContents = (String) arguments[1];
		
		try {
			newContents = SQLFormater.format(newContents );
			
			return newContents;						
		} catch (Exception e) {
			logger.error("sql format", e);
		}
		
		return newContents;
	}
	
	private String appendQueryText(Object[] arguments) {
		return editor.getAppendQueryText();
	}
	
	private void sqlToApplication(Object[] arguments) {
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			// dialog open
			SQLToStringDialog dialog = new SQLToStringDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Define.SQL_TO_APPLICATION.Java_StringBuffer.toString(), queryStruct[1]);
			dialog.open();
			
		}
	}

	/**
	 * download sql
	 * @param arguments
	 */
	private void downloadSQL(Object[] arguments) {
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.downloadExtFile(editor.getUserDB().getDisplay_name()+".sql", queryStruct[1]);
		}
	}
	
	/**
	 * 쿼리 히스토리 페이지로 이동합니다.
	 */
	private void moveHistoryPage() {
		editor.selectHistoryPage();
	}
	
	/**
	 * help popup
	 */
	private void helpPopup() {
		RDBShortcutHelpDialog dialog = new RDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
		dialog.open();
	}
}
