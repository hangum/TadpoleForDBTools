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
package com.hangum.tadpole.sql.system.internal.initialize;

import org.eclipse.osgi.util.NLS;

public class TadpoleMySQLDDL extends NLS {
	private static String BUNDLE_NAME = "com.hangum.tadpole.sql.system.internal.initialize.tadpole_mysql_ddl"; //$NON-NLS-1$

	/*
	 * 
	 * dbms의 참조무결성 오류가 발생할 수 있으므로 생성순서를 준수해야 한다. dbms error can occur because
	 * the referential integrity of the created order must be observed.
	 */
	public static String tadpole_system;
	public static String tadpole_system_pk;
	public static String user_group;
	public static String user_group_pk;
	public static String user;
	public static String user_pk;
	public static String user_role;
	public static String user_role_pk;
	public static String ext_account;
	public static String ext_account_pk;
	public static String ext_account_seq;
	public static String user_db;
	public static String user_db_pk;
	public static String user_db_filter;
	public static String user_db_filter_pk;
	public static String user_db_ext;
	public static String user_db_ext_pk;
	public static String security_class;
	public static String security_class_pk;
	public static String data_security;
	public static String data_security_pk;
	public static String user_db_resource;
	public static String user_db_resource_pk;
	public static String user_db_resource_data;
	public static String user_db_resource_data_pk;
	public static String user_info_data;
	public static String user_info_data_pk;
	public static String executed_sql_resource;
	public static String executed_sql_resource_pk;
	public static String executed_sql_resource_seq;
	public static String executed_sql_resource_data;
	public static String executed_sql_resource_data_pk;
	public static String data_security_seq;
	public static String data_security_fk;
	public static String data_security_fk2;
	public static String data_security_fk3;
	public static String executed_sql_resource_data_fk;
	public static String executed_sql_resource_data_fk2;
	public static String executed_sql_resource_data_seq;
	public static String executed_sql_resource_fk;
	public static String executed_sql_resource_fk2;
//	public static String ext_account_fk;
	public static String security_class_fk;
	public static String user_db_ext_fk;
	public static String user_db_filter_fk;
	public static String user_db_fk;
	// user_info_data 입력시 참조무결성 제약조건 위반 오류. 임시로 막음.
//	public static String user_db_fk2;
	public static String user_db_fk3;
	public static String user_db_resource_fk;
	public static String user_db_resource_fk2;
	public static String user_db_resource_fk3;
	public static String user_info_data_fk;
	// user_info_data 입력시 참조무결성 제약조건 위반 오류. 임시로 막음.
	//public static String user_info_data_fk2;
	public static String user_role_fk;
	public static String user_role_fk2;

	public static String security_class_seq;
	public static String tadpole_system_seq;
	public static String user_seq;
	public static String user_db_seq;
	public static String user_db_ext_seq;
	public static String user_db_filter_seq;
	public static String user_db_resource_seq;
	public static String user_db_resource_data_seq;
	public static String user_group_seq;
	public static String user_info_data_seq;
	public static String user_role_seq;
	
	public static String notes;
	public static String notes_pk;
	public static String notes_seq;
	public static String notes_fk_sender;
	public static String notes_fk_receiver;
	
	public static String notes_detail;
	public static String notes_detail_pk;
	public static String notes_detail_fk;
	public static String notes_detail_seq;
	
	public static String external_browser_info;
	public static String external_browser_info_pk;
	public static String external_browser_info_seq;
	public static String external_browser_info_fk;
	
	
	public static String schema_history;
	public static String schema_history_pk;
	public static String schema_history_seq;
	public static String schema_history_fk_dbseq;
	
	public static String schema_history_detail;
	public static String schema_history_detail_fk_userseq;
	public static String schema_history_detail_pk;
	public static String schema_history_detail_seq;
	
	
	public static String schedule_main;
	public static String pk_schedule_main;
	public static String schedule_main_seq;
	
	public static String schedule_detail;
	public static String pk_schedule_detail;
	public static String schedule_detail_seq;
	
	public static String schedule;
	public static String pk_schedule;
	public static String schedule_seq;
	
	public static String schedule_result;
	public static String pk_schedule_result;
	public static String schedule_result_seq;
	
	public static String fk_user_db_to_schedule_main;
	public static String fk_user_to_schedule_main;
	public static String fk_schedule_to_schedule_detail;
	public static String fk_schedule_main_to_schedule;
	public static String fk_schedule_main_to_schedule_result;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, TadpoleMySQLDDL.class);
	}

	public TadpoleMySQLDDL() {
	}
}
