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
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.DBSystemSchema;
import com.hangum.tadpole.engine.security.DBAccessCtlManager;
import com.hangum.tadpole.engine.sql.util.SQLUtil;

/**
 * make content assist util
 * 
 * @author hangum
 *
 */
public abstract class MakeContentAssistUtil {
	private static final Logger logger = Logger.getLogger(MakeContentAssistUtil.class);
	/** content assit  keyword define */
	protected enum CONTENT_ASSIST_KEYWORD_TYPE {TABLE, COLUMN};

	public static final String _PRE_GROUP = "||";
	public static final String _PRE_DEFAULT = "|";
	
	/**
	 *	스키마로 검색했을 경우에 스키마이름이 없는 리스트를 넘겨 주어야 한다.. 
	 *	content assist 리스트를 넘겨줄때 tester.tablename 으로 넘겨 주는데 스키마명 다음에 . 이 나오면 스키마 명이 두번 출력되기 때문이다. 
	 * 
	 * @param userDB
	 * @param strArryCursor
	 * @return
	 */
	protected String getSchemaOrTableContentAssist(UserDBDAO userDB, String[] strArryCursor) {
		String strCntAsstList 	= getContentAssist(userDB);
		String strCursorText 	= strArryCursor[0] + strArryCursor[1];
		
		if(StringUtils.contains(strCursorText, '.')) {
			String strSchemaName 		= StringUtils.substringBefore(strCursorText, ".") + ".";
//			String strTableName 		= StringUtils.substringAfter(strCursorText, ".");
			int intSep = StringUtils.indexOf(strCursorText, ".");
			
			if(logger.isDebugEnabled()) {
				logger.debug("[0]" + strArryCursor[0]);
				logger.debug("[1]" + strArryCursor[1]);
				logger.debug("[1][intSep]" + intSep);
				logger.debug("[1][strArryCursor[0].length()]" + strArryCursor[0].length());
				logger.debug("==> [Return table list]" + (strArryCursor[0].length() >= intSep));
			}
			
			// 텍스트 커서가 뒤에 있으면 테이블 명만 리턴한다.
			if(strArryCursor[0].length() >= intSep) {
				String strNewCntAsstList = "";
				
				String[] listGroup = StringUtils.splitByWholeSeparator(strCntAsstList, _PRE_GROUP);
				if(listGroup == null) return strNewCntAsstList;
				
				for (String strDefault : listGroup) {
					String[] listDefault = StringUtils.split(strDefault, _PRE_DEFAULT);
					if(listDefault != null & listDefault.length == 2) {
						if(StringUtils.startsWithIgnoreCase(listDefault[0], strSchemaName))
							strNewCntAsstList += makeObjectPattern("", StringUtils.removeStartIgnoreCase(listDefault[0], strSchemaName), listDefault[1]);
					}	// 
				}
				
				return strNewCntAsstList;
			}
		}
		
		return strCntAsstList;
	}
	
	/**
	 * content assist
	 * 
	 * @param userDB
	 * @return
	 */
	protected String getContentAssist(final UserDBDAO userDB) {
		final String strSchema = "".equals(userDB.getSchemaListSeparator())?getAssistSchemaList(userDB):userDB.getSchemaListSeparator();
		final String strTableList = "".equals(userDB.getTableListSeparator())?getAssistTableList(userDB):userDB.getTableListSeparator();
		final String strViewList = "".equals(userDB.getViewListSeparator())?getAssistViewList(userDB):userDB.getViewListSeparator();
		final String strFunction = "".equals(userDB.getFunctionLisstSeparator())?getFunctionList(userDB):userDB.getFunctionLisstSeparator();
		
		String strContentAssistList = strSchema;
		if(!StringUtils.isEmpty(strTableList)) {
			strContentAssistList += (StringUtils.isEmpty(strContentAssistList)?strTableList:_PRE_GROUP + strTableList);
		}
		if(!StringUtils.isEmpty(strViewList)) {
			strContentAssistList += (StringUtils.isEmpty(strContentAssistList)?strViewList:_PRE_GROUP + strViewList);
		}
		if(!StringUtils.isEmpty(strFunction)) {
			strContentAssistList += (StringUtils.isEmpty(strContentAssistList)?strFunction:_PRE_GROUP + strFunction);
		}
		    							
       return strContentAssistList;
	}
	
	/**
	 * List of assis schema name
	 * 
	 * @param userDB
	 * @return
	 */
	public String getAssistSchemaList(final UserDBDAO userDB) {
		StringBuffer strSchemaList = new StringBuffer();
		
		if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
			try {
				for (Object object : DBSystemSchema.getSchemas(userDB)) {
					Map map = (Map)object;
					strSchemaList.append(makeObjectPattern(null, ""+map.get("schema"), "Schema")); //$NON-NLS-1$
				}
				userDB.setSchemaListSeparator( StringUtils.removeEnd(strSchemaList.toString(), _PRE_GROUP) ); //$NON-NLS-1$
			} catch(Exception e) {
				logger.error("getSchema list", e);
			}
		}
		
		return strSchemaList.toString();
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

	public abstract List<TableDAO> getTableListOnlyTableName(final UserDBDAO userDB) throws Exception;
	
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
