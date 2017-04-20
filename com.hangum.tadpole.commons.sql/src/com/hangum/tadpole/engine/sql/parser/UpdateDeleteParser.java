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
package com.hangum.tadpole.engine.sql.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DML_TYPE;
import com.hangum.tadpole.engine.sql.parser.define.ParserDefine;
import com.hangum.tadpole.engine.sql.parser.dto.QueryDMLInfoDTO;

/**
 * update delete parser
 * 
 * @author hangum
 *
 */
public class UpdateDeleteParser {
	private static final Logger logger = Logger.getLogger(UpdateDeleteParser.class);
	
	/**
	 * ddl parser
	 * 
	 * @param sql
	 * @param queryInfoDTO
	 * @return
	 */
	public QueryDMLInfoDTO parseQuery(String sql, QueryDMLInfoDTO queryInfoDTO) {
		sql = StringUtils.upperCase(StringUtils.trim(sql));
		
		for (TABLE_PARSER ddl : TABLE_PARSER.values()) {
//			if(logger.isDebugEnabled()) logger.debug(ddl.getRegExp());
			
			Matcher matcher = Pattern.compile(ddl.getRegExp(), ParserDefine.PATTERN_FLAG).matcher(sql);
			if(matcher.find()) {
//				if(logger.isDebugEnabled()) logger.debug("=> finding: " + ddl.getRegExp());
				queryInfoDTO.setDmlType(ddl.getDmlType());
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
		
//		if(logger.isDebugEnabled()) {
//			logger.debug("===DDL Parser======================================");
//			logger.debug("SQL :" + sql);
//			logger.debug("object name: " + matcher.group(1));
//			logger.debug("intContentLength : " + intContentLength );
//			logger.debug("intEndIndex : " + intEndIndex );
//			logger.debug("start index: " + (intEndIndex - intContentLength));
//			logger.debug("===DDL Parser======================================");
//		}
		
		String objctName = StringUtils.substring(sql, (intEndIndex - intContentLength), intEndIndex);
		objctName = StringUtils.remove(objctName, "\"");
		objctName = StringUtils.remove(objctName, "'");
		objctName = StringUtils.remove(objctName, "`");
		
		return objctName;
	}
}

enum TABLE_PARSER {
	TABLE_UPDATE("UPDATE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DML_TYPE.UPDATE),
	TABLE_DELETE("DELETE\\s+FROM\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DML_TYPE.DELETE)
	;
	
	private String regExp;
	private QUERY_DML_TYPE dmlType;
	private TABLE_PARSER(String regExp, QUERY_DML_TYPE dmlType) {
		this.regExp = regExp;
		this.dmlType = dmlType;
	}
	public String getRegExp() {
		return regExp;
	}
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	public QUERY_DML_TYPE getDmlType() {
		return dmlType;
	}
	public void setDmlType(QUERY_DML_TYPE dmlType) {
		this.dmlType = dmlType;
	}
}