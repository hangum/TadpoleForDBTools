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

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;
import com.hangum.tadpole.rdb.core.dialog.table.AlterTableMetaDataDAO;
import com.hangum.tadpole.rdb.core.dialog.table.DataTypeDef;

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
	public static RequestResultDAO modifyColumnType(final UserDBDAO userDB, TableDAO tableDAO, AlterTableMetaDataDAO metaDataDao) throws Exception {
		RequestResultDAO resultDao = null;
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			// ALTER TABLE 테이블명 MODIFY 컬럼이름 새컬럼타입
			String strQuery = String.format("ALTER TABLE %s CHANGE %s %s %s", 
					tableDAO.getSysName(), metaDataDao.getColumnName(), metaDataDao.getColumnName(), metaDataDao.getDataTypeName());
			if(DataTypeDef.isSecondArgument(userDB.getDBDefine(), metaDataDao.getDataType())) {
				if(metaDataDao.getDataSize() == 0) metaDataDao.setDataSize(11);
				strQuery = String.format("ALTER TABLE %s CHANGE %s %s %s(%s)", 
					tableDAO.getSysName(), metaDataDao.getColumnName(), metaDataDao.getColumnName(), metaDataDao.getDataTypeName(), metaDataDao.getDataSize());
			}
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
	 * Rename table column 
	 * 
	 * @param userDB
	 * @param tableDAO
	 * @param oldColumnName
	 * @param newColumnName
	 * @return
	 */
	public static RequestResultDAO renameColumn(final UserDBDAO userDB, TableDAO tableDAO, AlterTableMetaDataDAO metaDataDao, String newColumnName) throws Exception {
		RequestResultDAO resultDao = null;
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			//ALTER TABLE `dbtype` CHANGE `tesst` `cho` INT(11)  NULL  DEFAULT NULL;
			String strQuery = String.format("ALTER TABLE %s CHANGE %s %s %s(%s)", 
												tableDAO.getSysName(), metaDataDao.getColumnName(), newColumnName, metaDataDao.getDataTypeName(), metaDataDao.getDataSize()
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
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT |
				userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT |
				userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT |
				userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT |
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
	 * @param tableName
	 * @param metaDataDao
	 */
	public static RequestResultDAO addColumn(final UserDBDAO userDB, String tableName, AlterTableMetaDataDAO metaDataDao) throws Exception {
		String strQuery = String.format("ALTER TABLE %s ADD %s %s %s", tableName, metaDataDao.getColumnName(), metaDataDao.getDataTypeName(), metaDataDao.getDefaultValue());
		return ExecuteDDLCommand.executSQL(userDB, strQuery); //$NON-NLS-1$
	}
}
