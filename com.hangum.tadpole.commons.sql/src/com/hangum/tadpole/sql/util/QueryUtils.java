package com.hangum.tadpole.sql.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Query utils
 * 
 * @author hangum
 *
 */
public class QueryUtils {
	private static final Logger logger = Logger.getLogger(QueryUtils.class);
	
	/**
	 * resultset to json
	 * 
	 * @param userDB
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static JsonArray selectToJson(UserDBDAO userDB, String sql) throws Exception {
		if(logger.isDebugEnabled()) logger.debug("user db : " + userDB.getDisplay_name() + " \n\t " + sql);
		final JsonArray jsonArry = new JsonArray();
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		QueryRunner qr = new QueryRunner(client.getDataSource());
		qr.query(sql, new ResultSetHandler<Object>() {

			@Override
			public Object handle(ResultSet rs) throws SQLException {
				 ResultSetMetaData metaData = rs.getMetaData();
				 int columnCnt = metaData.getColumnCount();
				 
				 while(rs.next()) {
					 JsonObject jsonObj = new JsonObject();
					 for(int i=1; i<=columnCnt; i++) {
						 String columnName = metaData.getColumnName(i);
						 String value = rs.getString(i); 
//						 logger.debug(" column name : " + columnName + "\t, value : " + value);
						 
						 jsonObj.addProperty(columnName.toLowerCase(), value);
					 }
					 jsonArry.add(jsonObj);
				 }
				return jsonArry;
			}
		});
		
		return jsonArry;
	}

}
