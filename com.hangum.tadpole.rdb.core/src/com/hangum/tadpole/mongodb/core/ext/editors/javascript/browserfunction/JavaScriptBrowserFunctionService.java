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
package com.hangum.tadpole.mongodb.core.ext.editors.javascript.browserfunction;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.ace.editor.core.dialogs.help.MongoDBShortcutHelpDialog;
import com.hangum.tadpole.mongodb.core.ext.editors.javascript.ServerSideJavaScriptEditor;

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
		return "mongojavascript.js" + ":ext:" + editor.getInputJavaScriptContent();
	}
	
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
			result = editor.performSave(newContents);
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
		
//		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
//			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.executeEval(newContents);
//		}
	}
	
//	private String doExecuteFormat(Object[] arguments) {
//		String newContents = (String) arguments[1];
//		
//		try {
//			newContents = JSONUtil.getPretty(newContents );			
//			return newContents;						
//		} catch (Exception e) {
//			logger.error("sql format", e);
//		}
//		
//		return newContents;
//	}

	/**
	 * download sql
	 * @param arguments
	 */
	private void downloadJavaScript(Object[] arguments) {
//		if (arguments.length == 2 && (arguments[1] instanceof String)) {
			String newContents = (String) arguments[1];
//			String[] queryStruct = newContents.split(CARET_QUERY_DELIMIT);
			
			editor.downloadJavaScript(editor.getUserDB().getDisplay_name() + ".js",  newContents);
//		}
	}

	/**
	 * help popup
	 */
	private void helpPopup() {
		MongoDBShortcutHelpDialog dialog = new MongoDBShortcutHelpDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NONE);
		dialog.open();
	}
}
