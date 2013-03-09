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
package com.hangum.tadpole.define;

/**
 * postgre DB의 디비를 정의합니다.
 * 
 * @author hangum
 *
 */
public class PostgreDMLTemplate extends MySQLDMLTemplate {
	public static final String TMP_GET_PARTDATA = "%s limit %s offset %s";

	// plan_table	
	public static final String TMP_EXPLAIN_EXTENDED = "EXPLAIN PLAN INTO %s FOR ";

	/** table */
	public static final String  TMP_CREATE_TABLE_STMT = "CREATE TABLE emp ( \r\n" +
														"    empname text, \r\n" +
														"    salary integer, \r\n" +
														"    last_date timestamp, \r\n" +
														"    last_user text \r\n" +
														"  );";
	
	/** view  */
	public static final String  TMP_CREATE_VIEW_STMT = "CREATE VIEW empComedies AS \r\n" +
													   " SELECT * \r\n" +
													   " FROM emp \r\n" +
													   " WHERE empname = 'Comedy';";
	
	/** index */
	public static final String  TMP_CREATE_INDEX_STMT = "CREATE UNIQUE INDEX empname_idx ON emp (empname);";
	
	
	
	/** procedure */
	public static final String  TMP_CREATE_PROCEDURE_STMT = "CREATE PROCEDURE simpleproc (OUT param1 INT) \r\n"+
																	  " BEGIN  \r\n"+
																	  "  SELECT COUNT(*) INTO param1 FROM t;  \r\n"+
																	  " END;";
	
	
	/** function */
	public static final String TMP_CREATE_FUNCTION_STMT = "CREATE FUNCTION emp_stamp() RETURNS trigger AS $emp_stamp$  \r\n"+
														  "  BEGIN  \r\n"+
														  "      -- Check that empname and salary are given  \r\n"+
														  "      IF NEW.empname IS NULL THEN \r\n"+
														  "          RAISE EXCEPTION 'empname cannot be null'; \r\n"+
														  "      END IF; \r\n"+
														  "      IF NEW.salary IS NULL THEN \r\n"+
														  "          RAISE EXCEPTION '% cannot have null salary', NEW.empname; \r\n"+
														  "      END IF; \r\n"+														
														  "      -- Who works for us when she must pay for it? \r\n"+
														  "      IF NEW.salary < 0 THEN \r\n"+
														  "          RAISE EXCEPTION '% cannot have a negative salary', NEW.empname; \r\n"+
														  "      END IF; \r\n"+														
														  "      -- Remember who changed the payroll when \r\n"+
														  "      NEW.last_date := current_timestamp; \r\n"+
														  "      NEW.last_user := current_user; \r\n"+
														  "      RETURN NEW; \r\n"+
														  "  END; \r\n"+
														  "	$emp_stamp$ LANGUAGE plpgsql;";	
	
	/** trigger */
	public static final String TMP_CREATE_TRIGGER_STMT = "CREATE TRIGGER emp_stamp BEFORE INSERT OR UPDATE ON films  \r\n" +
														" FOR EACH ROW EXECUTE PROCEDURE emp_stamp();";
}
