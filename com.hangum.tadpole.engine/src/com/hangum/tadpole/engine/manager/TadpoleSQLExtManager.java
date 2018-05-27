package com.hangum.tadpole.engine.manager;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.db.dynamodb.core.manager.DynamoDBManager;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.surface.BasicDBInfo;
import com.hangum.tadpole.hive.core.connections.HiveJDBC2Manager;

/**
 * Tadpole extension SQL manager
 * 
 * @author hangum
 *
 */
public class TadpoleSQLExtManager extends BasicDBInfo {
	private static final Logger logger = Logger.getLogger(TadpoleSQLExtManager.class);
	private static TadpoleSQLExtManager tadpoleSQLManager;
	static {
		if(tadpoleSQLManager == null) {
			tadpoleSQLManager = new TadpoleSQLExtManager();
		}
	}
	
	private TadpoleSQLExtManager() {}
	
	public Connection getConnection(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		Connection conn = null;
		
		if(DBGroupDefine.DYNAMODB_GROUP == userDB.getDBGroup()) {
			try {
				conn = DynamoDBManager.getInstance().getConnection(userDB.getUrl());
			} catch(Exception e) {
				logger.error("***** get DB Instance seq is " + userDB.getSeq() + "\n" , e);
				throw new TadpoleSQLManagerException(e);
			}
		} else if(userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT) {
			try {
				conn = HiveJDBC2Manager.getInstance().getConnection(userDB.getUrl(), userDB.getUsers(), userDB.getPasswd());
			} catch(Exception e) {
				logger.error("***** get DB Instance seq is " + userDB.getSeq() + "\n" , e);
				throw new TadpoleSQLManagerException(e);
			}
		}
		
		return conn;
	}
	
	/**
	 * 
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException
	 */
	public static TadpoleSQLExtManager getInstance() throws TadpoleSQLManagerException {
		return tadpoleSQLManager;
	}

}
