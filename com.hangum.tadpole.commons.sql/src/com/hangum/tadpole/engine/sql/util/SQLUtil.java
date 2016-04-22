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
package com.hangum.tadpole.engine.sql.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.db.metadata.TadpoleMetaData;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableConstraintsDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * <pre>
 *  java.sql.ResultSet과 ResultSetMeta를 TableViewer로 바꾸기 위해 가공하는 Util
 *  
 *  resource데이터를 저장하기 위해 data를 배열화시킨다.
 * </pre>
 * 
 * @author hangum
 *
 */
public class SQLUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLUtil.class);
	
	/** REGEXP pattern flag */
	private static final int PATTERN_FLAG = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL;
	
	/**
	 * pattern statement 
	 * 
	 * <PRE>
	 * 		CHECK는 MYSQL의 CHECK TABLE VIEW_TABLE_NAME; 명령으로 VIEW의 정보를 볼수 있습니다.
	 * 		PRAGMA는 sqlite의 시스템 쿼리 얻는 거.
	 * </PRE>
	 */
	private static final String MSSQL_PATTERN_STATEMENT = "|^SP_HELP.*|^EXEC.*";
	private static final String ORACLE_PATTERN_STATEMENT = "";
	private static final String MYSQL_PATTERN_STATEMENT = "|^CALL.*";
	private static final String PGSQL_PATTERN_STATEMENT = "";
	private static final String SQLITE_PATTERN_STATEMENT = "";
	private static final String CUBRID_PATTERN_STATEMENT = "";
	
	private static final String BASE_PATTERN_STATEMENT = "^SELECT.*|^EXPLAIN.*|^SHOW.*|^DESCRIBE.*|^DESC.*|^CHECK.*|^PRAGMA.*|^WITH.*|^OPTIMIZE.*" 
							+ MSSQL_PATTERN_STATEMENT
							+ ORACLE_PATTERN_STATEMENT
							+ MYSQL_PATTERN_STATEMENT
							+ PGSQL_PATTERN_STATEMENT
							+ SQLITE_PATTERN_STATEMENT
							+ CUBRID_PATTERN_STATEMENT;
	private static final Pattern PATTERN_DML_BASIC = Pattern.compile(BASE_PATTERN_STATEMENT, PATTERN_FLAG);
	
	/** 허용되지 않는 sql 정의 */
	private static final String[] NOT_ALLOWED_SQL = {
		/* MSSQL- USE DATABASE명 */
//		"USE"
		};
	
	/**
	 * remove comment
	 * 
	 * @param strSQL
	 * @return
	 */
	public static String removeComment(String strSQL) {
		if(null == strSQL) return "";
		return strSQL.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?:--.*)", "");
	}
	
	/**
	 * 쿼리중에 허용하지 않는 쿼리 목록.
	 * 쿼리문 위에 주석을 빼야... -- / ** * / / * * /
	 * 
	 * @param strSQL
	 * @return
	 */
	public static boolean isNotAllowed(String strSQL) {
		boolean isRet = false;
		String cmpSql = StringUtils.trim(removeComment(strSQL));
		
		for (String strNAllSQL : NOT_ALLOWED_SQL) {
			if(StringUtils.startsWithIgnoreCase(cmpSql, strNAllSQL)) {
				return true;
			}
		}
		
		return isRet;
	}
	
	/**
	 * 쿼리의 패턴이 <code>PATTERN_STATEMENT</code>인지?
	 * 
	 * @param strSQL
	 * @return
	 */
	public static boolean isStatement(String strSQL) {
		strSQL = removeComment(strSQL);
		if((PATTERN_DML_BASIC.matcher(strSQL)).matches()) {
			return true;
//		} else {
//			try {
			//	// 영문일때만 검사하도록 합니다. 영문이 아닐 경우 무조건 false 입니다.
			//	// 검사를 하는 이유는 한글이 파서에 들어가면 무한루프돌면서 에디터 전체가 데드락으로 빠집니다.
//			//	//if(!isEnglish(strSQL)) return false;
//				
//				CCJSqlParserManager parserManager = new CCJSqlParserManager();
//				Statement statement = parserManager.parse(new StringReader(strSQL));
//				if(statement instanceof Select) return true;
//			} catch(Exception e) {
//				logger.error("SQL Parser Exception.\n sql is [" + strSQL + "]");
//			}
//			return false;
		}
		
		return false;
	}
	
	/**
	 * sql 관련 없는 모든 코드를 삭제한다.
	 * 
	 * @param userDB
	 * @param exeSQL
	 * @return
	 */
	public static String removeCommentAndOthers(UserDBDAO userDB, String exeSQL) {
		exeSQL = StringUtils.trimToEmpty(exeSQL);
		exeSQL = removeComment(exeSQL);
		exeSQL = StringUtils.trimToEmpty(exeSQL);
		exeSQL = StringUtils.removeEnd(exeSQL, "/");
		exeSQL = StringUtils.trimToEmpty(exeSQL);
		//TO DO 오라클 프로시저등의 오브젝트는 마지막 딜리미터(;)가 없으면 오류입니다. 하여서 이 코드는 문제입니다.
		exeSQL = StringUtils.removeEnd(exeSQL, PublicTadpoleDefine.SQL_DELIMITER);
		
		return exeSQL;
	}
	
	/**
	 * 쿼리를 jdbc에서 실행 가능한 쿼리로 보정합니다.
	 * 
	 * @param userDB
	 * @param exeSQL
	 * @return
	 */
	public static String makeExecutableSQL(UserDBDAO userDB, String exeSQL) {
		
//		tmpStrSelText = UnicodeUtils.getUnicode(tmpStrSelText);
			
//			https://github.com/hangum/TadpoleForDBTools/issues/140 오류로 불럭지정하였습니다.
//			TO DO 특정 쿼리에서는 주석이 있으면 오류인데..DB에서 쿼리를 실행받는 다양한 조건을 고려할 필요가 있습니다. 
		
		// 문장 의 // 뒤에를 주석으로 인식 쿼리열에서 제외합니다.
		/*
		 *  mysql의 경우 주석문자 즉, -- 바로 다음 문자가 --와 붙어 있으면 주석으로 인식하지 않아 오류가 발생합니다. --comment 이면 주석으로 인식하지 않습니다.(다른 디비(mssql, oralce, pgsql)은 주석으로 인식합니다)
		 *  고칠가 고민하지만, 실제 쿼리에서도 동일하게 오류로 처리할 것이기에 주석을 지우지 않고 놔둡니다. - 2013.11.11- (hangum)
		 */

		exeSQL = StringUtils.trimToEmpty(exeSQL);

		// 주석제거.
		// oracle, tibero, altibase은 힌트가 주석 문법을 쓰므로 주석을 삭제하지 않는다.
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT | 
				userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT |
				userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT
		) {
			// ignore code
		} else {
			exeSQL = removeComment(exeSQL);
		}
		exeSQL = StringUtils.trimToEmpty(exeSQL);
		exeSQL = StringUtils.removeEnd(exeSQL, "/");
		exeSQL = StringUtils.trimToEmpty(exeSQL);
		//TO DO 오라클 프로시저등의 오브젝트는 마지막 딜리미터(;)가 없으면 오류입니다. 하여서 이 코드는 문제입니다.
		exeSQL = StringUtils.removeEnd(exeSQL, PublicTadpoleDefine.SQL_DELIMITER);
		
		return exeSQL;
	}
	
	/**
	 * 쿼리에 사용 할 Table, column name을 만듭니다.
	 * 
	 * @param userDB
	 * @param name
	 * @return
	 */
	public static String makeIdentifierName(UserDBDAO userDB, String name) {
		boolean isChanged = false;
		String retStr = name;
		TadpoleMetaData tmd = TadpoleSQLManager.getDbMetadata(userDB);
		
		if(tmd == null) return retStr;

		// mssql일 경우 시스템 테이블 스키서부터 "가 붙여 있는 경우 "가 있으면 []을 양쪽에 붙여 줍니다. --;; 
		if(userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT || userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT) {
			if(StringUtils.contains(name, "\"")) {
				return name = String.format("[%s]", name);
			}
		}
		
		switch(tmd.getSTORE_TYPE()) {
//		case NONE: 
//			retStr = tableName;
//			break;
		case BLANK: 
			if(name.matches(".*\\s.*")) {
				isChanged = true;
				retStr = makeFullyTableName(name, tmd.getIdentifierQuoteString());
			}
			break;
		case LOWCASE_BLANK:
			if(name.matches(".*[a-z\\s].*")) {
				isChanged = true;
				retStr = makeFullyTableName(name, tmd.getIdentifierQuoteString());
			}
			break;
		case UPPERCASE_BLANK:
			if(name.matches(".*[A-Z\\s].*")) {
				isChanged = true;
				retStr = makeFullyTableName(name, tmd.getIdentifierQuoteString());
			}
			break;
		}
		
		// Is keywords?
		// schema.tableName
		if(!isChanged) {
			String[] arryRetStr = StringUtils.split(retStr, ".");
			if(arryRetStr.length == 1) {
				if(StringUtils.containsIgnoreCase(","+tmd.getKeywords()+",", ","+arryRetStr[0]+",")) {
					retStr = tmd.getIdentifierQuoteString() + retStr + tmd.getIdentifierQuoteString();
				}
			} else if(arryRetStr.length > 1){
				if(StringUtils.containsIgnoreCase(","+tmd.getKeywords()+",", ","+arryRetStr[1]+",")) {
					retStr = tmd.getIdentifierQuoteString() + retStr + tmd.getIdentifierQuoteString();
				}
			}
		}
//		if(logger.isDebugEnabled()) logger.debug("[tmd.getSTORE_TYPE()]" + tmd.getSTORE_TYPE() + "[original]" + tableName + "[retStr = ]" + retStr);
		return retStr;
	}
	
	private static String makeFullyTableName(String tableName, String strIdentifier) {
		String retStr = "";
		
		for(String chunk : StringUtils.split(tableName, '.')) {
			retStr += strIdentifier + chunk + strIdentifier + ".";
		}
		retStr = StringUtils.removeEnd(retStr, ".");
		
		return retStr;
	}
	
	/**
	 * db resource data를 저장할때 2000byte 단위로 저장하도록 합니다.
	 * 
	 * @param resource data
	 * @return
	 */
	public static String[] makeResourceDataArays(String resourceContent)  {
		int cutsize = 1998;
		String[] tmpRetArryStr = new String[2000];
		resourceContent = resourceContent == null ? "" : resourceContent;
		byte[] byteSqlText = resourceContent.getBytes();
		
		int isEndTextHangul = 0;
		int workCnt = 0;

		while (byteSqlText.length > cutsize) {
			isEndTextHangul = 0;
			for (int i=0; i<cutsize; i++) {
				if (byteSqlText[i] < 0) isEndTextHangul++;
			}

			if (isEndTextHangul%2 != 0) {
				tmpRetArryStr[workCnt] = new String(byteSqlText, 0, cutsize + 1);
				byteSqlText = new String(byteSqlText, cutsize + 1, byteSqlText.length - (cutsize + 1)).getBytes();
			} else {
				tmpRetArryStr[workCnt] = new String(byteSqlText, 0, cutsize);
				byteSqlText = new String(byteSqlText, cutsize, byteSqlText.length - cutsize).getBytes();
			}

			workCnt++;
		}
		tmpRetArryStr[workCnt] = new String(byteSqlText);
		
		// 결과가 있는 만큼 담기위해
		String[] returnDataArry = new String[workCnt+1];
		for (int i=0; i<=workCnt; i++) {
			returnDataArry[i] = tmpRetArryStr[i];
		}
		
		return returnDataArry;
	}
	
	/**
	 * 에디터에서 쿼리 실행 단위 조절.
	 * 
	 * https://github.com/hangum/TadpoleForDBTools/issues/466
	 * 
	 * @param dbAction
	 * @return
	 */
	public static boolean isSELECTEditor(OBJECT_TYPE dbAction) {
		if(dbAction == OBJECT_TYPE.TABLES ||
				dbAction == OBJECT_TYPE.VIEWS ||
				dbAction == OBJECT_TYPE.SYNONYM ||
				dbAction == OBJECT_TYPE.INDEXES) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * sql of query type
	 * 
	 * @param sql
	 * @return query type
	 */
	public static PublicTadpoleDefine.QUERY_DML_TYPE sqlQueryType(String sql) {
		PublicTadpoleDefine.QUERY_DML_TYPE queryType = PublicTadpoleDefine.QUERY_DML_TYPE.UNKNOWN;
		
		try {
			Statement statement = CCJSqlParserUtil.parse(sql);
			if(statement instanceof Select) {
				queryType = PublicTadpoleDefine.QUERY_DML_TYPE.SELECT;
			} else if(statement instanceof Insert) {
				queryType = PublicTadpoleDefine.QUERY_DML_TYPE.INSERT;
			} else if(statement instanceof Update) {
				queryType = PublicTadpoleDefine.QUERY_DML_TYPE.UPDATE;
			} else if(statement instanceof Delete) {
				queryType = PublicTadpoleDefine.QUERY_DML_TYPE.DELETE;
//			} else {
//				queryType = PublicTadpoleDefine.QUERY_DML_TYPE.DDL;
			}
			
		} catch (Throwable e) {
			logger.error(String.format("sql parse exception. [ %s ]", sql));
			queryType = PublicTadpoleDefine.QUERY_DML_TYPE.UNKNOWN;
		}
		
		return queryType;
	}
	
	/**
	 * make quote mark
	 * 
	 * @param value
	 * @return
	 */
	public static String makeQuote(String value) {
		return String.format("'%s'", StringEscapeUtils.escapeSql(value));
	}
	
	/**
	 * index name
	 * @param tc
	 * @return
	 */
	public static String getIndexName(InformationSchemaDAO tc) {
		if("".equals(tc.getSchema_name()) | null == tc.getSchema_name()) return tc.getTABLE_NAME();
		else return String.format("%s.%s", tc.getSchema_name(), tc.getTABLE_NAME());
	}
	
	/**
	 * constraint name
	 * @param tc
	 * @return
	 */
	public static String getConstraintName(TableConstraintsDAO tc) {
		if("".equals(tc.getSchema_name()) | null == tc.getSchema_name()) return tc.getTABLE_NAME();
		else return String.format("%s.%s", tc.getSchema_name(), tc.getTABLE_NAME());
	}
	
	/**
	 * get procedure name
	 * 
	 * @param tc
	 * @return
	 */
	public static String getProcedureName(ProcedureFunctionDAO tc) {
		if("".equals(tc.getSchema_name()) | null == tc.getSchema_name()) return tc.getName();
		else return String.format("%s.%s", tc.getSchema_name(), tc.getName());
	}
	
	/**
	 * Table name
	 * @param userDB 
	 * @param tableDAO
	 * @return
	 */
	public static String getTableName(UserDBDAO userDB, TableDAO tableDAO) {
		if("".equals(tableDAO.getSchema_name()) || null == tableDAO.getSchema_name()) return tableDAO.getSysName(); //$NON-NLS-2$
		
		return tableDAO.getSchema_name() + "." + tableDAO.getSysName(); //$NON-NLS-2$
	}
	
}
