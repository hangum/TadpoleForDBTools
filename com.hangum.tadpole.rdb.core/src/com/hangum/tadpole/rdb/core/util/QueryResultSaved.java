/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.util;

import java.io.File;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.sql.util.export.CSVExpoter;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * 쿼리 결과 저장
 * 
 * @author hangum
 *
 */
public class QueryResultSaved {
	private static final Logger logger = Logger.getLogger(QueryResultSaved.class);
	
	/**
	 * query 결과 저장
	 * 
	 * @param strDir
	 * @param fileName
	 * @param rsDAO
	 */
	public static void queryResult(String strDir, String fileName, QueryExecuteResultDTO rsDAO) {
		String strFullPath = strDir + PublicTadpoleDefine.DIR_SEPARATOR + fileName + ".csv";
		
		try {
			if(!new File(strFullPath).exists()) CSVExpoter.makeHeaderFile(strFullPath, true, rsDAO, ',', "UTF-8");
			CSVExpoter.makeContentFile(strFullPath, true, rsDAO, ',', "UTF-8", "");
		} catch(Exception e) {
			logger.error("queryResultSave", e);
		}
	}

}
