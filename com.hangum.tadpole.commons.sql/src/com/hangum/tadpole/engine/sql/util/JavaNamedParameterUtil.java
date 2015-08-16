package com.hangum.tadpole.engine.sql.util;

import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

public class JavaNamedParameterUtil {
	private static final Logger logger = Logger.getLogger(JavaNamedParameterUtil.class);
	
	/**
	 * count of sql parameter 
	 * 
	 * @param userDB
	 * @param executeQuery
	 * @return
	 * @throws Exception
	 */
	public int calcParamCount(final UserDBDAO userDB, String executeQuery) throws Exception {
		int paramCount = 0;

		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			stmt = javaConn.prepareStatement(executeQuery);
			java.sql.ParameterMetaData pmd = stmt.getParameterMetaData();
			if(pmd != null) {
				paramCount = pmd.getParameterCount();	
			} else {
				paramCount = 0;
			}
			
		} catch (Exception e) {
			logger.error("Count parameter error", e);
			paramCount = 0;
			throw e;
		} finally {
			try {
				if(stmt != null) stmt.close();
			} catch (Exception e) {}
			
			try {
				if(javaConn != null) javaConn.close();
			} catch (Exception e) {}
		}
		
		return paramCount;
	}
}
