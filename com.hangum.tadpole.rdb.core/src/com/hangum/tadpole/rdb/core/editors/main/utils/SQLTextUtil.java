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
package com.hangum.tadpole.rdb.core.editors.main.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.db.metadata.constants.SQLConstants;

/**
 * sql text util
 * 
 * @author hangum
 *
 */
public class SQLTextUtil {
	private static final Logger logger = Logger.getLogger(SQLTextUtil.class);
	
	/**
	 * 쿼리에 불필요한 라인피드가 들어있으면 삭제 합니다.
	 * 
	 * @param query
	 * @return
	 */
	public static String delLineChar(String query) {
		return query.replaceAll("(\r\n|\n|\r)", "");
	}
	
	/**
	 * 커서 이전 포인트의 텍스트를 얻는다.
	 * 
	 * @param strQuery
	 * @param intPosition
	 * @return
	 */
	public static String[] findPreCursorObjectArry(String strQuery, int intPosition) {
		int startIndex = intPosition - 1;
		int endIndex = intPosition;
		return cusrsotObjectArry(strQuery, intPosition, startIndex, endIndex);
	}
	
	/**
	 * cursor object arry
	 * 
	 * @param strQuery
	 * @param intPosition
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	private static String[] cusrsotObjectArry(String strQuery, int intPosition, int startIndex, int endIndex) {
		String[] arryCursor = {"", ""};
		
		String strPosTxt = StringUtils.trimToEmpty(StringUtils.substring(strQuery, startIndex, endIndex));
//		if(logger.isDebugEnabled()) logger.debug("==> postion char : " + strPosTxt);
		if(StringUtils.isEmpty(strPosTxt)) return arryCursor;
		
		String strBeforeTxt = strQuery.substring(0, startIndex);
		String[] strArryBeforeTxt = StringUtils.split(strBeforeTxt, ' ');

		// 공백 배열로 만들어 제일 처음 백스트를 가져온다.
		String strAfterTxt = strQuery.substring(startIndex);
		String[] strArryAfterTxt = StringUtils.split(strAfterTxt, ' ');
		
		if(strArryBeforeTxt.length == 0) {
			arryCursor[0] = removeSpecialChar(strArryAfterTxt[0]);
			arryCursor[1] = "";
		} else {
			arryCursor[0] = removeSpecialChar(strArryBeforeTxt[strArryBeforeTxt.length-1]);
			arryCursor[1] = removeSpecialChar(strArryAfterTxt[0]);
		}
		return arryCursor;
	}
	
	/**
	 * 커서가 위치한 이전의 키워드를 찾는다.
	 * 
	 * @param strQuery
	 * @param intPosition
	 * @return
	 */
	public static String findPrevKeywork(String strQuery, int intPosition) {
		String strBeforeTxt = strQuery.substring(0, intPosition);
		String[] strArryBeforeTxt = StringUtils.split(strBeforeTxt, ' ');
		
		try {
		for (int i=1; i<=strArryBeforeTxt.length; i++) {
			String tmp = strArryBeforeTxt[strArryBeforeTxt.length-i];
			// 마지막 문자가 ; 라면 제거해준다.
			tmp = removeSpecialChar(tmp);
			
			if(SQLConstants.listTableKeywords.contains(tmp.toUpperCase())) {
				return tmp.toUpperCase();
			} else if(SQLConstants.listColumnKeywords.contains(tmp.toUpperCase())) {
				return tmp.toUpperCase();
			}
		}
		} catch(Exception e) {
			logger.error("preve keyword", e);
		}
				
		return "";
	}
	
	/**
	 * 마지막 문자의 특수문자 제거해준다.
	 * @param strWord
	 * @return
	 */
	public static String removeSpecialChar(String strWord) {
		if(strWord == null) return "";
		
		strWord = strWord.replace(";", "");
		strWord = StringUtils.removeStart(strWord, ",");
		strWord = StringUtils.removeEnd(strWord, ",");
		
		strWord = StringUtils.removeStart(strWord, "(");
		strWord = StringUtils.removeEnd(strWord, ")");
		
		return strWord;
	}
}
