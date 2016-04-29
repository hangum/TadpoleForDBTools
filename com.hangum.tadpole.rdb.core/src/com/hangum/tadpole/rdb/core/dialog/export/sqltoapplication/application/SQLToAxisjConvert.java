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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.AxisjConsts;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.AxisjHeaderDAO;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataGenDAO;

/**
 * sql to axisj(https://www.axisj.com/)
 * options : http://jdoc.axisj.com/AXGrid.html#.setConfig
 * 
 * @author hangum
 *
 */
public class SQLToAxisjConvert extends AbstractSQLTo {
	private static final Logger logger = Logger.getLogger(SQLToAxisjConvert.class);
	public static final String DEFAULT_VARIABLE = "AXGridTarget";
	public static final String PREFIX_TAB = "\n\t\t\t\t\t\t";
	public static final String GROUP_TEMPLATE = PREFIX_TAB + "{key : \"%s\", label : \"%s\", width : %s, align : %s, sort : %s, colHeadTool : %s, formatter : %s, tooltip : %s, disabled : %s, checked : %s },";
	public static final String GROUP_DATA_TEMPLATE = "%s:%s,";
	
	public static List<AxisjHeaderDAO> initializeHead(List<AxisjHeaderDAO> listAxisjHeader, UserDBDAO userDB, String sql) {
		try {
			QueryExecuteResultDTO queryResult = QueryUtils.executeQuery(userDB, sql, 0, 4);
			Map<Integer, String> columnLabel = queryResult.getColumnLabelName();
			Map<Integer, Integer> columnType = queryResult.getColumnType();
			
			for (int i=0; i<columnLabel.size(); i++) {				
				AxisjHeaderDAO axisjHeader = new AxisjHeaderDAO();
				
				axisjHeader.setSeqNo(i);
				axisjHeader.setKey(columnLabel.get(i));
				axisjHeader.setLabel(columnLabel.get(i));
				
				boolean isNumber = RDBTypeToJavaTypeUtils.isNumberType(columnType.get(i));
				axisjHeader.setAlign(isNumber?2:0);// 0:left, 1:center, 2:right
				axisjHeader.setChecked("function(){return false;}");
				axisjHeader.setColHeadTool(false);
				axisjHeader.setDisabled("function(){return false;}");
				axisjHeader.setFormatter("function(){return '';}");
				axisjHeader.setSort(1); //0:false, 1:Ascending, 2:Descending
				axisjHeader.setTooltip("function(){return '';}");
				axisjHeader.setWidth(100);
				
				listAxisjHeader.add(axisjHeader);
			}
		}catch (Exception e) {
			logger.error("SQL template exception", e);
		}
		return listAxisjHeader;
	}
	/**
	 * sql to string
	 * 
	 * @param userDB
	 * @param name
	 * @param sql
	 * @return
	 */
	public static String sqlToString(UserDBDAO userDB, String sql, Map options, List<AxisjHeaderDAO> listAxisjHeader) {
		String retHtml = "";
		try {
			String STR_TEMPLATE = IOUtils.toString(SQLToAxisjConvert.class.getResource("axis.js.template"));

			QueryExecuteResultDTO queryResult = QueryUtils.executeQuery(userDB, sql, 0, 4);
			Map<Integer, String> columnLabel = queryResult.getColumnLabelName();
			Map<Integer, Integer> columnType = queryResult.getColumnType();
			
			String strHead = "";
			StringBuffer sbGroup = new StringBuffer();
			for (AxisjHeaderDAO dao :listAxisjHeader ){
				sbGroup.append(String.format(GROUP_TEMPLATE, dao.getKey(), dao.getLabel(), dao.getWidth(), AxisjConsts.alignValue[ dao.getAlign()],AxisjConsts.sortValue[ dao.getSort()], dao.isColHeadTool()?"true":"false", dao.getFormatter(), dao.getTooltip(), dao.getDisabled(), dao.getChecked() ));
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
			
			retHtml = StringUtils.replaceOnce(STR_TEMPLATE, "_TDB_TEMPLATE_TITLE_", (String) options.get("name"));
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_THEME_", (String) options.get("theme"));
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_FIXEDCOL_", (String) options.get("fixedColSeq"));
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_FITTOWIDTH_", (String) options.get("fitToWidth"));
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_HEADALIGN_", (String) options.get("colHeadAlign"));
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_MERGECELL_", (String) options.get("mergeCells"));
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_HEIGHT_", (String) options.get("height"));
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_SORT_", (String) options.get("sort"));
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_HEADTOOL_", (String) options.get("colHeadTool"));
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_VIEWMODE_", (String) options.get("viewMode"));
			
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_HEAD_", strHead);
			retHtml = StringUtils.replaceOnce(retHtml, "_TDB_TEMPLATE_BODY_", strBody);
		} catch (Exception e) {
			logger.error("SQL template exception", e);
		}

		return retHtml;
	}
}
