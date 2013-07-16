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
package com.hangum.tadpole.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.util.secret.EncryptiDecryptUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * user_db table 관련 
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserDBQuery {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_UserDBQuery.class);
	
	/**
	 * 신규디비 등록할 수 있는지 검사합니다.
	 * 
	 * @param user_seq
	 * @param userDBDao
	 * @return
	 * @throws Exception
	 */
	public static boolean isNewDBValidate(int user_seq, UserDBDAO userDBDao) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("user_seq", 	user_seq);
		queryMap.put("display_name", userDBDao.getDisplay_name());
//		queryMap.put("url", 	userDBDao.getUrl());
//		queryMap.put("users", 	userDBDao.getUsers());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<Object> listUserDB = sqlClient.queryForList("isNewDBValidate", queryMap);
		
		if(listUserDB.size() == 0) return false;
		else return true;
	}
	
	/**
	 * 신규디비 등록시 이미 등록되어 있는지 검사합니다.
	 * 
	 * @param user_seq
	 * @param userDBDao
	 * @return
	 * @throws Exception
	 */
	public static boolean isAlreadyExistDB(int user_seq, UserDBDAO userDBDao) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("user_seq", 	user_seq);
		queryMap.put("url", 	userDBDao.getUrl());
		queryMap.put("users", 	userDBDao.getUsers());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<Object> listUserDB = sqlClient.queryForList("isAlreadyExistDB", queryMap);
		
		if(listUserDB.size() == 0) return false;
		else return true;
	}
	
	/**
	 * group의 그룹명을 리턴합니다.
	 * 
	 * @param groupSeqs
	 * @return
	 * @throws Exception
	 */
	public static List<String> getUserGroup(String groupSeqs) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (List<String>)sqlClient.queryForList("userDBGroup", groupSeqs); //$NON-NLS-1$
	}
	
	/**
	 * 신규 유저디비를 등록합니다.
	 * 
	 * @param userDb
	 * @param userSeq
	 */
	public static UserDBDAO newUserDB(UserDBDAO userDb, int userSeq) throws Exception {
		userDb.setUser_seq(userSeq);
		userDb.setPasswd( EncryptiDecryptUtil.encryption(userDb.getPasswd()) );
		
//		// 기존에 등록 되어 있는지 검사한다
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());

		// user_db등록
		UserDBDAO insertedUserDB = (UserDBDAO)sqlClient.insert("userDBInsert", userDb); //$NON-NLS-1$
		
		userDb.setSeq(insertedUserDB.getSeq());
		
		// table_filter 등록
		sqlClient.insert("userDBFilterInsert", userDb);
		
		// 데이터베이스 확장속성 등록
		sqlClient.insert("userDBEXTInsert", userDb);
		
		return userDb;
	}
	
	/**
	 * userDAO 수정 
	 * 
	 * @param oldUserDb
	 * @param newUserDb
	 * @param userSeq
	 * @return
	 * @throws Exception
	 */
	public static UserDBDAO updateUserDB(UserDBDAO newUserDb, UserDBDAO oldUserDb, int userSeq) throws Exception {
		newUserDb.setUser_seq(userSeq);
		newUserDb.setSeq(oldUserDb.getSeq());
		
		newUserDb.setPasswd( EncryptiDecryptUtil.encryption(newUserDb.getPasswd()) );
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("userDBUpdate", newUserDb); //$NON-NLS-1$
		
		return newUserDb;
	}
	
	/**
	 * 유저디비 + 메니저 디비 
	 * 단, 메니저일경우 메니져 디비만 리턴한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<UserDBDAO> getUserDB(String groupSeqs) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDBDAO> userDB =  (List<UserDBDAO>)sqlClient.queryForList("userDB", groupSeqs);//SessionManager.getSeq()); //$NON-NLS-1$
	
//		TODO 이 로직이 왜 쓰이는지 몰라서 블럭 처리 - hangum.0613
//		// user가 manager 일 경우 (session에 넣을때 부터..)
//		if(SessionManager.getManagerSeq() != -1) {
//			List<UserDBDAO> userManagerDB =  (List<UserDBDAO>)sqlClient.queryForList("userDB", SessionManager.getManagerSeq()); //$NON-NLS-1$
//			userDB.addAll(userManagerDB);
//		}
		
		return userDB;
	}
	
	/**
	 * 유저디비 + 메니저 디비 
	 * 단, 메니저일경우 메니져 디비만 리턴한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<UserDBDAO> getUserDB() throws Exception {
		return getUserDB(SessionManager.getGroupSeqs());
	}
	
	/**
	 * 유저의 디비를 보여 줍니다.
	 * @return
	 * @throws Exception
	 */
	public static List<UserDBDAO> getSpecificUserDB(int userSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return  (List<UserDBDAO>)sqlClient.queryForList("userDB", userSeq); //$NON-NLS-1$
	}
	
	/**
	 * 유저 삭제
	 * @param seq
	 * @throws Exception
	 */
	public static void removeUserDB(int seq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		sqlClient.update("userDBDelete", seq); //$NON-NLS-1$		
	}
}
