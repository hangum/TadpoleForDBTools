/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application;

import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;

/**
 * sql to axisj(https://www.axisj.com/)
 * options : http://jdoc.axisj.com/AXGrid.html#.setConfig
 * 
 * @author hangum
 *
 */
public class SQLToAxisjConvert extends AbstractSQLTo {
	private static final Logger logger = Logger.getLogger(SQLToAxisjConvert.class);
	public static final String DEFAULT_VARIABLE = "AXGrid";
	public static final String PREFIX_TAB = "\n\t\t\t\t\t\t";
	public static final String GROUP_TEMPLATE = PREFIX_TAB + "{key:\"%s\", label:\"%s\", width:\"100\", align:\"%s\"},";
	public static final String GROUP_DATA_TEMPLATE = "%s:%s,";
	
	/**
	 * sql to string
	 * 
	 * @param userDB
	 * @param name
	 * @param sql
	 * @return
	 */
	public static String sqlToString(UserDBDAO userDB, String name, String sql) {
		String retHtml = "";
		try {
			String STR_TEMPLATE = IOUtils.toString(SQLToAxisjConvert.class.getResource("axis.js.template"));

			QueryExecuteResultDTO queryResult = QueryUtils.executeQuery(userDB, sql, 0, 4);
			Map<Integer, String> columnLabel = queryResult.getColumnLabelName();
			Map<Integer, Integer> columnType = queryResult.getColumnType();
			
			String strHead = "";
			StringBuffer sbGroup = new StringBuffer();
			for (int i=0; i<columnLabel.size(); i++) {
				String strColumnLabel = columnLabel.get(i);
				boolean isNumber = RDBTypeToJavaTypeUtils.isNumberType(columnType.get(i));
				sbGroup.append(String.format(GROUP_TEMPLATE, strColumnLabel, strColumnLabel, isNumber?"right":"left"));
			}
			strHead = StringUtils.removeEnd(sbGroup.toString(), ",");
			// 
			
			String strBody = "";
			StringBuffer sbData = new StringBuffer();
			TadpoleResultSet tdbResult = queryResult.getDataList();
			for (Map<Integer, Object> resultRow : tdbResult.getData()) {
				sbData.setLength(0);
				for (int i=0; i<columnLabel.size(); i++) {
					String strColumnLabel = columnLabel.get(i);
					String strColumnValue = ""+resultRow.get(i);
					boolean isNumber = RDBTypeToJavaTypeUtils.isNumberType(columnType.get(i));
					sbData.append(String.format(GROUP_DATA_TEMPLATE, strColumnLabel, isNumber?strColumnValue:"\"" + strColumnValue + "\""));
				}
				
				strBody += PREFIX_TAB + "{" + StringUtils.removeEnd(sbData.toString(), ",") + "},";
			}
			strBody = StringUtils.removeEnd(strBody, ",");
			
			retHtml = StringUtils.replaceOnce(STR_TEMPLATE, "_TDB_TEMPLATE_TITLE_", name);
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_HEAD_", strHead);
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_BODY_", strBody);
		} catch (Exception e) {
			logger.error("SQL template exception", e);
		}

		return retHtml;
	}
}
