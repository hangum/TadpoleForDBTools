/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.browser.rap.core.dialog.export.application;

import org.apache.commons.lang.StringUtils;

import com.hangum.db.browser.rap.core.editors.main.SQLTextUtil;
import com.hangum.db.define.Define;

/**
 * sql to java
 * 
 * @author hangum
 *
 */
public class SQLToJavaConvert {
	public static final String name = "sqlBuff";
	
	/**
	 * sql to string
	 * 
	 * @param name
	 * @param sql
	 * @return
	 */
	public static String sqlToString(String name, String sql) {
		StringBuffer sbSQL = new StringBuffer("StringBuffer " + name + " = new StringBuffer();" + Define.LINE_SEPARATOR);
		
		sql = StringUtils.remove(sql, ";");		
		String[] splists = StringUtils.split(sql, Define.LINE_SEPARATOR);
		for (String part : splists) {
			
			if(!"".equals( StringUtils.trimToEmpty(part) )) {
				sbSQL.append(name + ".append(\"" + SQLTextUtil.delLineChar(part) + "\"); " + Define.LINE_SEPARATOR);
			}			
		}
		
		return sbSQL.toString();
	}

}
