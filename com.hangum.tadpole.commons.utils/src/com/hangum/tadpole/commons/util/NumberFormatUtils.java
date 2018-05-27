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

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * number format util
 * 
 * @author hangum
 *
 */
public class NumberFormatUtils {
	private static final Logger logger = Logger.getLogger(NumberFormatUtils.class);
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
			return addComma(new Double(value));
		} catch(NumberFormatException nfe) {
			return value;
		}
	}
	
	/**
	 * ,로만 찍도로.
	 * @param value
	 * @return
	 */
	public static String addComma(double value) {
		DecimalFormat df = new DecimalFormat("#,###.##");    
		String tmpVal = df.format(value).toString();
		
		if(-1 == StringUtils.indexOf(tmpVal, ".00")) {
			return tmpVal;
		} else {
			return StringUtils.replaceOnce(tmpVal, ".00", "");
		}				
	}
	
	/**
	 * 숫자일 경우 ,를 찍어보여줍니다.
	 * 
	 * @param value
	 * @return
	 */
	public static String addCommaLong(Object value) {
		if(value==null) return null;
		
		try{
			DecimalFormat nf = new DecimalFormat("###,###.##########################");
			nf.setDecimalSeparatorAlwaysShown(false);
			
			BigDecimal biNum = new BigDecimal(value.toString());
			return nf.format(biNum);
		} catch(Exception e){
			// nothing 
			if(logger.isDebugEnabled()) logger.error(e);
		}

		return value.toString();
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
			
			return addComma(val) + " bytes";			
		// kb
		} else if(value < 1024*1024) {
			double val = value / (1024);
			return addComma(val) + " KB";			
		// mb
		} else {
			double val = value / (1024 * 1024);
			return addComma(val) + " MB";
		}
	}

}
