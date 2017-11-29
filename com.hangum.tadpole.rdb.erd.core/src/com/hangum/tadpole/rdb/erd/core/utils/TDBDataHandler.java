/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.erd.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.db.dynamodb.core.manager.DynamoDBManager;
import com.hangum.tadpole.db.dynamodb.core.manager._KeyValueDAO;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLExtManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.sqlite.SQLiteForeignKeyListDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.security.DBAccessCtlManager;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 시스템 쿼리를 가져오기위해.
 * 
 * @author hangum
 *
 */
public class TDBDataHandler {
	private static final Logger logger = Logger.getLogger(TDBDataHandler.class);
	
	/**
	 * 선택된 Table의 컬럼 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @param tableDao
	 * @throws Exception
	 */
	public static List<TableColumnDAO> getColumns(UserDBDAO userDB, TableDAO tableDao) throws Exception {
		List<TableColumnDAO> returnColumns = new ArrayList<TableColumnDAO>();
		
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("db", userDB.getDb());
		String strTableName = "";
		if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) strTableName = tableDao.getSysName();
		else 												strTableName = tableDao.getName();
		

		if(DBGroupDefine.ALTIBASE_GROUP == userDB.getDBGroup()) {
			mapParam.put("user", StringUtils.substringBefore(strTableName, "."));
			mapParam.put("table", StringUtils.substringAfter(strTableName, "."));
		} else {
			mapParam.put("schema", tableDao.getSchema_name());
			mapParam.put("table", strTableName);
		}
		
		if(DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup()) {
			if("".equals(mapParam.get("schema")) || null == mapParam.get("schema")) {
				mapParam.put("schema", "public");
			}
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			returnColumns = sqlClient.queryForList("tableColumnList", mapParam); //$NON-NLS-1$
		} else if(DBGroupDefine.DYNAMODB_GROUP == userDB.getDBGroup()) {
			try {
				returnColumns = TadpoleSQLExtManager.getInstance().tableColumnList(userDB, mapParam);
			} catch(Exception e) {
				logger.error("table column error", e);
			}
			if(returnColumns.isEmpty()) {
				List<_KeyValueDAO> listTables = DynamoDBManager.getInstance().getTableColumn(userDB.getUsers(), userDB.getPasswd(), userDB.getDb(), tableDao.getName());
				
				for(int i=0; i<listTables.size(); i++) {
					_KeyValueDAO valObj = listTables.get(i);
					
		    		TableColumnDAO tcDAO = new TableColumnDAO();
		    		tcDAO.setName(valObj.getName());
		    		tcDAO.setType(valObj.getValue());
		    		
		    		returnColumns.add(tcDAO);
		    	}
			}
		} else {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			returnColumns = sqlClient.queryForList("tableColumnList", mapParam); //$NON-NLS-1$
		}
		
		if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()){
			try{
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
				List<SQLiteForeignKeyListDAO> foreignKeyList = sqlClient.queryForList("tableForeignKeyList", mapParam); //$NON-NLS-1$
				for (SQLiteForeignKeyListDAO fkeydao : foreignKeyList){
					for (TableColumnDAO dao : returnColumns){
						if (dao.getName().equals(fkeydao.getFrom() ) ){
							if (PublicTadpoleDefine.isPK(dao.getKey())){
								dao.setKey("MUL");
							}else{
								dao.setKey("FK");
							}
						}	 
					}
				}
			}catch(Exception e){
				logger.error("not found foreignkey for " + tableDao.getName());
			}
		}
		
		// if find the keyword is add system quote.
		for(TableColumnDAO td : returnColumns) {
			td.setTableDao(tableDao);
			td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getField()));
			
			// 코멘트에 행 분리자가 있다면 제거한다.
			td.setComment(StringUtils.remove(td.getComment(), "\n"));
		}
		
		returnColumns = DBAccessCtlManager.getInstance().getColumnFilter(tableDao, returnColumns, userDB);
		
		return returnColumns;
	}
}
