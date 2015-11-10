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
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.common.util.URI;
//import org.eclipse.emf.ecore.resource.Resource;
//import org.eclipse.xtext.resource.XtextResource;
//import org.eclipse.xtext.resource.XtextResourceSet;
//import org.eclipselabs.mongo.query.MongoSQLStandaloneSetup;
//import org.eclipselabs.mongo.query.query.AndWhereEntry;
//import org.eclipselabs.mongo.query.query.DoubleExpression;
//import org.eclipselabs.mongo.query.query.Expression;
//import org.eclipselabs.mongo.query.query.ExpressionWhereEntry;
//import org.eclipselabs.mongo.query.query.LongExpression;
//import org.eclipselabs.mongo.query.query.Operator;
//import org.eclipselabs.mongo.query.query.OrWhereEntry;
//import org.eclipselabs.mongo.query.query.SingleExpressionWhereEntry;
//import org.eclipselabs.mongo.query.query.StringExpression;
//import org.eclipselabs.mongo.query.query.WhereEntry;
//import org.eclipselabs.mongo.query.query.impl.ModelImpl;
//import org.eclipselabs.mongo.query.query.impl.OrWhereEntryImpl;
//
//import com.google.inject.Injector;
//import com.hangum.db.dao.mysql.TableColumnDAO;
//import com.hangum.tadpole.mongodb.core.Messages;
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBObject;
//
///**
// * MongoDB query parser
// * 
// * @author hangum
// *
// */
//public class MongoSQLParser {
//	static Logger logger = Logger.getLogger(MongoSQLParser.class);
//	
//	/**
//	 * where parser
//	 * 
//	 * @param where
//	 * @param columnInfo
//	 * @return
//	 */
//	public DBObject query(String where, Map<String, TableColumnDAO> columnInfo) throws Exception {
//		Injector injector = new MongoSQLStandaloneSetup().createInjectorAndDoEMFRegistration();
//		XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
//		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
//		
//		Resource resource = resourceSet.createResource(URI.createURI("dummy:/tadpole.mongosql")); //$NON-NLS-1$
//		
//		if(logger.isDebugEnabled()) logger.debug("\t-->[original]\t" + where);		 //$NON-NLS-1$
//		where = StringUtils.replace(where, " or ", " OR "); //$NON-NLS-1$ //$NON-NLS-2$
//		where = StringUtils.replace(where, " and ", " AND "); //$NON-NLS-1$ //$NON-NLS-2$
//		where = StringUtils.replace(where, " LIKE ", " like "); //$NON-NLS-1$ //$NON-NLS-2$
//		where = StringUtils.replace(where, " IN ", " in "); //$NON-NLS-1$ //$NON-NLS-2$
//		if(logger.isDebugEnabled()) logger.debug("\t-->[convert original]\t" + where); //$NON-NLS-1$
//		
//		InputStream in = new ByteArrayInputStream(("SELECT * FROM mongo://localhost:27017/test/rental WHERE " + where) .getBytes()); //$NON-NLS-1$
//		resource.load(in, resourceSet.getLoadOptions());
//		
//		ModelImpl model = (ModelImpl) resource.getContents().get(0);		
//        DBObject query = tansferModule(model, columnInfo);
//        
//        return query;
//	}
//	
//	/**
//	 * AND, OR 별로 분리
//	 * 
//	 * @param model
//	 * @return
//	 */
//	private DBObject tansferModule(ModelImpl model, Map<String, TableColumnDAO> columnInfo) throws Exception {
//		BasicDBObject query = new BasicDBObject();
//		WhereEntry rootEntry = model.getWhereEntry();
//
//		if(rootEntry instanceof ExpressionWhereEntry) {
//			if(logger.isDebugEnabled()) logger.debug("\t ExpressionWhereEntry"); //$NON-NLS-1$
//			transferExpressionWhereEntry(rootEntry, query, columnInfo);
//			
//		} else if(rootEntry instanceof AndWhereEntry) {
//			if(logger.isDebugEnabled()) logger.debug("\t AndWhereEntry"); //$NON-NLS-1$
//			transferAndWhereEntry(rootEntry, query, columnInfo);
//			
//		} else if(rootEntry instanceof OrWhereEntry) {
//			if(logger.isDebugEnabled()) logger.debug("\t OrWhereEntry"); //$NON-NLS-1$
//			transferOrWhereEntry(rootEntry, query, columnInfo);
//			
//		} else {
//			logger.error("***[MongoSQLParser][start] transfer head module *** "); //$NON-NLS-1$
//			logger.error("[msg]" + rootEntry); //$NON-NLS-1$
//			logger.error("***[MongoSQLParser][end] transfer head module *** "); //$NON-NLS-1$
//			
//			throw new Exception("***[MongoSQLParser] transfer head module *** [rootEntry]" + rootEntry); //$NON-NLS-1$
//		}
//		
//		return query;
//	}
//	
//	/**
//	 * or
//	 * 
//	 * @param entry
//	 * @param query
//	 */
//	private void transferOrWhereEntry(WhereEntry entry, DBObject query, Map<String, TableColumnDAO> columnInfo) throws Exception {
//		ArrayList<BasicDBObject> orList = new ArrayList<BasicDBObject>();
//		
//		OrWhereEntry root = (OrWhereEntry) entry;
//		EList<WhereEntry> list = root.getEntries();
//		
//		for(WhereEntry e : list) {
//			
//			if(e instanceof ExpressionWhereEntry) {
//				if(logger.isDebugEnabled()) logger.debug(" *** transfer or moudle[ExpressionWhereEntry][start] ****"); //$NON-NLS-1$
//				transferExpressionORWhereEntry(e, orList, columnInfo);
//				if(logger.isDebugEnabled()) logger.debug(" *** transfer or moudle[ExpressionWhereEntry][end] ****"); //$NON-NLS-1$
//				
//			} else if(e instanceof AndWhereEntry) {
//				if(logger.isDebugEnabled()) logger.debug(" *** transfer and moudle[or][[AndWhereEntry][start] ****"); //$NON-NLS-1$
//				// or 연산에서 and 연산으로 바뀌어 다시 초기화 해줍니다.
//				query.put("$or", orList); //$NON-NLS-1$
//				orList = new ArrayList<BasicDBObject>();
//				// or 연산에서 and 연산으로 바뀌어 다시 초기화 해줍니다.
//				
//				transferAndWhereEntry(e, query, columnInfo);
//				if(logger.isDebugEnabled()) logger.debug(" *** transfer and moudle[or][AndWhereEntry][end] ****"); //$NON-NLS-1$
//			} else {
//				logger.error(e);
//				throw new Exception ("Not Define case [or][transferOrWhereEntry] [WhereEntry]" + e); //$NON-NLS-1$
//			}
//		}
//		
//		// or 연산을 입력합니다.
//		if(orList.size() != 0) query.put("$or", orList); //$NON-NLS-1$
//	}
//	
//	/**
//	 * expression or where
//	 * 
//	 * @param entry
//	 * @param query
//	 */
//	private void transferExpressionORWhereEntry(WhereEntry entry, ArrayList<BasicDBObject> orList, Map<String, TableColumnDAO> columnInfo) throws Exception {
//		SingleExpressionWhereEntry expression = (SingleExpressionWhereEntry) entry;
//		if(logger.isDebugEnabled()) logger.debug("\t[OR][transferExpressionWhereEntry][name]" + expression.getName() + " [operation]" + expression.getOperator() + "[value]" + getValue(expression.getRhs()));			 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		
//		BasicDBObject dbObject = new BasicDBObject();
//		
//		TableColumnDAO column = columnInfo.get(expression.getName());
//		if(column == null) {
//			logger.error(expression.getName() + Messages.get().MongoSQLParser_29);
//			throw new Exception(expression.getName() + Messages.get().MongoSQLParser_29);
//		}
//
//		try {
//			if(expression.getOperator() == Operator.IN) {
//				
//				String strValue = getValue(expression.getRhs()).toString();
//				Object[] objValue = MongoDBJavaStrToJavaObj.convStrToObj(column.getType(), StringUtils.split(strValue, ",") ); //$NON-NLS-1$
//				
//				dbObject.put(expression.getName(), new BasicDBObject("$in", objValue)); //$NON-NLS-1$
//				
//			} else {
//				Object objValue = MongoDBJavaStrToJavaObj.convStrToObj(column.getType(), getValue(expression.getRhs()).toString() );
//				
//				// =
//				if(expression.getOperator() == Operator.EQUAL) {
//					dbObject.append(expression.getName(), objValue);
//					
//				// !=
//				} else if(expression.getOperator() == Operator.NOT_EQUAL) {
//					dbObject.put(expression.getName(), new BasicDBObject("$ne", objValue)); //$NON-NLS-1$
//					
//				// >
//				} else if(expression.getOperator() == Operator.GREATER_THEN) {
//					dbObject.put(expression.getName(), new BasicDBObject("$gt", objValue)); //$NON-NLS-1$
//				// >=
//				} else if(expression.getOperator() == Operator.GREATER_EQUAL) {
//					dbObject.put(expression.getName(), new BasicDBObject("$gte", objValue)); //$NON-NLS-1$
//					
//				// <
//				} else if(expression.getOperator() == Operator.LESS_THEN) {
//					dbObject.put(expression.getName(), new BasicDBObject("$lt", objValue)); //$NON-NLS-1$
//					
//				// <=
//				} else if(expression.getOperator() == Operator.LESS_EQUAL) {
//					dbObject.put(expression.getName(), new BasicDBObject("$lte", objValue)); //$NON-NLS-1$
//					
//				// like		
//				} else if(expression.getOperator() == Operator.LIKE) {
//					
//					Pattern regex = Pattern.compile(getValue(expression.getRhs()).toString());
//					dbObject.put(expression.getName(), regex);
//				} 
//			}
//		} catch(Exception e) {
//			logger.error(expression.getName() + Messages.get().MongoSQLParser_38 + column.getType() + Messages.get().MongoSQLParser_39, e);			
//			throw new Exception(expression.getName() + Messages.get().MongoSQLParser_38 + column.getType() + Messages.get().MongoSQLParser_39);
//		}
//		
//		// or 연산의 결과를 입력합니다.
//		orList.add(dbObject);
//	}
//	
//	/**
//	 * and
//	 * 
//	 * @param entry
//	 * @param query
//	 */
//	private void transferAndWhereEntry(WhereEntry entry, DBObject query, Map<String, TableColumnDAO> columnInfo) throws Exception {
//		AndWhereEntry root = (AndWhereEntry) entry;
//		EList<WhereEntry> list = root.getEntries();
//		for(WhereEntry e : list) {
//			if(e instanceof ExpressionWhereEntry) {
//				
//				if(logger.isDebugEnabled()) logger.debug(" *** transfer AND moudle[ExpressionWhereEntry][start] ****"); //$NON-NLS-1$
//				transferExpressionWhereEntry(e, query, columnInfo);
//				if(logger.isDebugEnabled()) logger.debug(" *** transfer AND moudle[ExpressionWhereEntry][end] ****"); //$NON-NLS-1$
//				
//			} else if(e instanceof OrWhereEntryImpl) {
//				
//				if(logger.isDebugEnabled()) logger.debug(" *** transfer and moudle[and][OrWhereEntryImpl] ****" );				 //$NON-NLS-1$
//				transferOrWhereEntry(e, query, columnInfo);				
//				if(logger.isDebugEnabled()) logger.debug(" *** transfer and moudle[and][OrWhereEntryImpl][end] ****"); //$NON-NLS-1$
//				
//			} else {
//				logger.error(e);
//				throw new Exception ("Not Define case [transferAndWhereEntry] " + e); //$NON-NLS-1$
//			}
//		}
//	}
//	
//	/**
//	 * expression and where
//	 * 
//	 * @param entry
//	 * @param query
//	 */
//	private void transferExpressionWhereEntry(WhereEntry entry, DBObject query, Map<String, TableColumnDAO> columnInfo) throws Exception {
//		SingleExpressionWhereEntry expression = (SingleExpressionWhereEntry) entry;
//		if(logger.isDebugEnabled()) logger.debug("\t[AND][transferExpressionWhereEntry][name]" + expression.getName() + " [operation]" + expression.getOperator() + "[value]" + getValue(expression.getRhs())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//		
//		TableColumnDAO column = columnInfo.get(expression.getName());
//		if(column == null) {
//			logger.error(expression.getName() + Messages.get().MongoSQLParser_50);
//			throw new Exception(expression.getName() + Messages.get().MongoSQLParser_50);
//		}
//		
//		try {
//			if(expression.getOperator() == Operator.IN) {
//				
//				String strValue = getValue(expression.getRhs()).toString();
//				logger.debug("[in]" + strValue);
//				Object objValue = MongoDBJavaStrToJavaObj.convStrToObj(column.getType(), StringUtils.split(strValue, ",") ); //$NON-NLS-1$
//				
//				query.put(expression.getName(), new BasicDBObject("$in", objValue)); //$NON-NLS-1$
//			} else {
//				
//				Object objValue = MongoDBJavaStrToJavaObj.convStrToObj(column.getType(), getValue(expression.getRhs()).toString() );
//				// =
//				if(expression.getOperator() == Operator.EQUAL) {
//					
//					query.put(expression.getName(), objValue);
//					
//				// !=
//				} else if(expression.getOperator() == Operator.NOT_EQUAL) {
//					query.put(expression.getName(), new BasicDBObject("$ne", objValue)); //$NON-NLS-1$
//					
//				// >
//				} else if(expression.getOperator() == Operator.GREATER_THEN) {
//					query.put(expression.getName(), new BasicDBObject("$gt", objValue)); //$NON-NLS-1$
//				// >=
//				} else if(expression.getOperator() == Operator.GREATER_EQUAL) {
//					query.put(expression.getName(), new BasicDBObject("$gte", objValue)); //$NON-NLS-1$
//					
//				// <
//				} else if(expression.getOperator() == Operator.LESS_THEN) {
//					query.put(expression.getName(), new BasicDBObject("$lt", objValue)); //$NON-NLS-1$
//					
//				// <=
//				} else if(expression.getOperator() == Operator.LESS_EQUAL) {
//					query.put(expression.getName(), new BasicDBObject("$lte", objValue)); //$NON-NLS-1$
//					
//				// like		
//				} else if(expression.getOperator() == Operator.LIKE) {
//					
//					Pattern regex = Pattern.compile(getValue(expression.getRhs()).toString());
//					query.put(expression.getName(), regex);
//				}
//			}
//		
//		} catch(Exception e) {
//			logger.error(expression.getName() + Messages.get().MongoSQLParser_38 + column.getType() + Messages.get().MongoSQLParser_39, e);			
//			throw new Exception(expression.getName() + Messages.get().MongoSQLParser_38 + column.getType() + Messages.get().MongoSQLParser_39);
//		}
//	}
//	
//	/**
//	 * Type of value
//	 * 
//	 * @param expr
//	 * @return
//	 */
//	private Object getValue(Expression expr){
//		if(expr instanceof StringExpression) {
//			return ((StringExpression) expr).getValue();
//		} else if(expr instanceof LongExpression) {
//			return ((LongExpression) expr).getValue();
//		} else if(expr instanceof DoubleExpression) {
//			return ((DoubleExpression) expr).getValue();
//		}
//		
//		return null;
//	}
////
////	public static void main(String[] args) throws Exception {
////		MongoSQLParser testQuery = new MongoSQLParser();
////		testQuery.query("rental_id<=10 and age>2", null);
////		
////	}
//}
