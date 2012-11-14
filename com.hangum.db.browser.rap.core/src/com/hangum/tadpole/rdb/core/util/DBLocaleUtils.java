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
	
	/**
	 * oracle list
	 * 
	 * @return
	 */
	public static List<String> getOracleList() {
		List<String> listLocale = new ArrayList<String>();
		
		listLocale.add("");
		
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
		
		listLocale.add("armscii8 | ARMSCII-8 Armenian");
		listLocale.add("ascii      | US ASCII");
		listLocale.add("big5      | Big5 Traditional Chinese");
		listLocale.add("binary    | Binary pseudo charset");
		listLocale.add("cp850    | DOS West European");
		listLocale.add("cp852    | DOS Central European");
		listLocale.add("cp866    | DOS Russian");
		listLocale.add("cp932    | SJIS for Windows Japanese");
		listLocale.add("cp1250   | Windows Central European");
		listLocale.add("cp1251   | Windows Cyrillic");
		listLocale.add("cp1256   | Windows Arabic");
		listLocale.add("cp1257   | Windows Baltic");
		listLocale.add("dec8      | DEC West European");
		listLocale.add("eucjpms  | UJIS for Windows Japanese");
		listLocale.add("euckr     | EUC-KR Korean");
		listLocale.add("gb2312   | GB2312 Simplified Chinese");
		listLocale.add("gbk       | GBK Simplified Chinese");
		listLocale.add("geostd8  | GEOSTD8 Georgian");
		listLocale.add("greek     | ISO 8859-7 Greek");
		listLocale.add("hebrew   | ISO 8859-8 Hebrew");
		listLocale.add("hp8       | HP West European");
		listLocale.add("keybcs2  | DOS Kamenicky Czech-Slovak");
		listLocale.add("koi8r      | KOI8-R Relcom Russian");
		listLocale.add("koi8u     | KOI8-U Ukrainian");
		listLocale.add("latin1    | cp1252 West European");
		listLocale.add("latin2    | ISO 8859-2 Central European");
		listLocale.add("latin5    | ISO 8859-9 Turkish");
		listLocale.add("latin7    | ISO 8859-13 Baltic");
		listLocale.add("macce   | Mac Central European");
		listLocale.add("macroman | Mac West European");
		listLocale.add("sjis       | Shift-JIS Japanese");
		listLocale.add("swe7    | 7bit Swedish");
		listLocale.add("ucs2     | UCS-2 Unicode");
		listLocale.add("tis620   | TIS620 Thai");
		listLocale.add("ujis       | EUC-JP Japanese");
		listLocale.add("utf8      | UTF-8 Unicode");
		listLocale.add("utf8mb4 | UTF-8 Unicode");
		listLocale.add("utf16     | UTF-16 Unicode");
		listLocale.add("utf32     | UTF-32 Unicode");
		
		return listLocale;
	}
}
