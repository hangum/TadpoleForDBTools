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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.UserDBOriginalDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
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
	 * 기존 디비 수정할 수 있는지 검사합니다.
	 * 
	 * @param user_seq
	 * @param userDBDao
	 * @param oldUserDBDao
	 * @return
	 * @throws Exception
	 */
	public static boolean isOldDBValidate(int user_seq, UserDBDAO userDBDao, UserDBDAO oldUserDBDao) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("user_seq", 	user_seq);
		queryMap.put("seq", oldUserDBDao.getSeq());
		queryMap.put("display_name", userDBDao.getDisplay_name());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<Object> listUserDB = sqlClient.queryForList("isOldDBValidate", queryMap);
		
		if(listUserDB.size() == 0) return false;
		else return true;
	}
	
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
		
		// data encryption
		UserDBOriginalDAO userEncryptDao = new UserDBOriginalDAO();
		userEncryptDao.setUser_seq(userDb.getUser_seq());
		userEncryptDao.setDbms_types(userDb.getDbms_types());
		userEncryptDao.setUrl(CipherManager.getInstance().encryption(userDb.getUrl()));
		userEncryptDao.setDb(CipherManager.getInstance().encryption(userDb.getDb()));
		
		userEncryptDao.setGroup_seq(userDb.getGroup_seq());
		userEncryptDao.setGroup_name(userDb.getGroup_name());
		userEncryptDao.setDisplay_name(userDb.getDisplay_name());
		userEncryptDao.setOperation_type(userDb.getOperation_type());
		
		userEncryptDao.setHost(CipherManager.getInstance().encryption(userDb.getHost()));
		userEncryptDao.setPort(CipherManager.getInstance().encryption(userDb.getPort()));
		userEncryptDao.setUsers(CipherManager.getInstance().encryption(userDb.getUsers()));
		userEncryptDao.setPasswd(CipherManager.getInstance().encryption(userDb.getPasswd()));

		userEncryptDao.setLocale(userDb.getLocale());
		
		// ext information
		userEncryptDao.setIs_readOnlyConnect(userDb.getIs_readOnlyConnect());
		userEncryptDao.setIs_autocommit(userDb.getIs_autocommit());
		userEncryptDao.setIs_showtables(userDb.getIs_showtables());
		
		userEncryptDao.setIs_table_filter(userDb.getIs_table_filter());
		userEncryptDao.setTable_filter_include(userDb.getTable_filter_include());
		userEncryptDao.setTable_filter_exclude(userDb.getTable_filter_exclude());
		
		userEncryptDao.setIs_profile(userDb.getIs_profile());
		userEncryptDao.setQuestion_dml(userDb.getQuestion_dml());
		//
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDBDAO insertedUserDB = (UserDBDAO)sqlClient.insert("userDBInsert", userEncryptDao); //$NON-NLS-1$
		
		userDb.setSeq(insertedUserDB.getSeq());
		
		// table_filter 등록
		sqlClient.insert("userDBFilterInsert", userDb);
		
		// 데이터베이스 확장속성 등록
		sqlClient.insert("userDBEXTInsert", userDb);
		
		return insertedUserDB;
	}
	
	/**
	 * userDAO 수정 
	 *
	 * @param newUserDb
	 * @param oldUserDb
	 * @param userSeq
	 * @return
	 * @throws Exception
	 */
	public static UserDBDAO updateUserDB(UserDBDAO newUserDb, UserDBDAO oldUserDb, int userSeq) throws Exception {
		
		UserDBOriginalDAO userEncryptDao = new UserDBOriginalDAO();
		userEncryptDao.setUser_seq(userSeq);
		userEncryptDao.setSeq(oldUserDb.getSeq());
		userEncryptDao.setDbms_types(newUserDb.getDbms_types());
		userEncryptDao.setUrl(CipherManager.getInstance().encryption(newUserDb.getUrl()));
		userEncryptDao.setDb(CipherManager.getInstance().encryption(newUserDb.getDb()));
		
		userEncryptDao.setGroup_seq(newUserDb.getGroup_seq());
		userEncryptDao.setGroup_name(newUserDb.getGroup_name());
		userEncryptDao.setDisplay_name(newUserDb.getDisplay_name());
		userEncryptDao.setOperation_type(newUserDb.getOperation_type());
		
		userEncryptDao.setHost(CipherManager.getInstance().encryption(newUserDb.getHost()));
		userEncryptDao.setPort(CipherManager.getInstance().encryption(newUserDb.getPort()));
		userEncryptDao.setUsers(CipherManager.getInstance().encryption(newUserDb.getUsers()));
		userEncryptDao.setPasswd(CipherManager.getInstance().encryption(newUserDb.getPasswd()));

		userEncryptDao.setLocale(newUserDb.getLocale());
		
		// ext information
		userEncryptDao.setIs_readOnlyConnect(newUserDb.getIs_readOnlyConnect());
		userEncryptDao.setIs_autocommit(newUserDb.getIs_autocommit());
		userEncryptDao.setIs_showtables(newUserDb.getIs_showtables());
		
		userEncryptDao.setIs_table_filter(newUserDb.getIs_table_filter());
		userEncryptDao.setTable_filter_include(newUserDb.getTable_filter_include());
		userEncryptDao.setTable_filter_exclude(newUserDb.getTable_filter_exclude());
		
		userEncryptDao.setIs_profile(newUserDb.getIs_profile());
		userEncryptDao.setQuestion_dml(newUserDb.getQuestion_dml());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("userDBUpdate", userEncryptDao); //$NON-NLS-1$
		
		// table_filter 등록
		sqlClient.update("userDBFilterUpdate", userEncryptDao);
		
		// 데이터베이스 확장속성 등록
		sqlClient.update("userDBEXTUpdate", userEncryptDao);
		
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
	 * 모든 유저의 디비를 보여 줍니다.
	 * @return
	 * @throws Exception
	 */
	public static List<UserDBDAO> getAllUserDB() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return  (List<UserDBDAO>)sqlClient.queryForList("userDBPermissions"); //$NON-NLS-1$
	}
	
	/**
	 * Get instance of database by sequence id
	 * 
	 * @param dbSeq
	 * @return
	 * @throws Exception
	 */
	public static UserDBDAO getUserDBInstance(int dbSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDBDAO userDB =  (UserDBDAO)sqlClient.queryForObject("userDBInstance", dbSeq);
		
		return userDB;
	}
	
	/**
	 * 유저의 디비를 보여 줍니다.
	 * @return
	 * @throws Exception
	 */
	public static List<UserDBDAO> getAllUserDB(String userSeq) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return  (List<UserDBDAO>)sqlClient.queryForList("userDB", ""+userSeq); //$NON-NLS-1$
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
