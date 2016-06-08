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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.editors.main.utils.SQLTextUtil;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * sql to java
 * 
 * reference http://www.mybatis.org/mybatis-3/sqlmap-xml.html
 * 
 * @author hangum
 *
 */
public class SQLToMyBatisConvert extends AbstractSQLTo {
	private static final Logger logger = Logger.getLogger(SQLToMyBatisConvert.class);
	public static final String DEFAULT_VARIABLE = "select";

	/**
	 * sql to string
	 * 
	 * @param name
	 * @param sql
	 * @return
	 */
	public static String sqlToString(UserDBDAO userDB, String name, String sql) {
		
		StringBuffer sbSQL = new StringBuffer();
		
		for (String strSQL : sql.split(PublicTadpoleDefine.SQL_DELIMITER)) {
			strSQL = SQLUtil.makeExecutableSQL(userDB, strSQL);
			try {
				Statement statement = CCJSqlParserUtil.parse(sql);
				if(statement instanceof Select) {
					sbSQL.append(getSelect(name, strSQL));		
				} else if(statement instanceof Insert) {
					sbSQL.append(getInsert(name, strSQL));
				} else if(statement instanceof Update) {
					sbSQL.append(getUpdate(name, strSQL));
				} else if(statement instanceof Delete) {
					sbSQL.append(getDelete(name, strSQL));
				}
			} catch (Throwable e) {
				logger.error(String.format("sql parse exception. [ %s ]", sql));
			}
		}

		return sbSQL.toString();
	}
	
	/**
	 * get insert
	 * 
	 * @param name
	 * @param sql
	 * @return
	 */
	private static String getInsert(String name, String sql) {
		StringBuffer sbSQL = new StringBuffer();
		
		sbSQL.append("<!-- \n");
		sbSQL.append(" Insert Statement \n");
		sbSQL.append("-->\n");
		sbSQL.append("<INSERT id=\"" + name + "\" parameterType=\"hashmap\" flushCache=\"true\" statementType=\"PREPARED\" keyProperty=\"\" keyColumn=\"\" useGeneratedKeys=\"\" timeout=\"20\" >" + PublicTadpoleDefine.LINE_SEPARATOR);
		sbSQL.append(makeSomeSQL(sql));
		sbSQL.append("</INSERT>" + PublicTadpoleDefine.LINE_SEPARATOR);
		
		return sbSQL.toString();
	}
	
	/**
	 * get update
	 * 
	 * @param name
	 * @param sql
	 * @return
	 */
	private static String getUpdate(String name, String sql) {
		StringBuffer sbSQL = new StringBuffer();
		
		sbSQL.append("<!-- \n");
		sbSQL.append(" Update statement \n");
		sbSQL.append("-->\n");
		sbSQL.append("<UPDATE id=\"" + name + "\" parameterType=\"hashmap\" flushCache=\"true\" statementType=\"PREPARED\" timeout=\"20\" >" + PublicTadpoleDefine.LINE_SEPARATOR);
		sbSQL.append(makeSomeSQL(sql));
		sbSQL.append("</UPDATE>" + PublicTadpoleDefine.LINE_SEPARATOR);
		
		return sbSQL.toString();
	}
	
	/**
	 * get delete
	 * 
	 * @param name
	 * @param sql
	 * @return
	 */
	private static String getDelete(String name, String sql) {
		StringBuffer sbSQL = new StringBuffer();
		
		sbSQL.append("<!-- \n");
		sbSQL.append(" DELETE Statement \n");
		sbSQL.append("-->\n");
		sbSQL.append("<DELETE id=\"" + name + "\" parameterType=\"hashmap\" flushCache=\"true\" statementType=\"PREPARED\" timeout=\"20\" >" + PublicTadpoleDefine.LINE_SEPARATOR);
		sbSQL.append(makeSomeSQL(sql));
		sbSQL.append("</DELETE>" + PublicTadpoleDefine.LINE_SEPARATOR);
		
		return sbSQL.toString();
	}
	
	/**
	 * get select
	 * 
	 * @param name
	 * @param sql
	 * @return
	 */
	private static String getSelect(String name, String sql) {
		StringBuffer sbSQL = new StringBuffer();
		
		sbSQL.append("<!-- \n");
		sbSQL.append(" SELECT statement \n");
//		sbSQL.append("ref : http://www.mybatis.org/mybatis-3/sqlmap-xml.html \n");
//		sbSQL.append("<SELECT\n");
//		sbSQL.append("  id=\"selectPerson\"\n");
//		sbSQL.append("  parameterType=\"int\"\n");
//		sbSQL.append("  parameterMap=\"deprecated\"\n");
//		sbSQL.append("  resultType=\"hashmap\"\n");
//		sbSQL.append("  resultMap=\"personResultMap\"\n");
//		sbSQL.append("  flushCache=\"false\"\n");
//		sbSQL.append("  useCache=\"true\"\n");
//		sbSQL.append("  timeout=\"10000\"\n");
//		sbSQL.append("  fetchSize=\"256\"\n");
//		sbSQL.append("  statementType=\"PREPARED\"\n");
//		sbSQL.append("  resultSetType=\"FORWARD_ONLY\"> \n");
		sbSQL.append("-->\n");
		sbSQL.append("<SELECT id=\"" + name + "\" parameterType=\"hashmap\" resultType=\"hashmap\" >" + PublicTadpoleDefine.LINE_SEPARATOR);
		sbSQL.append(makeSomeSQL(sql));
		sbSQL.append("</SELECT>" + PublicTadpoleDefine.LINE_SEPARATOR);
		
		return sbSQL.toString();
	}
	
	/**
	 * make some sql
	 * @param sql
	 * @return
	 */
	private static String makeSomeSQL(String sql) {
		StringBuffer sbSQL = new StringBuffer();
		StringBuffer strLine;
		StringBuffer strConst;
		
		sql = StringUtils.remove(sql, ";");		
		String[] splists = StringUtils.split(sql, PublicTadpoleDefine.LINE_SEPARATOR);
		int idx = 1;
		for (String part : splists) {
			
			if(!"".equals( StringUtils.trimToEmpty(part) ) )  {

				sbSQL.append("\t");
				part = SQLTextUtil.delLineChar(part);				
				strLine = new StringBuffer();
				strConst = new StringBuffer();
				boolean isOpen = false;
				for (int p=0; p<part.length(); p++) {
					 
					if (!isOpen && '\'' == part.charAt(p) ) {
						 strLine.append("#{");
						 strConst.append("/* '");
						 isOpen = true;
					 }else if (isOpen && '\'' == part.charAt(p) ) {
						 strLine.append("param_"+ idx++ +"}");
						 strConst.append("' */");
						 isOpen = false;
					 }else if (!isOpen){
						 strLine.append(part.charAt(p));
					 }else if (isOpen){
						 strConst.append(part.charAt(p));
					 }
				}
				sbSQL.append(strLine.toString() + strConst.toString());
				sbSQL.append(PublicTadpoleDefine.LINE_SEPARATOR);
			}//if Empty
		}//for
		
		return sbSQL.toString();
	}

}
