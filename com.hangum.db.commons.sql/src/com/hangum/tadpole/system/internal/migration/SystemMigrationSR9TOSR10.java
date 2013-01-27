/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.system.internal.migration;

import org.apache.log4j.Logger;

import com.hangum.tadpole.system.TadpoleSystemQuery;

/**
 * System migration sr9 to sr10
 * 
 * @author hangum
 *
 */
public class SystemMigrationSR9TOSR10 {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SystemMigrationSR9TOSR10.class);

	/**
	 * user_db table add column 
	 * 		ext1 VARCHAR(2000),
     		ext2 VARCHAR(2000), 
     		ext3 VARCHAR(2000),
	 * 
	 */
	public static void migration(String major_version, String sub_version) throws Exception {
		try {
			SystemMigration.runSQLExecuteBatch("ALTER TABLE user_db ADD COLUMN ext1 VARCHAR(2000)");
			SystemMigration.runSQLExecuteBatch("ALTER TABLE user_db ADD COLUMN ext2 VARCHAR(2000)");
			SystemMigration.runSQLExecuteBatch("ALTER TABLE user_db ADD COLUMN ext3 VARCHAR(2000)");
		
			// 시스템 버전 정보를 수정해 줍니다.
			TadpoleSystemQuery.updateSystemVersion(major_version, sub_version);
			
		} catch(Exception e) {
			logger.error("update system version(SR9 to SR10)", e);
			
			throw e;
		}
	}
}
