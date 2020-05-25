package com.tadpolehub.db.cassandra.core.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Apache Cassandra connection utils
 * 
 * @author hangum
 *
 */
public class ApacheCassandraUtils {
	private static final Logger logger = Logger.getLogger(ApacheCassandraUtils.class);

	/**
	 * connection check
	 * 
	 * @param userDB
	 * @return
	 * @throws SQLException
	 */
	public static boolean connectionCheck(UserDBDAO userDB) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		
		try {
			con = TadpoleSQLManager.getConnection(userDB);
			stmt = con.createStatement();
			stmt.executeQuery("SELECT * FROM system_schema.tables limit 1");
		
		} catch (Exception e) {
			logger.error("Apache cassandra conection check", e);
			throw new SQLException("Apache cassandra doesn't connection.  Please check database information.\n" + e.getMessage());
		} finally {
			if(stmt != null) try { stmt.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return true;
	}
	
	/**
	 * keyspace list
	 * 
	 * @param userDB
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getSchemas(final UserDBDAO userDB) throws Exception {
		List<String> listSchema = new ArrayList<String>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = TadpoleSQLManager.getConnection(userDB);
			stmt = con.createStatement();

			String strSQL = String.format("SELECT * FROM system_schema.keyspaces");
			if(logger.isDebugEnabled()) logger.debug(strSQL);
			rs = stmt.executeQuery(strSQL);
			while(rs.next()) {
				listSchema.add(rs.getString("keyspace_name"));
			}
			
		} catch (Exception e) {
			logger.error("Apache cassandra conection check", e);
			throw e;
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(stmt != null) try { stmt.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return listSchema;
	}
	
	/**
	 * Tables list
	 * 
	 * @param userDB
	 * @return
	 * @throws SQLException
	 */
	public static List<TableDAO> getTables(final UserDBDAO userDB) throws Exception {
		List<TableDAO> showTables = new ArrayList<TableDAO>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = TadpoleSQLManager.getConnection(userDB);
			stmt = con.createStatement();

			String strSQL = String.format("SELECT table_name, comment FROM system_schema.tables WHERE keyspace_name = '%s'", userDB.getDefaultSchemanName());
			if(logger.isDebugEnabled()) logger.debug(strSQL);
			rs = stmt.executeQuery(strSQL);
			while(rs.next()) {
				showTables.add(new TableDAO(rs.getString("table_name"), rs.getString("comment")));
			}
			
		} catch (Exception e) {
			logger.error("Apache cassandra conection check", e);
			throw e;
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(stmt != null) try { stmt.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return showTables;
	}
	
	/**
	 * Table column list
	 * 
	 * @param userDB
	 * @param strTableName
	 * @return
	 * @throws SQLException
	 */
	public static List<TableColumnDAO> getTableColumns(final UserDBDAO userDB, String strTableName) throws Exception {
		return getTableColumns(userDB, userDB.getDefaultSchemanName(), strTableName);
	}
	
	/**
	 * get table column
	 * 
	 * @param userDB
	 * @param strSchemaName
	 * @param strTableName
	 * @return
	 * @throws Exception
	 */
	public static List<TableColumnDAO> getTableColumns(final UserDBDAO userDB, String strSchemaName, String strTableName) throws Exception {
		List<TableColumnDAO> showTableColumn = new ArrayList<TableColumnDAO>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = TadpoleSQLManager.getConnection(userDB);
			stmt = con.createStatement();

			String strSQL = String.format("SELECT * FROM system_schema.columns WHERE keyspace_name = '%s' AND table_name = '%s'", strSchemaName, strTableName);
			if(logger.isDebugEnabled()) logger.debug(strSQL);
			rs = stmt.executeQuery(strSQL);
			while(rs.next()) {
				TableColumnDAO dao = new TableColumnDAO();
				dao.setName(rs.getString("column_name"));
				dao.setType(rs.getString("type"));
				dao.setKey(rs.getString("kind"));
				
				showTableColumn.add(dao);
			}
			
		} catch (Exception e) {
			logger.error("Apache cassandra conection check", e);
			throw e;
		} finally {
			if(rs != null) try { rs.close(); } catch(Exception e) {}
			if(stmt != null) try { stmt.close(); } catch(Exception e) {}
			if(con != null) try { con.close(); } catch(Exception e) {}
		}
		
		return showTableColumn;
	}
}
