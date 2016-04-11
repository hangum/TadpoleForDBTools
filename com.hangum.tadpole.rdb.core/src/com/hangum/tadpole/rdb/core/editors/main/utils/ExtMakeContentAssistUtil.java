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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.db.metadata.MakeContentAssistUtil;
import com.hangum.tadpole.db.metadata.TadpoleMetaData;
import com.hangum.tadpole.db.metadata.constants.SQLConstants;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * make content assist util
 * 
 * @author hangum
 *
 */
public class ExtMakeContentAssistUtil extends MakeContentAssistUtil {
	private static final Logger logger = Logger.getLogger(ExtMakeContentAssistUtil.class);
	
	/**
	 * make content assist util
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param intPosition
	 * @return
	 */
	public String makeContentAssist(final UserDBDAO userDB, String strQuery, final int intPosition) throws Exception {
		strQuery = StringUtils.replace(strQuery, "\n", " ");
		
		String listContentAssist = "";
		// 현재 위치한 커서 텍스트
		String[] strPrevArryCursor = SQLTextUtil.findPreCursorObjectArry(strQuery, intPosition);
		String strCursor = strPrevArryCursor[0] + strPrevArryCursor[1]; 
		if(logger.isDebugEnabled()) logger.debug("\t position text is [" + strCursor + "]");
		
		// 이전 키워드를 얻는다.
		String strPrevKeyword = SQLTextUtil.findPrevKeywork(strQuery, intPosition);
		if(logger.isDebugEnabled()) logger.debug("\t prevous keyword is : [" + strPrevKeyword + "]");
		CONTENT_ASSIST_KEYWORD_TYPE prevKeywordType = null;
		
		// 쿼리에 공백을 없앤다.
		strQuery = StringUtils.removeEnd(StringUtils.trimToEmpty(strQuery), ";") + " ";
		
		// 이전 키워드가 있다면 마지막이 테이블 키워드 인지, 컬럼 키워드 인지 찾는다.
		if(!"".equals(strPrevKeyword)) {
			if(SQLConstants.listTableKeywords.contains(strPrevKeyword)) {
				prevKeywordType = CONTENT_ASSIST_KEYWORD_TYPE.TABLE;
			} else if(SQLConstants.listColumnKeywords.contains(strPrevKeyword)) {
				prevKeywordType = CONTENT_ASSIST_KEYWORD_TYPE.COLUMN;
			}
		}
		
		if(prevKeywordType != null) {
			if(strCursor.length() == 0) {
				// 오브젝트의 컬럼 목록을 가져온다.
				if(prevKeywordType == CONTENT_ASSIST_KEYWORD_TYPE.COLUMN) {
					if(logger.isDebugEnabled()) logger.debug("==========[0][CURSOR][COLUMN] content assist : ");
					listContentAssist = getTableColumnAlias(userDB, strQuery, strCursor);
				// 오브젝트 목록을 가져온다.
				} else {
					if(logger.isDebugEnabled()) logger.debug("==========[0][CURSOR][TABLE] content assist : ");
					// insert 문이고 테이블 명을 발견했으면 컬럼 리스트를 리턴한다.
					listContentAssist = ifInsertGetColumn(userDB, strQuery, strCursor);
					
					// 컬럼이 없으면 테이블 목록을 넘겨준다.
					if("".equals(listContentAssist)) {
						listContentAssist = getSchemaOrTableContentAssist(userDB, strPrevArryCursor);
					}
				}
				
				if(logger.isDebugEnabled()) logger.debug("\t result : "  + listContentAssist);
			} else {
				// 오브젝트의 컬럼 목록을 가져온다.
				if(prevKeywordType == CONTENT_ASSIST_KEYWORD_TYPE.COLUMN) {
					if(logger.isDebugEnabled()) logger.debug("+++++++++++[1][DEFAULT][COLUMN] content assist : " + listContentAssist);
					listContentAssist = getTableColumnAlias(userDB, strQuery, strCursor);
				} else {
					// 디비스키마 목록, 테이블 목록, 키워드 일수도 있다. 
					if(logger.isDebugEnabled()) logger.debug("+++++++++++[1][DEFAULT][TABLE] content assist : " + listContentAssist);
					// insert 문이고 테이블 명을 발견했으면 컬럼 리스트를 리턴한다.
					listContentAssist = ifInsertGetColumn(userDB, strQuery, strCursor);
					
					// 컬럼이 없으면 테이블 목록을 넘겨준다.
					if("".equals(listContentAssist)) {
						listContentAssist = getSchemaOrTableContentAssist(userDB, strPrevArryCursor);
					}
				}
				if(logger.isDebugEnabled()) logger.debug("\t result : "  + listContentAssist);
				
			}
		} else {
			if(logger.isDebugEnabled()) logger.debug("[NONE] content assist : " + listContentAssist);
		}
		
		return listContentAssist;
	}
	
	/**
	 * insert 로 시작하고 테이블 명을 발견 했을 경우, 컬럼 리스트를 리턴해 주어야 한다.
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param strCursor
	 * @return
	 */
	private String ifInsertGetColumn(UserDBDAO userDB, String strQuery, String strCursor) {
		String listContentAssist = "";
		if(StringUtils.startsWithIgnoreCase(StringUtils.trimToEmpty(strQuery), "insert")) {
			listContentAssist = getTableColumnAlias(userDB, strQuery, strCursor);
		} 
		
		return listContentAssist;
	}
	
	/**
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param strCursor
	 */
	private String getTableColumnAlias(final UserDBDAO userDB,  final String strQuery, final String strCursor) {
		List<String> listName = new ArrayList<>();
		
		TadpoleMetaData dbMetaData = TadpoleSQLManager.getDbMetadata(userDB);
		String strQuote = dbMetaData.getIdentifierQuoteString();
		String strSeparator = ".";
		String strAlias = "";
		if(StringUtils.indexOf(strCursor, strSeparator) != -1) {
			strAlias = StringUtils.substring(strCursor, 0, StringUtils.indexOf(strCursor, strSeparator));			
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("query is [" + strQuery + "]");
			logger.debug("[quote]" + strQuote + " [alias]" + strAlias);
		}
		
		String tableNamePattern = "((?:" + strQuote + "(?:[.[^" + strQuote + "]]+)" + strQuote + ")|(?:[\\w" + Pattern.quote(strSeparator) + "]+))";
		String structNamePattern;
		if (StringUtils.isEmpty(strAlias)) {
			structNamePattern = "(?:from|update|join|into)\\s*" + tableNamePattern;
		} else {
			structNamePattern = tableNamePattern + "(?:\\s*\\.\\s*" + tableNamePattern + ")?" + "\\s+(?:(?:AS)\\s)?" + strAlias + "[\\s,]+";
		}
		
		try {
			Pattern aliasPattern = Pattern.compile(structNamePattern, Pattern.CASE_INSENSITIVE);
			Matcher matcher = aliasPattern.matcher(strQuery);
			if(!matcher.find()) {
				if(logger.isDebugEnabled()) logger.debug("=====> Not found match");
				return "";
			}
			
			if(matcher.groupCount() > 0) {
				for(int i = 1; i <= matcher.groupCount(); i++) {
					listName.add(matcher.group(i));
				}	
			} else {
				if(logger.isDebugEnabled()) logger.debug("=====> Not found object name");
				return "";
			}
			
		} catch (PatternSyntaxException e) {
			if(logger.isDebugEnabled()) logger.error("=====> find pattern exception");
			return "";
		}
		
		// find object column
		StringBuffer strColumnList = new StringBuffer();
		for(String tblName : listName) {
			strColumnList.append(getAssistColumnList(userDB, tblName)).append(_PRE_GROUP);
		}
		return strColumnList.toString();
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
			TajoConnectionManager manager = new TajoConnectionManager();
			showTables = manager.tableList(userDB);			
		} else {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTables = sqlClient.queryForList("tableListOnlyName", userDB.getDb()); //$NON-NLS-1$			
		}
		
		/** filter 정보가 있으면 처리합니다. */
		return getTableAfterwork(showTables, userDB);
	}
	
	/**
	 * List of assist table column name
	 * 
	 * @param userDB
	 * @param tableName
	 * @return
	 */
	public String getAssistColumnList(final UserDBDAO userDB, final String tableName) {
		String strColumnlist = ""; //$NON-NLS-1$
		
		String strSchemaName = "";
		String strTableName = tableName;
		if(userDB.getDBDefine() != DBDefine.ALTIBASE_DEFAULT) {
			if(StringUtils.contains(tableName, '.')) {
				String[] arrTblInfo = StringUtils.split(tableName, ".");
				strSchemaName = arrTblInfo[0];
				strTableName = arrTblInfo[1];
			}
		}

		// 테이블 컬럼 리스트를 가져오기 위해 테이블 이름을 디비에서 저장되어 있는 대소문자 기준으로 가져옵니다. 
		TadpoleMetaData tadpoleMetaData = TadpoleSQLManager.getDbMetadata(userDB);
		if(tadpoleMetaData.getSTORE_TYPE() == TadpoleMetaData.STORES_FIELD_TYPE.LOWCASE_BLANK) {
			strSchemaName = StringUtils.upperCase(strSchemaName);
			strTableName = StringUtils.upperCase(strTableName);
		} else if(tadpoleMetaData.getSTORE_TYPE() == TadpoleMetaData.STORES_FIELD_TYPE.UPPERCASE_BLANK) {
			strSchemaName = StringUtils.lowerCase(strSchemaName);
			strTableName = StringUtils.lowerCase(strTableName);
		}
		
		try {
			TableDAO table = new TableDAO(strTableName, "");
			table.setSysName(strTableName);
			table.setSchema_name(strSchemaName);
			table.setName(strTableName);
			
			List<TableColumnDAO> showTableColumns = TadpoleObjectQuery.getTableColumns(userDB, table);
			for (TableColumnDAO tableDao : showTableColumns) {
				strColumnlist += tableDao.getSysName() + _PRE_DEFAULT + tableDao.getType() + _PRE_GROUP; //$NON-NLS-1$
			}
			strColumnlist = StringUtils.removeEnd(strColumnlist, _PRE_GROUP); //$NON-NLS-1$
		} catch(Exception e) {
			logger.error("MainEditor get the table column list", e); //$NON-NLS-1$
		}
		
		return strColumnlist;
	}
}
