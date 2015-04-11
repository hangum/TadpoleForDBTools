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
package com.hangum.tadpole.commons.util;

/**
 * OS마다 다른 단축키를 검사합니다.
 * 
 * @author hangum
 *
 */
public class ShortcutPrefixUtils {

	/**
	 * window기준으로 ctrl키를 시스템 별로 정의합니다.   
	 * 
	 * @return
	 */
	public final static String getCtrlShortcut() {
		ServletUserAgent.OS_SIMPLE_TYPE osSimpleType = RequestInfoUtils.findOSSimpleType();
		String prefixOSShortCut = "Ctrl ";
		if(osSimpleType == ServletUserAgent.OS_SIMPLE_TYPE.MACOSX) prefixOSShortCut = "Command ";
		
		return prefixOSShortCut;
	}
	
	/**
	 * window기준으로 ctrl키를 시스템 별로 정의합니다.   
	 * 
	 * @return
	 */
	public final static String getAltShortcut() {
		ServletUserAgent.OS_SIMPLE_TYPE osSimpleType = RequestInfoUtils.findOSSimpleType();
		String prefixOSShortCut = "Alt ";
		if(osSimpleType == ServletUserAgent.OS_SIMPLE_TYPE.MACOSX) prefixOSShortCut = "Option ";
		
		return prefixOSShortCut;
	}
}
