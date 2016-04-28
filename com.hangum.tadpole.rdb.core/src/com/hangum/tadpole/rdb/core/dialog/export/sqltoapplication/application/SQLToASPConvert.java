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
 * sql to php
 * 
 * @author hangum
 *
 */
public class SQLToASPConvert extends AbstractSQLTo {
	public static final String DEFAULT_VARIABLE = "strSQL";
	
	public static String sqlToString(String name, String sql) {
		StringBuffer sbSQL = new StringBuffer("");
		
		sql = StringUtils.remove(sql, ";");		
		String[] splists = StringUtils.split(sql, PublicTadpoleDefine.LINE_SEPARATOR);
		for(int i = 0; i<splists.length; i++) {
			if(!"".equals( StringUtils.trimToEmpty(splists[i]) )) {
				
				if(i == 0) sbSQL.append(name + " = \"" + SQLTextUtil.delLineChar(splists[i]) + " \" \r\n");
				else sbSQL.append(name  + " = " + name + " & \"" +  SQLTextUtil.delLineChar(splists[i]) + " \" \r\n");
			}
		}
		
		return sbSQL.toString();
	}
}
