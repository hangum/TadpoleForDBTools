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
import org.apache.log4j.Logger;

import com.hangum.db.define.Define;

/**
 * sql text util
 * 
 * @author hangum
 *
 */
public class SQLTextUtil {
	private static final Logger logger = Logger.getLogger(SQLTextUtil.class);
	
	/**
	 * 경우의 수
	 * 
	 * 1.  ;가 하나만 있거나 하나도 없을 경우는 =>  모든 텍스트를 쿼리본다.
	 * 2.  현재 커서의 포인트와 쿼리 불럭의 포인트를 비교합니다. 
	 *   
	 * 
	 * @param query
	 * @param cursorPoint
	 * @return
	 */
	public static String executeQuery(String query, int cursorPoint) throws Exception {
		if( query.split(Define.SQL_DILIMITER).length == 1 || query.indexOf(Define.SQL_DILIMITER) == -1) return StringUtils.trimToEmpty(query);
		int queryPosition = -1;
		String[] querys = StringUtils.split(query, Define.SQL_DILIMITER);
	
		if(logger.isDebugEnabled()) logger.debug("=====[query]" + query);
		if(logger.isDebugEnabled()) logger.debug("=====[mouse point]" + cursorPoint);

		int queryBeforeCount = 0;
		for(int i=0; i<querys.length; i++) {
			int firstSearch = -1;
			if(i == 0) {
				firstSearch = StringUtils.indexOf(query, Define.SQL_DILIMITER) + 2;
			} else {
				// 쿼리 텍스트 + 2(;) + 이전 쿼리의 전체 수 
				firstSearch = querys[i].length() + 2;
			}
			
			queryBeforeCount += firstSearch;

			if(cursorPoint <= queryBeforeCount) {
				queryPosition = i;
				break;
			}
		}
		
		if(logger.isDebugEnabled()) logger.debug("[selection pointsion]" + queryPosition);
		
		if(queryPosition == -1) {
			if(logger.isDebugEnabled()) logger.debug("[select stmt]" + querys[querys.length-1]);
			return querys[querys.length-1];
		} else {
			if(logger.isDebugEnabled()) logger.debug("[select stmt]" + querys[queryPosition]);
			return querys[queryPosition];
		}
		
	}
	
	/**
	 * 쿼리에 불필요한 라인피드가 들어있으면 삭제 합니다.
	 * 
	 * @param query
	 * @return
	 */
	public static String delLineChar(String query) {
		return query.replaceAll("(\r\n|\n|\r)", "");
	}
}
