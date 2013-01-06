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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditor;
import com.hangum.tadpole.rdb.core.dialog.editor.MongoDBShortcutHelpDialog;
import com.hangum.tadpole.util.JSONUtil;

/**
 * query editor browser function
 * 
 * @author hangum
 *
 */
public class JavaScriptBrowserFunctionService extends BrowserFunction implements IJavaScriptBrowserFunction {
	private static final Logger logger = Logger.getLogger(JavaScriptBrowserFunctionService.class);
        
	private ServerSideJavaScriptEditor editor;

	public JavaScriptBrowserFunctionService(Browser browser, String name, ServerSideJavaScriptEditor editor) {
		super(browser, name);
		this.editor = editor;
	}
	
	@Override
	public Object function(Object[] arguments) {
		super.function(arguments);
		
		if (arguments.length == 0 || !(arguments[0] instanceof Double)) {
			return null;
		}
		int action = ((Double) arguments[0]).intValue();
		
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
				
			case DOWNLOAD_SQL:
				downloadJavaScript(arguments);
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
		if("".equals(editor.getInputJavaScriptName())) {
			return "mongojavascript.js"+ ":ext:";
		}
		return editor.getInputJavaScriptName() + ".js" + ":ext:" + editor.getInputJavaScriptContent();
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
//
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
//
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
			
			editor.executeEval(queryStruct[1]);
		}
	}
	
	private String doExecuteFormat(Object[] arguments) {
		String newContents = (String) arguments[1];
		
		try {
			newContents = JSONUtil.getPretty(newContents );			
			return newContents;						
		} catch (Exception e) {
			logger.error("sql format", e);
		}
		
		return newContents;
	}

	/**
	 * download sql
	 * @param arguments
	 */
	private void downloadJavaScript(Object[] arguments) {
		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.downloadJavaScript(editor.getUserDB().getDisplay_name() + ".js",  queryStruct[1]);
		}
	}
//	
//	/**
//	 * 쿼리 히스토리 페이지로 이동합니다.
//	 */
//	private void moveHistoryPage() {
//		editor.selectHistoryPage();
//	}
//	
	/**
	 * help popup
	 */
	private void helpPopup() {
		MongoDBShortcutHelpDialog dialog = new MongoDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
		dialog.open();
	}
}
