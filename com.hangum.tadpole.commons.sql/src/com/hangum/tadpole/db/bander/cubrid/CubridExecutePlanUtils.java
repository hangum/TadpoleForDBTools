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
package com.hangum.tadpole.db.bander.cubrid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;

import cubrid.jdbc.driver.CUBRIDStatement;

/**
 * cubrid execute plan
 * 
 * <pre>
 *  쿼리 실행시 / *+ recompile * / 를 붙여야합니다.
 *  
 *  자세한 정보는 다음을 참조합니다.
 * 	q&a : http://www.cubrid.com/zbxe/bbs_developer_qa/346565
 * </pre>
 * 
 * @author hangum
 *
 */
public class CubridExecutePlanUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CubridExecutePlanUtils.class);
	
	/** 쿼리 플랜을 수행하기 위한 옵션 */
	private static final String RECOMPILE = " /*+ recompile */ ";
	
	/**
	 * cubrid execute plan
	 * 
	 * @param userDB
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static String plan(UserDBDAO userDB, String sql) throws Exception {
		if(!sql.toLowerCase().startsWith("select")) {
			logger.error("[cubrid execute plan ]" + sql);
			throw new Exception ("This statment not select. please check.");
		}
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
//			Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
//			conn = DriverManager.getConnection(userDB.getUrl(), userDB.getUsers(), userDB.getPasswd());
//			conn.setAutoCommit(false); // 플랜 정보를 가져오기 위해서는 auto commit을 false로 설정해야 함.
			conn = TadpoleSQLManager.getInstance(userDB).getDataSource().getConnection();
			conn.setAutoCommit(false); // 플랜 정보를 가져오기 위해서는 auto commit을 false로 설정해야 함.

			sql = StringUtils.trim(sql).substring(6);
			if(logger.isDebugEnabled()) logger.debug("[qubrid modifying query]" + sql);
			sql = "select " + RECOMPILE + sql;				

			pstmt = conn.prepareStatement(sql);
			((CUBRIDStatement) pstmt).setQueryInfo(true);
			rs = pstmt.executeQuery();
			
			String plan = ((CUBRIDStatement) pstmt).getQueryplan(); // 수행한 질의 플랜 정보를 가져오는 메소드.
//			conn.commit();
			
			if(logger.isDebugEnabled()) logger.debug("cubrid plan text : " + plan);
			
			return plan;
			
		} finally {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		}
	}
	
	/**
	 * cubrid plan은 text로 결과가 넘오 와서 올챙이 테이블 <헤더> 만들어 줍니다.
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> getMapColumns() {
		HashMap<Integer, String> mapColumns = new HashMap<Integer, String>();
		mapColumns.put(0, "Query Plan");
		
		return mapColumns;
	}
	
	/**
	 * cubrid plan은 text로 결과가 넘오 와서 올챙이 테이블 <데이터>로 만들어 줍니다.
	 * 
	 * @param data
	 * @return
	 */
	public static TadpoleResultSet getMakeData(String data) {
		List<Map<Integer, Object>> sourceDataList = new ArrayList<Map<Integer, Object>>();
		
		HashMap<Integer, Object> tmpRs = new HashMap<Integer, Object>();
		tmpRs.put(0, data);		
		sourceDataList.add(tmpRs);

		TadpoleResultSet trs = new TadpoleResultSet(sourceDataList);
		return trs;
	}
}
