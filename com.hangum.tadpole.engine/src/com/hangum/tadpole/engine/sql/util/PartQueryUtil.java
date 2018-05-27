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
package com.hangum.tadpole.engine.sql.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.template.AltibaseDMLTemplate;
import com.hangum.tadpole.engine.sql.template.CubridDMLTemplate;
import com.hangum.tadpole.engine.sql.template.HIVEDMLTemplate;
import com.hangum.tadpole.engine.sql.template.MSSQLDMLTemplate;
import com.hangum.tadpole.engine.sql.template.MySQLDMLTemplate;
import com.hangum.tadpole.engine.sql.template.OracleDMLTemplate;
import com.hangum.tadpole.engine.sql.template.PostgreDMLTemplate;
import com.hangum.tadpole.engine.sql.template.RedShiftDMLTemplate;
import com.hangum.tadpole.engine.sql.template.SQLiteDMLTemplate;
import com.hangum.tadpole.engine.sql.template.TiberoDMLTemplate;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * 각 DBMS에 맞는 쿼리문을 생성합니다.
 * 
 * @author hangum
 *
 */
public class PartQueryUtil {
	private static final Logger logger = Logger.getLogger(PartQueryUtil.class);
	
	/**
	 *  각 DBMS에 맞는 SELECT 문을 만들어줍니다.
	 *  
	 *  @param userDB 사용디비
	 *  @param strQuery 사용 쿼리
	 *  @param intStartPosition 시작 포인트
	 *  @param intRowCnt 몇 건 데이터
	 *  
	 * @return
	 */
	public static String makeSelect(UserDBDAO userDB, String strQuery, int intStartPos, int intRowCnt) {
		String requestQuery = strQuery;
		
//		if(logger.isDebugEnabled()) logger.debug("make select : " + intStartPos + ", " + intRowCnt);
		
		if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			strQuery = ifDuplicateColumnToChange(userDB, strQuery);
			requestQuery = String.format(MySQLDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intRowCnt);
		} else if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()) {
			strQuery = ifDuplicateColumnToChange(userDB, strQuery);
			requestQuery = String.format(OracleDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intStartPos+intRowCnt);

		} else if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) {
			requestQuery = String.format(SQLiteDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intRowCnt);
		} else if(DBGroupDefine.CUBRID_GROUP == userDB.getDBGroup()) {
			requestQuery = String.format(CubridDMLTemplate.TMP_GET_PARTDATA, strQuery, intStartPos, intRowCnt);
		} else if(DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup()) {
			requestQuery = String.format(PostgreDMLTemplate.TMP_GET_PARTDATA, strQuery,  intStartPos, intRowCnt);
		} else if(DBGroupDefine.ALTIBASE_GROUP == userDB.getDBGroup()) {
			requestQuery = String.format(AltibaseDMLTemplate.TMP_GET_PARTDATA, strQuery,  intStartPos, intRowCnt);

		}
		
		return requestQuery;
	}
	
	/**
	 * 1) 중복 이름의 컬럼이 있는지 검사하여 넘겨준다.
	 * 2) oracle 일 경우 rownum, rowid 가 있을 경우 alias 시켜줍니다.
	 * 
	 * @param userDB
	 * @param strQuery
	 * @return
	 */
	private static String ifDuplicateColumnToChange(UserDBDAO userDB, String strQuery) {
		try {
			Statement statement = CCJSqlParserUtil.parse(strQuery);
			List<String> listColumn = new ArrayList<String>();
			if(statement instanceof Select) {
				Select selectStatement = (Select) statement;
				PlainSelect ps = (PlainSelect)selectStatement.getSelectBody();
				List<SelectItem> listSelectItems = ps.getSelectItems();

				int intSameNameCnt = 0;
				for (SelectItem selectItem : listSelectItems) {
					if(selectItem instanceof SelectExpressionItem) {
						Expression expression = ((SelectExpressionItem)selectItem).getExpression();
						
						try {
							Column col = (Column)expression;
							Alias alias = ((SelectExpressionItem)selectItem).getAlias();
							
							// 오라클 디비일 경우는 rowid, rownum 일 경우 alias 이름을 붙여줍니다.
				            if(userDB.getDBGroup() == DBGroupDefine.ORACLE_GROUP) {
				            	if("ROWID".equals(StringUtils.upperCase(col.getColumnName()))) {
				            		col.setColumnName(col.getColumnName() + " " + "rowid_as");
				            	} else if("ROWNUM".equals(StringUtils.upperCase(col.getColumnName()))) {
				            		col.setColumnName(col.getColumnName() + " " + "rownum_as");
				            	}
				            }
							
							String strColumn = "";
				            if(alias == null) strColumn = col.getColumnName();
				            else strColumn = alias.getName();
				            
				            if(listColumn.contains(strColumn)) {
				            	// 중복 이름이 존재하면 기존 컬럼 이름을 _1 를 붙인다.
				            	strColumn = strColumn + "_" + intSameNameCnt;
				            	if(alias == null) {
				            		col.setColumnName(col.getColumnName() + " " + strColumn);
				            	}
					            else alias.setName(strColumn);
				            	intSameNameCnt++;
				            }
				            
			            	listColumn.add(strColumn);
						} catch(Exception e) {
							// ignore exception
						}
					}
				}
				if(intSameNameCnt >= 1) {
					String strChangeQuery = statement.toString();
					if(logger.isDebugEnabled()) logger.debug("-----> column Name change query: " + strChangeQuery);
					return strChangeQuery;
				}
			}
			
		} catch(Exception e) {
			logger.error("SQL JParser exception.", e);
		}
		
		return strQuery;
	}
	
	/**
	 * 각 dbms에 맞는 explain query를 만들어 줍니다.
	 * 
	 * @param userDB
	 * @param query
	 * @return
	 */
	public static String makeExplainQuery(UserDBDAO userDB, String query) throws SQLException {
		String resultQuery = "";
		
		if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			resultQuery = MySQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			
		} else if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()) {
			if(DBDefine.ORACLE_DEFAULT == userDB.getDBDefine()) {
				resultQuery =  OracleDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			} else if(DBDefine.TIBERO_DEFAULT == userDB.getDBDefine()) {
				resultQuery =  String.format(TiberoDMLTemplate.TMP_EXPLAIN_EXTENDED, "%" + query + "%");
			}
		} else if(DBGroupDefine.MSSQL_GROUP == userDB.getDBGroup()) {
	      resultQuery =  MSSQLDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBGroupDefine.SQLITE_GROUP == userDB.getDBGroup()) {
			resultQuery = SQLiteDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBGroupDefine.CUBRID_GROUP == userDB.getDBGroup()) {
			resultQuery = query;
		} else if(DBGroupDefine.HIVE_GROUP == userDB.getDBGroup()) {
			resultQuery = HIVEDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
		} else if(DBGroupDefine.POSTGRE_GROUP == userDB.getDBGroup()) {
			if(userDB.getDBDefine() == DBDefine.AMAZON_REDSHIFT_DEFAULT) {
				resultQuery = RedShiftDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			} else {
				resultQuery = PostgreDMLTemplate.TMP_EXPLAIN_EXTENDED + query;
			}
			
		} else {
			throw new SQLException(String.format("%s Database not uupport DBMS Query Plan.", userDB.getDBDefine().getDBToString()));
		}

		if(logger.isDebugEnabled()) logger.debug("[plan query]" + resultQuery);
		
		return resultQuery;
	}
}
