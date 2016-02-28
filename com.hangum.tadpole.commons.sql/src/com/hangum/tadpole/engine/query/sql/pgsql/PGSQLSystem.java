package com.hangum.tadpole.engine.query.sql.pgsql;

import java.sql.SQLException;
import java.util.List;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * postgresql extension
 * 
 * @author hangum
 *
 */
public class PGSQLSystem {
	/**
	 * return namespace
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List getExtension(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("listExtension");
	}
}
