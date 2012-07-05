package com.hangum.tadpole.mongodb.core.connection;

import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Messages;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * mongo db connection
 * 
 * @author hangum
 *
 */
public class MongoDBConnection {
	private static final Logger logger = Logger.getLogger(MongoDBConnection.class);
	
	/**
	 * mongo db connection
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static DB connection(UserDBDAO userDB) throws Exception {
		
		try {
			Mongo mongo = new Mongo(new DBAddress(userDB.getUrl()) );
			List<String> listDB = mongo.getDatabaseNames();
			
			boolean isDB = false;
			for (String dbName : listDB) if(userDB.getDatabase().equals(dbName)) isDB = true;						
			if(!isDB) {
				throw new Exception(userDB.getDatabase() + Messages.MongoDBConnection_0);
			}
			
			// password 검색
			DB db = mongo.getDB(userDB.getDatabase());
			if(!"".equals(userDB.getUser())) { //$NON-NLS-1$
				boolean auth = db.authenticate(userDB.getUser(), userDB.getPasswd().toCharArray() );
				if(!auth) {
					throw new Exception(Messages.MongoDBConnection_3);
				}
			}
			
			return db;
			
		} catch (UnknownHostException e) {
			logger.error("mongo db connection", e); //$NON-NLS-1$
			
			throw new Exception(Messages.MongoDBConnection_2);
		} catch (MongoException e) {
			logger.error("monodb exception", e); //$NON-NLS-1$
			throw new Exception(Messages.MongoDBConnection_4 + e.getMessage());
		}
		
	}
}
