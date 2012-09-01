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
package com.hangum.db.browser.rap.core.editors.main;

import org.apache.commons.lang.StringUtils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "This is a string.\nThis is a long string.\r";
        String str1 = str.replaceAll("(\r\n|\n|\r)", "<br />");
        System.out.println("["+str1+"]");
        
        
        String str2 = StringUtils.replace(str, "(\r\n|\n|\r)", "<br />");
        System.out.println("["+str2+"]");
	}

}
