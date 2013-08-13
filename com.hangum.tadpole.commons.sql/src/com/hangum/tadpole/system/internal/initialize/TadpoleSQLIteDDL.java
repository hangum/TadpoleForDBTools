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
package com.hangum.tadpole.system.internal.initialize;

import org.eclipse.osgi.util.NLS;

public class TadpoleSQLIteDDL extends NLS {
	private static String BUNDLE_NAME = "com.hangum.tadpole.system.internal.initialize.tadpole_sqlite_ddl"; //$NON-NLS-1$

	public static String tadpole_system;
	public static String user_group;
	public static String user;
	public static String user_role;
	public static String ext_account;
	public static String user_db;
	public static String user_db_filter;
	public static String user_db_ext;
	public static String security_class;
	public static String data_security;
	public static String user_db_resource;
	public static String user_db_resource_data;
	public static String user_info_data;
	public static String executed_sql_resource;
	public static String executed_sql_resource_data;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, TadpoleSQLIteDDL.class);
	}

	public TadpoleSQLIteDDL() {
	}
}
