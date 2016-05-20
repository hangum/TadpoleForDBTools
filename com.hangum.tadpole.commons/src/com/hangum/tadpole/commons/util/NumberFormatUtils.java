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
package com.hangum.tadpole.commons.util;

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
	 * to currency format
	 * @param value
	 * @return
	 */
	public static String currencyFormat(String value) {
		DecimalFormat currencyFormatter = new DecimalFormat("###,####,##0");
		return currencyFormatter.format(Integer.parseInt(value));
	}
	
	/**
	 * to currency format
	 * @param value
	 * @return
	 */
	public static String currencyFormat(double value) {
		DecimalFormat currencyFormatter = new DecimalFormat("###,####,##0");
		return currencyFormatter.format(value);
	}
	
	/**
	 * to currency format
	 * @param value
	 * @return
	 */
	public static String currencyFormat(Integer value) {
		DecimalFormat currencyFormatter = new DecimalFormat("###,####,##0");
		return currencyFormatter.format(value);
	}
	
	/**
	 * current to int
	 * @param value
	 * @return
	 */
	public static Integer currencyToInt(String value) {
		return Integer.parseInt(StringUtils.remove(value, ","));
	}
	
	/**
	 * 데이터를 표현합니다.
	 * @param value
	 * @return
	 */
	public static String commaFormat(String value) {
		if(null == value) return "";
		
		try {
			return commaFormat(new Double(value));
		} catch(NumberFormatException nfe) {
			return value;
		}
	}
	
	
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
