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
package com.hangum.tadpole.rdb.erd.core.relation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ReferencedTableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * cubrid db의 table relation 정보 생성
 * 
 * http://www.cubrid.org/questions/317560 참고.
 * 
 * @author hangum
 *
 */
public class CubridTableRelation {
	private static final Logger logger = Logger.getLogger(CubridTableRelation.class);
	
	/**
	 * 테이블 relation 정보 생성
	 * 
	 * @param userDB
	 * @return
	 */
	public static List<ReferencedTableDAO> makeCubridRelation(UserDBDAO userDB, String tableName) throws Exception {
		List<ReferencedTableDAO> listRealRefTableDAO = new ArrayList<ReferencedTableDAO>();
		
		Connection conn = null;
		ResultSet rs = null;

		String[] tableNames = StringUtils.split(tableName, ',');
		for (String table : tableNames) {
			table = StringUtils.replace(table, "'", "");
			
			try {
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
				conn = sqlClient.getDataSource().getConnection();
				
				rs = conn.getMetaData().getImportedKeys("", "", table);
				while (rs.next()) {
		            
		            ReferencedTableDAO ref = new ReferencedTableDAO();
		            if(rs.getString("FK_NAME") == null || "".equals(rs.getString("FK_NAME"))) ref.setConstraint_name(rs.getString("FK_NAME"));
		            else ref.setConstraint_name(rs.getString("fk_name"));
		            
					// Table names
					if(rs.getString("FKTABLE_NAME") == null || "".equals(rs.getString("FKTABLE_NAME"))) ref.setTable_name(rs.getString("FKTABLE_NAME"));
					else ref.setTable_name(rs.getString("fktable_name"));
					
					if(rs.getString("FKCOLUMN_NAME") == null || "".equals(rs.getString("FKCOLUMN_NAME"))) ref.setColumn_name(rs.getString("FKCOLUMN_NAME"));
					else ref.setColumn_name(rs.getString("fkcolumn_name"));
					
					if(rs.getString("PKTABLE_NAME") == null || "".equals(rs.getString("PKTABLE_NAME"))) ref.setReferenced_table_name(rs.getString("PKTABLE_NAME"));
					else ref.setReferenced_table_name(rs.getString("pktable_name"));
					
					if(rs.getString("PKCOLUMN_NAME") == null || "".equals(rs.getString("PKCOLUMN_NAME"))) ref.setReferenced_column_name(rs.getString("PKCOLUMN_NAME"));
					else ref.setReferenced_column_name(rs.getString("pkcolumn_name"));
					
					if(logger.isDebugEnabled()) logger.debug(ref.toString());
					
					listRealRefTableDAO.add(ref);
		        }				
		        
			} catch (Exception e) {
				logger.error("cubrid relation", e);
				throw new Exception("Cubrid relation exception " +  e.getMessage());
			} finally {
				if(rs != null) try { rs.close(); } catch(Exception e) {}
				if(conn != null) try { conn.close(); } catch(Exception e) {}
			}
			
		}	// end last for
		
		return listRealRefTableDAO;
	}
	
	/**
	 * 모든 테이블 relation 정보 생성
	 * 
	 * @param userDB
	 * @return
	 */
	public static List<ReferencedTableDAO> makeCubridRelation(UserDBDAO userDB) throws Exception {
		List<ReferencedTableDAO> listRealRefTableDAO = new ArrayList<ReferencedTableDAO>();
		
		// 모든 테이블 목록을 가져옵니다.		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
			
			String tables = "";
			for (Object table : showTables) {
				tables += String.format("'%s',", table);
			}
			if("".equals(tables)) return listRealRefTableDAO;
			
			return makeCubridRelation(userDB, StringUtils.removeEnd(tables, "'"));			
		} catch(Exception e) {
			logger.error("cubrid all table", e);
			throw new Exception("Cubrid relation exception " +  e.getMessage());
		}
	}

}
