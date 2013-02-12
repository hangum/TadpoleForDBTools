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
package com.hangum.tadpole.mongodb.erd.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.geometry.Rectangle;

import com.hangum.tadpole.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.utils.MongoDBTableColumn;
import com.hangum.tadpole.mongodb.erd.core.relation.RelationUtil;
import com.hangum.tadpole.mongodb.model.Column;
import com.hangum.tadpole.mongodb.model.DB;
import com.hangum.tadpole.mongodb.model.MongodbFactory;
import com.hangum.tadpole.mongodb.model.Table;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

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
		
	private MongodbFactory tadpoleFactory = MongodbFactory.eINSTANCE;
	
	/**
	 * logindb의  모든 테이블 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @return
	 */
	public DB getDBAllTable(UserDBDAO userDB) throws Exception {
		this.userDB = userDB;
		DB db = tadpoleFactory.createDB();
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
		List<TableDAO> showTables = new ArrayList<TableDAO>();
		
		Mongo mongo = new Mongo(new DBAddress(userDB.getUrl()) );
		com.mongodb.DB mongoDB = mongo.getDB(userDB.getDb());
		
		for (String col : mongoDB.getCollectionNames()) {
			TableDAO dao = new TableDAO();
			dao.setName(col);
			
			showTables.add(dao);
		}
		
		return showTables;

	}
	
	
	/**
	 * table의 컬럼 정보를 가져옵니다.
	 * 
	 * @param strTBName
	 * @return
	 * @throws Exception
	 */
	public List<CollectionFieldDAO> getColumns(String db, String strTBName) throws Exception {

		Mongo mongo = new Mongo(new DBAddress(userDB.getUrl()) );
		com.mongodb.DB mongoDB = mongo.getDB(userDB.getDb());
		DBCollection coll = mongoDB.getCollection(strTBName);
									
		return MongoDBTableColumn.tableColumnInfo(coll.getIndexInfo(), coll.findOne());
	}
	
}
