package com.hangum.tadpole.mongodb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.Document;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * mongodb execute command
 * 
 * @author hangum
 *
 */
public class MongodbExecuteCommand {
	private static Logger logger = Logger.getLogger(MongodbExecuteCommand.class);
	
	/**
	 * mongodb execute command
	 * 
	 * @param userDBDAO 
	 * @param reqQuery
	 * @param _intPreferenceQueryTimeout
	 * @param intSelectLimitCnt
	 * @return
	 * @throws Exception
	 */
	public static List<DBObject> executeCommand(UserDBDAO userDBDAO, RequestQuery reqQuery, int _intPreferenceQueryTimeout, int intSelectLimitCnt) throws Exception {
		
		List<DBObject> listDBObject = null;
		
		// find 명령의 경우 limit 를 준다. 
		//	limit에는 어드민이 지정해 준 값 만큼 limit를 설정할 수 있도록 한다.
		//		만약에, limit를 설정했다면 사용자가 설정한 limit가 설정의 limit보다 작다면 설정의 limit를 무시해야
		//
		String _sql = SQLUtil.makeExecutableSQL(userDBDAO, reqQuery.getOriginalSql());
		if(StringUtils.startsWith(_sql, "db.") && StringUtils.indexOf(_sql, "find(") != -1) {
			// maxTimeMS 옵션도 걸어야겠다. 
			_sql += String.format(".limit(%s).maxTimeMS(%s)", intSelectLimitCnt,  _intPreferenceQueryTimeout * 1000);
			if(logger.isDebugEnabled()) logger.debug("\t find query is " + _sql);
		} else {
			if(logger.isDebugEnabled()) logger.debug("\t find query is " + _sql);
		}
		
		Connection _con = null;
		Statement _stmt = null;
		ResultSet _rs 	= null;
		try {
			Class.forName("com.dbschema.MongoJdbcDriver");
//	        _con = DriverManager.getConnection(MongoDBConnectionUtils.getURL(userDBOriginal), null, null);
			_con = DriverManager.getConnection(userDBDAO.getUrl(), null, null);
	        _con.setSchema(userDBDAO.getSchema());
	        
	        _stmt = _con.createStatement();
	        _rs = _stmt.executeQuery(_sql);
	        
			if(_rs != null) {
				listDBObject = new ArrayList<DBObject>();
				while(_rs.next()) {
					Object obj = _rs.getObject(1);
					
					// string 으로 넘어올때는  db.getCollectionNames() 같은 명령은 결과가 String 이다. 
					if(obj instanceof String) {
						String _strJson = String.format("{'name' : '%s'}", obj.toString());
						listDBObject.add((DBObject) JSON.parse(_strJson));
					} else {
						listDBObject.add(new BasicDBObject((Document)obj));	
					}
				}
			}
		} catch(Exception e) {
			logger.error("Execute command: " + _sql);
			logger.error("Mongodb Excecute command", e);
			
			throw e;
		} finally {
			try { if(_rs != null) _rs.close(); } catch(Exception e) {}
			try { if(_stmt != null) _stmt.close(); } catch(Exception e) {}
			try { 
				if(_con != null) {
					_con.close();
					_con = null;
				}
			} catch(Exception e) {}
		}
		
		return listDBObject;
	}
	
}
