package com.hangum.tadpole.engine.sql.util.dbms;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.executer.ProcedureExecuterManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * mysql, marai utils
 * 
 * @author hangum
 *
 */
public class MySQLUtils {
	private static final Logger logger = Logger.getLogger(MySQLUtils.class);
	
	/**
	 * get db collation
	 * @param userDB
	 * @return
	 */
	public static String[] getCollation(UserDBDAO userDB) {
		String strSQL = "SHOW COLLATION";
		
		ResultSet resultSet = null;
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		List<String> listCollation = new ArrayList<String>();
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			statement = javaConn.createStatement();
			
			resultSet = statement.executeQuery(strSQL);
			while(resultSet.next()) listCollation.add(resultSet.getString("Collation"));
		} catch(Exception e) {
			logger.error("mysql collatioon", e);
		} finally {
			if(resultSet != null) try { resultSet.close(); } catch(Exception e) {}
			if(statement != null) try { statement.close(); } catch(Exception e) {}
			if(javaConn != null) try { javaConn.close(); } catch(Exception e) {}
		}
		
		return listCollation.toArray(new String[listCollation.size()]);
	}
}
