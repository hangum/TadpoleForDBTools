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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	public static final String SQL_NOTE_LIST = "SELECT 				" + 
										"			seq, 		" +
										"		    types, 		" +
										"		    sender_seq, " +
										"			(select email from user where  seq = sender_seq) sendUserId, " +
										"		    receiver_seq, " +
										"		    (select email from user where  seq = receiver_seq) receiveUserId, " +
										"			title, " +
										"		    sender_date, " +
										"		    receiver_date, " +
										"		    is_read, " +
										"		    sender_delyn, " +
										"		    receiver_delyn, " +
										"		    create_time, " +
										"		    delyn " +
										"		FROM notes  " +
										"		WHERE   " +
										"			receiver_seq 	= ? " +  
										"		AND is_system_read = 'NO'";
	
	/**
	 * 사용자에게 알려야 할 노트 리스트를 
	 * 
	 * @param userSeq
	 * @return
	 * @throws Exception
	 */
	public static List<NotesDAO> getAlertNote(int userSeq) throws Exception {
		List<NotesDAO> retListNotes = new ArrayList<NotesDAO>(); 
				
		// 리턴한다.
		java.sql.Connection javaConn = null;
		java.sql.PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			javaConn = client.getDataSource().getConnection();
			stmt = javaConn.prepareStatement(SQL_NOTE_LIST);
			stmt.setInt(1, userSeq);
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				NotesDAO noteDao = new NotesDAO();
				noteDao.setSeq(rs.getInt("seq"));
				noteDao.setTypes(rs.getString("types"));
				noteDao.setSender_seq(rs.getInt("sender_seq"));
				noteDao.setSendUserId(rs.getString("sendUserId"));
				noteDao.setReceiver_seq(rs.getInt("receiver_seq"));
				noteDao.setReceiveUserId(rs.getString("receiveUserId"));
				
				noteDao.setTitle(rs.getString("title"));
				
				noteDao.setSender_date(rs.getDate("sender_date"));
				noteDao.setReceiver_date(rs.getDate("receiver_date"));
				noteDao.setIs_read(rs.getString("is_read"));
				noteDao.setSender_delyn(rs.getString("sender_delyn"));
				noteDao.setReceiver_delyn(rs.getString("receiver_delyn"));
				noteDao.setCreate_time(rs.getString("create_time"));
				noteDao.setDelYn(rs.getString("delyn"));
				
				retListNotes.add(noteDao);
			}
			
		} catch(Exception e) {
			logger.error("Read user note", e);
		} finally {
			try { if(rs != null) rs.close();} catch(Exception e) {}
			try { if(stmt != null) stmt.close();} catch(Exception e) {}
			try { if(javaConn != null) javaConn.close(); } catch(Exception e) {}
		}
		
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
		sqlClient.update("noteSystemRead", noteSeq);
	}
	
	/**
	 * read note
	 * 
	 * @param noteSeq
	 * @throws Exception
	 */
	public static void readNote(int noteSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("noteRead", noteSeq);
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
	 * @param types
	 * @param List<String> listAllUser
	 * @param senderSeq
	 * @param receiveSeq
	 * @param title
	 * @param strContent
	 * @throws Exception
	 */
	public static void saveNote(String types, List<Integer> listAllUserSeq,  int senderSeq, int receiveSeq, String title, String strContent) throws Exception {
		
		NotesDAO noteDao = new NotesDAO();
		noteDao.setTypes(types);
		noteDao.setSender_seq(senderSeq);
		noteDao.setReceiver_seq(receiveSeq);
		noteDao.setTitle(title);
		noteDao.setSender_date(new java.sql.Date(System.currentTimeMillis()));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		// com.hangum.tadpole.notes.core.define.NotesDefine.TYPES.PERSON
		if("PERSON".equals(types)) {
			noteDao =  (NotesDAO)sqlClient.insert("noteInsert", noteDao); //$NON-NLS-1$
		} else {
			for(int userSeq: listAllUserSeq) {
				noteDao.setReceiver_seq(userSeq);
				noteDao =  (NotesDAO)sqlClient.insert("noteInsert", noteDao); //$NON-NLS-1$
			}
		}
		
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
