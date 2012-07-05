package com.hangum.db.system;

import java.sql.Statement;

import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * tadpole system에서 공통으로 사용하는 모듈
 * 
 * @author hangum
 *
 */
public class TadpoleSystemCommons {

	/**
	 * smtm.execute문의 쿼리를 날립니다. 즉 select 이외의 문...
	 * 
	 * @param selText
	 */
	public static void executSQL(UserDBDAO userDB, String selText) throws Exception {
		java.sql.Connection javaConn = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			Statement stmt = javaConn.createStatement();
			boolean result =  stmt.execute( selText );
	
		} finally {
			try { javaConn.close(); } catch(Exception e){
				// igonr exception
			}
		}		
	}
}
