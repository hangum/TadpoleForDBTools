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

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

/**
 * browser evaluate utils
 * 
 * @author hangum
 *
 */
public class EvaluateWidgets extends Composite {
	private static final Logger logger = Logger.getLogger(EvaluateWidgets.class);
	protected Browser browserEditor = null;
	
	/**
	 * 
	 * @param parent
	 * @param style
	 */
	public EvaluateWidgets(Composite parent, int style) {
		super(parent, style);
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
	public void browserEvaluate(String command, String ... args) {
		if(logger.isDebugEnabled()) {
			logger.debug("\t ### send command is : [command] " + command + ", [args]" + args);
		}
		
		try {
			browserEditor.evaluate(String.format(command, TadpoleEditorUtils.makeGrantArgs(args)));
		} catch(Exception e) {
			logger.error("browser evaluate [ " + command + " ]\r\n", e); //$NON-NLS-1$ //$NON-NLS-2$
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
	public String browserEvaluateToStr(String command, String ... args) {
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
	
	@Override
	protected void checkSubclass() {
	}

}
