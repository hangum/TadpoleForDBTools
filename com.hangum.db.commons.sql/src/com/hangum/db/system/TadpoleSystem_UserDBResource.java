package com.hangum.db.system;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.commons.sql.util.SQLUtil;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.dao.system.UserDBResourceDAO;
import com.hangum.db.dao.system.UserDBResourceDataDAO;
import com.hangum.db.define.Define;
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
	 * @param filename
	 * @throws Exception
	 */
	public static UserDBResourceDAO saveResource(UserDBDAO userDB, Define.RESOURCE_TYPE type, String filename, String contents) throws Exception {
		UserDBResourceDAO resourceDao = new UserDBResourceDAO();
		resourceDao.setUser_seq(userDB.getUser_seq());
		resourceDao.setTypes(type.toString());
		resourceDao.setDb_seq(userDB.getSeq());
		resourceDao.setFilename(filename);
		
		// 기존에 등록 되어 있는지 검사한다
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		UserDBResourceDAO userDBResource =  (UserDBResourceDAO)sqlClient.insert("userDbResourceInsert", resourceDao); //$NON-NLS-1$
		
		// content data를 저장합니다.
		insertResourceData(userDBResource, contents);
		
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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		sqlClient.delete("userDbResourceDataDelete", userDBResource.getSeq()); //$NON-NLS-1$
		
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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		
		// content data를 저장합니다.
		UserDBResourceDataDAO dataDao = new UserDBResourceDataDAO();
		dataDao.setUser_db_resource_seq(userDBResource.getSeq());
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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
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
	public static boolean userDBResourceDuplication(Define.RESOURCE_TYPE type, int user_seq, int db_seq, String filename) throws Exception {
		UserDBResourceDAO erd = new UserDBResourceDAO();
		erd.setTypes(type.toString());
		erd.setUser_seq(user_seq);
		erd.setDb_seq(db_seq);
		erd.setFilename(filename);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
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
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		
		sqlClient.delete("userDbResourceDataDelete", userDBErd.getSeq()); //$NON-NLS-1$
		sqlClient.delete("userDBResourceDelete", userDBErd); //$NON-NLS-1$
	}
	
	/**
	 * userdb의 resource data를 얻습니다.
	 * 
	 * @param userDBResource
	 * @throws Exception
	 */
	public static String getResourceData(UserDBResourceDAO userDBResource) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		List<UserDBResourceDataDAO> datas =  (List<UserDBResourceDataDAO>)sqlClient.queryForList("userDBResourceData", userDBResource); //$NON-NLS-1$
		
		String retData = "";
		for (UserDBResourceDataDAO userDBResourceDataDAO : datas) {
			retData += userDBResourceDataDAO.getDatas();
		}
	
		return retData;
	}
	
}
