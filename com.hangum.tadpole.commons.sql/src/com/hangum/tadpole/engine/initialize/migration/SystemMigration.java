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
package com.hangum.tadpole.engine.initialize.migration;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.sql.TadpoleSystemQuery;

/**
 * System abstract class
 * 
 * @author hangum
 *
 */
public abstract class SystemMigration {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SystemMigration.class);
	
	protected void updateVersion(String major_version, String sub_version) throws Exception {
		try {
			// 시스템 버전 정보를 수정해 줍니다.
			TadpoleSystemQuery.updateSystemVersion(major_version, sub_version);
			
		} catch(Exception e) {
			logger.error("System migration exception : update version number", e);
			
			throw e;
		}
	}

	public abstract void migration(String major_version, String sub_version) throws Exception;
}
