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
package com.hangum.tadpole.engine.query.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBOriginalDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.DBAccessControlDAO;
import com.hangum.tadpole.session.manager.SessionManager;
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
	 * update db other information
	 * ex) db lock? visible
	 * 
	 * @param userDB
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static void updateDBOtherInformation(final UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("updateDBOtherInformation", userDB);
	}
	
	/**
	 * Registered Database(Not include sqlite)
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List getRegisteredDB() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("getRegisteredDB");
	}
	
	
	
	/**
	 * 기존 디비 수정할 수 있는지 검사합니다.
	 * 
	 * @param user_seq
	 * @param userDBDao
	 * @param oldUserDBDao
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static boolean isOldDBValidate(int user_seq, UserDBDAO userDBDao, UserDBDAO oldUserDBDao) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("user_seq", 	user_seq);
		queryMap.put("seq", oldUserDBDao.getSeq());
		queryMap.put("display_name", userDBDao.getDisplay_name());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<Object> listUserDB = sqlClient.queryForList("isOldDBValidate", queryMap);
		
		if(listUserDB.isEmpty()) return false;
		else return true;
	}
	
	/**
	 * 신규디비 등록할 수 있는지 검사합니다.
	 * 
	 * @param user_seq
	 * @param userDBDao
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static boolean isNewDBValidate(int user_seq, UserDBDAO userDBDao) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("user_seq", 	user_seq);
		queryMap.put("display_name", userDBDao.getDisplay_name());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<Object> listUserDB = sqlClient.queryForList("isNewDBValidate", queryMap);
		
		if(listUserDB.isEmpty()) return false;
		else return true;
	}
	
	/**
	 * 신규디비 등록시 이미 등록되어 있는지 검사합니다.
	 * 
	 * @param user_seq
	 * @param userDBDao
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static boolean isAlreadyExistDB(int user_seq, UserDBDAO userDBDao) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("user_seq", 	user_seq);
		queryMap.put("url", 	userDBDao.getUrl());
		queryMap.put("users", 	userDBDao.getUsers());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<Object> listUserDB = sqlClient.queryForList("isAlreadyExistDB", queryMap);
		
		if(listUserDB.isEmpty()) return false;
		else return true;
	}
	
	/**
	 * 신규 유저디비를 등록합니다.
	 * 
	 * @param userDb
	 * @param userSeq
	 */
	public static UserDBDAO newUserDB(UserDBDAO userDb, int userSeq) throws TadpoleSQLManagerException, SQLException {
		userDb.setUser_seq(userSeq);
		
		// data encryption
		UserDBOriginalDAO userEncryptDao = new UserDBOriginalDAO();
		userEncryptDao.setUser_seq(userDb.getUser_seq());
		userEncryptDao.setDbms_type(userDb.getDbms_type());
		userEncryptDao.setUrl(CipherManager.getInstance().encryption(userDb.getUrl()));
		userEncryptDao.setDb(CipherManager.getInstance().encryption(userDb.getDb()));
		
		userEncryptDao.setGroup_name(userDb.getGroup_name());
		userEncryptDao.setDisplay_name(userDb.getDisplay_name());
		userEncryptDao.setOperation_type(userDb.getOperation_type());
		userEncryptDao.setIs_lock(userDb.getIs_lock());
		
		userEncryptDao.setHost(CipherManager.getInstance().encryption(userDb.getHost()));
		userEncryptDao.setPort(CipherManager.getInstance().encryption(userDb.getPort()));
		userEncryptDao.setUsers(CipherManager.getInstance().encryption(userDb.getUsers()));
		userEncryptDao.setPasswd(CipherManager.getInstance().encryption(userDb.getPasswd()));
		
		userEncryptDao.setUrl_user_parameter(userDb.getUrl_user_parameter());

		userEncryptDao.setLocale(userDb.getLocale());
		
		// ext information
		userEncryptDao.setIs_readOnlyConnect(userDb.getIs_readOnlyConnect());
		userEncryptDao.setIs_autocommit(userDb.getIs_autocommit());
		userEncryptDao.setIs_showtables(userDb.getIs_showtables());
		
		userEncryptDao.setIs_profile(userDb.getIs_profile());
		userEncryptDao.setQuestion_dml(userDb.getQuestion_dml());
		
		userEncryptDao.setIs_external_browser(userDb.getIs_external_browser());
		
//		userEncryptDao.setIs_visible(userDb.getIs_visible());
		userEncryptDao.setIs_summary_report(userDb.getIs_summary_report());
		userEncryptDao.setIs_monitoring(userDb.getIs_monitoring());
		//
		
		userEncryptDao.setExt1(userDb.getExt1());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDBDAO insertedUserDB = (UserDBDAO)sqlClient.insert("userDBInsert", userEncryptDao); //$NON-NLS-1$
		
		userDb.setSeq(insertedUserDB.getSeq());
		
		// tadpole_user_db_role
		TadpoleUserDbRoleDAO roleDao = TadpoleSystem_UserRole.insertTadpoleUserDBRole(userSeq, 
					insertedUserDB.getSeq(), 
					PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString()
				);
		
		// to save access control
		DBAccessControlDAO dbAccessCtl = TadpoleSystem_AccessControl.saveDBAccessControl(roleDao);
		userDb.setDbAccessCtl(dbAccessCtl);
		
		// 데이터베이스 확장속성 등록
		sqlClient.insert("userDBEXTInsert", userDb);
		
		for(ExternalBrowserInfoDAO extDao : userDb.getListExternalBrowserdao()) {
			extDao.setDb_seq(userDb.getSeq());
			sqlClient.insert("externalBrowserInsert", extDao);
		}
		
		return insertedUserDB;
	}
	
	/**
	 * userDAO 수정 
	 *
	 * @param newUserDb
	 * @param oldUserDb
	 * @param userSeq
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static UserDBDAO updateUserDB(UserDBDAO newUserDb, UserDBDAO oldUserDb, int userSeq) throws TadpoleSQLManagerException, SQLException {
		
		UserDBOriginalDAO userEncryptDao = new UserDBOriginalDAO();
		userEncryptDao.setUser_seq(userSeq);
		userEncryptDao.setSeq(oldUserDb.getSeq());
		userEncryptDao.setDbms_type(newUserDb.getDbms_type());
		userEncryptDao.setUrl(CipherManager.getInstance().encryption(newUserDb.getUrl()));
		userEncryptDao.setDb(CipherManager.getInstance().encryption(newUserDb.getDb()));
		
		userEncryptDao.setGroup_name(newUserDb.getGroup_name());
		userEncryptDao.setDisplay_name(newUserDb.getDisplay_name());
		userEncryptDao.setOperation_type(newUserDb.getOperation_type());
		
		userEncryptDao.setHost(CipherManager.getInstance().encryption(newUserDb.getHost()));
		userEncryptDao.setPort(CipherManager.getInstance().encryption(newUserDb.getPort()));
		userEncryptDao.setUsers(CipherManager.getInstance().encryption(newUserDb.getUsers()));
		userEncryptDao.setPasswd(CipherManager.getInstance().encryption(newUserDb.getPasswd()));
		
		userEncryptDao.setUrl_user_parameter(newUserDb.getUrl_user_parameter());

		userEncryptDao.setLocale(newUserDb.getLocale());
		
		// ext information
		userEncryptDao.setIs_readOnlyConnect(newUserDb.getIs_readOnlyConnect());
		userEncryptDao.setIs_autocommit(newUserDb.getIs_autocommit());
		userEncryptDao.setIs_showtables(newUserDb.getIs_showtables());
		
		userEncryptDao.setIs_profile(newUserDb.getIs_profile());
		userEncryptDao.setQuestion_dml(newUserDb.getQuestion_dml());
		
		userEncryptDao.setIs_visible(newUserDb.getIs_visible());
		userEncryptDao.setIs_summary_report(newUserDb.getIs_summary_report());
		userEncryptDao.setIs_monitoring(newUserDb.getIs_monitoring());
		
		userEncryptDao.setExt1(newUserDb.getExt1());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("userDBUpdate", userEncryptDao); //$NON-NLS-1$
		
		// 데이터베이스 확장속성 등록
		sqlClient.update("userDBEXTUpdate", userEncryptDao);
		
		return newUserDb;
	}
	
	/**
	 * group의 그룹명을 리턴합니다.
	 * 
	 * @param groupSeqs
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<String> getUserGroupName() throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_seq", SessionManager.getUserSeq());
		mapParam.put("thisTime", System.currentTimeMillis());
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDBDAO> listUserDB = sqlClient.queryForList("userDB", mapParam);
		
		// set db access control
		List<String> listGroupName = new ArrayList<String>();
		for (UserDBDAO userDB : listUserDB) {
			if(!listGroupName.contains(userDB.getGroup_name())) {
				listGroupName.add(userDB.getGroup_name());
			}
		}
		
		return listGroupName;
	}
	
	/**
	 * 유저그룹의 디비 리스트
	 * 
	 * @param strGoupName
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getUserGroupDB(String strGoupName) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("group_name", strGoupName);
		mapParam.put("user_seq", SessionManager.getUserSeq());
		mapParam.put("thisTime", System.currentTimeMillis());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDBDAO> listUserDB = sqlClient.queryForList("userGroupDB", mapParam);

		// set db access control
		for (UserDBDAO userDBDAO : listUserDB) {
			DBAccessControlDAO dbAccessCtl = TadpoleSystem_AccessControl.getDBAccessControl(userDBDAO.getRole_seq());
			userDBDAO.setDbAccessCtl(dbAccessCtl);
		}
		
		return listUserDB;
	}
	
	/**
	 * session userdb
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<UserDBDAO> getSessionUserDB() throws TadpoleSQLManagerException, SQLException {
		UserDAO userDB = new UserDAO();
		userDB.setSeq(SessionManager.getUserSeq());
		
		return getUserDB(userDB);
	}
	
	/**
	 * 유저디비
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getUserDB(UserDAO userDB) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_seq", userDB.getSeq());
		mapParam.put("thisTime", System.currentTimeMillis());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDBDAO> listUserDB = sqlClient.queryForList("userDB", mapParam);

		// set db access control
		for (UserDBDAO userDBDAO : listUserDB) {
			DBAccessControlDAO dbAccessCtl = TadpoleSystem_AccessControl.getDBAccessControl(userDBDAO.getRole_seq());
			userDBDAO.setDbAccessCtl(dbAccessCtl);
		}
		
		return listUserDB;
	}
	
	/**
	 * get already registered
	 * @return
	 */
	public static Map<String, UserDBDAO> getUserDBByHost() {
		Map<String, UserDBDAO> mapRegisterdDB = new HashMap<String, UserDBDAO>();
		try {
			for (UserDBDAO userDBDAO : getSessionUserDB()) {
				mapRegisterdDB.put(userDBDAO.getHost(), userDBDAO); 
			}
		} catch(Exception e) {
			logger.error("get alredy regist db", e);
		}
		
		return mapRegisterdDB;
	}

	/**
	 * 자신이 생성한 사용자 리스트
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getCreateUserDB(UserDAO userDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (List<UserDBDAO>)sqlClient.queryForList("getCreateUserDB", userDB.getSeq());
	}
	
	/**
	 * 자신이 생성한 사용자 리스트
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getCreateUserDB() throws TadpoleSQLManagerException, SQLException {
		UserDAO userDAO = new UserDAO();
		userDAO.setSeq(SessionManager.getUserSeq());
		return getCreateUserDB(userDAO);
	}
	
	/**
	 * 모든 유저의 디비를 보여 줍니다.
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getAllUserDB() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return  (List<UserDBDAO>)sqlClient.queryForList("userDBPermissions"); //$NON-NLS-1$
	}
	
	/**
	 * 일별 보고서 종류.
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getDailySummaryReportDB() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return  (List<UserDBDAO>)sqlClient.queryForList("dailySummaryReportDB"); //$NON-NLS-1$
	}
	
	
	/**
	 * Get instance of database by sequence id
	 * 
	 * @param dbSeq
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static UserDBDAO getUserDBInstance(int dbSeq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDBDAO userDB =  (UserDBDAO)sqlClient.queryForObject("userDBInstance", dbSeq);
		
		return userDB;
	}

	/**
	 * 유저 삭제
	 * @param seq
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static void removeUserDB(int seq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		sqlClient.update("userDBDelete", seq); //$NON-NLS-1$		
	}

	/**
	 * 사용자 디비 롤을 삭제합니다.
	 * 
	 * @param seq
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static void removeUserRoleDB(int seq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("userDBRoleRemove", seq); //$NON-NLS-1$
	}

}
