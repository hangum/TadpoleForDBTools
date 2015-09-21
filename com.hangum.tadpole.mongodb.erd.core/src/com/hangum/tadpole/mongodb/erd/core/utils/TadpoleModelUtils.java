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
package com.hangum.tadpole.mongodb.erd.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;

import com.hangum.tadpole.engine.query.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.mongodb.erd.core.relation.RelationUtil;
import com.hangum.tadpole.mongodb.model.Column;
import com.hangum.tadpole.mongodb.model.DB;
import com.hangum.tadpole.mongodb.model.MongodbFactory;
import com.hangum.tadpole.mongodb.model.Table;

/**
 * db의 모델을 생성합니다.
 * 
 * @author hangum
 *
 */
public enum TadpoleModelUtils {
	INSTANCE;
	/** default logger */
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
	public static final int GAP_HIGHT =  50;
	public static final int GAP_WIDTH =  300;
		
	private MongodbFactory tadpoleFactory = MongodbFactory.eINSTANCE;
	
	/**
	 * logindb의  모든 테이블 정보를 리턴합니다.
	 * 
	 * @param monitor
	 * @param userDB
	 * @return
	 */
	public DB getDBAllTable(final IProgressMonitor monitor, UserDBDAO userDB) throws Exception {
		this.userDB = userDB;
		DB db = tadpoleFactory.createDB();
		db.setDbType(userDB.getDbms_type());
		db.setId(userDB.getUsers());
		db.setUrl(userDB.getUrl());
		
		// 테이블목록.
		List<TableDAO> tables = getTables();
		// 전체 참조 테이블 목록.
		Map<String, Table> mapDBTables = new HashMap<String, Table>();
		
		// 
		int count = 0;
		Rectangle prevRectangle = null;
		
		int nextTableX = START_TABLE_WIDTH;
		int nextTableY = START_TABLE_HIGHT;
		
		for(int i=0; i<tables.size(); i++) {
			monitor.subTask(String.format("Working %s/%s", i, tables.size()));
			
			final TableDAO table = tables.get(i);
			Table tableModel = tadpoleFactory.createTable();
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
			List<CollectionFieldDAO> columnList = getColumns(userDB.getDb(), table.getName());
			for (CollectionFieldDAO columnDAO : columnList) {
				
				Column column = tadpoleFactory.createColumn();
				
				column.setField(columnDAO.getField());
				column.setKey(columnDAO.getKey());
				column.setType(columnDAO.getType());
				if("BasicDBObject".equals(columnDAO.getType())) {
					makeSubDoc(tableModel, column, columnDAO);
				}
				
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
	 * add sub document
	 * 
	 * @param columnDAO
	 */
	private void makeSubDoc(Table tableModel, Column parentColumn, CollectionFieldDAO columnDAO) {
		
		for (CollectionFieldDAO cfDAO : columnDAO.getChildren()) {
			Column column = tadpoleFactory.createColumn();					
			column.setField(cfDAO.getField());
			column.setKey(cfDAO.getKey());
			column.setType(cfDAO.getType());
			if("BasicDBObject".equals(cfDAO.getType())) {
				makeSubDoc(tableModel, column, cfDAO);
			}
			column.setTable(tableModel);
			
			parentColumn.getSubDoc().add(column);
		}
	}
	
	/**
	 * table 정보를 가져옵니다.
	 */
	public List<TableDAO> getTables() throws Exception {
		return MongoDBQuery.listCollection(userDB);
	}
	
	
	/**
	 * table의 컬럼 정보를 가져옵니다.
	 * 
	 * @param strTBName
	 * @return
	 * @throws Exception
	 */
	public List<CollectionFieldDAO> getColumns(String db, String strTBName) throws Exception {
		return MongoDBQuery.collectionColumn(userDB, strTBName);
	}
	
}
