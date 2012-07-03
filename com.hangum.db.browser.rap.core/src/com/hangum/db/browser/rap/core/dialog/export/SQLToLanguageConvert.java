package com.hangum.db.browser.rap.core.dialog.export;

import com.hangum.db.browser.rap.core.dialog.export.application.SQLToJavaConvert;
import com.hangum.db.browser.rap.core.dialog.export.application.SQLToPHPConvert;
import com.hangum.db.define.Define;

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
