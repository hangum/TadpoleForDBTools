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
package com.hangum.db.browser.rap.core.dialog.export;

import com.hangum.db.browser.rap.core.dialog.export.application.SQLToJavaConvert;
import com.hangum.db.browser.rap.core.dialog.export.application.SQLToPHPConvert;
import com.hangum.db.define.Define;

/**
 * sql to language convert
 * 
 * @author hangum
 *
 */
public class SQLToLanguageConvert {
	private Define.SQL_TO_APPLICATION application ;

	public SQLToLanguageConvert(Define.SQL_TO_APPLICATION application) {
		this.application = application;
	}
	
	public String sqlToString(String name, String sql) {
		if(application == Define.SQL_TO_APPLICATION.PHP) {
			return SQLToPHPConvert.sqlToString(name, sql);
		} else if(application == Define.SQL_TO_APPLICATION.Java_StringBuffer) {
			return SQLToJavaConvert.sqlToString(name, sql);
		}
		
		return "*** not set appliation type ****";
	}
	
}
