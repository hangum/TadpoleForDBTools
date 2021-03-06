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
package com.hangum.tadpole.rdb.core.viewers.object.sub.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.db.dynamodb.core.manager.DynamoDBManager;
import com.hangum.tadpole.db.dynamodb.core.manager._KeyValueDAO;
import com.hangum.tadpole.db.metadata.MakeContentAssistUtil;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLExtManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.sqlite.SQLiteForeignKeyListDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.security.DBAccessCtlManager;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDDLCommand;
import com.hangum.tadpole.engine.utils.RequestQueryUtil;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.tadpolehub.db.cassandra.core.db.ApacheCassandraUtils;

/**
 * DB Object 관련 쿼리를 뫃아 놓습니다.
 * 
 * @author hangum
 *
 */
public class TadpoleObjectQuery {
	private static Logger logger = Logger.getLogger(TadpoleObjectQuery.class);
	
	/**
	 * update comment
	 * 
	 * @param userDB
	 * @param dao
	 */
	public static void updateComment(final UserDBDAO userDB, TableDAO dao) throws Exception {
		if (DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup() || DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup()) {
			ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, String.format("COMMENT ON TABLE %s IS %s", dao.getSysName(), SQLUtil.makeQuote(dao.getComment()))));

		} else if (userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT) {
			StringBuffer query = new StringBuffer();
			try {
				query.append(" exec sp_dropextendedproperty 'MS_Description' ").append(", 'user' ,").append(userDB.getUsers()).append(",'table' ").append(" , '").append(dao.getSysName()).append("'");
				ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, query.toString()));
			} catch(Exception e) {
				// 주석이 최초로 등록될때는 삭제될 주석이 없으므로 오류 발생함.
			}

			query = new StringBuffer();
			query.append(" exec sp_addextendedproperty 'MS_Description', '").append(dao.getComment()).append("' ,'user' ,").append(userDB.getUsers()).append(",'table' ").append(" , '").append(dao.getName()).append("'");
			ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, query.toString()));
		} else if (userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT ) {
			StringBuffer query = new StringBuffer();
			try {
				query.append(" exec sp_dropextendedproperty 'MS_Description' ").append(", 'schema' , "+dao.getSchema_name()+",'table' ").append(" , '").append(dao.getTable_name()).append("'");
				ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, query.toString()));
			} catch(Exception e) {
				// 주석이 최초로 등록될때는 삭제될 주석이 없으므로 오류 발생함.
			}

			query = new StringBuffer();
			query.append(" exec sp_addextendedproperty 'MS_Description', '").append(dao.getComment()).append("' ,'schema' , "+dao.getSchema_name()+" ,'table' ").append(" , '").append(dao.getTable_name()).append("'");
			ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, query.toString()));

		} else if (DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			String strSQL = String.format("ALTER TABLE %s COMMENT %s", dao.getFullName(), SQLUtil.makeQuote(dao.getComment()));
			ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, strSQL));
		}
	}
	
	/**
	 * Rename table 
	 * 
	 * @param userDB
	 * @param tableDAO
	 * @param strNewname
	 * @return
	 */
	public static void renameTable(final UserDBDAO userDB, TableDAO tableDAO, String strNewname) throws Exception {
		String strSQL = "";
		
		if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			strSQL = String.format("ALTER TABLE %s RENAME %s", tableDAO.getFullName(), SQLUtil.makeIdentifierName(userDB, tableDAO.getSchema_name()) +"."+ SQLUtil.makeIdentifierName(userDB,strNewname));
		} else if(DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup() ||
				DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup() ||
				DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()
		) {
			if(!StringUtils.equals(strNewname, strNewname.toUpperCase() )){
				strNewname = SQLUtil.makeIdentifierName(userDB, strNewname);
			}
			strSQL = String.format("ALTER TABLE %s RENAME TO %s", tableDAO.getSysName(), strNewname);
		} else if(DBGroupDefine.MSSQL_GROUP == userDB.getDBGroup()) {
			strSQL = String.format("sp_rename %s, %s", tableDAO.getSysName(), strNewname);
		} else if(DBGroupDefine.CUBRID_GROUP == userDB.getDBGroup()) {
			strSQL = String.format("RENAME TABLE %s AS %s", tableDAO.getSysName(), strNewname);
		} else {
			throw new Exception("Not support rename table.");
		}
		
		// 쿼리를 실행한다.
		ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, strSQL));
	}
	
	/**
	 * 보여 주어야할 테이블 목록을 정의합니다.
	 *
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static List<TableDAO> getTableList(final UserDBDAO userDB) throws Exception {
		List<TableDAO> showTables = null;
		
		if(DBGroupDefine.DYNAMODB_GROUP == userDB.getDBGroup()) {
			List<Map<String, String>> listTables = DynamoDBManager.getInstance().getTables(userDB.getUsers(), userDB.getPasswd(), userDB.getDb());
			
			showTables = new ArrayList<>();
			for (Map map : listTables) {
				TableDAO tableDao = new TableDAO(""+map.get("name"), ""+map.get("comment"));
				tableDao.setTable_type(""+map.get("table_type"));
				
				showTables.add(tableDao);
			}
		} else if(DBGroupDefine.ApacheCassandra_GROUP == userDB.getDBGroup()) {
			
			showTables = ApacheCassandraUtils.getTables(userDB);

		} else {
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			if(userDB.getDBGroup() == DBGroupDefine.ORACLE_GROUP) {
				//showTables = sqlClient.queryForList("tableList", StringUtils.upperCase(userDB.getUsers())); //$NON-NLS-1$
				// 오라클의 경우 로그인 유저로 하는게 아니라 스키마 변경 선택한걸로 해야함.
				// changeSchema할때 변경 안됨...
				showTables = sqlClient.queryForList("tableList", StringUtils.upperCase(userDB.getDefaultSchemanName())); //$NON-NLS-1$			
			} else {
				showTables = sqlClient.queryForList("tableList", userDB.getDefaultSchemanName()); //$NON-NLS-1$
			}
		}
		
		/** filter 정보가 있으면 처리합니다. */
		return getTableAfterwork(showTables, userDB);
	}
	
	/**
	 * 테이블이 차지하는 저장공간 정보를 조회한다.
	 */
	public static Object getTableSizeInfo(UserDBDAO userDB, TableDAO tableDao) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("db", userDB.getDb());
		if(StringUtils.isEmpty(tableDao.getSchema_name())){
			mapParam.put("schema_name", userDB.getSchema());
		}else{
			mapParam.put("schema_name", tableDao.getSchema_name());
		}
		mapParam.put("table", tableDao.getName());
		
		return  client.queryForObject("getTableSizeInfo", mapParam);
	}
	
	/**
	 * 테이블의 통계수집 및 추가정보를 조회한다.
	 */
	public static Object getStatisticsInfo(UserDBDAO userDB, TableDAO tableDao) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("db", userDB.getDb());
		if(StringUtils.isEmpty(tableDao.getSchema_name())){
			mapParam.put("schema_name", userDB.getSchema());
		}else{
			mapParam.put("schema_name", tableDao.getSchema_name());
		}
		mapParam.put("table", tableDao.getName());

		return  client.queryForObject("getStatisticsInfo", mapParam);
		
	}
	
	/**
	 * 뷰의 추가정보를 조회한다.
	 */
	public static Object getViewStatisticsInfo(UserDBDAO userDB, TableDAO tableDao) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("db", userDB.getDb());
		if(StringUtils.isEmpty(tableDao.getSchema_name())){
			mapParam.put("schema_name", userDB.getSchema());
		}else{
			mapParam.put("schema_name", tableDao.getSchema_name());
		}
		mapParam.put("table", tableDao.getName());

		return  client.queryForObject("getViewStatisticsInfo", mapParam);
		
	}
	
	/**
	 * 선택된 Table의 컬럼 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @param tableDao
	 * @throws Exception
	 */
	public static List<TableColumnDAO> getTableColumns(UserDBDAO userDB, TableDAO tableDao) throws Exception {
		List<TableColumnDAO> returnColumns = new ArrayList<TableColumnDAO>();
		String strSchema = tableDao.getSchema_name();
		if("".equals(strSchema)) strSchema = userDB.getDb();
		
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
		} else if(DBGroupDefine.ApacheCassandra_GROUP == userDB.getDBGroup()) {
			returnColumns = ApacheCassandraUtils.getTableColumns(userDB, strSchema, strTableName);
			
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
		}
		
		returnColumns = DBAccessCtlManager.getInstance().getColumnFilter(tableDao, returnColumns, userDB);
		
		return returnColumns;
	}

	
	/**
	 * Table 정보 처리 후에 
	 * 
	 * @param showTables
	 * @param userDB
	 * @return
	 */
	private static List<TableDAO> getTableAfterwork(List<TableDAO> showTables, final UserDBDAO userDB) {
		/** filter 정보가 있으면 처리합니다. */
		showTables = DBAccessCtlManager.getInstance().getTableFilter(showTables, userDB);
		
		// 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
		StringBuffer strViewList = new StringBuffer();
		for(TableDAO td : showTables) {
			td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getName()));
			strViewList.append(MakeContentAssistUtil.makeObjectPattern(td.getSchema_name(), td.getSysName(), "Table")); //$NON-NLS-1$
		}
		userDB.setTableListSeparator( StringUtils.removeEnd(strViewList.toString(), MakeContentAssistUtil._PRE_GROUP)); //$NON-NLS-1$
		
		// setting UserDBDAO 
		userDB.setDBObject(OBJECT_TYPE.TABLES, userDB.getDefaultSchemanName(), showTables);
		
		return showTables;
	}
	
	/**
	 * @param userDB
	 * @param strObject
	 * @return
	 */
	public static TableDAO getTable(UserDBDAO userDB, TableDAO tableDAO) throws Exception {
		TableDAO tableDao = null;
		List<TableDAO> showTables = new ArrayList<TableDAO>();
		
		if(DBGroupDefine.HIVE_GROUP == userDB.getDBGroup()) {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<TableDAO> tmpShowTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
			
			for(TableDAO dao : tmpShowTables) {
				if(dao.getName().equals(tableDAO.getName())) {
					showTables.add(dao);
					break;
				}
			}
		} else if(DBGroupDefine.ALTIBASE_GROUP == userDB.getDBGroup()) {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			
			mapParam.put("user_name", 	StringUtils.substringBefore(tableDAO.getSchema_name(), ".")); //$NON-NLS-1$
			mapParam.put("table_name", 	StringUtils.substringAfter(tableDAO.getName(), ".")); //$NON-NLS-1$
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTables = sqlClient.queryForList("table", mapParam); //$NON-NLS-1$
		} else if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()) {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("schema", 	tableDAO.getSchema_name()); //$NON-NLS-1$
			mapParam.put("name", 	tableDAO.getName()); //$NON-NLS-1$
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTables = sqlClient.queryForList("table", mapParam); //$NON-NLS-1$			
		} else {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("db", 	tableDAO.getSchema_name()); //$NON-NLS-1$
			mapParam.put("name", 	tableDAO.getName()); //$NON-NLS-1$
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTables = sqlClient.queryForList("table", mapParam); //$NON-NLS-1$			
		}
		
		/** filter 정보가 있으면 처리합니다. */
		showTables = DBAccessCtlManager.getInstance().getTableFilter(showTables, userDB);
		
		if(!showTables.isEmpty()) { 
			tableDao = showTables.get(0);
			tableDao.setSysName(SQLUtil.makeIdentifierName(userDB, tableDao.getName()));
			return tableDao;
		} else {
			return null;
		}
	}

	/**
	 * 전체 오브젝트 목록을 조회한다.
	 * @return
	 * @throws Exception
	 */
	
	public static List<HashMap> getObjectInfo(UserDBDAO userDB, Map<String,String> object_map) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		
		return sqlClient.queryForList("allObjects", object_map); //$NON-NLS-1$
	}

	/**
	 * 인덱스의 컬럼 목록을 조회한다.
	 * @param userDB
	 * @param indexDao
	 * @return
	 * @throws Exception
	 */
	
	public static List<InformationSchemaDAO> getIndexColumns(UserDBDAO userDB, InformationSchemaDAO indexDao) throws Exception {

		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		HashMap<String, String>paramMap = new HashMap<String, String>();
		paramMap.put("table_schema", indexDao.getTABLE_SCHEMA()==null?indexDao.getSchema_name():indexDao.getTABLE_SCHEMA()); //$NON-NLS-1$
		paramMap.put("table_name", indexDao.getTABLE_NAME()); //$NON-NLS-1$
		paramMap.put("index_name", indexDao.getINDEX_NAME()); //$NON-NLS-1$
		
		return sqlClient.queryForList("indexDetailList", paramMap); //$NON-NLS-1$
	}
	
	/**
	 * 인덱스의 통계정보를 조회한다.
	 * @param userDB
	 * @param tableDao
	 * @return
	 * @throws Exception
	 */
	public static Object getIndexStatisticsInfo(UserDBDAO userDB, InformationSchemaDAO indexDao) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		HashMap<String, String>paramMap = new HashMap<String, String>();
		paramMap.put("schema_name", indexDao.getTABLE_SCHEMA()==null?indexDao.getSchema_name():indexDao.getTABLE_SCHEMA()); //$NON-NLS-1$
		paramMap.put("index_name", indexDao.getINDEX_NAME()); //$NON-NLS-1$

		return  client.queryForObject("getIndexStatisticsInfo", paramMap);
		
	}
	
}
