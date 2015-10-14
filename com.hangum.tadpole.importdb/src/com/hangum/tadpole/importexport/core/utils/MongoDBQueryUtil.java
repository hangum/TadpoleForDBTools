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
package com.hangum.tadpole.importexport.core.utils;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * mongodb query util
 * 
 * @author hangum
 */
public class MongoDBQueryUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBQueryUtil.class);
	
	private int DATA_COUNT = 1000;
	
	private UserDBDAO userDB;
	private String requestQuery;
	
	private List<DBObject> listDBOject;
	
	/** 처음한번은 반듯이 동작해야 하므로 */
	private boolean isFirst = true;
	private int startPoint = 0;
	private int nextPoint = -1;
	
	public MongoDBQueryUtil(UserDBDAO userDB, String requestQuery) {
		this.userDB = userDB;
		this.requestQuery = requestQuery;
	}
	
	public boolean nextQuery() throws Exception {
		startPoint = nextPoint+1;
		nextPoint = nextPoint + DATA_COUNT;
		runSQLSelect();
		
		return hasNext();
	}
	
	/**
	 * 테이블에 쿼리를 실행합니다.
	 */
	private void runSQLSelect() throws Exception {
		
		DBCollection dbCollection = MongoDBQuery.findCollection(userDB, requestQuery);
		DBCursor dbCursor = dbCollection.find().skip(startPoint).limit(DATA_COUNT);
		
		listDBOject = dbCursor.toArray();
	}
	
	public boolean hasNext() {
		if(isFirst) {
			isFirst = false;
		} else {
			if(listDBOject.size() < DATA_COUNT) return false;
		}
		 
		return true;		
	}
	
	public List<DBObject> getCollectionDataList() {
		return listDBOject;
	}
	
}
