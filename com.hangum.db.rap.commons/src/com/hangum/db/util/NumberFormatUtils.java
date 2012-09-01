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
package com.hangum.db.util;

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

/**
 * number format util
 * 
 * @author hangum
 *
 */
public class NumberFormatUtils {
	/**
	 * ,로만 찍도로.
	 * @param value
	 * @return
	 */
	public static String commaFormat(double value) {
//		String tmpVal = String.format("%.2f", value);
		DecimalFormat df = new DecimalFormat("#,###.##");    
		String tmpVal = df.format(value).toString();
		
		if(-1 == StringUtils.indexOf(tmpVal, ".00")) {
			return tmpVal;
		} else {
			return StringUtils.replaceOnce(tmpVal, ".00", "");
		}				
	}
	
	/**
	 * kb, mb 변환
	 * 
	 * @param value
	 * @return
	 */
	public static String kbMbFormat(double value) {
		// bytes
		if(value < 1024) {
			double val = value;
			
			return commaFormat(val) + " bytes";			
		// kb
		} else if(value < 1024*1024) {
			double val = value / (1024);
			return commaFormat(val) + " KB";			
		// mb
		} else {
			double val = value / (1024 * 1024);
			return commaFormat(val) + " MB";
		}
	}
}
