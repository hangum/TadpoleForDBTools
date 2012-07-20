package com.hangum.db.system;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.rap.commons.session.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 올챙이 시스템의 사용자 데이터를 정의 합니다.
 * 프리퍼런스 데이터를 저장합니다.
 * 
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserInfoData {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSystem_UserInfoData.class);

	/**
	 * 모든 세션 정보를 가져옵니다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List allUserInfoData() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		return sqlClient.queryForList("allUserInfoData", SessionManager.getSeq()); //$NON-NLS-1$
	}
	
}
 