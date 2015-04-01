/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.sql;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.TableFilterDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * table, column filter controller
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 4. 1.
 *
 */
public class TadpoleSystem_TableColumnFilter {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_TableColumnFilter.class);
	
	/**
	 * find filters at db of table, columns
	 * 
	 * @param dbSeq
	 * @return
	 * @throws Exception
	 */
	public static List<TableFilterDAO> getTableColumnFilters(int dbSeq) throws Exception{
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getTableFilter", dbSeq);
	}
	
	/**
	 * find filter at table name
	 * 
	 * @param dbSeq
	 * @param strTableName
	 * @return
	 * @throws Exception
	 */
	public static List<TableFilterDAO> getTableColumnFilters(TableFilterDAO tableColumnFilter) throws Exception{
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getTableFilterTable", tableColumnFilter);
	}
	
	/**
	 * insert table, column filter
	 * 
	 * @param tableColumnFilter
	 * @throws Exception
	 */
	public static void insertTableColumnFilter(TableFilterDAO tableColumnFilter) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.insert("insertTableColumnFilter", tableColumnFilter);
	}

	/**
	 * delete table filter
	 * 
	 * @param tableFilterDao
	 */
	public static void deleteTableColumnFilters(TableFilterDAO tableFilterDao) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("deleteTableColumnFilters", tableFilterDao);
	}
}
