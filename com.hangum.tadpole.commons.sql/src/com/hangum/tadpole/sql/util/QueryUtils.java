package com.hangum.tadpole.sql.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringIndexDAO;
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
	 * @param indexDao
	 * @return
	 * @throws Exception
	 */
	public static JsonArray selectToJson(UserDBDAO userDB, MonitoringIndexDAO indexDao) throws Exception {
		final JsonArray jsonArry = new JsonArray();

		// generate query parameter.
		List<Object> listParam = new ArrayList<>();
		if(StringUtils.isNotEmpty(indexDao.getParam_1_init_value())) listParam.add(indexDao.getParam_1_init_value());
		if(StringUtils.isNotEmpty(indexDao.getParam_2_init_value())) listParam.add(indexDao.getParam_2_init_value());
		if(logger.isDebugEnabled()) {
			logger.debug("====> " + indexDao.getQuery());
//			logger.debug("\t parameter : " + listParam.get(0));
		}
		
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		QueryRunner qr = new QueryRunner(client.getDataSource());
		qr.query(indexDao.getQuery(), listParam.toArray(), new ResultSetHandler<Object>() {

			@Override
			public Object handle(ResultSet rs) throws SQLException {
				ResultSetMetaData metaData = rs.getMetaData();

				while (rs.next()) {
					JsonObject jsonObj = new JsonObject();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						String columnName = metaData.getColumnName(i);
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
