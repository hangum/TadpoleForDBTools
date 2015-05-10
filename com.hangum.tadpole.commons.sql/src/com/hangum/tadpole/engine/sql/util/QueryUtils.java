package com.hangum.tadpole.engine.sql.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
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
	 * query to json
	 * 
	 * @param userDB
	 * @param strQuery
	 * @return
	 * @throws Exception
	 */
	public static JsonArray selectToJson(final UserDBDAO userDB, final String strQuery) throws Exception {
		return selectToJson(userDB, strQuery, new ArrayList());
	}
	
	/**
	 * query to json
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param listParam
	 */
	public static JsonArray selectToJson(final UserDBDAO userDB, final String strQuery, final List<Object> listParam) throws Exception {
		final JsonArray jsonArry = new JsonArray();
		
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		QueryRunner qr = new QueryRunner(client.getDataSource());
		qr.query(strQuery, listParam.toArray(), new ResultSetHandler<Object>() {

			@Override
			public Object handle(ResultSet rs) throws SQLException {
				ResultSetMetaData metaData = rs.getMetaData();

				while (rs.next()) {
					JsonObject jsonObj = new JsonObject();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						String columnName = metaData.getColumnLabel(i);
						String value = rs.getString(i) == null ? "" : rs.getString(i);
						
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
