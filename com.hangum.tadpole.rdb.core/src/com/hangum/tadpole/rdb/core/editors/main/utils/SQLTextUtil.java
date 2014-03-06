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

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;

/**
 * sql text util
 * 
 * @author hangum
 *
 */
public class SQLTextUtil {
	private static final Logger logger = Logger.getLogger(SQLTextUtil.class);
	
	/**
	 * sql문의 주석을 제거합니다.
	 * 쿼리 처음에 주석이 존재하면 오류가 나므로 주석을 제거해야합니다.
	 * 
	 * 즉 다음과 같은 쿼리는 정상이다.
	 * <code>
	 	SELECT ArtistId, / * 이름 쿼리 * /
		Name  -- 이름
		FROM Artist;
	 *  </code>
	 *  
	 *  다음과 같은 쿼리는 비 정상이다.
	 *  <code>
	    / * 이름 쿼리 * /
	    -- 쿼리 상단에 주석을 추가하면 오류입니다.
		SELECT ArtistId, / * 이름 쿼리 * /
		Name  -- 이름
		FROM Artist;
	 *  </code>
	 * 
	 * 주석의 종류는 다음과 같습니다. 
	 * 1. -- 로 시작하는 한줄 주석
	 * 2. / * 로 시작하고 * / 로 끝나는 여러줄 주석을 만들 수 있습니다.
	 * 
	 *  주의 할 것은 " " 사이는 무시해야합니다.
	 *  오라클 힌트는 / * + 로 시작하므로 주의해야 합니다. 
	 * 
	 * @param querys
	 * @return
	 */
	public static String removeComment(String sourceSQL) {
		String strReturnSQL = "";
		
		strReturnSQL = StringUtils.removeEnd(sourceSQL, "--");
//		String[] args = StringUtils.substringsBetween(sourceSQL, arg1, arg2);
		
		
		return strReturnSQL;
	}
	
//	/**
//	 * 쿼리의 현재 포지션을 분리자로 나누어 현재 실행되어야 하는 쿼리를 추출하는 메소드. 
//	 * 
//	 * 경우의 수
//	 * 1. 키워드(Define.SQL_DILIMITER(;)) 또는 라인피드가 하나만 있거나 하나도 없을 경우는 =>  모든 텍스트를 쿼리본다.
//	 * 2. 현재 커서의 포인트와 쿼리 불럭의 포인트를 비교합니다. 
//	 *   
//	 * @param query
//	 * @param cursorPosition
//	 * @return
//	 */
//	public static String executeQuery(String query, int cursorPosition) {//throws Exception {
//		if( query.split(PublicTadpoleDefine.SQL_DILIMITER).length == 1 || query.indexOf(PublicTadpoleDefine.SQL_DILIMITER) == -1) {
//			return StringUtils.trimToEmpty(query);
//		}
//
//		String[] querys = StringUtils.split(query, PublicTadpoleDefine.SQL_DILIMITER);	
//
//		int queryBeforeCount = 0;
//		for(int i=0; i<querys.length; i++) {
//			// dilimiter와 그다음 커서를 추가한 +2 입니다.
//			int firstSearch = querys[i].length() + 2;
//			
//			queryBeforeCount += firstSearch;
//			if(cursorPosition <= queryBeforeCount) {
//				if(logger.isDebugEnabled()) logger.debug("[cursorPosition]" + cursorPosition + "[find postion]" + queryBeforeCount + "[execute query]" + StringUtils.trim(querys[i]));
//				return StringUtils.trim(querys[i]);
//			}
//		}
//		
//		if(logger.isDebugEnabled()) logger.debug("[last find execute query]" + StringUtils.trim(querys[querys.length-1]));
//		return StringUtils.trim(querys[querys.length-1]);
//	}
//	
//	/**
//	 * 쿼리를 분리자로 나누지 않고 전체 쿼리를 수행할때 처리하는 메소드
//	 * 
//	 * @param query
//	 * @return
//	 */
//	public static String executeQuery(String query) {
//		return StringUtils.trim(query);
//	}
	
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
