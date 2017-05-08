/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.utils.plan;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.utils.RequestQuery;

/**
 * tibero execute plan
 * 
 * @author hangum
 *
 */
public class TiberoExecutePlanUtils {
	private static final Logger logger = Logger.getLogger(TiberoExecutePlanUtils.class);
	/**
	 * tibero query plan을 실행합니다. 
	 * 
	 * @param userDB
	 * @param reqQuery
	 * @param planTableName
	 * @throws Exception
	 */
	public static String plan(UserDBDAO userDB, RequestQuery reqQuery, java.sql.Connection javaConn) throws Exception {
		String strUUID = Utils.getUniqueID();
		
		String strExeSQL = "-- " + strUUID + PublicTadpoleDefine.LINE_SEPARATOR + reqQuery.getSql();
		
		// 사용자 쿼리를 날린다.
		QueryExecuteResultDTO queryResultDTO = QueryUtils.executeQuery(userDB, strExeSQL, 0, 10000);
		
		// 사용자 쿼리의 실행 아이디를 얻는다.
		// 티베로에서 처음의 주석문자이면 해당 문자를 삭제해야한다.
//		String strSQL = StringUtils.replace(StringUtils.trim(strExeSQL), PublicTadpoleDefine.LINE_SEPARATOR, " ");
		
		String query = PartQueryUtil.makeExplainQuery(userDB, strUUID);
		if(logger.isDebugEnabled()) logger.debug("[plan query] " + strUUID);
	
		QueryExecuteResultDTO planIDResultDTO = QueryUtils.executeQuery(userDB, query, 0, 1000);
		
		List<Map<Integer, Object>> resultData = planIDResultDTO.getDataList().getData();
		if(resultData.isEmpty()) {
			throw new Exception("Does not found sql plan.");
		}
		Map<Integer, Object> mapResult = resultData.get(0);
		
		return ""+mapResult.get(0);
	}
}
