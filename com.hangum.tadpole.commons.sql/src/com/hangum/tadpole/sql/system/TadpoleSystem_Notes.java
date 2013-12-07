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

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.NotesDAO;
import com.hangum.tadpole.sql.dao.system.NotesDetailDAO;
import com.hangum.tadpole.sql.util.SQLUtil;
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
	 * @param types
	 * @param userSeq
	 * @return
	 * @throws Exception
	 */
	public static List<NotesDAO> getNoteList(String types, int userSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("types", types);
		mapParam.put("userSeq", userSeq);
		
		return (List<NotesDAO>)sqlClient.queryForList("findNotes", mapParam); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * 
	 * @param senderSeq
	 * @param receiveSeq
	 * @param title
	 * @param strContent
	 * @throws Exception
	 */
	public static void saveNote(String types, int senderSeq, int receiveSeq, String title, String strContent) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());		
		
		NotesDAO noteDao = new NotesDAO();
		noteDao.setTypes(types);
		noteDao.setSender_seq(senderSeq);
		noteDao.setReceiver_seq(receiveSeq);
		noteDao.setTitle(title);
		noteDao.setSender_date(new Date(System.currentTimeMillis()));
		
		noteDao =  (NotesDAO)sqlClient.insert("noteInsert", noteDao); //$NON-NLS-1$
		
		NotesDetailDAO detailDao = new NotesDetailDAO();
		detailDao.setNote_seq(noteDao.getSeq());
		insertNoteData(detailDao, strContent);;
	}
	
	/**
	 * note data 
	 * 
	 * @param noteDetailDao
	 * @param noteDetailDao
	 * @throws Exception
	 */
	private static void insertNoteData(NotesDetailDAO noteDetailDao, String strContent) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		// content data를 저장합니다.
		String[] arrayContent = SQLUtil.makeResourceDataArays(strContent);
		for (String content : arrayContent) {
			noteDetailDao.setData(content);		
			sqlClient.insert("noteDataInsert", noteDetailDao); //$NON-NLS-1$				
		}
	}
}
