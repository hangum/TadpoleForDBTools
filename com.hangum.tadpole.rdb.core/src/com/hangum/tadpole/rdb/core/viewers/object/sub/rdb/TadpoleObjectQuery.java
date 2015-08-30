/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.sqlite.SQLiteForeignKeyListDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.security.DBAccessCtlManager;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * DB Object 관련 쿼리를 뫃아 놓습니다.
 * 
 * @author hangum
 *
 */
public class TadpoleObjectQuery {
	private static Logger logger = Logger.getLogger(TadpoleObjectQuery.class);
	
	/**
	 * 선택된 Table 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @param tableDao
	 * @throws Exception
	 */
	public static List<TableColumnDAO> makeShowTableColumns(UserDBDAO userDB, TableDAO tableDao) throws Exception {
		List<TableColumnDAO> returnColumns = new ArrayList<TableColumnDAO>();
		
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("db", userDB.getDb());
		String strTableName = "";
		if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) strTableName = tableDao.getSysName();
		else 												strTableName = tableDao.getName();
		
		mapParam.put("schema", tableDao.getSchema_name());
		mapParam.put("table", strTableName);

		if(userDB.getDBDefine() != DBDefine.TAJO_DEFAULT) {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			returnColumns = sqlClient.queryForList("tableColumnList", mapParam); //$NON-NLS-1$
		} else {
			returnColumns = new TajoConnectionManager().tableColumnList(userDB, mapParam);
		}
		
		if(DBDefine.SQLite_DEFAULT == userDB.getDBDefine()){
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
			td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getField()));
		}
		
		returnColumns = DBAccessCtlManager.getInstance().getColumnFilter(tableDao, returnColumns, userDB);
		
		return returnColumns;
	}
	
}
