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

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_STATUS;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DML_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.SQL_TYPE;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.parser.BasicTDBSQLParser;
import com.hangum.tadpole.engine.sql.parser.dto.QueryInfoDTO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.session.manager.SessionManager;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
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
public class RequestQuery implements Cloneable {
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
	
	/** {@link PublicTadpoleDefine.SQL_STATEMENT_TYPE#PREPARED_STATEMENT} 일경우에 변수에 값이 들어간 전체 퀄기ㅏ 들어가 있다 */
	private String sqlAddParameter = "";
	
	/** 현재 실행 중인 sql statement type */
	private PublicTadpoleDefine.SQL_STATEMENT_TYPE sqlStatementType = PublicTadpoleDefine.SQL_STATEMENT_TYPE.NONE;
	/** SQL이 preparedStatement일 경우 SQL_STATEMENT_TYPE.PREPARED_STATEMENT 상태일때의 파라미터 종류 */ 
	private Object[] statementParameter = null;
	
	private OBJECT_TYPE dbAction = OBJECT_TYPE.TABLES;
	
	/** 사용자 쿼리를 지정한다 */
	private EditorDefine.QUERY_MODE mode = EditorDefine.QUERY_MODE.QUERY;
			
	/** 사용자가 쿼리를 실행 하는 타입 */
	private EditorDefine.EXECUTE_TYPE executeType = EditorDefine.EXECUTE_TYPE.NONE;

	/** sql is statement */
	private boolean isStatement = false;
	
	/** sql type */
	private SQL_TYPE sqlType = SQL_TYPE.DML;
	
	/** User request query type */
	private QUERY_DML_TYPE sqlDMLType = QUERY_DML_TYPE.UNKNOWN;
	
	/** TABLE, INDEX, VIEW, OTHERES */
	private QUERY_DDL_TYPE sqlDDLType = QUERY_DDL_TYPE.UNKNOWN;
	private String sqlObjectName = "";
	private QUERY_DDL_STATUS queryStatus = QUERY_DDL_STATUS.UNKNOWN;
	
	
	/** query result */
	private RequestResultDAO resultDao = new RequestResultDAO();
	
	/**
	 * 
	 * @param userDB 
	 * @param originalSql 쿼리
	 * @param dbAction 수행을 요청한 곳.
	 * @param mode 전체인지, 부분인지 {@code EditorDefine.QUERY_MODE}
	 * @param type 쿼리, 실행 계획인지 {@code EditorDefine.EXECUTE_TYPE}
	 * @param isAutoCommit autocommit
	 */
	public RequestQuery(UserDBDAO userDB, String originalSql, OBJECT_TYPE dbAction, EditorDefine.QUERY_MODE mode, EditorDefine.EXECUTE_TYPE type, boolean isAutoCommit) {
		//
		// 사용자가 네트웍을 바꾸어서 사용하면 어떻게 되지???
		//
		this.userIp = SessionManager.getLoginIp();// RWT.getRequest().getRemoteAddr();
		
		this.originalSql = originalSql;
		this.dbAction = dbAction;
		this.sql = SQLUtil.makeExecutableSQL(userDB, originalSql);
		parseSQL(this.sql);
		
//		logger.debug("================================================================================================");
//		logger.debug("[originalSql]" + originalSql);
//		logger.debug("[sql]" + sql);
//		logger.debug("================================================================================================");
		this.mode = mode;
		this.executeType = type;
		this.isAutoCommit = isAutoCommit;
	}

	/**
	 * implements clone
	 */
	public Object clone() throws CloneNotSupportedException {
		RequestQuery clone = (RequestQuery)super.clone();
		return clone;
	}
	
	/**
	 * sql of query type
	 * 
	 * @param sql
	 * @return query type
	 */
	public void parseSQL(String sql) {
		BasicTDBSQLParser parser = new BasicTDBSQLParser();
		QueryInfoDTO queryInfoDto = parser.parser(sql);
		setStatement(queryInfoDto.isStatement());

		try {
			Statement statement = CCJSqlParserUtil.parse(sql);
			setSqlType(SQL_TYPE.DML);
		
			if(statement instanceof Select) {
				sqlDMLType = QUERY_DML_TYPE.SELECT;
			} else if(statement instanceof Insert) {
				sqlDMLType = QUERY_DML_TYPE.INSERT;
			} else if(statement instanceof Update) {
				sqlDMLType = QUERY_DML_TYPE.UPDATE;
			} else if(statement instanceof Delete) {
				sqlDMLType = QUERY_DML_TYPE.DELETE;
			}
		} catch (Throwable e) {
			logger.error(String.format("sql parse exception. [ %s ]", sql));
		}
		
		if(sqlDMLType.equals(QUERY_DML_TYPE.UNKNOWN)) {
			if(queryInfoDto.isStatement()) {
				setSqlType(SQL_TYPE.DML);
				sqlDMLType = QUERY_DML_TYPE.UNKNOWN;
				
			} else {
				setSqlType(SQL_TYPE.DDL);
				sqlDDLType = queryInfoDto.getQueryDDLType();
				queryStatus = queryInfoDto.getQueryStatus();
				sqlObjectName = queryInfoDto.getObjectName();
				
				//
				// CREATE, ALTER , 
				// 프로시저, 펑션, 트리거, 패키지, 마지막에 ; 문자가 있어야 정상 실행 됩니다. - 좀더 확인 필요 hangum
				// 시노님은없어야합니다. - 2016-07-29 (niliri)
				//
				if(queryStatus == QUERY_DDL_STATUS.CREATE | queryStatus == QUERY_DDL_STATUS.ALTER)
				if(sqlDDLType == QUERY_DDL_TYPE.PROCEDURE | sqlDDLType == QUERY_DDL_TYPE.FUNCTION | sqlDDLType == QUERY_DDL_TYPE.TRIGGER | sqlDDLType == QUERY_DDL_TYPE.PACKAGE) {
					if(!StringUtils.endsWith(this.sql, PublicTadpoleDefine.SQL_DELIMITER)) {
						this.sql += PublicTadpoleDefine.SQL_DELIMITER;
					}
				}
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

	/**
	 * @return the isStatement
	 */
	public boolean isStatement() {
		return isStatement;
	}

	/**
	 * @param isStatement the isStatement to set
	 */
	public void setStatement(boolean isStatement) {
		this.isStatement = isStatement;
	}

	/**
	 * @return the sqlType
	 */
	public SQL_TYPE getSqlType() {
		return sqlType;
	}

	/**
	 * @param sqlType the sqlType to set
	 */
	public void setSqlType(SQL_TYPE sqlType) {
		this.sqlType = sqlType;
	}

	/**
	 * @return the sqlDMLType
	 */
	public QUERY_DML_TYPE getSqlDMLType() {
		return sqlDMLType;
	}

	/**
	 * @param sqlDMLType the sqlDMLType to set
	 */
	public void setSqlDMLType(QUERY_DML_TYPE sqlDMLType) {
		this.sqlDMLType = sqlDMLType;
	}

	/**
	 * @return the sqlDDLType
	 */
	public QUERY_DDL_TYPE getSqlDDLType() {
		return sqlDDLType;
	}

	/**
	 * @param sqlDDLType the sqlDDLType to set
	 */
	public void setSqlDDLType(QUERY_DDL_TYPE sqlDDLType) {
		this.sqlDDLType = sqlDDLType;
	}

	/**
	 * @return the sqlObjectName
	 */
	public String getSqlObjectName() {
		return sqlObjectName;
	}

	/**
	 * @param sqlObjectName the sqlObjectName to set
	 */
	public void setSqlObjectName(String sqlObjectName) {
		this.sqlObjectName = sqlObjectName;
	}

	/**
	 * @return the queryStatus
	 */
	public QUERY_DDL_STATUS getQueryStatus() {
		return queryStatus;
	}

	/**
	 * @param queryStatus the queryStatus to set
	 */
	public void setQueryStatus(QUERY_DDL_STATUS queryStatus) {
		this.queryStatus = queryStatus;
	}
	
	public PublicTadpoleDefine.SQL_STATEMENT_TYPE getSqlStatementType() {
		return sqlStatementType;
	}

	public void setSqlStatementType(PublicTadpoleDefine.SQL_STATEMENT_TYPE sqlStatementType) {
		this.sqlStatementType = sqlStatementType;
	}
	
	/**
	 * @return the statementParameter
	 */
	public Object[] getStatementParameter() {
		return statementParameter;
	}

	/**
	 * @param statementParameter the statementParameter to set
	 */
	public void setStatementParameter(Object[] statementParameter) {
		this.statementParameter = statementParameter;
	}
	
	/**
	 * @return the sqlAddParameter
	 */
	public String getSqlAddParameter() {
		return sqlAddParameter;
	}

	/**
	 * @param sqlAddParameter the sqlAddParameter to set
	 */
	public void setSqlAddParameter(String sqlAddParameter) {
		this.sqlAddParameter = sqlAddParameter;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RequestQuery [userIp=" + userIp + ", isAutoCommit=" + isAutoCommit + ", originalSql=" + originalSql + ", sqlStatementType=" + sqlStatementType
				+ ", sql=" + sql + ", \ndbAction=" + dbAction + ", mode=" + mode + ", executeType=" + executeType
				+ ", \nisStatement=" + isStatement + ", sqlType=" + sqlType + ", sqlDMLType=" + sqlDMLType
				+ ", \nsqlDDLType=" + sqlDDLType + ", sqlObjectName=" + sqlObjectName + ", queryStatus=" + queryStatus
				+ ", \nresultDao=" + resultDao + "]";
	}
	
}
