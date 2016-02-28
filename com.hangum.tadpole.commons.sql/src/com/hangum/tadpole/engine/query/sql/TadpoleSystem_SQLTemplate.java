package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.util.List;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.TadpoleTemplateDAO;
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
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<TadpoleTemplateDAO> listSQLTemplate() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("listSQLTemplate", SessionManager.getUserSeq());
	}

	/**
	 * insert sql templcate
	 * @param dao
	 */
	public static void insertSQLTemplate(TadpoleTemplateDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("insertSQLTemplate", dao);
	}

}
