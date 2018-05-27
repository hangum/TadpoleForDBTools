/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.initialize.historyhub;

import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * History hub initialize
 * 
 * @author hangum
 *
 */
public class HistoryHubInitialize {
	private static final Logger logger = Logger.getLogger(HistoryHubInitialize.class);
	public static HistoryHubInitialize instance;

	private HistoryHubInitialize() {}
	
	public static HistoryHubInitialize getInstance() {
		if(instance == null) {
			instance = new HistoryHubInitialize();
		}
		
		return instance;
	}
	
	/**
	 * initialize table create
	 * 
	 * @param userDB
	 */
	public void initialize(final UserDBDAO userDB) {
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_history_data_location())) return;
		
		String strFile = "";
		if(userDB.getDBGroup() == DBGroupDefine.MYSQL_GROUP) strFile = "mysql_history_stmt.sql";
		else strFile = "oracle_history_stmt.sql";
		
		try {
			// create table
			String listString = IOUtils.toString(HistoryHubInitialize.class.getResourceAsStream(strFile), StandardCharsets.UTF_8.name());
			
			List<String> listQuery = new ArrayList<String>();
			for (String strSQL : listString.split(PublicTadpoleDefine.SQL_DELIMITER)) {
				listQuery.add(SQLUtil.makeExecutableSQL(userDB, strSQL));
			}
			executeSQLS(userDB, listQuery);
		} catch(Exception e) {
			logger.error("initialize history hub table", e);
		}
	}
	
	/**
	 * execute sqls
	 * 
	 * @param userDB
	 * @param listStrSQL
	 */
	private void executeSQLS(final UserDBDAO userDB, final List<String> listStrSQL) {
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			statement = javaConn.createStatement();
			
			for (String strQuery : listStrSQL) {
				if(StringUtils.startsWithIgnoreCase(strQuery.trim(), "CREATE TABLE")) { //$NON-NLS-1$
					strQuery = StringUtils.replaceOnce(strQuery, "(", " ("); //$NON-NLS-1$ //$NON-NLS-2$
				}
				if(!"".equals(StringUtils.trim(strQuery))) {
					statement.addBatch(strQuery);
				}
			}
			statement.executeBatch();
		} catch(Exception e) {
			logger.error("Execute batch update", e); //$NON-NLS-1$
		} finally {
			try { if(statement != null) statement.close();} catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e){}
		}
	}

}
