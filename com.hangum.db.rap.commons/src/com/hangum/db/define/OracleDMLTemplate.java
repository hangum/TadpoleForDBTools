package com.hangum.db.define;

/**
 * Oracle DB의 디비를 정의합니다.
 * 
 * @author hangum
 *
 */
public class OracleDMLTemplate extends MySQLDMLTemplate {
	public static final String TMP_GET_PARTDATA = "%s where ROWNUM > %s and ROWNUM <= %s";

	// plan_table	
	public static final String TMP_EXPLAIN_EXTENDED = "EXPLAIN PLAN INTO %s FOR ";

	
}
