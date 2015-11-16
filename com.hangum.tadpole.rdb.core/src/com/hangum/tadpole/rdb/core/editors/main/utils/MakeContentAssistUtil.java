/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * make content assist util
 * 
 * @author hangum
 *
 */
public class MakeContentAssistUtil extends com.hangum.tadpole.db.metadata.MakeContentAssistUtil {
	private static final Logger logger = Logger.getLogger(MakeContentAssistUtil.class);
	
	/**
	 * content assist
	 * 
	 * @param userDB
	 * @return
	 */
	public String makeContentAssistUtil(final UserDBDAO userDB) {
		final String strTableList = "".equals(userDB.getTableListSeparator())?getAssistTableList(userDB):userDB.getTableListSeparator();
		final String strViewList = "".equals(userDB.getViewListSeparator())?getAssistViewList(userDB):userDB.getViewListSeparator();
		final String sstrTmpFunction = "".equals(userDB.getFunctionLisstSeparator())?getFunctionList(userDB):userDB.getFunctionLisstSeparator();
		final String strConstList = strTableList + 
	    							(strViewList.equals("")?"":"|" + strViewList) +
	    							(sstrTmpFunction.equals("")?"":"|" + sstrTmpFunction);
	    							;
		    							
       return strConstList;
	}
	
	/**
	 * List of assist table name 
	 * 
	 * @return
	 */
	public String getAssistTableList(final UserDBDAO userDB) {
		StringBuffer strTablelist = new StringBuffer();
		
		try {
			List<TableDAO> showTables = new ArrayList<TableDAO>();
			if(userDB.getListTable().isEmpty()) showTables = getTableListOnlyTableName(userDB);
			else showTables = userDB.getListTable();
			
			for (TableDAO tableDao : showTables) {
				strTablelist.append(tableDao.getSysName()).append("|"); //$NON-NLS-1$
			}
		} catch(Exception e) {
			logger.error("MainEditor get the table list", e); //$NON-NLS-1$
		}

		userDB.setTableListSeparator( StringUtils.removeEnd(strTablelist.toString(), "|") ); //$NON-NLS-1$
		
		return userDB.getTableListSeparator();
	}
	
	/**
	 * 보여 주어야할 테이블 목록을 정의합니다.
	 *
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public List<TableDAO> getTableListOnlyTableName(final UserDBDAO userDB) throws Exception {
		List<TableDAO> showTables = null;
				
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT) {
			showTables = new TajoConnectionManager().tableList(userDB);			
		} else {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTables = sqlClient.queryForList("tableListOnlyName", userDB.getDb()); //$NON-NLS-1$			
		}
		
		/** filter 정보가 있으면 처리합니다. */
		return getTableAfterwork(showTables, userDB);
	}
	
}
