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
	/** content assit  keyword define */
	protected enum CONTENT_ASSIST_KEYWORD_TYPE {TABLE, COLUMN};

	public static final String _PRE_GROUP = "||";
	public static final String _PRE_DEFAULT = "|";
	
	/**
	 * setting default keyword 
	 * @param userDB
	 */
	public void defaultSetKeyword(UserDBDAO userDB) {
//		to do 테이블은 디비가 선택되면 처음 호출 되므로 제외하는것이 효율이 좋을듯합니다. 
//		getAssistTableList(userDB);
		getAssistViewList(userDB);
		getFunctionList(userDB);
	}
	
	/**
	 * content assist
	 * 
	 * @param userDB
	 * @return
	 */
	public String getContentAssist(final UserDBDAO userDB) {
		final String strTableList = "".equals(userDB.getTableListSeparator())?getAssistTableList(userDB):userDB.getTableListSeparator();
		final String strViewList = "".equals(userDB.getViewListSeparator())?getAssistViewList(userDB):userDB.getViewListSeparator();
		final String strFunction = "".equals(userDB.getFunctionLisstSeparator())?getFunctionList(userDB):userDB.getFunctionLisstSeparator();
		
		String strConstList = strTableList;
		if(!StringUtils.isEmpty(strViewList)) {
			strConstList += (StringUtils.isEmpty(strConstList)?strViewList:_PRE_GROUP + strViewList);
		}
		if(!StringUtils.isEmpty(strFunction)) {
			strConstList += (StringUtils.isEmpty(strConstList)?strFunction:_PRE_GROUP + strFunction);
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
				strTablelist.append(makeObjectPattern(tableDao.getSchema_name(), tableDao.getSysName(), "Table")); //$NON-NLS-1$
			}
		} catch(Exception e) {
			logger.error("getTable list", e); //$NON-NLS-1$
		}
		userDB.setTableListSeparator( StringUtils.removeEnd(strTablelist.toString(), _PRE_GROUP) ); //$NON-NLS-1$
		
		return userDB.getTableListSeparator();
	}
	/**
	 * getView list
	 * @return
	 */
	public String getAssistViewList(final UserDBDAO userDB) {
		try {
			DBSystemSchema.getViewList(userDB);
		} catch(Exception e) {
			logger.error("getView list", e); //$NON-NLS-1$
		}
		
		return userDB.getViewListSeparator();
	}
	
	/**
	 * getFunctionList
	 * @return
	 */
	public String getFunctionList(final UserDBDAO userDB) {
		try {
			DBSystemSchema.getFunctionList(userDB);
		} catch (Exception e) {
			logger.error("getFunction list", e); //$NON-NLS-1$
		}
		
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
		StringBuffer strTablelist = new StringBuffer();
		for (TableDAO tableDao : showTables) {
			tableDao.setSysName(SQLUtil.makeIdentifierName(userDB, tableDao.getName()));
			strTablelist.append(makeObjectPattern(tableDao.getSchema_name(), tableDao.getSysName(), "Table")); //$NON-NLS-1$
		}
		userDB.setTableListSeparator( StringUtils.removeEnd(strTablelist.toString(), _PRE_GROUP) ); //$NON-NLS-1$
		
		return showTables;
	}
	
	/**
	 * 
	 * @param objSchemaName  schema name
	 * @param objName object name
	 * @param objType object type(table, view, function)
	 * @return
	 */
	public static String makeObjectPattern(String objSchemaName, String objName, String objType) {
		if("".equals(objSchemaName) | null == objSchemaName) {
			return String.format("%s|%s||", objName, objType); //$NON-NLS-1$			
		} else {
			return String.format("%s.%s|%s||", objSchemaName, objName, objType); //$NON-NLS-1$
		}
	}

}
