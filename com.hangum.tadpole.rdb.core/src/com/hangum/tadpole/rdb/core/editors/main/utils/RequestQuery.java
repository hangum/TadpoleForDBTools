/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_TYPE;
import com.hangum.tadpole.engine.sql.util.SQLUtil;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 에디터에서 사용자가 실행하려는 쿼리 정보를 정의합니다. 
 * 
 * @author hangum
 *
 */
public class RequestQuery {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(RequestQuery.class);
	
	/** 쿼리 실행자 ip */	
	private String userIp = ""; 
	
	/** 요청 쿼리가 오토 커밋이었는지 */
	private boolean isAutoCommit = false;
	
	/** 초기 입력 받은 sql */
	private String originalSql = "";
	
	/** 에디터가 실행 가능한 쿼리로 수정한 */
	private String sql = "";
	
	private OBJECT_TYPE dbAction = OBJECT_TYPE.TABLES;
	
	/** 사용자 쿼리를 지정한다 */
	private EditorDefine.QUERY_MODE mode = EditorDefine.QUERY_MODE.QUERY;
			
	/** 사용자가 쿼리를 실행 하는 타입 */
	private EditorDefine.EXECUTE_TYPE executeType = EditorDefine.EXECUTE_TYPE.NONE;
	
	/** User request query type */
	private PublicTadpoleDefine.QUERY_TYPE queryType = PublicTadpoleDefine.QUERY_TYPE.SELECT;
	
	/** TABLE, INDEX, VIEW, OTHERES */
	private QUERY_DDL_TYPE queryDDLType = QUERY_DDL_TYPE.UNKNOWN;
	
	/** query result */
	private RequestResultDAO resultDao = new RequestResultDAO();

	/**
	 * 
	 * @param originalSql 쿼리
	 * @param dbAction 수행을 요청한 곳.
	 * @param mode 전체인지, 부분인지 {@code EditorDefine.QUERY_MODE}
	 * @param type 쿼리, 실행 계획인지 {@code EditorDefine.EXECUTE_TYPE}
	 * @param isAutoCommit autocommit
	 */
	public RequestQuery(String originalSql, OBJECT_TYPE dbAction, EditorDefine.QUERY_MODE mode, EditorDefine.EXECUTE_TYPE type, boolean isAutoCommit) {
		this.userIp = RWT.getRequest().getRemoteAddr();
		
		this.originalSql = originalSql;
		this.dbAction = dbAction;
		this.sql = SQLUtil.sqlExecutable(originalSql);
		sqlQueryType(this.sql);
		
		this.mode = mode;
		this.executeType = type;
		this.isAutoCommit = isAutoCommit;
	}

	/**
	 * sql of query type
	 * 
	 * @param sql
	 * @return query type
	 */
	public void sqlQueryType(String sql) {
		
		try {
			Statement statement = CCJSqlParserUtil.parse(sql);
			if(statement instanceof Select) {
				queryType = QUERY_TYPE.SELECT;
			} else if(statement instanceof Insert) {
				queryType = QUERY_TYPE.INSERT;
			} else if(statement instanceof Update) {
				queryType = QUERY_TYPE.UPDATE;
			} else if(statement instanceof Delete) {
				queryType = QUERY_TYPE.DELETE;
			} else {
				queryType = QUERY_TYPE.DDL;
				
				if(statement instanceof CreateTable) {
//					logger.debug( "table name is " + ((CreateTable) statement).getTable().getName() );
					queryDDLType = QUERY_DDL_TYPE.TABLE;
				} else if(statement instanceof CreateView) {
//					logger.debug( "view name is " + ((CreateView) statement).getView().getName() );
					queryDDLType = QUERY_DDL_TYPE.VIEW;
				} else if(statement instanceof CreateIndex) {
//					logger.debug( "index name is " + ((CreateIndex) statement).getIndex().getName() );
					queryDDLType = QUERY_DDL_TYPE.INDEX;
				}		
			}
			
		} catch (Throwable e) {
//			logger.error(String.format("sql parse exception. [ %s ]", sql), e);
			logger.error(String.format("sql parse exception. [ %s ]", sql));
			
			// if does exist "CREATE or replace PROCEDURE"
			if(StringUtils.startsWithIgnoreCase(sql, "CREATE or replace PROCEDURE")) {
				queryType 		= QUERY_TYPE.DDL;
				queryDDLType 	= QUERY_DDL_TYPE.PROCEDURE;
			} else {
				queryType 	= QUERY_TYPE.UNKNOWN;
			}
		}
	}
	

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * @return the mode
	 */
	public EditorDefine.QUERY_MODE getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(EditorDefine.QUERY_MODE mode) {
		this.mode = mode;
	}

	/**
	 * @return the executeType
	 */
	public EditorDefine.EXECUTE_TYPE getExecuteType() {
		return executeType;
	}

	/**
	 * @param executeType the executeType to set
	 */
	public void setExecuteType(EditorDefine.EXECUTE_TYPE executeType) {
		this.executeType = executeType;
	}

	/**
	 * @return the queryType
	 */
	public PublicTadpoleDefine.QUERY_TYPE getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType the queryType to set
	 */
	public void setQueryType(PublicTadpoleDefine.QUERY_TYPE queryType) {
		this.queryType = queryType;
	}

	/**
	 * @return the originalSql
	 */
	public String getOriginalSql() {
		return originalSql;
	}

	/**
	 * @param originalSql the originalSql to set
	 */
	public void setOriginalSql(String originalSql) {
		this.originalSql = originalSql;
	}

	/**
	 * @return the isAutoCommit
	 */
	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	/**
	 * @param isAutoCommit the isAutoCommit to set
	 */
	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

	/**
	 * @return the userIp
	 */
	public String getUserIp() {
		return userIp;
	}

	/**
	 * @param userIp the userIp to set
	 */
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	/**
	 * @return the dbAction
	 */
	public OBJECT_TYPE getDbAction() {
		return dbAction;
	}

	/**
	 * @param dbAction the dbAction to set
	 */
	public void setDbAction(OBJECT_TYPE dbAction) {
		this.dbAction = dbAction;
	}

	/**
	 * @return the queryDDLType
	 */
	public QUERY_DDL_TYPE getQueryDDLType() {
		return queryDDLType;
	}

	/**
	 * @param queryDDLType the queryDDLType to set
	 */
	public void setQueryDDLType(QUERY_DDL_TYPE queryDDLType) {
		this.queryDDLType = queryDDLType;
	}

	/**
	 * @return the resultDao
	 */
	public RequestResultDAO getResultDao() {
		return resultDao;
	}

	/**
	 * @param resultDao the resultDao to set
	 */
	public void setResultDao(RequestResultDAO resultDao) {
		this.resultDao = resultDao;
	}

}
