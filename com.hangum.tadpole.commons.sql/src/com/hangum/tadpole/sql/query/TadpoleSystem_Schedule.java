/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.sql.query;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.sql.dao.system.ScheduleDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * schedule
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_Schedule {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_Schedule.class);

	/**
	 * 
	 * @param userDB
	 * @param title
	 * @param desc
	 * @param cronExp
	 * @param listSchedule
	 */
	public static void addSchedule(final UserDBDAO userDB, String title, String desc, String cronExp, List<ScheduleDAO> listSchedule) {
		
	}
}
