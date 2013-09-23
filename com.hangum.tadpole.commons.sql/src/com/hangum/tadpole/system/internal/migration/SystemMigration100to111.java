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
package com.hangum.tadpole.system.internal.migration;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.system.TadpoleSystemQuery;
import com.hangum.tadpole.system.internal.migration.utils.SystemMigrationUtils;

/**
 * System migration 1.0.0 to 1.1.1
 * 
 * @author hangum
 *
 */
public class SystemMigration100to111 extends SystemMigration {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SystemMigration100to111.class);

	/**
	 * 
	 */
	public void migration(String major_version, String sub_version) throws Exception {
		try {
			// posgre sql일때 type을 바꾸었습니다.
			String strQuery = "UPDATE user_db SET dbms_types = '" + DBDefine.POSTGRE_DEFAULT.getDBToString() + "' WHERE dbms_types = 'postgre'";
			SystemMigrationUtils.runSQLExecuteBatch(strQuery);
			
			// 시스템 버전 정보를 수정해 줍니다.
			TadpoleSystemQuery.updateSystemVersion(major_version, sub_version);
			
		} catch(Exception e) {
			logger.error("System migration exception 1.0.0 to 1.1.1", e);
			
			throw e;
		}
	}
}
