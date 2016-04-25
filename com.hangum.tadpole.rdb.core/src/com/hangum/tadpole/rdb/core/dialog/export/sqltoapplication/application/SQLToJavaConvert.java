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
public class SQLToJavaConvert extends AbstractSQLTo {
	public static final String DEFAULT_VARIABLE = "sqlBuff";
	
	/**
	 * sql to string
	 * 
	 * @param name
	 * @param sql
	 * @return
	 */
	public static String sqlToString(String name, String sql) {
		StringBuffer sbSQL = new StringBuffer("StringBuffer " + name + " = new StringBuffer();" + PublicTadpoleDefine.LINE_SEPARATOR);
		
		sql = StringUtils.remove(sql, ";");		
		String[] splists = StringUtils.split(sql, PublicTadpoleDefine.LINE_SEPARATOR);
		for (String part : splists) {
			
			if(!"".equals( StringUtils.trimToEmpty(part) )) {
				// https://github.com/hangum/TadpoleForDBTools/issues/181 fix
				sbSQL.append(name + ".append(\" " + SQLTextUtil.delLineChar(part) + " \"); " + PublicTadpoleDefine.LINE_SEPARATOR);
			}			
		}
		
		return sbSQL.toString();
	}

}
