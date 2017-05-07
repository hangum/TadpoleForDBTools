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
package com.hangum.tadpole.sql.parse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.dao.SQLStatementStruct;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.parser.UpdateDeleteParser;
import com.hangum.tadpole.engine.sql.parser.dto.QueryDMLInfoDTO;

/**
 * update delete statment parse
 * 
 * @author hangum
 *
 */
public class UpdateDeleteStatementParser {
	private static final Logger logger = Logger.getLogger(UpdateDeleteStatementParser.class);
	
	/**
	 * 
	 * 
	 * @param userDB
	 * @param strSQL
	 * @return
	 * @throws Exception
	 */
	public static SQLStatementStruct getParse(UserDBDAO userDB, String strSQL) throws Exception {
		
		QueryDMLInfoDTO dmlInfoDto = new QueryDMLInfoDTO();
		UpdateDeleteParser parser = new UpdateDeleteParser();
		parser.parseQuery(strSQL, dmlInfoDto);
		
		String strObjecName = dmlInfoDto.getObjectName();
		String strWhereAfter = StringUtils.substringAfterLast(strSQL, "where");
		if("".equals(strWhereAfter)) {
			strWhereAfter = StringUtils.substringAfterLast(strSQL, "WHERE");
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("=============================================================================");
			logger.debug("object name : " + strObjecName);
			logger.debug("where after query: " + strWhereAfter);
			logger.debug("=============================================================================");
		}
		
		if("".equals(strObjecName)) throw new Exception("Table not found");
		
		SQLStatementStruct sqlStatementStruce = new SQLStatementStruct();
		sqlStatementStruce.setObjectName(strObjecName);
		sqlStatementStruce.setWhere(strWhereAfter);
		
		return sqlStatementStruce;
	}
}
