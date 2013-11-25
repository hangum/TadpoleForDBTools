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

import java.util.HashMap;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
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
	private static TadpoleSQLManager tadpoleSQLManager = null;
	
	static {
		if(tadpoleSQLManager == null) {
			tadpoleSQLManager = new TadpoleSQLManager();
			dbManager = new HashMap<String, SqlMapClient>();
		} 
	}
	
	private TadpoleSQLManager() {}
	
	/**
	 * DB 정보를 생성한다.
	 * 
	 * @param dbInfo
	 * @return
	 * @throws Exception
	 */
	public static SqlMapClient getInstance(UserDBDAO dbInfo) throws Exception {
		SqlMapClient sqlMapClient = null;
		
		synchronized (dbManager) {
			try {
				
				String searchKey = getKey(dbInfo);
				sqlMapClient = dbManager.get( searchKey );
				if(sqlMapClient == null) {

					// oracle 일 경우 로케일을 설정합니다.
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
				}
				
			} catch(Exception e) {
				logger.error("get DB Instance", e);
				
				throw new Exception(e);
			}
		}

		return sqlMapClient;
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
			SqlMapClient sqlMapClient = dbManager.remove(getKey(dbInfo));
			sqlMapClient = null;
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
