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

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.sql.dao.system.UserDBResourceDataDAO;
import com.hangum.tadpole.sql.util.SQLUtil;
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
	 * 저장 
	 * 
	 * @param userDB
	 * @param userDBResource
	 * @param contents
	 * 
	 * @throws Exception
	 */
	public static UserDBResourceDAO saveResource(UserDBDAO userDB, UserDBResourceDAO userDBResource, String contents) throws Exception {
		// 기존에 등록 되어 있는지 검사한다
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		UserDBResourceDAO retUserDBResource =  (UserDBResourceDAO)sqlClient.insert("userDbResourceInsert", userDBResource); //$NON-NLS-1$
		
		// content data를 저장합니다.
		insertResourceData(retUserDBResource, contents);
		
		return userDBResource;
	}
	
	/**
	 * update 
	 * 
	 * @param dbResource
	 * @param content
	 * @throws Exception
	 */
	public static void updateResource(UserDBResourceDAO userDBResource, String contents) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		sqlClient.delete("userDbResourceDataDelete", userDBResource.getResource_seq()); //$NON-NLS-1$
		
		insertResourceData(userDBResource, contents);
	}
	
	/**
	 * resource data 
	 * 
	 * @param userDBResource
	 * @param contents
	 * @throws Exception
	 */
	private static void insertResourceData(UserDBResourceDAO userDBResource, String contents) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
		// content data를 저장합니다.
		UserDBResourceDataDAO dataDao = new UserDBResourceDataDAO();
		dataDao.setUser_db_resource_seq(userDBResource.getResource_seq());
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
	 * @throws Exception
	 */
	public static List<UserDBResourceDAO> userDbErdTree(UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return (List<UserDBResourceDAO>)sqlClient.queryForList("userDbResourceTree", userDB); //$NON-NLS-1$
	}
	
	/**
	 * 이름이 중복되었는지 검사
	 * 
	 * @param user_seq
	 * @param db_seq
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static boolean userDBResourceDuplication(PublicTadpoleDefine.RESOURCE_TYPE type, int user_seq, int db_seq, String filename) throws Exception {
		UserDBResourceDAO erd = new UserDBResourceDAO();
//		erd.setTypes(type.toString());
//		erd.setUser_seq(user_seq);
//		erd.setDb_seq(db_seq);
//		erd.setFilename(filename);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		return sqlClient.queryForList("userDBResourceDuplication", erd).size()  == 0; //$NON-NLS-1$
	}
	
	/**
	 * user_db_resource 삭제.
	 * user_db_resource_data 삭제.
	 * 
	 * @param userDBErd
	 * @throws Exception
	 */
	public static void delete(UserDBResourceDAO userDBErd) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		
//		sqlClient.delete("userDbResourceDataDelete", userDBErd.getSeq()); //$NON-NLS-1$
		sqlClient.update("userDBResourceDelete", userDBErd); //$NON-NLS-1$
	}
	
	/**
	 * userdb의 resource data를 얻습니다.
	 * 
	 * @param userDBResource
	 * @throws Exception
	 */
	public static String getResourceData(UserDBResourceDAO userDBResource) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List<UserDBResourceDataDAO> datas =  (List<UserDBResourceDataDAO>)sqlClient.queryForList("userDBResourceData", userDBResource); //$NON-NLS-1$
		
		StringBuffer retData = new StringBuffer();
		for (UserDBResourceDataDAO userDBResourceDataDAO : datas) {
			retData.append(userDBResourceDataDAO.getDatas());
		}
	
		return retData.toString();
	}

}
