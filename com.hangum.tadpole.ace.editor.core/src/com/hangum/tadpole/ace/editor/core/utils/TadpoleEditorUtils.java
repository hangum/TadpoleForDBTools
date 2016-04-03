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
package com.hangum.tadpole.ace.editor.core.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

/**
 * Tadpole Editor Utils
 * 
 * @author hangum
 *
 */
public class TadpoleEditorUtils {
	private static final Logger logger = Logger.getLogger(TadpoleEditorUtils.class);
	
	/**
	 * Tadpole Editor grant text
	 * 
	 * @param initContent
	 * @return
	 */
	public static String getGrantText(String initContent) {
		return StringEscapeUtils.escapeJavaScript(initContent);
	}
	
	/**
	 * Argument를 java script가 허용한 argument로 만듭니다.
	 * @param args
	 * @return
	 */
	public static String[] makeGrantArgs(String ... args) {
		String[] grantsARgs = new String[args.length];
		for(int i=0; i<args.length; i++) {
			grantsARgs[i] = getGrantText(args[i]);
		}
		
		return grantsARgs;
	}
}
