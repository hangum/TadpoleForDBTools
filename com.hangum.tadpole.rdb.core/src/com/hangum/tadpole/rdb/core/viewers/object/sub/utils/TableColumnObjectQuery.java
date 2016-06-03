/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.dialog.table.mysql.TableColumnUpdateDAO;

/**
 * Execute table column object 
 * 
 * @author hangum
 *
 */
public class TableColumnObjectQuery {
	private static Logger logger = Logger.getLogger(TableColumnObjectQuery.class);
	
	/**
	 * modify table column type 
	 * 
	 * @param userDB
	 * @param tableDAO
	 * @param metaDataDao
	 * @param strTypeName
	 * @return
	 */
	public static RequestResultDAO modifyColumnType(final UserDBDAO userDB, TableDAO tableDAO, TableColumnUpdateDAO metaDataDao) throws Exception {
		RequestResultDAO resultDao = null;
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			// ALTER TABLE 테이블명 MODIFY 컬럼이름 새컬럼타입
			String strQuery = String.format("ALTER TABLE %s CHANGE %s %s %s", 
					tableDAO.getSysName(), metaDataDao.getColumnName(), metaDataDao.getColumnName(), metaDataDao.getDataType());
			resultDao = ExecuteDDLCommand.executSQL(userDB, strQuery);
			
		} else {
			throw new Exception("Not support rename table.");
		}
		
		return resultDao;
	}
	
	/**
	 * Rename table column 
	 * 
	 * @param userDB
	 * @param tableDAO
	 * @param oldColumnName
	 * @param newColumnName
	 * @return
	 */
	public static RequestResultDAO renameColumn(final UserDBDAO userDB, TableDAO tableDAO, TableColumnUpdateDAO metaDataDao, String newColumnName) throws Exception {
		RequestResultDAO resultDao = null;
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			//ALTER TABLE `dbtype` CHANGE `tesst` `cho` INT(11)  NULL  DEFAULT NULL;
			String strQuery = String.format("ALTER TABLE %s CHANGE %s %s %s", 
												tableDAO.getSysName(), metaDataDao.getColumnName(), newColumnName, metaDataDao.getDataType()
					);
			resultDao = ExecuteDDLCommand.executSQL(userDB, strQuery);
//		} else if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT |
//					userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT |
//					userDB.getDBDefine() == DBDefine.SQLite_DEFAULT
//		) {
//			String strQuery = String.format("ALTER TABLE %s RENAME TO %s", tableDAO.getSysName(), oldColumnName);
//			resultDao = ExecuteDDLCommand.executSQL(userDB, strQuery);
//		} else if(userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT) {
//			String strQuery = String.format("sp_rename %s, %s", tableDAO.getSysName(), oldColumnName);
//			resultDao = ExecuteDDLCommand.executSQL(userDB, strQuery);
//		} else if(userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT) {
//			String strQuery = String.format("RENAME TABLE %s AS %s", tableDAO.getSysName(), oldColumnName);
//			resultDao = ExecuteDDLCommand.executSQL(userDB, strQuery);
		} else {
			throw new Exception("Not support rename table.");
		}
		
		return resultDao;
	}
	
	/**
	 * Delete table column
	 * 
	 * @param userDB
	 * @param tableColumnDao
	 * @return
	 */
	public static RequestResultDAO deleteColumn(final UserDBDAO userDB, final List<TableColumnDAO> listTableColumnDao) throws Exception {
		RequestResultDAO resultDao = null;
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT ||
				userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT ||
				userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT
		) {
			for(TableColumnDAO tableColumnDao: listTableColumnDao) {
				resultDao = deleteColumn(userDB, tableColumnDao.getTableDao().getSysName(), tableColumnDao.getField());
			}
			
		} else {
			throw new Exception("Not support delete column.");
		}
		
		return resultDao;
	}
	
	/**
	 * delete column 
	 * @param userDB
	 * @param tableName
	 * @param columnName
	 * @return
	 * @throws Exception
	 */
	public static RequestResultDAO deleteColumn(final UserDBDAO userDB, String tableName, String columnName) throws Exception {
		String strQuery = String.format("ALTER TABLE %s DROP COLUMN %s", tableName, columnName);
		return ExecuteDDLCommand.executSQL(userDB, strQuery); //$NON-NLS-1$
	}
	
	/**
	 * 컬럼을 추가합니다. 
	 * 
	 * @param userDB
	 * @param tableDAO
	 * @param metaDataDao
	 */
	public static RequestResultDAO addColumn(final UserDBDAO userDB, final TableDAO tableDAO, final TableColumnUpdateDAO metaDataDao) throws Exception {
		RequestResultDAO addColumnResultDAO = null;
		
		String strQuery = String.format("ALTER TABLE %s ADD COLUMN %s %s %s COMMENT %s ", 
											tableDAO.getSysName(), 
											metaDataDao.getColumnName(), 
											metaDataDao.getDataType(), 
											metaDataDao.isNotNull()?"NOT NULL":"NULL", 
											SQLUtil.makeQuote(metaDataDao.getComment())
				);
		
		if(!"".equals(metaDataDao.getCollation())) { 
			strQuery += String.format(" COLLATE %s ", metaDataDao.getCollation());
		}
		if(metaDataDao.isPrimaryKey()) {
			strQuery += " PRIMARY KEY ";
		}
		if(metaDataDao.isAutoIncrement()) {
			strQuery += " auto_increment ";
		} else {
			if(!"".equals(StringUtils.trimToEmpty(metaDataDao.getDefaultValue()))) {
				strQuery += String.format(" DEFAULT %s ", SQLUtil.makeQuote(metaDataDao.getDefaultValue()));
			}
		}
		
		addColumnResultDAO =  ExecuteDDLCommand.executSQL(userDB, strQuery); //$NON-NLS-1$	
		return addColumnResultDAO;
	}
	
	/**
	 * update column
	 * 
	 * @param userDB
	 * @param tableDAO
	 * @param tableColumnDAO 
	 * @param metaDataDao
	 * @return
	 * @throws Exception
	 */
	public static RequestResultDAO updateColumn(final UserDBDAO userDB, final TableDAO tableDAO, TableColumnDAO tableColumnDAO, final TableColumnUpdateDAO metaDataDao) throws Exception {
		RequestResultDAO addColumnResultDAO = null;
		
		String strQuery = String.format("ALTER TABLE %s CHANGE COLUMN %s %s %s %s COMMENT %s ", 
											tableDAO.getSysName(), 
											tableColumnDAO.getField(),
											metaDataDao.getColumnName(), 
											metaDataDao.getDataType(), 
											metaDataDao.isNotNull()?"NOT NULL":"NULL", 
											SQLUtil.makeQuote(metaDataDao.getComment())
				);
		
		if(!"".equals(metaDataDao.getCollation())) { 
			strQuery += String.format(" COLLATE %s ", metaDataDao.getCollation());
		}
		if(metaDataDao.isPrimaryKey()) {
			strQuery += " PRIMARY KEY ";
		}
		if(metaDataDao.isAutoIncrement()) {
			strQuery += " auto_increment ";
		} else {
			if(!"".equals(StringUtils.trimToEmpty(metaDataDao.getDefaultValue()))) {
				strQuery += String.format(" DEFAULT %s ", SQLUtil.makeQuote(metaDataDao.getDefaultValue()));
			}
		}
		
		addColumnResultDAO = ExecuteDDLCommand.executSQL(userDB, strQuery);	
		return addColumnResultDAO;
	}
	
	/**
	 * update comment
	 * 
	 * @param userDB
	 * @param tableDAO
	 * @param columnDAO
	 */
	public static void updateComment(final UserDBDAO userDB, final TableDAO tableDAO, TableColumnDAO columnDAO) throws Exception {

		StringBuffer query = new StringBuffer();

		if (userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT || userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT) {
			String strQuery = String.format("COMMENT ON COLUMN %s.%s IS %s", tableDAO.getSysName(), columnDAO.getField(), SQLUtil.makeQuote(columnDAO.getComment()));
			
			try{
				ExecuteDDLCommand.executSQL(userDB, strQuery);
			}catch(Exception e){
				//  org.postgresql.util.PSQLException: No results were returned by the query.
			}

		} else if (userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT) {
			query.append(" exec sp_dropextendedproperty 'MS_Description' ").append(", 'user' ,").append(userDB.getUsers());
			query.append(",'table' , '").append(tableDAO.getSysName()).append("'");
			query.append(",'column' , '").append(columnDAO.getSysName()).append("'");
			try{
				ExecuteDDLCommand.executSQL(userDB, query.toString());
			}catch(Exception e){
				// 주석이 최초로 등록될때는 삭제될 주석이 없으므로 오류 발생함.
			}

			query = new StringBuffer();
			query.append(" exec sp_addextendedproperty 'MS_Description', '").append(columnDAO.getComment()).append("' ,'user' ,").append(userDB.getUsers());
			query.append(",'table' , '").append(tableDAO.getSysName()).append("'");
			query.append(",'column', '").append(columnDAO.getSysName()).append("'");
			ExecuteDDLCommand.executSQL(userDB, query.toString());

		} else if (userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT ) {
			query.append(" exec sp_dropextendedproperty 'MS_Description' ").append(", 'schema' , " + tableDAO.getSchema_name());
			query.append(",'table' , '").append(tableDAO.getTable_name()).append("'");
			query.append(",'column' , '").append(columnDAO.getSysName()).append("'");
			try{
				ExecuteDDLCommand.executSQL(userDB, query.toString());
			}catch(Exception e){
				// 주석이 최초로 등록될때는 삭제될 주석이 없으므로 오류 발생함.
			}

			query = new StringBuffer();
			query.append(" exec sp_addextendedproperty 'MS_Description', '").append(columnDAO.getComment()).append("' ,'schema' , " + tableDAO.getSchema_name());
			query.append(",'table' , '").append(tableDAO.getTable_name()).append("'");
			query.append(",'column', '").append(columnDAO.getSysName()).append("'");
			ExecuteDDLCommand.executSQL(userDB, query.toString());

		} else if (userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {

			String strQuery = String.format("ALTER TABLE %s CHANGE %s %s %s %s COMMENT %s", 
											tableDAO.getSysName(),
											columnDAO.getField(), columnDAO.getField(), columnDAO.getType(), ("NO".equals(columnDAO.getNull())?"NOT NULL":"NULL"), 
											SQLUtil.makeQuote(columnDAO.getComment()));
			ExecuteDDLCommand.executSQL(userDB, strQuery);
			
			strQuery = String.format("ALTER TABLE %s ALTER %s SET DEFAULT %s", 
					tableDAO.getSysName(), columnDAO.getField(),  
					SQLUtil.makeQuote(columnDAO.getDefault()));
			ExecuteDDLCommand.executSQL(userDB, strQuery);

		}

	}

}
