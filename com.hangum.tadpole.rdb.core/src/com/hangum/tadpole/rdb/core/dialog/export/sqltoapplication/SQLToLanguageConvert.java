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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToJavaConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToMyBatisConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToPHPConvert;

/**
 * sql to language convert
 * 
 * @author hangum
 *
 */
public class SQLToLanguageConvert {
	private EditorDefine.SQL_TO_APPLICATION application ;

	public SQLToLanguageConvert(EditorDefine.SQL_TO_APPLICATION application) {
		this.application = application;
	}
	
	public String sqlToString(String name, String sql) {
		if(application == EditorDefine.SQL_TO_APPLICATION.PHP) {
			return SQLToPHPConvert.sqlToString(name, sql);
		} else if(application == EditorDefine.SQL_TO_APPLICATION.Java_StringBuffer) {
			return SQLToJavaConvert.sqlToString(name, sql);
		} else if(application == EditorDefine.SQL_TO_APPLICATION.MyBatisSelect) {
			return SQLToMyBatisConvert.sqlToString(name, sql);
		}
		
		return "*** not set appliation type ****";
	}	
	
	public String getDefaultVariable() {
		if(application == EditorDefine.SQL_TO_APPLICATION.PHP) {
			return SQLToPHPConvert.DEFAULT_VARIABLE;
		} else if(application == EditorDefine.SQL_TO_APPLICATION.Java_StringBuffer) {
			return SQLToJavaConvert.DEFAULT_VARIABLE;
		} else if(application == EditorDefine.SQL_TO_APPLICATION.MyBatisSelect) {
			return SQLToMyBatisConvert.DEFAULT_VARIABLE;
		}
		
		return "*** not set appliation type ****";
	}
	
}
