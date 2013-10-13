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
package com.hangum.tadpole.sql.system.internal.migration;

import org.apache.log4j.Logger;

import com.hangum.tadpole.sql.system.internal.migration.utils.SystemMigrationUtils;

/**
 * System migration 1.0.0 to 1.1.2
 * 
 * @author hangum
 *
 */
public class SystemMigration100to112 extends SystemMigration {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SystemMigration100to112.class);

	/**
	 * 
	 */
	public void migration(String major_version, String sub_version) throws Exception {
		try {
			String strQuery = "alter table user add security_question varchar(60)  not null  default 'QUESTION2'";
			SystemMigrationUtils.runSQLExecuteBatch(strQuery);
			
			strQuery = "alter table user add security_answer varchar(60)  not null  default 'tadpole'";
			SystemMigrationUtils.runSQLExecuteBatch(strQuery);
		} catch(Exception e) {
			logger.error("System migration exception 1.0.0 to 1.1.2", e);
			
			throw e;
		}
		updateVersion(major_version, sub_version);		
	}
}
