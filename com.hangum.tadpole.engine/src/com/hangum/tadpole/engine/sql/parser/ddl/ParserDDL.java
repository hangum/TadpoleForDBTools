/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.parser.ddl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.sql.parser.define.ParserDefine;
import com.hangum.tadpole.engine.sql.parser.dto.QueryInfoDTO;

/**
 * DDL SQL Parser
 * 
 * @author hangum
 *
 */
public class ParserDDL {
	private static final Logger logger = Logger.getLogger(ParserDDL.class);
	
	/**
	 * ddl parser
	 * 
	 * @param sql
	 * @param queryInfoDTO
	 * @return
	 */
	public QueryInfoDTO parseQuery(String sql, QueryInfoDTO queryInfoDTO) {
		sql = StringUtils.upperCase(StringUtils.trim(sql));
		
		for (DefineDDL ddl : DefineDDL.values()) {
//			if(logger.isDebugEnabled()) logger.debug(ddl.getRegExp());
			
			Matcher matcher = Pattern.compile(ddl.getRegExp(), ParserDefine.PATTERN_FLAG).matcher(sql);
			if(matcher.find()) {
//				if(logger.isDebugEnabled()) logger.debug("=> finding: " + ddl.getRegExp());
				queryInfoDTO.setQueryDDLType(ddl.getDdlType());
				queryInfoDTO.setQueryStatus(ddl.getDdlStatus());
				queryInfoDTO.setObjectName(getObjectName(matcher, sql));
				
				break;
			}
		}

		return queryInfoDTO;
	}
	
	/**
	 * getObjectName
	 * 
	 * @param matcher
	 * @param sql
	 * @return
	 */
	private String getObjectName(Matcher matcher, String sql) {
		int intEndIndex = matcher.end(1);
		int intContentLength = matcher.group(1).length();
		
		if(logger.isDebugEnabled()) {
			logger.debug("===DDL Parser======================================");
			logger.debug("SQL :" + sql);
			logger.debug("object name: " + matcher.group(1));
			logger.debug("intContentLength : " + intContentLength );
			logger.debug("intEndIndex : " + intEndIndex );
			logger.debug("start index: " + (intEndIndex - intContentLength));
			logger.debug("===DDL Parser======================================");
		}
		
		String objctName = StringUtils.substring(sql, (intEndIndex - intContentLength), intEndIndex);
		objctName = StringUtils.remove(objctName, "\"");
		objctName = StringUtils.remove(objctName, "'");
		objctName = StringUtils.remove(objctName, "`");
		
		return objctName;
	}
}
