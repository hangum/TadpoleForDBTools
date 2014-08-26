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
package com.hangum.tadpole.engine.manager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.db.metadata.TadpoleMetaData;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.internal.map.SQLMap;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * DB Manager에서 관리하는 디비에 관해서 정의한다.
 * 
 * @author hangum
 *
 */
public class TadpoleSQLManager {
	private static final Logger logger = Logger.getLogger(TadpoleSQLManager.class);
	
	/** db 인스턴스를 가지고 있는 아이 */
	private static Map<String, SqlMapClient> dbManager = null;
	private static Map<String, TadpoleMetaData> dbMetadata = new HashMap<String, TadpoleMetaData>();
	
	private static TadpoleSQLManager tadpoleSQLManager = null;
	
	static {
		if(tadpoleSQLManager == null) {
			tadpoleSQLManager = new TadpoleSQLManager();
			dbManager = new HashMap<String, SqlMapClient>();
		} 
	}
	
	private TadpoleSQLManager() {}
	
	/**
	 * <pre>
	 * DB 정보를 생성한다.
	 * 
	 * 엔진환경에서 가지고 있어야 하는 것으로서 데이터베이스 부가정보를 가지고 있어야 할듯합니다.
	 * 	테이블의 "나 '가 대소문자 유무등을 환경정보로가지고 있어야겟습니다.
	 * 
	 * </pre>
	 * 
	 * @param dbInfo
	 * @return
	 * @throws Exception
	 */
	public static SqlMapClient getInstance(UserDBDAO dbInfo) throws Exception {
		SqlMapClient sqlMapClient = null;
		Connection conn = null;
		
		synchronized (dbManager) {
			String searchKey = getKey(dbInfo);
			try {
				sqlMapClient = dbManager.get( searchKey );
				if(sqlMapClient == null) {

					// oracle 일 경우 로케일을 설정 
					try { 
						if(DBDefine.getDBDefine(dbInfo) == DBDefine.ORACLE_DEFAULT) {
							if(dbInfo.getLocale() != null && !"".equals(dbInfo.getLocale())) {
								Locale.setDefault(new Locale(dbInfo.getLocale()));
							}
						}
					} catch(Exception e) {
						logger.error("set locale error", e);
					}
					// oracle 일 경우 locale 설정 
					
					// connection pool 을 가져옵니다.
					sqlMapClient = SQLMap.getInstance(dbInfo);
					dbManager.put(searchKey, sqlMapClient);
					
					// metadata를 가져와서 저장해 놓습니다.
					conn = sqlMapClient.getDataSource().getConnection(); 
					setMetaData(searchKey, dbInfo, conn.getMetaData().getSQLKeywords());
				}
				
			} catch(Exception e) {
				logger.error("get DB Instance", e);
				
				dbManager.remove(searchKey);
				
				throw new Exception(e);
			} finally {
				if(conn != null) try {conn.close();} catch(Exception e) {}
			}
		}

		return sqlMapClient;
	}

	/**
	 * 전체 connection pool 정보를 가져옵니다.
	 */
	public static void getConnectionPoolStatus() {
		SqlMapClient[] sqlMaps = (SqlMapClient[])dbManager.values().toArray();
		for (SqlMapClient sqlMapClient : sqlMaps) {

			BasicDataSource basicDataSource = (BasicDataSource)sqlMapClient.getDataSource();
			logger.info("NumActive 	: " + basicDataSource.getNumActive());
			logger.info("NumIdle 	: " + basicDataSource.getNumIdle());
		}
	}
	
	/**
	 * 각 DB의 metadata를 넘겨줍니다.
	 * 
	 * @return
	 */
	public static void setMetaData(String searchKey, final UserDBDAO dbInfo, String sqlKeywords) throws Exception {
		TadpoleMetaData tmd = null;
		
		// https://github.com/hangum/TadpoleForDBTools/issues/412 디비의 메타데이터가 틀려서 설정하였습니다. 
		switch ( dbInfo.getDBDefine() ) {
			case ORACLE_DEFAULT:		
				tmd = new TadpoleMetaData("\"", TadpoleMetaData.STORES_FIELD_TYPE.LOWCASE_BLANK);
				break;
			case MSSQL_DEFAULT:			
			case MSSQL_8_LE_DEFAULT:	
			case SQLite_DEFAULT:		
				tmd = new TadpoleMetaData("\"", TadpoleMetaData.STORES_FIELD_TYPE.BLANK);
				break;
			case POSTGRE_DEFAULT:		
			case TAJO_DEFAULT: 			
				tmd = new TadpoleMetaData("\"", TadpoleMetaData.STORES_FIELD_TYPE.UPPERCASE_BLANK);
				break;
			default:
				tmd = new TadpoleMetaData("'", TadpoleMetaData.STORES_FIELD_TYPE.NONE);
		}

		// set keyword
		if(dbInfo.getDBDefine() == DBDefine.SQLite_DEFAULT) {
			// not support keyword http://sqlite.org/lang_keywords.html
			tmd.setKeywords("BORT,ACTION,ADD,AFTER,ALL,ALTER,ANALYZE,AND,AS,ASC,ATTACH,AUTOINCREMENT,BEFORE,BEGIN,BETWEEN,BY,CASCADE,CASE,CAST,CHECK,COLLATE,COLUMN,COMMIT,CONFLICT,CONSTRAINT,CREATE,CROSS,CURRENT_DATE,CURRENT_TIME,CURRENT_TIMESTAMP,DATABASE,DEFAULT,DEFERRABLE,DEFERRED,DELETE,DESC,DETACH,DISTINCT,DROP,EACH,ELSE,END,ESCAPE,EXCEPT,EXCLUSIVE,EXISTS,EXPLAIN,FAIL,FOR,FOREIGN,FROM,FULL,GLOB,GROUP,HAVING,IF,IGNORE,IMMEDIATE,IN,INDEX,INDEXED,INITIALLY,INNER,INSERT,INSTEAD,INTERSECT,INTO,IS,ISNULL,JOIN,KEY,LEFT,LIKE,LIMIT,MATCH,NATURAL,NO,NOT,NOTNULL,NULL,OF,OFFSET,ON,OR,ORDER,OUTER,PLAN,PRAGMA,PRIMARY,QUERY,RAISE,RECURSIVE,REFERENCES,REGEXP,REINDEX,RELEASE,RENAME,REPLACE,RESTRICT,RIGHT,ROLLBACK,ROW,SAVEPOINT,SELECT,SET,TABLE,TEMP,TEMPORARY,THEN,TO,TRANSACTION,TRIGGER,UNION,UNIQUE,UPDATE,USING,VACUUM,VALUES,VIEW,VIRTUAL,WHEN,WHERE,WITH,WITHOU");
		} else if(dbInfo.getDBDefine() == DBDefine.MONGODB_DEFAULT) {
			// not support this method
		} else if(dbInfo.getDBDefine() == DBDefine.HIVE_DEFAULT ||
				dbInfo.getDBDefine() == DBDefine.HIVE2_DEFAULT
		) {
			// not support this methods
			tmd.setKeywords("");
		} else {
			tmd.setKeywords(sqlKeywords);
		}
		
		dbMetadata.put(searchKey, tmd);
	}
	
	/**
	 * 현재 연결된 Connection pool 정보를 리턴합니다.
	 * 
	 * @return
	 */
	public static Map<String, SqlMapClient> getDbManager() {
		return dbManager;
	}
	public static Map<String, TadpoleMetaData> getDbMetadata() {
		return dbMetadata;
	}
	
	/**
	 * DBMetadata
	 * @return
	 */
	public static TadpoleMetaData getDbMetadata(final UserDBDAO dbInfo) {
		return dbMetadata.get(getKey(dbInfo));
	}
	
	/**
	 * DB 정보를 삭제한다.
	 * 
	 * @param dbInfo
	 */
	public static void removeInstance(UserDBDAO dbInfo) {
		synchronized (dbManager) {
			String key = getKey(dbInfo);
			SqlMapClient sqlMapClient = dbManager.remove(key);
			sqlMapClient = null;
		}
	}
	
	/**
	 * map의 카를 가져옵니다.
	 * @param dbInfo
	 * @return
	 */
	public static String getKey(UserDBDAO dbInfo) {
		return dbInfo.getSeq()  		+ PublicTadpoleDefine.DELIMITER + 
				dbInfo.getDbms_types()  + PublicTadpoleDefine.DELIMITER +
				dbInfo.getUrl()  		+ PublicTadpoleDefine.DELIMITER +
				dbInfo.getUsers()  		+ PublicTadpoleDefine.DELIMITER +
				dbInfo.getPasswd()  	+ PublicTadpoleDefine.DELIMITER;
	}
}
