package com.hangum.tadpole.engine.sql.parser;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Tadpole SQL parser
 * 		
 * @author hangum
 *
 */
public interface TDBSQLParser {
//	/** SQL TYPE
//	 * http://www.orafaq.com/faq/what_are_the_difference_between_ddl_dml_and_dcl_commands
//	 */
//	public static enum SQL_TYPE {DDL, DML, DCL, TCL};
//
//	/** query type */
//	public static enum QUERY_TYPE {SELECT, INSERT, UPDATE, DELETE, DDL, UNKNOWN};
//	
//	/** query ddl type, 현재 jsqlparser에서는 이 세가지 타입만을 지원합니다 */
//	public static enum QUERY_DDL_TYPE {TABLE, VIEW, INDEX, PROCEDURE, FUNCTION, TRIGGER, PACKAGE, SYNONYM, UNKNOWN};
	 

	
	void parser(UserDBDAO userDB, String sql);
	PublicTadpoleDefine.SQL_TYPE getSQLType();
}
