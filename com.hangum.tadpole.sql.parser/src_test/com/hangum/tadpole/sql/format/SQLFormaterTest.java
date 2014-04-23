package com.hangum.tadpole.sql.format;

import kry.sql.format.ISqlFormat;
import kry.sql.format.ISqlFormatRule;
import kry.sql.format.SqlFormat;
import kry.sql.format.SqlFormatRule;
import kry.sql.util.StringUtil;

import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.sql.rule.SQLFormatRule;

public class SQLFormaterTest  {

	/**
	 * get user sql rule
	 * @return
	 */
	public static SqlFormatRule getSqlFormatRule() {
		SqlFormatRule rule = new SqlFormatRule();
		
		rule.setIndentString(StringUtil.padLeft("", 2, ' '));
		rule.setDecodeSpecialFormat(true);//!Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatDecode()));
		rule.setInSpecialFormat(true);//Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatIn()));
		rule.setOutSqlSeparator(SqlFormatRule.SQL_SEPARATOR_SEMICOLON);
		rule.setRemoveEmptyLine(false);//Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatRemoveEmptyLine()));
		rule.setIndentEmptyLine(false);//Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatRemoveEmptyLine()));
		rule.setConvertName(ISqlFormatRule.CONVERT_STRING_NONE);
		rule.setConvertKeyword(ISqlFormatRule.CONVERT_STRING_NONE);
//		rule.setNewLineBeforeAndOr(true);//Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatNewLineBeforeAndOr()));
//		rule.setNewLineBeforeComma(true);//Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatNewLineBeforeComma()));
		
		String[] arrDataTypes = {"INTEGER","char"};
		rule.setDataTypes(arrDataTypes);
		rule.setNewLineDataTypeParen(false);
		rule.setRemoveComment(true);	
		
//		rule.setWordBreak(true);//Boolean.parseBoolean(GetPreferenceGeneral.getSQLFormatWordBreak()));
////		try {
////			rule.setWidth(Integer.parseInt(GetPreferenceGeneral.getSQLFormatWordWidth()));
////		} catch(NumberFormatException nfe) {
//			rule.setWidth(80);
////		}
		
		return rule;
	}
	
	/**
	 * sql formatting 합니다.
	 * 
	 * @param dbType
	 * @param strOriginalSQL
	 * @return
	 * @throws Exception
	 */
	public static String format(String strOriginalSQL) throws Exception {
		
		try {
			ISqlFormat formatter = new SqlFormat(getSqlFormatRule());
			return formatter.format(strOriginalSQL, 0);
		} catch (Exception e) {
			e.printStackTrace();
			
			return strOriginalSQL;
		}
	}

	 public static void main(String args[]) {
		 String sql = "";//select * from tab;";
//		 sql += "Select aa From aTest;";
//		 sql +=  
//				 "select Distinct ID, max(FM), max(Univ3), max(Major), max(Average), max(Cfull), max(Post1), max(Post12), max(Place1), max(Test1), max(Score1), max(Cert1), max(Status), max(Bohun)"+ 
//				 "from "+
//				 "("+
//				 "select ID, FM, '', Major, '', '', '', '', '', '', '', '', '', Bohun"+
//				 "from PersoninfoTbl where 조건"+
//				 "union "+
//				 "select ID, '', Univ3, '', Average, Cfull, '', '', '', '', '', '', '', ''"+
//				 "from SchoollingTbl where 조건"+
//				 "union "+
//				 "select ID, '', '', '', '', '', Post1, Post12, Place1, '', '', '', '', ''"+
//				 "from JobApplyTbl where 조건"+
//				 "union "+
//				 "select ID, '', '', '', '', '', '', '', '', Test1, Score1, '', '', ''"+
//				 "from LanguageinfoTbl where 조건"+
//				 "union "+
//				 "select ID, '', '', '', '', '', '', '', '', '', '',  Cert1, '', ''"+
//				 "from CertificationinfoTbl where 조건"+
//				 "union"+
//				 "select ID, '', '', '', '', '', '', '', '', '', '', '', Status, ''"+
//				 "from MilitaryinfoTbl where 조건"+
//				 ");";
		 sql += "/* test */ " + 
				 "create table   sample-table   ("+
				 " -- test \n \n \n" +
				 "id INTEGER NOT NULL,  "+
				 "name char(60) default NULL,"+ 
				 "PRIMARY KEY (id) "+
				 ");";	
		 
		 try {
			String retSql = format(sql);
			System.out.println(retSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
