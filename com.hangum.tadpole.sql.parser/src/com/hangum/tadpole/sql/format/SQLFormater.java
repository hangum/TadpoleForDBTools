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
package com.hangum.tadpole.sql.format;

import kry.sql.format.ISqlFormat;
import kry.sql.format.SqlFormat;

import org.apache.log4j.Logger;

import com.hangum.tadpole.sql.rule.SQLFormatRule;


/**
 * SQL을 포메팅합니다.
 * 
 * @author hangum
 *
 */
public class SQLFormater {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLFormater.class);
	
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
			ISqlFormat formatter = new SqlFormat(SQLFormatRule.getSqlFormatRule());
			return formatter.format(strOriginalSQL, 0);
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
			String retSql = SQLFormater.format(sql);
			System.out.println(retSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
