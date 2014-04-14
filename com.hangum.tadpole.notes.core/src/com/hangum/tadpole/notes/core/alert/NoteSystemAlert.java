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
package com.hangum.tadpole.notes.core.alert;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.NotesDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_Notes;

/**
 * note system alert
 * 
 * @author hangum
 *
 */
public class NoteSystemAlert {
	private static final Logger logger = Logger.getLogger(NoteSystemAlert.class);
	
	/**
	 * system note alert list
	 * 
	 * @return
	 */
	public static List<NotesDAO> getSystemNoteAlert() {
		
		try {
			return TadpoleSystem_Notes.getAlertNote(SessionManager.getSeq());
		} catch (Exception e) {
			logger.error("System Note alert exception", e);
		}
		
		return new ArrayList<NotesDAO>();
	}
	
}
