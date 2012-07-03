package com.hangum.db.system;

import java.util.List;

import org.apache.log4j.Logger;

import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.dao.system.UserDBResourceDAO;
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
	 * @param userDB
	 * @param filepath
	 * @param filename
	 * @throws Exception
	 */
	public static UserDBResourceDAO saveResource(UserDBDAO userDB, Define.RESOURCE_TYPE type, String filepath, String filename) throws Exception {
		UserDBResourceDAO resourceDao = new UserDBResourceDAO();
		resourceDao.setUser_seq(userDB.getUser_seq());
		resourceDao.setType(type.toString());
		resourceDao.setDb_seq(userDB.getSeq());
		resourceDao.setFilepath(filepath);
		resourceDao.setFilename(filename);
		
		// 기존에 등록 되어 있는지 검사한다
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		sqlClient.insert("userDbResourceInsert", resourceDao); //$NON-NLS-1$

		// 저장한 seq를 얻는다.
		return (UserDBResourceDAO)sqlClient.queryForObject("userDBResourceDuplication", resourceDao); //$NON-NLS-1$
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
		erd.setType(type.toString());
		erd.setUser_seq(user_seq);
		erd.setDb_seq(db_seq);
		erd.setFilename(filename);
		
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		return sqlClient.queryForList("userDBResourceDuplication", erd).size()  == 0; //$NON-NLS-1$
	}
	
	/**
	 * user db삭제
	 * 
	 * @param userDBErd
	 * @throws Exception
	 */
	public static void delete(UserDBResourceDAO userDBErd) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		sqlClient.delete("userDBResourceDelete", userDBErd); //$NON-NLS-1$
	}
	
}
