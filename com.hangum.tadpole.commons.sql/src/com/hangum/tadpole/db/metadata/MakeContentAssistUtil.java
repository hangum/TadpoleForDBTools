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
package com.hangum.tadpole.db.metadata;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.DBSystemSchema;
import com.hangum.tadpole.engine.security.DBAccessCtlManager;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * make content assist util
 * 
 * @author hangum
 *
 */
public class MakeContentAssistUtil {
	private static final Logger logger = Logger.getLogger(MakeContentAssistUtil.class);
	public static final String DEL_GROUP = "||";
	public static final String DEL_DEFAULT = "|";
	
	/**
	 * content assist
	 * 
	 * @param userDB
	 * @return
	 */
	public String getContentAssist(final UserDBDAO userDB) {
		final String strTableList = "".equals(userDB.getTableListSeparator())?getAssistTableList(userDB):userDB.getTableListSeparator();
		final String strViewList = "".equals(userDB.getViewListSeparator())?getAssistViewList(userDB):userDB.getViewListSeparator();
		final String strTmpFunction = "".equals(userDB.getFunctionLisstSeparator())?getFunctionList(userDB):userDB.getFunctionLisstSeparator();
		
		String strConstList = strTableList;
		if(!StringUtils.isEmpty(strViewList)) {
			strConstList += (StringUtils.isEmpty(strConstList)?strViewList:DEL_GROUP + strViewList);
		}
		if(!StringUtils.isEmpty(strTmpFunction)) {
			strConstList += (StringUtils.isEmpty(strConstList)?strTmpFunction:DEL_GROUP + strTmpFunction);
		}
		    							
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
				strTablelist.append(makeObjectPattern(tableDao.getSysName(), "Table")); //$NON-NLS-1$
			}
		} catch(Exception e) {
			logger.error("getTable list", e); //$NON-NLS-1$
		}
		userDB.setTableListSeparator( StringUtils.removeEnd(strTablelist.toString(), DEL_GROUP) ); //$NON-NLS-1$
		
		return userDB.getTableListSeparator();
	}
	/**
	 * getView list
	 * @return
	 */
	public String getAssistViewList(final UserDBDAO userDB) {
		StringBuffer strTablelist = new StringBuffer();
		try {
			for (TableDAO viewDao : DBSystemSchema.getViewList(userDB)) {
				strTablelist.append(makeObjectPattern(viewDao.getSysName(), "View")); //$NON-NLS-1$
			}
		} catch(Exception e) {
			logger.error("getView list", e); //$NON-NLS-1$
		}
		userDB.setViewListSeparator( StringUtils.removeEnd(strTablelist.toString(), DEL_GROUP)); //$NON-NLS-1$
		
		return userDB.getViewListSeparator();
	}
	
	/**
	 * getFunctionList
	 * @return
	 */
	public String getFunctionList(final UserDBDAO userDB) {
		StringBuffer strFunctionlist = new StringBuffer();
		
		try {
			for (ProcedureFunctionDAO functionDao : DBSystemSchema.getFunctionList(userDB)) {
				strFunctionlist.append(makeObjectPattern(functionDao.getName(), "Function")); //$NON-NLS-1$
			}
		} catch (Exception e) {
			logger.error("getFunction list", e); //$NON-NLS-1$
		}
		userDB.setFunctionLisstSeparator(StringUtils.removeEnd(strFunctionlist.toString(), DEL_GROUP));
		
		return userDB.getFunctionLisstSeparator(); //$NON-NLS-1$
	}
	
	/**
	 * 보여 주어야할 테이블 목록을 정의합니다.
	 *
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	private List<TableDAO> getTableListOnlyTableName(final UserDBDAO userDB) throws Exception {
		List<TableDAO> showTables = null;
				
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT) {
			showTables = new ArrayList<TableDAO>();//().tableList(userDB);			
		} else {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTables = sqlClient.queryForList("tableListOnlyName", userDB.getDb()); //$NON-NLS-1$			
		}
		
		/** filter 정보가 있으면 처리합니다. */
		return getTableAfterwork(showTables, userDB);
	}
	/**
	 * Table 정보 처리 후에 
	 * 
	 * @param showTables
	 * @param userDB
	 * @return
	 */
	public List<TableDAO> getTableAfterwork(List<TableDAO> showTables, final UserDBDAO userDB) {
		/** filter 정보가 있으면 처리합니다. */
		showTables = DBAccessCtlManager.getInstance().getTableFilter(showTables, userDB);
		
		// 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
		for(TableDAO td : showTables) {
			td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getName()));
		}
		
		return showTables;
	}
	
	/**
	 * 
	 * @param objName
	 * @param objType
	 * @return
	 */
	public static String makeObjectPattern(String objName, String objType) {
		return objName + DEL_DEFAULT + objType + DEL_GROUP;
	}
}
