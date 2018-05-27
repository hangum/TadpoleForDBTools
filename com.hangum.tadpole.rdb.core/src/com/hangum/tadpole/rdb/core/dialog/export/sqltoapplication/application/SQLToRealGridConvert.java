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
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDMLCommand;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * sql to axisj(http://www.realgrid.com/)
 * 
 * @author hangum
 *
 */
public class SQLToRealGridConvert extends AbstractSQLTo {
	private static final Logger logger = Logger.getLogger(SQLToRealGridConvert.class);
	public static final String PREFIX_TAB = "\n\t\t\t";
	public static final String GROUP_TEMPLATE = PREFIX_TAB + "{fieldName: \"%s\", dataType: \"%s\"},";
	public static final String GROUP_DATA_TEMPLATE = PREFIX_TAB + "{ name: \"%s\", type: \"series\", fieldNames: \"%s\", width: 150, renderer: { type: \"sparkLine\", highFill: \"#ff008800\",  lowFill: \"#ffff0000\", lastFill: \"#ff888888\" },";
	public static final String DEFAULT_VARIABLE = "";
	
	/**
	 * sql to string
	 * 
	 * @param name
	 * @param sql
	 * @return
	 */
	public static String sqlToString(UserDBDAO userDB, String name, String sql) {
		String retHtml = "";
		try {
			String STR_TEMPLATE = IOUtils.toString(SQLToRealGridConvert.class.getResource("realgrid.js.template"));

			QueryExecuteResultDTO queryResult = ExecuteDMLCommand.executeQuery(userDB, sql, 0, 4);
			Map<Integer, String> columnLabel = queryResult.getColumnLabelName();
			Map<Integer, Integer> columnType = queryResult.getColumnType();
			
			String strField = "";
			StringBuffer sbField = new StringBuffer();
			for (int i=0; i<columnLabel.size(); i++) {
				String strColumnLabel = columnLabel.get(i);
				boolean isNumber = RDBTypeToJavaTypeUtils.isNumberType(columnType.get(i));
				sbField.append(String.format(GROUP_TEMPLATE, strColumnLabel, isNumber?"number":"text"));
			}
			strField = StringUtils.removeEnd(sbField.toString(), ",");
			
			String strColumn = "";
			StringBuffer sbColumn = new StringBuffer();
			for (int i=0; i<columnLabel.size(); i++) {
				String strColumnLabel = columnLabel.get(i);
				sbColumn.append(String.format(GROUP_DATA_TEMPLATE, strColumnLabel, strColumnLabel));
			}
			strColumn = StringUtils.removeEnd(sbColumn.toString(), ",");

			retHtml = StringUtils.replaceOnce(STR_TEMPLATE, "_TDB_TEMPLATE_FIELD_", strField);
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_COLUMN_", strColumn);
		} catch (Exception e) {
			logger.error("SQL template exception", e);
		}

		return retHtml;
	}
}
