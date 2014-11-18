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
package com.hangum.tadpole.rdb.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * db locale list
 * 
 * @author hangum
 *
 */
public class DBLocaleUtils {
	public static final String NONE_TXT = "none";
	
	/**
	 * oracle list
	 * 
	 * @return
	 */
	public static List<String> getOracleList() {
		List<String> listLocale = new ArrayList<String>();
		
		listLocale.add(NONE_TXT);
		
		listLocale.add("ko");
		listLocale.add("ja");
		listLocale.add("zh");
		listLocale.add("de");
		listLocale.add("fr");
		listLocale.add("it");
		listLocale.add("en");
		
		return listLocale;
	}
	
	/**
	 * mysql list
	 * 
	 * http://dev.mysql.com/doc/refman//5.5/en/charset-charsets.html
	 * 
	 * @return
	 */
	public static List<String> getMySQLList() {
		List<String> listLocale = new ArrayList<String>();
		listLocale.add(NONE_TXT);
		listLocale.add("armscii8");
		listLocale.add("ascii");
		listLocale.add("big5");
		listLocale.add("binary");
		listLocale.add("cp850");
		listLocale.add("cp852");
		listLocale.add("cp866");
		listLocale.add("cp932");
		listLocale.add("cp1250");
		listLocale.add("cp1251");
		listLocale.add("cp1256");
		listLocale.add("cp1257");
		listLocale.add("dec8");
		listLocale.add("eucjpms");
		listLocale.add("euckr");
		listLocale.add("gb2312");
		listLocale.add("gbk");
		listLocale.add("geostd8");
		listLocale.add("greek");
		listLocale.add("hebrew");
		listLocale.add("hp8");
		listLocale.add("keybcs2");
		listLocale.add("koi8r");
		listLocale.add("koi8u");
		listLocale.add("latin1");
		listLocale.add("latin2");
		listLocale.add("latin5");
		listLocale.add("latin7");
		listLocale.add("macce");
		listLocale.add("macroman");
		listLocale.add("sjis");
		listLocale.add("swe7");
		listLocale.add("ucs2");
		listLocale.add("tis620");
		listLocale.add("ujis");
		listLocale.add("utf8");
		listLocale.add("utf8mb4");
		listLocale.add("utf16");
		listLocale.add("utf32");
		
		return listLocale;
	}
	
//	/**
//	 * find full combo text
//	 * 
//	 * @param locale
//	 * @return
//	 */
//	public static String findMySQLFullLocale(String locale) {
//		for(String strLocale : getMySQLList()) {
//			if(StringUtils.startsWith(strLocale, locale)) return strLocale;
//		}
//		
//		return "";
//	}
}
