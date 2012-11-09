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
package com.hangum.tadpole.rdb.core.editors.main;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.define.Define;

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
	 * 1. 키워드(Define.SQL_DILIMITER(;)) 또는 라인피드가 하나만 있거나 하나도 없을 경우는 =>  모든 텍스트를 쿼리본다.
	 * 2. 현재 커서의 포인트와 쿼리 불럭의 포인트를 비교합니다. 
	 *   
	 * @param query
	 * @param cursorPoint
	 * @return
	 */
	public static String executeQuery(String query, int cursorPoint) throws Exception {
		if( query.split(Define.SQL_DILIMITER).length == 1 || query.indexOf(Define.SQL_DILIMITER) == -1) {
			return StringUtils.trimToEmpty(query);
		}

		String[] querys = StringUtils.split(query, Define.SQL_DILIMITER);	
		if(logger.isDebugEnabled()) {
			logger.debug("=====[query]" + query + " =====[mouse point]" + cursorPoint);
		}

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
				return querys[i];
			}
		}
		
		return querys[querys.length-1];
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
	
	public static void main(String[] args) {
		String query = " SELECT store_id, manager_staff_id, address_id, last_update "+
" FROM store;"+
" "+
""+
""+
" SELECT staff_id, first_name, last_name, address_id, picture, email, store_id, active, username, password, last_update"+ 
" FROM staff;"+
""+
" SELECT city_id, city, country_id, last_update"+ 
" FROM city;"; 
 
		try {
			String exeQuery = SQLTextUtil.executeQuery(query, 30);
			System.out.println(exeQuery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
}
