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
package com.hangum.tadpole.sql.parser.format;

import kry.sql.format.ISqlFormat;
import kry.sql.format.SqlFormat;
import kry.sql.format.SqlFormatRule;
import kry.sql.util.StringUtil;

import org.apache.log4j.Logger;

import com.hangum.tadpole.preference.get.GetPreferenceGeneral;


/**
 * SQL을 포메팅합니다.
 * 
 * @author hangumNote
 *
 */
public class FormatSQL {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FormatSQL.class);
	
	/**
	 * sql formatting 합니다.
	 * 
	 * @param dbType
	 * @param strOriginalSQL
	 * @return
	 * @throws Exception
	 */
	public static String format(String strOriginalSQL) throws Exception {
		
		SqlFormatRule rule = new SqlFormatRule();
		rule.setRemoveEmptyLine(true);
		int tabSize = Integer.parseInt(GetPreferenceGeneral.getDefaultTabSize());
		boolean optDecode = Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatDecode());// true;
		boolean optIn = Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatIn());//false;

		rule.setIndentString(StringUtil.padLeft("", tabSize, ' '));
		rule.setDecodeSpecialFormat(!optDecode);
		rule.setInSpecialFormat(optIn);
		rule.setOutSqlSeparator(SqlFormatRule.SQL_SEPARATOR_SEMICOLON);
		
		try {
			ISqlFormat formatter = new SqlFormat(rule);
			return formatter.format(strOriginalSQL, 2);
		} catch (Exception e) {
			logger.error("sql format exception", e);
			
			return strOriginalSQL;
		}
	}

	 public static void main(String args[]) {
		 String sql = "select * from tab;";
		 sql += "Select aa From aTest;";
		 sql +=  
				 "select Distinct ID, max(FM), max(Univ3), max(Major), max(Average), max(Cfull), max(Post1), max(Post12), max(Place1), max(Test1), max(Score1), max(Cert1), max(Status), max(Bohun)"+ 
				 "from "+
				 "("+
				 "select ID, FM, '', Major, '', '', '', '', '', '', '', '', '', Bohun"+
				 "from PersoninfoTbl where 조건"+
				 "union "+
				 "select ID, '', Univ3, '', Average, Cfull, '', '', '', '', '', '', '', ''"+
				 "from SchoollingTbl where 조건"+
				 "union "+
				 "select ID, '', '', '', '', '', Post1, Post12, Place1, '', '', '', '', ''"+
				 "from JobApplyTbl where 조건"+
				 "union "+
				 "select ID, '', '', '', '', '', '', '', '', Test1, Score1, '', '', ''"+
				 "from LanguageinfoTbl where 조건"+
				 "union "+
				 "select ID, '', '', '', '', '', '', '', '', '', '',  Cert1, '', ''"+
				 "from CertificationinfoTbl where 조건"+
				 "union"+
				 "select ID, '', '', '', '', '', '', '', '', '', '', '', Status, ''"+
				 "from MilitaryinfoTbl where 조건"+
				 ");";
		 sql += 
				 "CREATE TABLE   sample_table   ("+ 
				 "id INTEGER NOT NULL,  "+
				 "name char(60) default NULL,"+ 
				 "PRIMARY KEY (id) "+
				 ");";	
		 
		 try {
			String retSql = FormatSQL.format(sql);
			System.out.println(retSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
