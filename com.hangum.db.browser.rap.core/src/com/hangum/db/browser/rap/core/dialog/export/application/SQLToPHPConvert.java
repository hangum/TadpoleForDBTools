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
		
		sql = StringUtils.remove(sql, ";");		
		String[] splists = StringUtils.split(sql, Define.LINE_SEPARATOR);
		for(int i = 0; i<splists.length; i++) {
			if(!"".equals( StringUtils.trimToEmpty(splists[i]) )) {
				
				if(i == 0) sbSQL.append("$" + name + " = \"" + SQLTextUtil.delLineChar(splists[i]) + "\"; \r\n");
				else sbSQL.append("$" + name  + " .= \"" +  SQLTextUtil.delLineChar(splists[i]) + "\"; \r\n");
			}
		}
		
		return sbSQL.toString();
	}
}
