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

import org.apache.commons.lang.StringUtils;
import org.hibernate.pretty.Formatter;


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
	public static String format(String lowSQL) throws Exception {
		String retStr = "";
		
		String[] arraySQL = StringUtils.split(lowSQL, ";");
		for(int i=0; i<arraySQL.length; i++) {
			Formatter formatter = new Formatter(arraySQL[i]);		
			
			// formatter에서 첫 부분에 \n을 넘겨 주어서.. 제거 합니다.
			String tmpSql = StringUtils.removeStart(formatter.format(), "\n");
			
			if(i == (arraySQL.length-1)) retStr += tmpSql + ";";
			else retStr += tmpSql + ";\n\n";
		}
		
		return retStr;
	}

	 public static void main(String args[]) {
		 String sql = "select * from tab;";
		 sql += "Select aa From aTest";
		 
		 try {
			String retSql = FormatSQL.format(sql);
			System.out.println(retSql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
