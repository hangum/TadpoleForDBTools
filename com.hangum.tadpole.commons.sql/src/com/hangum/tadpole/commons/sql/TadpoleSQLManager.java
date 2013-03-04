/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.sql;

import java.util.HashMap;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.commons.sql.map.SQLMap;
import com.hangum.tadpole.dao.system.UserDBDAO;
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
						if(DBDefine.getDBDefine(dbInfo.getTypes()) == DBDefine.ORACLE_DEFAULT) {
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
		return dbInfo.getTypes()+dbInfo.getUrl()+dbInfo.getUsers()+dbInfo.getPasswd();
	}
}
