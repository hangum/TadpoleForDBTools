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
package com.hangum.tadpole.sql.rule;

import kry.sql.format.ISqlFormatRule;
import kry.sql.format.SqlFormatRule;
import kry.sql.util.StringUtil;

import com.hangum.tadpole.preference.get.GetPreferenceGeneral;

/**
 * user sql format rule
 * 
 * @author hangum
 *
 */
public class SQLFormatRule {
	
	/**
	 * get user sql rule
	 * @return
	 */
	public static SqlFormatRule getSqlFormatRule() {
		SqlFormatRule rule = new SqlFormatRule();
		
		rule.setIndentString(StringUtil.padLeft("", Integer.parseInt(GetPreferenceGeneral.getDefaultTabSize()), ' '));
		rule.setDecodeSpecialFormat(!Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatDecode()));
		rule.setInSpecialFormat(Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatIn()));
		rule.setOutSqlSeparator(SqlFormatRule.SQL_SEPARATOR_SEMICOLON);
		rule.setRemoveEmptyLine(Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatRemoveEmptyLine()));
		rule.setIndentEmptyLine(Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatRemoveEmptyLine()));
		rule.setConvertName(ISqlFormatRule.CONVERT_STRING_NONE);
		rule.setConvertKeyword(ISqlFormatRule.CONVERT_STRING_NONE);
		rule.setNewLineBeforeAndOr(Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatNewLineBeforeAndOr()));
		rule.setNewLineBeforeComma(Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatNewLineBeforeComma()));
		
		rule.setWordBreak(Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatWordBreak()));
		try {
			rule.setWidth(Integer.parseInt(GetPreferenceGeneral.getSQLFormatWordWidth()));
		} catch(NumberFormatException nfe) {
			rule.setWidth(80);
		}
		
		return rule;
	}
}
