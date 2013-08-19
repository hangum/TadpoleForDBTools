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
		rule.setRemoveEmptyLine(true);
		int tabSize = Integer.parseInt(GetPreferenceGeneral.getDefaultTabSize());
		boolean optDecode = Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatDecode());// true;
		boolean optIn = Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatIn());//false;

		rule.setIndentString(StringUtil.padLeft("", tabSize, ' '));
		rule.setDecodeSpecialFormat(!optDecode);
		rule.setInSpecialFormat(optIn);
		rule.setOutSqlSeparator(SqlFormatRule.SQL_SEPARATOR_SEMICOLON);
		rule.setRemoveEmptyLine(true);
		rule.setIndentEmptyLine(true);
		rule.setConvertName(ISqlFormatRule.CONVERT_STRING_NONE);
		rule.setConvertKeyword(ISqlFormatRule.CONVERT_STRING_NONE);
		rule.setNewLineBeforeAndOr(false);
		rule.setNewLineBeforeComma(false);
		
		rule.setWordBreak(false);
		
		return rule;
	}
}
