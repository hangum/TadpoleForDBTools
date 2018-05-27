package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.util.List;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.SQLTemplateDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * SQL templcate 
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_SQLTemplate {
	
	/**
	 * list public sql template
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<SQLTemplateDAO> listPublicSQLTemplate() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("listPublicSQLTemplate");
	}
	
	/**
	 * list private sql template
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<SQLTemplateDAO> listPrivateSQLTemplate() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return sqlClient.queryForList("listPrivateSQLTemplate", SessionManager.getUserSeq());
	}
	
	/**
	 * insert sql templcate
	 * @param dao
	 */
	public static void insertSQLTemplate(SQLTemplateDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.insert("insertSQLTemplate", dao);
	}

	/**
	 * update sql template
	 * 
	 * @param dao
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static void updateSQLTemplate(SQLTemplateDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.insert("updataeSQLTemplate", dao);
	}
	
	/**
	 * delete sql template
	 * 
	 * @param dao
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static void deleteSQLTemplate(SQLTemplateDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.delete("deleteSQLTemplate", dao);
	}

}
