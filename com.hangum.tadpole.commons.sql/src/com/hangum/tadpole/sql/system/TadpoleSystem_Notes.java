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
package com.hangum.tadpole.sql.system;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.NotesDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Notes
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_Notes {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_Notes.class);
	
	/**
	 * receive note list
	 * 
	 * @param userSeq
	 * @return
	 * @throws Exception
	 */
	public static List<NotesDAO> getReceiveNoteList(int userSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (List<NotesDAO>)sqlClient.queryForList("findReceiveNotes", userSeq); //$NON-NLS-1$
	}
	
}
