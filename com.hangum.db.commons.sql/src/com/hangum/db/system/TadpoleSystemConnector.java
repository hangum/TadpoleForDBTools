package com.hangum.db.system;

import java.io.File;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import com.hangum.db.Messages;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.define.Define;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * tadpole system의 connection과 연결을 담당합니다.
 * 
 * @author hangum
 *
 */
public class TadpoleSystemConnector {
	private static final Logger logger = Logger.getLogger(TadpoleSystemConnector.class);
	
	public static final String DB_FILE_LOCATION = Platform.getInstallLocation().getURL().getFile() + "configuration/tadpole/db/";// //$NON-NLS-1$
	public static final String DB_NAME = "tadpole-system.db"; //$NON-NLS-1$
	public static final String DB_INFORMATION = Messages.TadpoleSystemConnector_2;
	
	/** default group */
	public static final String GROUP_NAME = "Default_Group";
	
	/** guest mode */
	public static final String GUEST_EMAIL = "guest.tadpole@gmail.com"; //$NON-NLS-1$
	public static final String GUEST_PASSWD = "guest"; //$NON-NLS-1$
	public static final String GUEST_NAME = "tadpole-guest"; //$NON-NLS-1$
	
	/** manager mode */
	public static final String MANAGER_EMAIL = "manager.tadpole@gmail.com"; //$NON-NLS-1$
	public static final String MANAGER_PASSWD = "manager"; //$NON-NLS-1$
	public static final String MANAGER_NAME = "tadpole-manager"; //$NON-NLS-1$
	
	/** ADMIN INFO */
	public static final String ADMIN_EMAIL = "adi.tadpole@gmail.com"; //$NON-NLS-1$
	public static final String ADMIN_PASSWD = "admin"; //$NON-NLS-1$
	public static final String ADMIN_NAME = "tadpole-admin"; //$NON-NLS-1$
	
	/**
	 * tadpole system의 default userDB
	 * 
	 * @return
	 */
	public static UserDBDAO getUserDB() {
		if(!new File(DB_FILE_LOCATION).exists()) {
			new File(DB_FILE_LOCATION).mkdirs();					
		}
		logger.debug(DB_FILE_LOCATION + DB_NAME);
		
		final String dbUrl = String.format(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDB_URL_INFO(), DB_FILE_LOCATION + DB_NAME);
		UserDBDAO userDB = new UserDBDAO();
		userDB.setType(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDatabase(DB_INFORMATION);
		userDB.setDisplay_name(DB_INFORMATION);
		userDB.setPasswd(""); //$NON-NLS-1$
		userDB.setUser(""); //$NON-NLS-1$
		
		return userDB;
	}
	
	/**
	 * system table의 존재 유무를 파악하여 생성합니다.
	 * 
	 */
	public static void createSystemTable() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemConnector.getUserDB());
		List isUserTable = sqlClient.queryForList("isUserTable"); //$NON-NLS-1$
		
		if(isUserTable.size() != 1) {
			java.sql.Connection javaConn = null;
			
			try {
				javaConn = sqlClient.getDataSource().getConnection();
				
				// 테이블 생성
				Statement stmt = javaConn.createStatement();
				
				// group
				boolean boolResult = stmt.execute( Messages.TadpoleSystemConnector_group_create);
				logger.info("Group" + (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7) );
				
				// user
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_table_create );
				logger.info(Messages.TadpoleSystemConnector_5+ (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7) );
				
				// user db
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_table_create);
				logger.info(Messages.TadpoleSystemConnector_9+ (!boolResult?Messages.TadpoleSystemConnector_10:Messages.TadpoleSystemConnector_11) );
				
				// user resource
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_erd_table_create	);
				logger.info(Messages.TadpoleSystemConnector_13+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
				// user info data
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_info_data);
//				logger.info("user_info_data"+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
//				// ext account
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_external_account );
//				logger.info("external user table "+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
				// 기본 그룹
				int seqAdm = TadpoleSystem_UserGroupQuery.newUserDB("AdminGroup");
				int seqTest = TadpoleSystem_UserGroupQuery.newUserDB("TestGroup");
				
				// 기본 유저 저장
				TadpoleSystem_UserQuery.newUser(seqAdm, ADMIN_EMAIL, ADMIN_PASSWD, ADMIN_NAME, Define.USER_TYPE.ADMIN.toString(), Define.YES_NO.YES.toString());
				TadpoleSystem_UserQuery.newUser(seqTest, MANAGER_EMAIL, MANAGER_PASSWD, MANAGER_NAME, Define.USER_TYPE.MANAGER.toString(), Define.YES_NO.YES.toString());
				TadpoleSystem_UserQuery.newUser(seqTest, GUEST_EMAIL, GUEST_PASSWD, GUEST_NAME, Define.USER_TYPE.GUEST.toString(), Define.YES_NO.YES.toString());
			} catch(Exception e) {
				logger.error("System table crateion", e);
				throw e;

			} finally {
				try { javaConn.close(); } catch(Exception e){}
			}
		}
	}
	
}
