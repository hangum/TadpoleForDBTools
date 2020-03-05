package com.hangum.tadpole.mongodb;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * mongodb connection utils
 * @author hangum
 *
 */
public class MongoDBConnectionUtils {
	/**
	 * get Mongodb option
	 * 
	 * @param userDB
	 * @return
	 */
	public static String getURL(UserDBDAO userDB) {
		String dbUrl = "";
		/* mongodb://[username:password@]host1[:port1][,...hostN[:portN]][/[database][?options]] */
		// textReplicaSet
		if(!"".equals(userDB.getUsers())) {
			dbUrl = String.format(
						DBDefine.MONGODB_DEFAULT.getDB_URL_INFO(),
						userDB.getUsers() + ":" + userDB.getPasswd() + "@" +
						String.format("%s:%s", userDB.getHost(), userDB.getPort()), 
						userDB.getDb()
					);
		} else {
			dbUrl = String.format(
					DBDefine.MONGODB_DEFAULT.getDB_URL_INFO(), 
					String.format("%s:%s", userDB.getHost(), userDB.getPort()), 
					userDB.getDb()
				);
		}
		
		// 옵션을 추가 합니다. 
		if(!"".equals(userDB.getUrl_user_parameter())) {
			dbUrl += "?" + userDB.getUrl_user_parameter();
		}
		
		return dbUrl;
	}
}
