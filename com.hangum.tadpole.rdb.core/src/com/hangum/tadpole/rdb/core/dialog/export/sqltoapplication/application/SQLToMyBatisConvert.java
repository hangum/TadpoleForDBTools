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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.rdb.core.editors.main.utils.SQLTextUtil;

/**
 * sql to java
 * 
 * @author hangum
 *
 */
public class SQLToMyBatisConvert {
	public static final String DEFAULT_VARIABLE = "select";
/*
	
	<select id="selectPerson" parameterType="int" resultType="hashmap">
	  SELECT * FROM PERSON WHERE ID = #{id}
	</select>
*/	
	/**
	 * sql to string
	 * 
	 * @param name
	 * @param sql
	 * @return
	 */
	public static String sqlToString(String name, String sql) {
		StringBuffer sbSQL = new StringBuffer();
		StringBuffer strLine;
		StringBuffer strConst;
		
		sbSQL.append("<!-- \n");
		sbSQL.append("ref : http://www.mybatis.org/mybatis-3/sqlmap-xml.html \n");
		sbSQL.append("<SELECT\n");
		sbSQL.append("  id=\"selectPerson\"\n");
		sbSQL.append("  parameterType=\"int\"\n");
		sbSQL.append("  parameterMap=\"deprecated\"\n");
		sbSQL.append("  resultType=\"hashmap\"\n");
		sbSQL.append("  resultMap=\"personResultMap\"\n");
		sbSQL.append("  flushCache=\"false\"\n");
		sbSQL.append("  useCache=\"true\"\n");
		sbSQL.append("  timeout=\"10000\"\n");
		sbSQL.append("  fetchSize=\"256\"\n");
		sbSQL.append("  statementType=\"PREPARED\"\n");
		sbSQL.append("  resultSetType=\"FORWARD_ONLY\"> \n-->\n");
		sbSQL.append("<SELECT id=\"" + name + "\" parameterType=\"hashmap\" resultType=\"hashmap\" >" + PublicTadpoleDefine.LINE_SEPARATOR);
		
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
					 
					if (!isOpen & '\'' == part.charAt(p) ) {
						 strLine.append("#{");
						 strConst.append("/* '");
						 isOpen = true;
					 }else if (isOpen & '\'' == part.charAt(p) ) {
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
		sbSQL.append("</SELECT>" + PublicTadpoleDefine.LINE_SEPARATOR);
		
		return sbSQL.toString();
	}

}
