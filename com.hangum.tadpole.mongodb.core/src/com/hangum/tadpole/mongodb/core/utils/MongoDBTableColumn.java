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
package com.hangum.tadpole.mongodb.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.dao.mongodb.CollectionFieldDAO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * mongodb의 collection(table)의 정보를 리턴합니다.
 * 
 * @author hangum
 *
 */
public class MongoDBTableColumn {
	static Logger logger = Logger.getLogger(MongoDBTableColumn.class);
	
	/**
	 * TableViewer의 컬럼 정보를 보여주도록 합니다.
	 * 
	 * @return
	 */
	public static Map<Integer, String> getTabelColumnView(DBObject dbObject) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		int i=0;
		Set<String> names = dbObject.keySet();		
		for (String name : names) {
			map.put(i, name);
			i++;
		}
		
		return map;
	}

	/**
	 * mongodb table column 정보
	 * 
	 * @param indexInfo
	 * @param dbObject
	 * @return
	 */
	public static List<CollectionFieldDAO> tableColumnInfo(List<DBObject> indexInfo, DBObject dbObject) {
		Map<String, Boolean> mapIndex = new HashMap<String, Boolean>();
		
		// key list parsing
		for (DBObject indexObject : indexInfo) {
			String realKey = StringUtils.substringBetween(indexObject.get("key").toString(), "\"", "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			mapIndex.put(realKey, true);
		}
		
		// column info
		List<CollectionFieldDAO> retColumns = new ArrayList<CollectionFieldDAO>();		
		try {
			// 컬럼이 하나도 없을 경우..
			if(dbObject == null) return retColumns;
			
			Set<String> names = dbObject.keySet();			
			for (String name : names) {
				CollectionFieldDAO column = new CollectionFieldDAO(name, 
														dbObject.get(name) != null?dbObject.get(name).getClass().getSimpleName():"Unknow",  //$NON-NLS-1$
														mapIndex.get(name) != null?"YES":"NO"); //$NON-NLS-1$ //$NON-NLS-2$
				// 자식들이 있는 케이스 이면
				if( dbObject.get(name) instanceof BasicDBObject ) {
					makeTableColumn(column, (BasicDBObject)dbObject.get(name));
				}
				
				retColumns.add(column);
			}
		} catch(Exception e) {
			logger.error("get MongoDB table column info", e); //$NON-NLS-1$
		}
		
		return retColumns;
	}
	
	/**
	 * sub column 정보를 리턴합니다.
	 * 
	 * @param column
	 * @param dbObject
	 */
	private static void makeTableColumn(CollectionFieldDAO column, BasicDBObject dbObject) {
		Set<String> names = dbObject.keySet();
		
		List<CollectionFieldDAO> listChildField = new ArrayList<CollectionFieldDAO>(); 
		for (String name : names) {	
			CollectionFieldDAO columnSub = new CollectionFieldDAO(name,  		//$NON-NLS-1$
															dbObject.get(name) != null ? dbObject.get(name).getClass().getSimpleName():"Unknow",  //$NON-NLS-1$
															"NO");			 	//$NON-NLS-1$
			
			if( dbObject.get(name) instanceof BasicDBObject ) {
				makeTableColumn(columnSub, (BasicDBObject)dbObject.get(name));
			}
			
			listChildField.add(columnSub);		
		}
		
		column.setChildren(listChildField);
	}
}
