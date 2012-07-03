package com.hangum.db.browser.rap.core.dialog.export.application;

import org.apache.commons.lang.StringUtils;

import com.hangum.db.browser.rap.core.editors.main.SQLTextUtil;
import com.hangum.db.define.Define;

/**
 * sql to php
 * 
 * @author hangum
 *
 */
public class SQLToPHPConvert {
	public static final String name = "query";
	
	public static String sqlToString(String name, String sql) {
		StringBuffer sbSQL = new StringBuffer("");
		
		String[] splists = sql.split(Define.LINE_SEPARATOR);
		for(int i = 0; i<splists.length; i++) {
			if(!"".equals( StringUtils.trimToEmpty(splists[i]) )) {
				
				if(i == 0) sbSQL.append("$" + name + " = \"" + SQLTextUtil.delLineChar(splists[i]) + "\";" + Define.LINE_SEPARATOR);
				else sbSQL.append("$" + name  + " .= \"" +  SQLTextUtil.delLineChar(splists[i]) + "\";" + Define.LINE_SEPARATOR);
			}
		}
		sbSQL.append(Define.LINE_SEPARATOR);
		
		return sbSQL.toString();
	}
}
