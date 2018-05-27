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
//package com.hangum.tadpole.mongodb.core.utils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import junit.framework.TestCase;
//
//import com.hangum.db.dao.mysql.TableColumnDAO;
//import com.mongodb.DBObject;
//
///**
// * monogo query test
// * 
// * @author hangum
// *
// */
//public class MongoSQLParserTest extends TestCase {
//	
//	/**
//	 * test 를 위한 기본 테이블 
//	 * @return
//	 */
//	public Map<String, TableColumnDAO> getColumnInfo() {
//		Map<String, TableColumnDAO> columnInfo = new HashMap<String, TableColumnDAO>();
//		columnInfo.put("rental_id", new TableColumnDAO("rental_id", "java.lang.Integer", "0"));
//		columnInfo.put("age", new TableColumnDAO("age", "java.lang.Integer", "10"));
//		columnInfo.put("name", new TableColumnDAO("name", "java.lang.String", ""));
//		columnInfo.put("mail", new TableColumnDAO("mail", "java.lang.Boolean", "true"));
//		
//		
//		return columnInfo;
//	}
//	
//	/**
//	 * 컬럼이 integer인지 검사
//	 * 
//	 */
//	public void testIntegerQuery() {
//		try {
//			MongoSQLParser testQuery = new MongoSQLParser();
//			DBObject dbObject = testQuery.query("rental_id <= 10", getColumnInfo());
//			
//			if(dbObject.toString().equals("{ \"rental_id\" : { \"$lte\" : 10}}")) {
//			} else {
//				fail("make query error " + dbObject.toString());
//			}
//			
//		} catch(Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	/**
//	 * 컬럼이 string인지 검사
//	 */
//	public void testStringQuery() {
//		
//		try {
//			MongoSQLParser testQuery = new MongoSQLParser();
//			DBObject dbObject = testQuery.query("name = 'cho'", getColumnInfo());
//			
//			if(dbObject.toString().equals("{ \"name\" : \"cho\"}")) {
//			} else {
//				fail("make query error " + dbObject.toString());
//			}
//			
//		} catch(Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	/**
//	 * 컬럼이 boolean인지 검사
//	 */
//	public void testBooleanQuery() {
//		
//		try {
//			MongoSQLParser testQuery = new MongoSQLParser();
//			DBObject dbObject = testQuery.query("mail = 'true'", getColumnInfo());
//			
//			if(dbObject.toString().equals("{ \"mail\" : true}")) {
//			} else {
//				fail("make query error " + dbObject.toString());
//			}
//			
//		} catch(Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	/**
//	 * and 조건 검사
//	 */
//	public void testAndQuery() {
//		
//		try {
//			MongoSQLParser testQuery = new MongoSQLParser();
//			DBObject dbObject = testQuery.query("name = '10' and rental_id >= 100", getColumnInfo());
//			
//			if(dbObject.toString().equals("{ \"name\" : \"10\" , \"rental_id\" : { \"$gte\" : 100}}")) {
//			} else {
//				fail("make query error " + dbObject.toString());
//			}
//				
//		} catch(Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	/**
//	 * like 조건 검사
//	 * 
//	 */
//	public void testLikeQuery() {
//		try {
//			MongoSQLParser testQuery = new MongoSQLParser();
//			DBObject dbObject = testQuery.query("name like '.*en*'", getColumnInfo());
//			
//			if(dbObject.toString().equals("{ \"name\" : { \"$regex\" : \".*en*\"}}")) {
//			} else {
//				fail("make query error " + dbObject.toString());
//			}
//			
//		} catch(Exception e) {
//			fail(e.getMessage());
//		}
//	}
//	
//	/**
//	 * in 조건 검사
//	 * 
//	 */
//	public void testInQuery() {
//		try {
//			MongoSQLParser testQuery = new MongoSQLParser();
//			DBObject dbObject = testQuery.query("age in '10,100'", getColumnInfo());
//			
//			if(dbObject.toString().equals("{ \"age\" : { \"$in\" : [ 10 , 100]}}")) {
//			} else {
//				fail("make query error " + dbObject.toString());
//			}
//			
//		} catch(Exception e) {
//			fail(e.getMessage());
//		}
//	}
//
//}
