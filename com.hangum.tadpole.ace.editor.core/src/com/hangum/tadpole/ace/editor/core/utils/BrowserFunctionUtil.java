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
package com.hangum.tadpole.ace.editor.core.utils;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;

import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;

/**
 * 
 * 
 * @author hangum
 *
 */
public class BrowserFunctionUtil {
	private static final Logger logger = Logger.getLogger(BrowserFunctionUtil.class);
	private Browser browserEditor;
	
	public BrowserFunctionUtil(Browser browserEditor) {
		this.browserEditor = browserEditor;
	}
	/**
	 * orion text setfocus
	 */
	protected void setOrionTextFocus() {
		try {
			browserEditor.evaluate(EditorFunctionService.SET_FOCUS);
		} catch(Exception e) {
			// ignore exception
		}
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
	public void browserEvaluate(String command, String... args) {
		if(logger.isDebugEnabled()) {
			logger.debug("\t ### send command is : [command] " + command + ", [args]" + Arrays.toString(args));
		}
		
		try {
			browserEditor.evaluate(String.format(command, TadpoleEditorUtils.makeGrantArgs(args)));
		} catch(Exception e) {
//			logger.error("browser evaluate [ " + command + " ]\r\n", e); //$NON-NLS-1$ //$NON-NLS-2$
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
	public String browserEvaluateToStr(String command, String... args) {
		if(logger.isDebugEnabled()) {
			logger.debug("\t ### send command is : " + command);
		}
		
		try {
			Object ret = browserEditor.evaluate(String.format(command, TadpoleEditorUtils.makeGrantArgs(args)));
			return ret.toString();
		} catch(Exception e) {
			logger.error("browser evaluate [ " + command + " ]\r\n", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return "";
	}
	

}
