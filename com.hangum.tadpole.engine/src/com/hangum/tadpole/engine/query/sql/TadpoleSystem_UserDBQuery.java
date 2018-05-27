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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.util.LoadConfigFile;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TDBGatewayManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.gateway.ExtensionDBDAO;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBOriginalDAO;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.DBAccessControlDAO;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("updateDBOtherInformation", userDB);
	}
	
	/**
	 * Registered Database(Not include sqlite)
	 * 
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List getRegisteredDB() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
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
		queryMap.put("group_name", userDBDao.getGroup_name());
		queryMap.put("display_name", userDBDao.getDisplay_name());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		List<Object> listUserDB = sqlClient.queryForList("isOldDBValidate", queryMap);
		
		if(listUserDB.isEmpty()) return false;
		else return true;
	}
	
//	/**
//	 * 신규디비 등록할 수 있는지 검사합니다.
//	 * 
//	 * @param user_seq
//	 * @param userDBDao
//	 * @return
//	 * @throws TadpoleSQLManagerException, SQLException 
//	 */
//	public static boolean isNewDBValidate(int user_seq, UserDBDAO userDBDao) throws TadpoleSQLManagerException, SQLException {
//		Map<String, Object> queryMap = new HashMap<String, Object>();
//		queryMap.put("user_seq", 	user_seq);
//		queryMap.put("group_name", userDBDao.getGroup_name());
//		queryMap.put("display_name", userDBDao.getDisplay_name());
//		
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		List<Object> listUserDB = sqlClient.queryForList("isNewDBValidate", queryMap);
//		
//		if(listUserDB.isEmpty()) return false;
//		else return true;
//	}
	
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
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(GetAdminPreference.getSaveDBPassword())) {
			userEncryptDao.setPasswd(CipherManager.getInstance().encryption(userDb.getPasswd()));
		} else {
			userEncryptDao.setPasswd("");
		}
		
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
		userEncryptDao.setIs_history_data_location(userDb.getIs_history_data_location());
		//
		
		userEncryptDao.setExt1(userDb.getExt1());
		userEncryptDao.setCreate_time(new Timestamp(System.currentTimeMillis()));
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		UserDBDAO insertedUserDB = (UserDBDAO)sqlClient.insert("userDBInsert", userEncryptDao); //$NON-NLS-1$
		
		userDb.setSeq(insertedUserDB.getSeq());
		
		// tadpole_user_db_role
		TadpoleUserDbRoleDAO roleDao = TadpoleSystem_UserRole.insertTadpoleUserDBRole(userSeq, 
					insertedUserDB.getSeq(), 
					PublicTadpoleDefine.DB_USER_ROLE_TYPE.ADMIN.toString(),
					""
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
		
		// 시스템 어드민의 패스워드 저장 여부에 따라 저장
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(GetAdminPreference.getSaveDBPassword())) {
			userEncryptDao.setPasswd(CipherManager.getInstance().encryption(newUserDb.getPasswd()));
		} else {
			userEncryptDao.setPasswd("");
		}
		
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
		userEncryptDao.setExt2(newUserDb.getExt2());
		userEncryptDao.setExt3(newUserDb.getExt3());
		userEncryptDao.setExt4(newUserDb.getExt4());
		userEncryptDao.setExt5(newUserDb.getExt5());
		userEncryptDao.setExt6(newUserDb.getExt6());
		userEncryptDao.setExt7(newUserDb.getExt7());
		userEncryptDao.setExt8(newUserDb.getExt8());
		userEncryptDao.setExt9(newUserDb.getExt9());
		userEncryptDao.setExt10(newUserDb.getExt10());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("userDBUpdate", userEncryptDao); //$NON-NLS-1$
		
		// 데이터베이스 확장속성 등록
		sqlClient.update("userDBEXTUpdate", userEncryptDao);
		
		return newUserDb;
	}
	
	/**
	 * group의 그룹명을 리턴합니다.
	 * 
	 * @param userSeq
	 * @param isAdmin 어드민 검색 여부
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<String> getUserGroupName(int userSeq, boolean isAdmin) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_seq", userSeq);//SessionManager.getUserSeq());
		mapParam.put("thisTime", System.currentTimeMillis());
		mapParam.put("isAdmin", isAdmin?"true":"false");
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		List<UserDBDAO> listUserDB = sqlClient.queryForList("userDB", mapParam);
		
		// group name filters
		final String []strGroupNameFilters = GetAdminPreference.getViewGroupNameFilter();
		
		// set db access control
		List<String> listGroupName = new ArrayList<String>();
		for (UserDBDAO userDB : listUserDB) {
			if(!listGroupName.contains(userDB.getGroup_name())) {
				boolean isAdd = true;
				for (String strGroupName : strGroupNameFilters) {
					if(userDB.getGroup_name().startsWith(strGroupName)) {
						isAdd = false;
					}
				}
				
				if(isAdd) listGroupName.add(userDB.getGroup_name());
			}
		}
		
		return listGroupName;
	}
	
	/**
	 * 사용자 디비 한개의 값을 리턴한다.
	 * 
	 * @param intUserSeq
	 * @param isAdmin
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getUserDB(int intUserSeq, int intDBSeq, boolean isAdmin) throws TadpoleSQLManagerException, SQLException {
		
		long longCurrentTime = System.currentTimeMillis();
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_seq", intUserSeq);
		mapParam.put("db_seq", intDBSeq);
		mapParam.put("thisTime", longCurrentTime);
		mapParam.put("isAdmin", isAdmin?"true":"false");
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		List<UserDBDAO> listUserDB = sqlClient.queryForList("userDB", mapParam);
		
		//listUserDB = filterUserDB(listUserDB);
		
		// set db access control
		for (UserDBDAO userDBDAO : listUserDB) {
			DBAccessControlDAO dbAccessCtl = TadpoleSystem_AccessControl.getDBAccessControl(userDBDAO.getRole_seq());
			userDBDAO.setDbAccessCtl(dbAccessCtl);
		}
		
		return listUserDB;
	}
	
	/**
	 * 유저그룹의 디비 리스트
	 * 
	 * @param strGoupName
	 * @param intUserSeq
	 * @param isAdmin
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getUserGroupDB(String strGoupName, int intUserSeq, boolean isAdmin) throws TadpoleSQLManagerException, SQLException {
		
		long longCurrentTime = System.currentTimeMillis();
		
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("group_name", strGoupName);
		mapParam.put("user_seq", intUserSeq);
		mapParam.put("thisTime", longCurrentTime);
		mapParam.put("isAdmin", isAdmin?"true":"false");
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		List<UserDBDAO> listUserDB = sqlClient.queryForList("userGroupDB", mapParam);
		
		listUserDB = filterUserDB(listUserDB);
		
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
	 * 사용자의 유효한 디비 중에 필터되어야 하는 디비 리스트
	 * 
	 * @param userDAO
	 * @param isValid 권한에 이상이 없는 디비만 가져온다.
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<UserDBDAO> getUserDB(UserDAO userDAO, boolean isValid) throws TadpoleSQLManagerException, SQLException {
		List<DBDefine> listFilter = new ArrayList<DBDefine>();
		
		return getUserDB(userDAO, listFilter, true);
	}
	
	
	/**
	 * 사용자의 유효한 디비 중에 필터되어야 하는 디비 리스트
	 * 
	 * @param userDAO
	 * @param listFilter
	 * @param isValid 권한에 이상이 없는 디비만 가져온다.
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<UserDBDAO> getUserDB(UserDAO userDAO, List<DBDefine> listFilter, boolean isValid) throws TadpoleSQLManagerException, SQLException {
		List<UserDBDAO> listUserDBs = getUserDB(userDAO);
		
		if(isValid) {
			List<UserDBDAO> listRemoveDB = new ArrayList<UserDBDAO>();
			for (UserDBDAO userDBDAO : listUserDBs) {
				if(!userDBDAO.is_isUseEnable()) listRemoveDB.add(userDBDAO);
			}
			for (UserDBDAO userDBDAO : listRemoveDB) {
				listUserDBs.remove(userDBDAO);
			}
		}
		
		return listUserDBs;
	}
	
	/**
	 * 사용자 디비 중에 필터되어야 하는 디비 리스트
	 * 
	 * @param userDAO
	 * @param listFilter
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<UserDBDAO> getUserDB(UserDAO userDB, List<DBDefine> listFilter) throws TadpoleSQLManagerException, SQLException {
		List<UserDBDAO> listUserDBs = getUserDB(userDB);
		for (DBDefine dbDefine : listFilter) {
			List<UserDBDAO> listRemoveDB = new ArrayList<UserDBDAO>();
			for (UserDBDAO userDBDAO : listUserDBs) {
				if(dbDefine == userDBDAO.getDBDefine()) listRemoveDB.add(userDBDAO);
			}
			
			for (UserDBDAO userDBDAO : listRemoveDB) {
				listUserDBs.remove(userDBDAO);
			}
		}
		
		return listUserDBs;
	}
	
	/**
	 * 유저디비
	 * 
	 * @param userDAO
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getUserDB(UserDAO userDAO) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_seq", userDAO.getSeq());
		mapParam.put("thisTime", System.currentTimeMillis());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		List<UserDBDAO> listUserDB = sqlClient.queryForList("userDBs", mapParam);
		
		listUserDB = filterUserDB(listUserDB);

		// set db access control
		for (UserDBDAO userDBDAO : listUserDB) {
			DBAccessControlDAO dbAccessCtl = TadpoleSystem_AccessControl.getDBAccessControl(userDBDAO.getRole_seq());
			userDBDAO.setDbAccessCtl(dbAccessCtl);
		}
		
		return listUserDB;
	}
	
	/**
	 * 자신이 공유 받은 디비만 복사한다.
	 * 
	 * @param userDAO
	 * @return
	 */
	public static List<TadpoleUserDbRoleDAO> getUserSharedDB(UserDAO userDAO) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("user_seq", userDAO.getSeq());
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		List<TadpoleUserDbRoleDAO> listUserDB = sqlClient.queryForList("userSharedDB", mapParam);
		
		return listUserDB;
	}
	
	/**
	 * filter user DB
	 * 
	 * @param listUserDB
	 * @return
	 */
	public static List<UserDBDAO> filterUserDB(List<UserDBDAO> listUserDB) {
		// product type filter
		final String []strProductTypeFilters = GetAdminPreference.getViewProductTypeFilter();
		for (String strProductType : strProductTypeFilters) {
			List<UserDBDAO> listRemoveUserDB = new ArrayList<UserDBDAO>();
			for (UserDBDAO userDBDAO : listUserDB) {
				if(strProductType.equals(userDBDAO.getOperation_type())) {
					listRemoveUserDB.add(userDBDAO);
				}
			}
			
			for (UserDBDAO userDBDAO : listRemoveUserDB) {
				listUserDB.remove(userDBDAO);
			}
		}
		
		// is gateway
		if(LoadConfigFile.isEngineGateway()) {
			List<ExtensionDBDAO> listExtensionInfo = new ArrayList<ExtensionDBDAO>();
			try {
				listExtensionInfo = TadpoleSystem_ExtensionDB.getUserDBs(SessionManager.getEMAIL());
			} catch(Exception e) {
				logger.error("get gateway list" + e.getMessage());
			}
		
//			List<UserDBDAO> listRemoveUserDB = new ArrayList<UserDBDAO>();
			for (UserDBDAO userDBDAO : listUserDB) {
				final String strKey = TDBGatewayManager.getExtensionKey(userDBDAO);
				boolean isFineDB = false;
				
				for (ExtensionDBDAO extensionDBDAO : listExtensionInfo) {
					if(strKey.equals(extensionDBDAO.getSearch_key())) {
						
						userDBDAO.setTerms_of_use_starttime(extensionDBDAO.getTerms_of_use_starttime());
						userDBDAO.setTerms_of_use_endtime(extensionDBDAO.getTerms_of_use_endtime());
						
						isFineDB = true;
						break;
					}
				}
				
				if(!isFineDB) {
					userDBDAO.set_isUseEnable(false);
					userDBDAO.set_sysMessage(PublicTadpoleDefine.AUTH_CODE_DEFINE.AUTH_EXPIRE.name());
				}
			}
		}

		final long longCurrentTime = System.currentTimeMillis();
		// set db access control
		for (UserDBDAO userDBDAO : listUserDB) {
			Timestamp stTime = userDBDAO.getTerms_of_use_starttime();
			Timestamp edTime = userDBDAO.getTerms_of_use_endtime();

			if(!(stTime.getTime() <= longCurrentTime && edTime.getTime() >= longCurrentTime)) {
				if(logger.isDebugEnabled()) logger.debug("The db period has expired. " + userDBDAO.getDisplay_name());
				userDBDAO.set_isUseEnable(false);
				userDBDAO.set_sysMessage(PublicTadpoleDefine.AUTH_CODE_DEFINE.TIME_EXPIRE.name());
			}
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

//	/**
//	 * 자신이 생성한 사용자 리스트
//	 * 
//	 * @param userDB
//	 * @return
//	 * @throws TadpoleSQLManagerException, SQLException 
//	 */
//	public static List<UserDBDAO> getCreateUserDB(UserDAO userDB) throws TadpoleSQLManagerException, SQLException {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		return (List<UserDBDAO>)sqlClient.queryForList("getCreateUserDB", userDB.getSeq());
//	}
//	
//	/**
//	 * 자신이 생성한 사용자 리스트
//	 * @return
//	 * @throws TadpoleSQLManagerException, SQLException 
//	 */
//	public static List<UserDBDAO> getCreateUserDB() throws TadpoleSQLManagerException, SQLException {
//		UserDAO userDAO = new UserDAO();
//		userDAO.setSeq(SessionManager.getUserSeq());
//		return getCreateUserDB(userDAO);
//	}
	
	/**
	 * 모든 유저의 디비를 보여 줍니다.
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getAllUserDB() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		return  (List<UserDBDAO>)sqlClient.queryForList("userDBPermissions"); //$NON-NLS-1$
	}
	
	/**
	 * 일별 보고서 종류.
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static List<UserDBDAO> getDailySummaryReportDB() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		UserDBDAO userDB =  (UserDBDAO)sqlClient.queryForObject("userDBInstance", dbSeq);
		
		return userDB;
	}
	
	/**
	 * 디비와 유저 정보를 리턴한다.
	 * 
	 * @param dbSeq
	 * @param userSeq
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static UserDBDAO getUserDBAccessCtl(int dbSeq, int userSeq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		UserDBDAO userDB =  (UserDBDAO)sqlClient.queryForObject("userDBInstance", dbSeq);
		
		return userDB;
	} 

	/**
	 * 유저 삭제
	 * @param seq
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static void removeUserDB(int seq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		
		sqlClient.update("userDBDelete", seq); //$NON-NLS-1$		
	}

	/**
	 * 사용자 디비 롤을 삭제합니다.
	 * 
	 * @param seq
	 * @throws TadpoleSQLManagerException, SQLException 
	 */
	public static void removeUserRoleDB(int seq) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleEngineUserDB.getUserDB());
		sqlClient.update("userDBRoleRemove", seq); //$NON-NLS-1$
	}

}
