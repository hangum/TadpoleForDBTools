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
package com.hangum.tadpole.ace.editor.core.widgets;

import org.apache.log4j.Logger;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

/**
 * editor browser function
 * 
 * @author hangum
 *
 */
public class TadpoleEditorFunctionService extends BrowserFunction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleEditorFunctionService.class);
	private TadpoleEditorWidget editor;

	public TadpoleEditorFunctionService(Browser browser, String name, TadpoleEditorWidget editor) {
		super(browser, name);
		this.editor = editor;
	}

	@Override
	public Object function(Object[] arguments) {
		int action = Integer.parseInt(arguments[0].toString());
		
		return null;
	}
		
}
