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
	private BrowserFunctionUtil bfUtils = null;
	
	/**
	 * 
	 * @param parent
	 * @param style
	 */
	public EvaluateWidgets(Composite parent, int style) {
		super(parent, style);
	}
	
	public void initWidget() {
		bfUtils = new BrowserFunctionUtil(browserEditor);
	}
	
	public BrowserFunctionUtil getBfUtils() {
		return bfUtils;
	}
	
	@Override
	protected void checkSubclass() {
	}

}
