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
package com.hangum.tadpole.rdb.core.dialog.export;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.rdb.core.dialog.export.application.SQLToJavaConvert;
import com.hangum.tadpole.rdb.core.dialog.export.application.SQLToPHPConvert;

/**
 * sql to language convert
 * 
 * @author hangum
 * 
 */
public class SQLToLanguageConvert {
	private PublicTadpoleDefine.SQL_TO_APPLICATION application;

	public SQLToLanguageConvert(PublicTadpoleDefine.SQL_TO_APPLICATION application) {
		this.application = application;
	}

	public String sqlToString(String name, String sql) {
		if (application == PublicTadpoleDefine.SQL_TO_APPLICATION.PHP) {
			return SQLToPHPConvert.sqlToString(name, sql);
		} else if (application == PublicTadpoleDefine.SQL_TO_APPLICATION.Java_StringBuffer) {
			return SQLToJavaConvert.sqlToString(name, sql);
		}

		return "*** not set appliation type ****";
	}

}
