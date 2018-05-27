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
package com.hangum.tadpole.rdb.erd.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;

import com.hangum.tadpole.db.dynamodb.core.manager.DynamoDBManager;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.erd.core.relation.RelationUtil;
import com.hangum.tadpole.rdb.model.Column;
import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.RdbFactory;
import com.hangum.tadpole.rdb.model.Table;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * db의 모델을 생성합니다.
 * 
 * @author hangum
 *
 */
public enum TadpoleModelUtils {
	INSTANCE;
	
	private static final Logger logger = Logger.getLogger(TadpoleModelUtils.class);
	
	/** 한 행에 테이블을 표시하는 갯수 */
	public static final int ROW_COUNT = 5;
	
	/** 테이블의 시작 포인트 */
	public static final int START_TABLE_WIDTH = 50;
	public static final int START_TABLE_HIGHT = 50;
	
	public static final int END_TABLE_WIDTH = -1;
	public static final int END_TABLE_HIGHT = -1;
	
	/** 다음 테이블의 간격 */
	public static final int GAP_HIGHT =  50;
	public static final int GAP_WIDTH =  350;
		
	private RdbFactory factory = RdbFactory.eINSTANCE;
	
	/**
	 * 
	 */
	private TadpoleModelUtils() {
	}
	
	/**
	 * logindb의  모든 테이블 정보를 리턴합니다.
	 * 
	 * @param monitor
	 * @param userDB
	 * @return
	 */
	public DB getDBAllTable(final IProgressMonitor monitor, final UserDBDAO userDB) throws Exception {

		DB db = factory.createDB();
		db.setDbType(userDB.getDbms_type());
		db.setId(userDB.getUsers());
		db.setUrl(userDB.getUrl());
		
		// 테이블목록.
		List<TableDAO> tables = getAllTables(userDB);
		// 전체 참조 테이블 목록.
		Map<String, Table> mapDBTables = new HashMap<String, Table>();
		
		// 
		int count = 0;
		Rectangle prevRectangle = null;
		
		int nextTableX = START_TABLE_WIDTH;
		int nextTableY = START_TABLE_HIGHT;
		
		for(int i=0; i<tables.size(); i++) {
			monitor.subTask(String.format("Working %s/%s", i, tables.size()));
			
			final TableDAO tableDao = tables.get(i);
			Table tableModel = factory.createTable();
			tableModel.setSchema(tableDao.getSchema_name());
			tableModel.setDb(db);
			tableModel.setName(tableDao.getName());
			
			if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) {
				tableModel.setComment("");	
			} else {
				String tableComment = tableDao.getComment();
				if(tableComment == null) tableComment = "";
				tableComment = StringUtils.substring(tableComment, 0, 10);
				tableModel.setComment(tableComment);
			}
			
			mapDBTables.put(tableModel.getName(), tableModel);
			
			// 첫번째 보여주는 항목.
			if(prevRectangle == null) {
				prevRectangle = new Rectangle(START_TABLE_WIDTH, START_TABLE_HIGHT, END_TABLE_WIDTH, END_TABLE_HIGHT); 
			} else {
				// 테이블의 좌표를 잡아줍니다. 
				prevRectangle = new Rectangle(nextTableX, 
											nextTableY, 
											END_TABLE_WIDTH, 
											END_TABLE_HIGHT);
			}
//				logger.debug("###########################################################################################################################");
//				logger.debug("###########################################################################################################################");
//				logger.debug("###########################################################################################################################");
//				logger.debug(prevRectangle.toString());
//				logger.debug("###########################################################################################################################");
//				logger.debug("###########################################################################################################################");
			tableModel.setConstraints(prevRectangle);
			
			// column add
			List<TableColumnDAO> columnList = TDBDataHandler.getColumns(userDB, tableDao);
			for (TableColumnDAO columnDAO : columnList) {
				
				Column column = factory.createColumn();
				column.setDefault(columnDAO.getDefault());
				column.setExtra(columnDAO.getExtra());
				column.setField(columnDAO.getField());
				column.setNull(columnDAO.getNull());
				column.setKey(columnDAO.getKey());
				column.setType(columnDAO.getType());
				
				String strComment = columnDAO.getComment();
				if(strComment == null) strComment = "";
				strComment = StringUtils.substring(strComment, 0, 10);
				column.setComment(strComment);
				
				column.setTable(tableModel);
				tableModel.getColumns().add(column);
			}
			
			// 테이블 hehght를 계산합니다.
			// row count * 컬럼높이 * 테이블명 높이
			int columnsHeight = tableModel.getColumns().size() * 18 + 30;
			
			// 화면 출력하기 위해
			count++;
			
			// 행을 더해주고 열을 초기화 해줍니다. 
			if(count == ROW_COUNT) {
				count = 0;
				
				nextTableX = prevRectangle.getTopRight().x + GAP_WIDTH;
				nextTableY = START_TABLE_WIDTH;
			} else {
				nextTableY = prevRectangle.getBottomLeft().y + columnsHeight + GAP_HIGHT;
			}
			
		}	// end table list
	
		// 관계를 만듭니다.
		RelationUtil.calRelation(userDB, mapDBTables, db);
		
		return db;
	}
	
	/**
	 * table 정보를 가져옵니다.
	 */
	public List<TableDAO> getAllTables(final UserDBDAO userDB) throws Exception {
		List<TableDAO> listAllTables = null;
		
		if(DBGroupDefine.DYNAMODB_GROUP == userDB.getDBGroup()) {
			List<Map<String, String>> listTables = DynamoDBManager.getInstance().getTables(userDB.getUsers(), userDB.getPasswd(), userDB.getDb());
			
			listAllTables = new ArrayList<>();
			for (Map map : listTables) {
				TableDAO tableDao = new TableDAO(""+map.get("name"), "");
				tableDao.setTable_type(""+map.get("table_type"));
				
				listAllTables.add(tableDao);
			}
		} else {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			listAllTables = sqlClient.queryForList("tableList", StringUtils.isBlank(userDB.getSchema()) ? userDB.getDb() : userDB.getSchema()); //$NON-NLS-1$
		}
		
		// 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
		for(TableDAO td : listAllTables) {
			td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getName()));
		}
		
		return listAllTables;
	}
	
	/**
	 * 테이블 이름 정보의 list를 만듭니다.
	 * i hate this code. --;;;
	 * 
	 * @param userDB
	 * @param listTableName
	 * @return
	 * @throws Exception
	 */
	public List<TableDAO> getTable(final UserDBDAO userDB, List<String> listTableName) throws Exception {
		List<TableDAO> listAllTables = null;
		if(DBGroupDefine.DYNAMODB_GROUP == userDB.getDBGroup()) {
			List<Map<String, String>> listTables = DynamoDBManager.getInstance().getTables(userDB.getUsers(), userDB.getPasswd(), userDB.getDb());
			
			listAllTables = new ArrayList<>();
			for (Map map : listTables) {
				TableDAO tableDao = new TableDAO(""+map.get("name"), "");
				tableDao.setTable_type(""+map.get("table_type"));
				
				listAllTables.add(tableDao);
			}
		} else {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			listAllTables = sqlClient.queryForList("tableList",StringUtils.isBlank(userDB.getSchema()) ? userDB.getDb() : userDB.getSchema()); //$NON-NLS-1$
		}

		Map<String, TableDAO> mapTabls = new HashMap<String, TableDAO>();
		for(TableDAO td : listAllTables) {
			mapTabls.put(td.getName(), td);
		}
		
		List<TableDAO> listWantTables = new ArrayList<TableDAO>();
		for(String strTableName : listTableName) {
			if(mapTabls.containsKey(strTableName)) {
				listWantTables.add(mapTabls.get(strTableName));				
			}
		}
		
		// 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
		for(TableDAO td : listWantTables) {
			td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getName()));
		}
		
		return listWantTables;
	}
}
