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
package com.hangum.sql.parser.format;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.pp.para.GFmtOpt;
import gudusoft.gsqlparser.pp.para.GFmtOptFactory;
import gudusoft.gsqlparser.pp.stmtformattor.FormattorFactory;

/**
 * SQL을 포메팅합니다.
 * 
 * @author hangumNote
 *
 */
public class FormatSQL {
	
	/**
	 * sql formatting 합니다.
	 * 
	 * @param dbType
	 * @param lowSQL
	 * @return
	 * @throws Exception
	 */
	public static String format(ParserDefine.DB_TYPE dbType, String lowSQL) throws Exception {
		TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmysql);
		sqlparser.sqltext = lowSQL;
		 
		int ret = sqlparser.parse();
        if (ret == 0){
        	GFmtOpt option = GFmtOptFactory.newInstance();
            String formatSQL = FormattorFactory.pp(sqlparser, option);

            return formatSQL;
        } else {
            throw new Exception(sqlparser.getErrormessage());
        }
	}

	 public static void main(String args[]) {
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmysql);

         sqlparser.sqltext = "select col1, \r\n" + 
         "col2,sum(col3) from table1, table2 where col4 > col5 and col6= 1000;";
         sqlparser.sqltext += "select test from hangum;";
         sqlparser.sqltext += "select test2 from hangum2;";
         
//        sqlparser.sqltext = "select * from test3";

        int ret = sqlparser.parse();
        if (ret == 0){
            GFmtOpt option = GFmtOptFactory.newInstance();
            String result = FormattorFactory.pp(sqlparser, option);
            System.out.println(result);
        }else{
            System.out.println(sqlparser.getErrormessage());
        }
     }
}
