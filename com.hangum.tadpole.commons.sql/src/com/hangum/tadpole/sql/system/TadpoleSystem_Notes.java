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

import java.text.SimpleDateFormat;
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
	 * 사용자에게 알려야 할 노트 리스트를 
	 * 
	 * @param userSeq
	 * @return
	 * @throws Exception
	 */
	public static List<NotesDAO> getAlertNote(int userSeq) throws Exception {
		// 리턴한다.
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<NotesDAO> retListNotes = sqlClient.queryForList("getAlertNote", userSeq);

		//  받은 모든 노트를 읽음 처리.
		if(!retListNotes.isEmpty()) sqlClient.update("noteSystemRead", userSeq);
		
		return retListNotes;
	}
	
	/**
	 * read system note
	 * 
	 * @param noteSeq
	 * @throws Exception
	 */
	public static void readSystemNote(int noteSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.delete("noteSystemRead", noteSeq);
	}
	
	/**
	 * read note
	 * 
	 * @param noteSeq
	 * @throws Exception
	 */
	public static void readNote(int noteSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.delete("noteRead", noteSeq);
	}
	
	/**
	 * note delete
	 * 
	 * @param noteSeq
	 * @throws Exception
	 */
	public static void deleteNote(int noteSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.delete("noteDelete", noteSeq);
	}
	
	/**
	 * receive note list
	 * 
	 * @param userSeq
	 * @param types
	 * @param strRead
	 * @param strTitle
	 * @param startDate
	 * @param endDate
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<NotesDAO> getNoteList(int userSeq, String types, String strRead, String strTitle, Date startDate, Date endDate) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("userSeq", userSeq);
		mapParam.put("types", types);
		mapParam.put("strRead", strRead);
		mapParam.put("strTitle", "".equals(strTitle) ? "" : "%" + strTitle + '%');
		mapParam.put("startDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate));
		mapParam.put("endDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
		
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
		
		Date now = new java.sql.Date(System.currentTimeMillis());
		
		NotesDAO noteDao = new NotesDAO();
		noteDao.setTypes(types);
		noteDao.setSender_seq(senderSeq);
		noteDao.setReceiver_seq(receiveSeq);
		noteDao.setTitle(title);
		noteDao.setSender_date(now);
		
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
	
	/**
	 * note data 
	 * 
	 * @param noteDAO
	 * @return
	 * @throws Exception
	 */
	public static String getNoteData(NotesDAO noteDAO) throws Exception {
		String retData = "";
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<NotesDetailDAO> listNoteDetail = sqlClient.queryForList("getNoteData", noteDAO.getSeq());
		for (NotesDetailDAO notesDetailDAO : listNoteDetail) {
			retData += notesDetailDAO.getData();
		}
		
		return retData;
	}
}
