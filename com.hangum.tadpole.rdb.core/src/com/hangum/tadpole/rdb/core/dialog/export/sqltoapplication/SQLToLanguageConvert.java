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

import java.util.List;
import java.util.Map;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToASPConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToAxisjConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToJavaConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToMyBatisConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToPHPConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToRealGridConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.axisj.AxisjHeaderDAO;

/**
 * sql to language convert
 * 
 * @author hangum
 *
 */
public class SQLToLanguageConvert {
	private UserDBDAO userDB;
	private EditorDefine.SQL_TO_APPLICATION application ;

	public SQLToLanguageConvert(UserDBDAO userDB, EditorDefine.SQL_TO_APPLICATION application) {
		this.userDB = userDB;
		this.application = application;
	}
	
	public String sqlToString(String sql, Map options, List<AxisjHeaderDAO> listAxisjHeader) {
		
		if(application == EditorDefine.SQL_TO_APPLICATION.PHP) {
			return SQLToPHPConvert.sqlToString((String) options.get("name"), sql);
		} else if(application == EditorDefine.SQL_TO_APPLICATION.ASP) {
			return SQLToASPConvert.sqlToString((String) options.get("name"), sql);
		} else if(application == EditorDefine.SQL_TO_APPLICATION.Java_StringBuffer) {
			return SQLToJavaConvert.sqlToString((String) options.get("name"), sql);
		} else if(application == EditorDefine.SQL_TO_APPLICATION.MyBatis) {
			return SQLToMyBatisConvert.sqlToString(userDB, (String) options.get("name"), sql);
		} else if(application == EditorDefine.SQL_TO_APPLICATION.AXISJ) {
			return SQLToAxisjConvert.sqlToString(userDB, sql, options, listAxisjHeader);
		} else if(application == EditorDefine.SQL_TO_APPLICATION.REAL_GRID) {
			return SQLToRealGridConvert.sqlToString(userDB, (String) options.get("name"), sql);
		}
		
		return "*** not set appliation type ****";
	}	
	
	public String getDefaultVariable() {
		if(application == EditorDefine.SQL_TO_APPLICATION.PHP) {
			return SQLToPHPConvert.DEFAULT_VARIABLE;
		} else if(application == EditorDefine.SQL_TO_APPLICATION.ASP) {
			return SQLToASPConvert.DEFAULT_VARIABLE;
		} else if(application == EditorDefine.SQL_TO_APPLICATION.Java_StringBuffer) {
			return SQLToJavaConvert.DEFAULT_VARIABLE;
		} else if(application == EditorDefine.SQL_TO_APPLICATION.MyBatis) {
			return SQLToMyBatisConvert.DEFAULT_VARIABLE;
		} else if(application == EditorDefine.SQL_TO_APPLICATION.AXISJ) {
			return SQLToAxisjConvert.DEFAULT_VARIABLE;
		} else if(application == EditorDefine.SQL_TO_APPLICATION.REAL_GRID) {
			return SQLToRealGridConvert.DEFAULT_VARIABLE;
		}
		
		return "*** not set appliation type ****";
	}
	
}
