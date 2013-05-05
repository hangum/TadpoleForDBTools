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

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

/**
 * query editor browser function
 * 
 * @author hangum
 *
 */
public class JavaScriptBrowserFunctionService extends BrowserFunction implements IJavaScriptBrowserFunction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JavaScriptBrowserFunctionService.class);
        
	private TadpoleOrionHubEditor editor;

	public JavaScriptBrowserFunctionService(Browser browser, String name, TadpoleOrionHubEditor editor) {
		super(browser, name);
		this.editor = editor;
	}
	
	@Override
	public Object function(Object[] arguments) {
//		super.function(arguments);
//		
//		if (arguments.length == 0 || !(arguments[0] instanceof Double)) {
//			return null;
//		}
		int action = Integer.parseInt(arguments[0].toString());//((Double) arguments[0]).intValue();
//		logger.debug("Java Script Browser function Action type is " + action);
	
		switch (action) {
			case DIRTY_CHANGED:
				return doDirtyChanged(arguments);
				
//			case GET_CONTENT_NAME:
//				return doGetContentName(arguments);

			case GET_INITIAL_CONTENT:
				return doGetInitialContent(arguments);

			case SAVE:
				return doSave(arguments);
			
			case APPEND_QUERY_TEXT:
				return appendQueryText(arguments);
				
			case RE_NEW_TEXT:
				return reNewText(arguments);
				
//			case STATUS_CHANGED:
//				return doStatusChanged(arguments);
//			
//			case DOWNLOAD_SQL:
//				downloadJavaScript(arguments);
//				break;
//				
//			case HELP_POPUP:
//				helpPopup();
//				break;
////				
//			default:
//				return null;
		}
		
		return null;
	}
	
	/**
	 * 기존 텍스트를 삭제하고 새로운 텍스트를 입력합니다.
	 * 
	 * @param arguments
	 * @return
	 */
	private Object reNewText(Object[] arguments) {
		return editor.getInitContent();
	}

	/**
	 * editor에 초기 환경 및 데이터를 설정 합니다.
	 * 
	 * @param arguments
	 * @return
	 */
	private Object doGetInitialContent(Object[] arguments) {
//		if(logger.isDebugEnabled()) logger.debug(editor.getInputFileName() + "[doGetInitialContent]===>]" + editor.getInitContent() + "[end text]");
		return editor.getInputFileName() + ".js" + ":ext:" + editor.getInitContent();
	}
	
	/**
	 * 데이터에 초기 데이터를 저장합니다.
	 * 
	 * @param arguments
	 * @return
	 */
	private Object doSave(Object[] arguments) {
		String newContents = (String) arguments[1];
		return newContents;
	}

	private Object doDirtyChanged(Object[] arguments) {
		return true;
	}
	
	private String appendQueryText(Object[] arguments) {
//		System.out.println("append query text is called##################");		
		return editor.getAppendQueryText();
	}
}
