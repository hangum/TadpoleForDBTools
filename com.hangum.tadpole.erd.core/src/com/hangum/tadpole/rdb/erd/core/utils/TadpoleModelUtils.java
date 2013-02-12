/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.erd.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.geometry.Rectangle;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
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
	
	private UserDBDAO userDB;
	
	/** 한 행에 테이블을 표시하는 갯수 */
	public static final int ROW_COUNT = 5;
	
	/** 테이블의 시작 포인트 */
	public static final int START_TABLE_WIDTH = 50;
	public static final int START_TABLE_HIGHT = 50;
	
	public static final int END_TABLE_WIDTH = -1;
	public static final int END_TABLE_HIGHT = -1;
	
	/** 다음 테이블의 간격 */
	public static final int GAP_HIGHT =  40;
	public static final int GAP_WIDTH =  300;
		
	private RdbFactory factory = RdbFactory.eINSTANCE;
	
	/**
	 * logindb의  모든 테이블 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @return
	 */
	public DB getDBAllTable(UserDBDAO userDB) throws Exception {
		this.userDB = userDB;
		DB db = factory.createDB();
		db.setDbType(userDB.getTypes());
		db.setId(userDB.getUsers());
		db.setUrl(userDB.getUrl());
		
		// 테이블목록
		List<TableDAO> tables = getTables();
		// 전체 참조 테이블 목록
		Map<String, Table> mapDBTables = new HashMap<String, Table>();
		
		// 
		int count = 0;
		Rectangle prevRectangle = null;
		
		int nextTableX = START_TABLE_WIDTH;
		int nextTableY = START_TABLE_HIGHT;
		
		for(TableDAO table : tables) {
			Table tableModel = factory.createTable();
			tableModel.setDb(db);
			tableModel.setName(table.getName());
			mapDBTables.put(tableModel.getName(), tableModel);
			
			// 첫번째 보여주는 항 
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
			List<TableColumnDAO> columnList = getColumns(userDB.getDb(), table.getName());
			for (TableColumnDAO columnDAO : columnList) {
				
				Column column = factory.createColumn();
				column.setDefault(columnDAO.getDefault());
				column.setExtra(columnDAO.getExtra());
				column.setField(columnDAO.getField());
				column.setNull(columnDAO.getNull());
				column.setKey(columnDAO.getKey());
				column.setType(columnDAO.getType());
				
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
	public List<TableDAO> getTables() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
	}
	
	
	/**
	 * table의 컬럼 정보를 가져옵니다.
	 * 
	 * @param strTBName
	 * @return
	 * @throws Exception
	 */
	public List<TableColumnDAO> getColumns(String db, String strTBName) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("db", db);
		param.put("table", strTBName);
		
		return sqlClient.queryForList("tableColumnList", param);
	}
	
}
