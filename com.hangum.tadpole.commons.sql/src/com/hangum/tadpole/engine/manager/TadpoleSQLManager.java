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

import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.connections.TadpoleConnectionInfo;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.map.SQLMap;
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
	private static HashMap<String, SqlMapClient> dbManager = null;
	private static HashMap<String, TadpoleConnectionInfo> dbConnectionInfo = new HashMap<String, TadpoleConnectionInfo>();
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
		
		synchronized (dbManager) {
			
			Connection conn = null;
			try {
				
				String searchKey = getKey(dbInfo);
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
					
					sqlMapClient = SQLMap.getInstance(dbInfo);
					dbManager.put(searchKey, sqlMapClient);

					// connection의 부가 정보를 기록합니다.. 
					conn = sqlMapClient.getDataSource().getConnection();
					TadpoleConnectionInfo tci = new TadpoleConnectionInfo();
					
					
					tci.setIdentifierQuoteString(conn.getMetaData().getIdentifierQuoteString());
					dbConnectionInfo.put(searchKey, tci);
				}
				
			} catch(Exception e) {
				logger.error("get DB Instance", e);
				
				throw new Exception(e);
			} finally {
				try { if(conn != null) conn.close(); } catch(Exception e) {}
			}
		}

		return sqlMapClient;
	}
	
	/**
	 * 
	 * @param dbInfo
	 * @return
	 * @throws Exception
	 */
	public static TadpoleConnectionInfo getConnectionInfo(UserDBDAO dbInfo) throws Exception {
		return dbConnectionInfo.get(getKey(dbInfo));
	}
	
	/**
	 * 현재 연결된 Connection pool 정보를 리턴합니다.
	 * 
	 * @return
	 */
	public static HashMap<String, SqlMapClient> getDbManager() {
		return dbManager;
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
			
			dbConnectionInfo.remove(key);
		}
	}
	
	/**
	 * map의 카를 가져옵니다.
	 * @param dbInfo
	 * @return
	 */
	private static String getKey(UserDBDAO dbInfo) {
		return dbInfo.getSeq()  		+ PublicTadpoleDefine.DELIMITER + 
				dbInfo.getDbms_types()  + PublicTadpoleDefine.DELIMITER +
				dbInfo.getUrl()  		+ PublicTadpoleDefine.DELIMITER +
				dbInfo.getUsers()  		+ PublicTadpoleDefine.DELIMITER +
				dbInfo.getPasswd()  	+ PublicTadpoleDefine.DELIMITER;
	}
}
