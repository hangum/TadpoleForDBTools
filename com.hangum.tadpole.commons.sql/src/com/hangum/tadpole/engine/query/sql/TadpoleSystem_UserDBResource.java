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

import com.hangum.tadpole.commons.exception.TadpoleRuntimeException;
import com.hangum.tadpole.commons.exception.TadpoleSQLManagerException;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.RESOURCE_TYPE;
import com.hangum.tadpole.engine.Messages;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDataDAO;
import com.hangum.tadpole.engine.restful.RESTFulNotFoundURLException;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * user_db_erd 관련 코드 
 * 
 * @author hangum
 *
 */
public class TadpoleSystem_UserDBResource {
	private static final Logger logger = Logger.getLogger(TadpoleSystem_UserDBResource.class);
	
	/**
	 * Returns restful api list
	 * 
	 * @param intUserSeq
	 * @return
	 * @throws SQLException 
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<ResourceManagerDAO> getRESTFulAPIList() throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (List<ResourceManagerDAO>)sqlClient.queryForList("getRESTFulAPIList", SessionManager.getUserSeq()); //$NON-NLS-1$
	}
	
	/**
	 * find restful
	 *  
	 * @param mapCredential
	 * @return
	 * @throws RESTFulNotFoundURLException 
	 * @throws SQLException 
	 * @throws TadpoleSQLManagerException 
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDBResourceDAO findRESTURL(int intUserSEQ, String strUserDomainURL) throws RESTFulNotFoundURLException, SQLException, TadpoleSQLManagerException {
		Map<String, Object> mapCredential = new HashMap<String, Object>();
		mapCredential.put("user_seq", intUserSEQ); //$NON-NLS-1$
		mapCredential.put("restapi_uri", strUserDomainURL); //$NON-NLS-1$
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDBResourceDAO userResource = (UserDBResourceDAO)sqlClient.queryForObject("findRESTURL", mapCredential); //$NON-NLS-1$
		
		if(userResource == null) {
			throw new RESTFulNotFoundURLException("Not found your request url. Check your URL.");
		}
		
		return userResource;
	}
	
	/**
	 * find api 
	 * @param strAPIKey
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDBResourceDAO findAPIKey(String strAPIKey) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDBResourceDAO userResource = (UserDBResourceDAO)sqlClient.queryForObject("findAPIKey", strAPIKey); //$NON-NLS-1$
		
		return userResource;
	}
		
	/**
	 * 저장 
	 * 
	 * @param userDB
	 * @param userDBResource
	 * @param contents
	 * 
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static UserDBResourceDAO saveResource(UserDBDAO userDB, UserDBResourceDAO userDBResource, String contents) throws TadpoleSQLManagerException, SQLException {
		// 기존에 등록 되어 있는지 검사한다
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDBResourceDAO retUserDBResource =  (UserDBResourceDAO)sqlClient.insert("userDbResourceInsert", userDBResource); //$NON-NLS-1$
		
		// content data를 저장합니다.
		insertResourceData(retUserDBResource, contents);
		
		return retUserDBResource;
	}
	
	/**
	 * update resource_title & shared_type
	 * 
	 * @param dbResource
	 * @param content
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateResourceHeader(ResourceManagerDAO userDBResource) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("userDbResourceUpdate", userDBResource); //$NON-NLS-1$
	}
	
//	/**
//	 * update resource_title & description
//	 * 
//	 * @param dbResource
//	 * @param content
//	 * @throws TadpoleSQLManagerException, SQLException
//	 */
//	public static void userDbResourceHeadUpdate(ResourceManagerDAO userDBResource) throws TadpoleSQLManagerException, SQLException {
//		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
//		sqlClient.update("userDbResourceHeadUpdate", userDBResource); //$NON-NLS-1$
//	}
	
	/**
	 * updateResourceAuto 
	 * 
	 * @param dbResource
	 * @param content
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateResourceAuto(UserDBResourceDAO userDBResource, String contents) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.delete("userDbResourceDataAutoDelete", userDBResource.getResource_seq()); //$NON-NLS-1$
		
		insertResourceData(userDBResource, contents);
	}
	
	/**
	 * update 
	 * 
	 * @param dbResource
	 * @param content
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void updateResource(UserDBResourceDAO userDBResource, String contents) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("userDbResourceDataDelete", userDBResource.getResource_seq()); //$NON-NLS-1$
		
		insertResourceData(userDBResource, contents);
	}
	
	/**
	 * auto resource data 
	 * 
	 * @param userDB
	 * @param dBResource
	 * @param dBResource2 
	 * @param newContents
	 */
	public static UserDBResourceDAO updateAutoResourceDate(UserDBDAO userDB, UserDBResourceDAO dBResourceAuto, UserDBResourceDAO dBResource, String newContents) throws TadpoleSQLManagerException, SQLException {
		// auto save 도 시작.
		if(dBResource == null && dBResourceAuto == null) {
			if(logger.isDebugEnabled()) logger.debug("=[0]===[updateAutoResourceDate][dBResource is null][dBResourceAuto is null]============");
			UserDBResourceDAO defDao = new UserDBResourceDAO();
			defDao.setDb_seq(userDB.getSeq());
			defDao.setName(PublicTadpoleDefine.DEFAUL_RESOURCE_NAME);
			defDao.setResource_types(RESOURCE_TYPE.AUTO_SQL.name());
			defDao.setShared_type(PublicTadpoleDefine.SHARED_TYPE.PRIVATE.name());
			defDao.setRestapi_yesno(PublicTadpoleDefine.YES_NO.NO.name());
			defDao.setUser_seq(SessionManager.getUserSeq());
			
			// 기존에 등록 되어 있는지 검사한다
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			UserDBResourceDAO retUserDBResource =  (UserDBResourceDAO)sqlClient.insert("userDbResourceInsert", defDao); //$NON-NLS-1$
			insertResourceData(retUserDBResource, newContents);
			
			return retUserDBResource;
		// auto save 가 한번이라도 있었음.
		} else if(dBResource == null && dBResourceAuto != null) {
			if(logger.isDebugEnabled()) logger.debug("=[1]===[updateAutoResourceDate][dBResource is null][dBResourceAuto is not null]============");
			// 기존에 등록 되어 있는 것을 삭제하고 저장한다.
			updateResourceAuto(dBResourceAuto, newContents);
			
			return dBResourceAuto;
		} else {
			if(logger.isDebugEnabled()) logger.debug("=[2]===[updateAutoResourceDate][dBResource is not null]============");
			// 기존에 등록 되어 있는지 검사한다
			updateResource(dBResource, newContents);
			
			return dBResource;
		}
	}

	/**
	 * resource data 
	 * 
	 * @param userDBResource
	 * @param contents
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	private static void insertResourceData(UserDBResourceDAO userDBResource, String contents) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		// content data를 저장합니다.
		UserDBResourceDataDAO dataDao = new UserDBResourceDataDAO();
		dataDao.setUser_db_resource_seq(userDBResource.getResource_seq());
		dataDao.setGroup_seq(System.currentTimeMillis());
		dataDao.setUser_seq(SessionManager.getUserSeq());
		
		String[] arrayContent = SQLUtil.makeResourceDataArays(contents);
		for (String content : arrayContent) {
			dataDao.setDatas(content);		
			sqlClient.insert("userDbResourceDataInsert", dataDao); //$NON-NLS-1$				
		}
	}
	
	/**
	 * 해당 db의 모든 erd리스트를 가져온다
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<UserDBResourceDAO> userDbResourceTree(UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		List<UserDBResourceDAO> listRealResource = new ArrayList<UserDBResourceDAO>();
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("seq", 		userDB.getSeq()); //$NON-NLS-1$
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDBResourceDAO> listUserDBResources =  (List<UserDBResourceDAO>)sqlClient.queryForList("userDbResourceTree", queryMap); //$NON-NLS-1$
		for (UserDBResourceDAO userDBResourceDAO : listUserDBResources) {
			if(PublicTadpoleDefine.SHARED_TYPE.PUBLIC.toString().equals(userDBResourceDAO.getShared_type())) {
				userDBResourceDAO.setParent(userDB);
				listRealResource.add(userDBResourceDAO);
			} else {
				
				// 리소스 중에서 개인 리소스만 넣도록 합니다.
				if(SessionManager.getUserSeq() == userDBResourceDAO.getUser_seq()) {
					userDBResourceDAO.setParent(userDB);
					listRealResource.add(userDBResourceDAO);
				}
			}
		}
		
		return listRealResource;
	}
	
	/**
	 * 리소스 목록을 조회한다.
	 * @param <ResourceManagerDAO>
	 * @param <ResourceManagerDAO>
	 * 
	 * @param userDB
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static List<ResourceManagerDAO> userDbResource(UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (List<ResourceManagerDAO>)sqlClient.queryForList("userDbResourceManager", userDB); //$NON-NLS-1$
	}
	
	/**
	 * 이름 또는 api url이 중복되었는지 검사
	 * 
	 * @param userDB
	 * @param retResourceDao
	 * @return
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void userDBResourceDuplication(UserDBDAO userDB, UserDBResourceDAO dbResourceDAO) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());		
		if(!sqlClient.queryForList("userDBResourceDuplication", dbResourceDAO).isEmpty()) { //$NON-NLS-1$
			throw new TadpoleRuntimeException(Messages.get().TadpoleSystem_UserDBResource_6);
		}
		
		if(!dbResourceDAO.getRestapi_uri().equals("")) {
			if(!sqlClient.queryForList("userDBResourceAPIDuplication", dbResourceDAO).isEmpty()) { //$NON-NLS-1$
				throw new TadpoleRuntimeException(Messages.get().TadpoleSystem_UserDBResource_8);
			}
		}
	}
	
	/**
	 * 리소스 업데이트시 중복 오류 검사.
	 * 
	 * @param userDB
	 * @param dao
	 */
	public static void userDBResourceDupUpdate(UserDBDAO userDB, ResourceManagerDAO dao) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());		
		if(!sqlClient.queryForList("userDBResourceDuplicationUpdate", dao).isEmpty()) { //$NON-NLS-1$
			throw new TadpoleRuntimeException(Messages.get().TadpoleSystem_UserDBResource_6);
		}
		
		if(!dao.getRestapi_uri().equals("")) {
			if(!sqlClient.queryForList("userDBResourceAPIDuplicationUpdate", dao).isEmpty()) { //$NON-NLS-1$
				throw new TadpoleRuntimeException(Messages.get().TadpoleSystem_UserDBResource_8);
			}
		}
	}
	
	/**
	 * default db resource data
	 * 
	 * @param userDB
	 * @param userSeq
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static UserDBResourceDAO getDefaultDBResourceData(UserDBDAO userDB) throws TadpoleSQLManagerException, SQLException {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("db_seq", 		userDB.getSeq()); //$NON-NLS-1$
		queryMap.put("user_seq", 	SessionManager.getUserSeq()); //$NON-NLS-1$
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDBResourceDAO resourcesDao =  (UserDBResourceDAO)sqlClient.queryForObject("userDBResourceDefault", queryMap); //$NON-NLS-1$
		if(resourcesDao == null) return null;
		
		String strResource = getResourceData(resourcesDao);
		resourcesDao.setDataString(strResource);
		
		return resourcesDao;
		
	}
	
	/**
	 * user_db_resource 삭제.
	 * user_db_resource_data 삭제.
	 * 
	 * @param userDBErd
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static void delete(UserDBResourceDAO userDBErd) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.update("userDBResourceDelete", userDBErd); //$NON-NLS-1$
	}
	
	/**
	 * userdb의 resource data를 얻습니다.
	 * 
	 * @param userDBResource
	 * @throws TadpoleSQLManagerException, SQLException
	 */
	public static String getResourceData(UserDBResourceDAO userDBResource) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDBResourceDataDAO> datas =  (List<UserDBResourceDataDAO>)sqlClient.queryForList("userDBResourceData", userDBResource); //$NON-NLS-1$
		
		StringBuffer retData = new StringBuffer();
		for (UserDBResourceDataDAO userDBResourceDataDAO : datas) {
			retData.append(userDBResourceDataDAO.getDatas());
		}
	
		return retData.toString();
	}
	
	/**
	 * resource data history
	 * 
	 * @param userDBResource
	 * @return
	 * @throws TadpoleSQLManagerException
	 * @throws SQLException
	 */
	public static List<UserDBResourceDataDAO> getResouceDataHistory(ResourceManagerDAO userDBResource) throws TadpoleSQLManagerException, SQLException {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDBResourceDataDAO> datas =  (List<UserDBResourceDataDAO>)sqlClient.queryForList("getResouceDataHistory", userDBResource.getResource_seq()); //$NON-NLS-1$
		return datas;
	}

}
