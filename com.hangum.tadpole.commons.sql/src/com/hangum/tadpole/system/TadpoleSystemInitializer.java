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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.Messages;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.TadpoleSystemDAO;
import com.hangum.tadpole.dao.system.UserDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserGroupDAO;
import com.hangum.tadpole.system.internal.migration.SystemMigrationSR9TOSR10;
import com.hangum.tadpole.util.ApplicationArgumentUtils;
import com.hangum.tadpole.util.secret.EncryptiDecryptUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * <pre>
 * tadpole system의 connection과 연결을 담당합니다.
 * 
 * 엔진의 런타임 옵션은targetProject/docs/engine argument options.txt 를 참고합니다.
 * </pre> 
 * 
 * @author hangum
 *
 */
public class TadpoleSystemInitializer {
	private static final Logger logger = Logger.getLogger(TadpoleSystemInitializer.class);
	
	private static UserDBDAO tadpoleEngineDB;
	
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
	 * 시스템 시작환경이 디비를 공유모드로 사용할지 환경을 선택합니다.
	 *  -dbServer C:\dev\eclipse-SDK-4.2RC1-Xtext-2.3.0RC1-win32\eclipse\workspace-tadpole\TadpoleForDBTools\targetProject\TadpoleEngine.cfg
	 */
	static {
		String dbServerPath = "";
		
		// 로컬 디비를 사용 할 경우. 
		if(!ApplicationArgumentUtils.isDBServer()) {
			if(!new File(DB_FILE_LOCATION).exists()) {
				new File(DB_FILE_LOCATION).mkdirs();					
			}
			if(logger.isDebugEnabled()) logger.debug(DB_FILE_LOCATION + DB_NAME);
		
		// 원격디비를 사용 할 경우.
		} else {
			try {
				dbServerPath = ApplicationArgumentUtils.getDbServer();				
				if(!new File(dbServerPath).exists()) {
					logger.error("DBServer file not found. " + dbServerPath);
					System.exit(0);
				}
			} catch(Exception e) {
				logger.error("Tadpole Argument error. check ini file is -dbServer value. ", e);
				System.exit(0);
			}
		}
	
		// 엔진 디비를 초기화합니다.
		initEngineDB(dbServerPath);	
	}
	
	/**
	 * tadpole system의 default userDB
	 * 
	 * @return
	 */
	public static UserDBDAO getUserDB() {
		return tadpoleEngineDB;
	}
	
	/**
	 * system 초기화 합니다.
	 * 
	 * 1. 테이블이 없다면 테이블 생성하고, 초기데이터를 만듭니다.
	 * 2. 테이블이 있다면 디비가 버전과 동일한지 검사합니다.
	 * 
	 * @throws Exception
	 */
	public static boolean initSystem() throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
		List isUserTable = sqlClient.queryForList("isUserTable"); //$NON-NLS-1$
		
		if(isUserTable.size() != 1) {
			createSystemTable();			
			return true;
		} else {
			logger.info(SystemDefine.NAME + " " + SystemDefine.MAJOR_VERSION + " SR" + SystemDefine.SUB_VERSION + " start...");
			return systemCheck();
		}
	}
	
	/**
	 * System 버전과 application 버전을 채크합니다.
	 */
	private static boolean systemCheck() throws Exception {
		TadpoleSystemDAO tsdao = TadpoleSystemQuery.getSystemInfo();
		if(SystemDefine.MAJOR_VERSION.equals(tsdao.getMajor_version()) && SystemDefine.SUB_VERSION.equals(tsdao.getSub_version())) {
			return true;
		} else {
			logger.info("System migration start....");
			SystemMigrationSR9TOSR10.migration(SystemDefine.MAJOR_VERSION, "10");
			
			logger.info("System migration end....");
			
			return true;
		}
	}

	/**
	 * system table의 존재 유무를 파악하여 생성합니다.
	 * 
	 */
	private static void createSystemTable() throws Exception {
		
		java.sql.Connection javaConn = null;
		String createMsg = "";
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			javaConn = sqlClient.getDataSource().getConnection();
			
			// 테이블 생성
			Statement stmt = javaConn.createStatement();				
			boolean boolResult = false;
			
			if(!ApplicationArgumentUtils.isDBServer()) {
				
				// system db 생성
				createMsg = "System Table create [" + Messages.TadpoleSystemConnector_system_info_create  + "]"; 
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_system_info_create );
				logger.info( "System" + (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7));
				
				// group
				createMsg = "Group Table create [" + Messages.TadpoleSystemConnector_group_create  + "]"; 
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_group_create );
				logger.info( "Group" + (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7));
				
				// user
				createMsg = Messages.TadpoleSystemConnector_5 + "[" + Messages.TadpoleSystemConnector_user_table_create + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_table_create );
				logger.info( Messages.TadpoleSystemConnector_5 + (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7));
				
				// user role table
				createMsg = Messages.TadpoleSystemConnector_5 + "[" + Messages.TadpoleSystemConnector_user_role_table_create + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_role_table_create );
				logger.info( Messages.TadpoleSystemConnector_5 + (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7));
				
//					// ext account
//				createMsg = "external account table [" + Messages.TadpoleSystemConnector_user_external_account + "]";
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_external_account );
//				logger.info("external account table "+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
				// user db
				createMsg = Messages.TadpoleSystemConnector_9 + "[" + Messages.TadpoleSystemConnector_user_db_table_create + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_table_create );
				logger.info(Messages.TadpoleSystemConnector_9+ (!boolResult?Messages.TadpoleSystemConnector_10:Messages.TadpoleSystemConnector_11) );
				
				// user_db_filter
				createMsg = "[user_db_filter]" + "[" + Messages.TadpoleSystemConnector_user_db_filter + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_filter );
				logger.info("[user_db_filter]" + (!boolResult?Messages.TadpoleSystemConnector_10:Messages.TadpoleSystemConnector_11) );

				// user_db_filter
				createMsg = "[user_db_ext]" + "[" + Messages.TadpoleSystemConnector_user_db_ext + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_ext );
				logger.info("[user_db_ext]" + (!boolResult?Messages.TadpoleSystemConnector_10:Messages.TadpoleSystemConnector_11) );
				
				// security_class
				createMsg = "[security_class]" + "[" + Messages.TadpoleSystemConnector_security_class + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_security_class );
				logger.info("[security_class]" + (!boolResult?Messages.TadpoleSystemConnector_10:Messages.TadpoleSystemConnector_11) );
				
				//data_security
				createMsg = "[data_security]" + "[" + Messages.TadpoleSystemConnector_data_security + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_data_security );
				logger.info("[data_security]" + (!boolResult?Messages.TadpoleSystemConnector_10:Messages.TadpoleSystemConnector_11) );
				
				// user resource
				createMsg = Messages.TadpoleSystemConnector_13 + "][" + Messages.TadpoleSystemConnector_user_db_resource_create + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_resource_create	);
				logger.info(Messages.TadpoleSystemConnector_13+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
				// user resource data
				createMsg = "[user_db_resource_data] Tables [" + Messages.TadpoleSystemConnector_user_db_resource_data_create + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_resource_data_create	);
				logger.info("[user_db_resource_data] Tables" + (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
				// user info data
				createMsg = "[user_info_data] Tables [" + Messages.TadpoleSystemConnector_user_info_data + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_info_data);
				logger.info("[user_info_data] Tables "+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
				// execute_sql_resource_create data
				createMsg = "[executed_sql_resource] Tables [" + Messages.TadpoleSystemConnector_execute_sql_resource_create + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_execute_sql_resource_create	);
				logger.info("[execute_sql_resource] Tables" + (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
				// 	executed_sql_resource_data
				createMsg = "[executed_sql_resource_data] Tables [" + Messages.TadpoleSystemConnector_execute_sql_resource_date_create + "]";
				boolResult = stmt.execute( Messages.TadpoleSystemConnector_execute_sql_resource_date_create	);
				logger.info("[executed_sql_resource_data] Tables" + (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
				
			// default is cubrid
			} else {
				
				throw new Exception("Not support this function. Please contact to the admin(adi.tadpole@gmail.com)");
				
//				createMsg = "System Table create [" + Messages.TadpoleSystemConnector_system_info_create_CUBRID  + "]"; 
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_system_info_create_CUBRID );
//				logger.info( "System" + (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7));
//				
//				// group
//				createMsg = "Group Table create [" + Messages.TadpoleSystemConnector_group_create_CUBRID  + "]";
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_group_create_CUBRID);
//				logger.info("Group" + (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7) );
//				
//				// user
//				createMsg = Messages.TadpoleSystemConnector_5 + "[" + Messages.TadpoleSystemConnector_user_table_create_CUBRID + "]";
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_table_create_CUBRID );
//				logger.info(Messages.TadpoleSystemConnector_5+ (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7) );
//				
//				// ext account
//				createMsg = "external account table [" + Messages.TadpoleSystemConnector_user_external_account_CUBRID + "]";
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_external_account_CUBRID );
//				logger.info("external account table "+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
//				
//				// user db
//				createMsg = Messages.TadpoleSystemConnector_9 + "[" + Messages.TadpoleSystemConnector_user_db_table_create_CUBRID + "]";
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_table_create_CUBRID );
//				logger.info(Messages.TadpoleSystemConnector_9+ (!boolResult?Messages.TadpoleSystemConnector_10:Messages.TadpoleSystemConnector_11) );
//				
//				// user resource
//				createMsg = Messages.TadpoleSystemConnector_13 + "[" + Messages.TadpoleSystemConnector_user_db_resource_create_CUBRID + "]";
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_resource_create_CUBRID );
//				logger.info(Messages.TadpoleSystemConnector_13+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
//				
//				// user resource data
//				createMsg = "user_db_resource_data Tables [" + Messages.TadpoleSystemConnector_user_db_resource_data_create_CUBRID + "]";
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_resource_data_create_CUBRID );
//				logger.info("user_db_resource_data Tables" + (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
//				
//				// user info data
//				createMsg = "user_info_data Tables [" + Messages.TadpoleSystemConnector_user_info_data_CUBRID + "]";
//				boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_info_data_CUBRID );
//				logger.info("user_info_data"+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
				
			}
			
			// 시스템 기본 정보를 입력합니다.
			TadpoleSystemQuery.newSystemInfo(SystemDefine.NAME, SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION, SystemDefine.INFORMATION);
			
			// add basic group
			createMsg = "AdminGroup crateing....";
			UserGroupDAO groupAdmin = TadpoleSystem_UserGroupQuery.newUserGroup("AdminGroup");
			
			createMsg = "TestGroup crateing....";
			UserGroupDAO groupTest = TadpoleSystem_UserGroupQuery.newUserGroup("TestGroup");
			
			// add basic user
			createMsg = ADMIN_EMAIL + " user creating....";
			UserDAO adminUser = TadpoleSystem_UserQuery.newUser(ADMIN_EMAIL, ADMIN_PASSWD, ADMIN_NAME, "en_us");
			
			createMsg = MANAGER_EMAIL + " user creating....";
			UserDAO managerUser = TadpoleSystem_UserQuery.newUser(MANAGER_EMAIL, MANAGER_PASSWD, MANAGER_NAME, "en_us");
			
			createMsg = GUEST_EMAIL + " user creating....";
			UserDAO gusetUser = TadpoleSystem_UserQuery.newUser(GUEST_EMAIL, GUEST_PASSWD, GUEST_NAME, "en_us");
			
			// add group_role
			TadpoleSystem_UserRole.newUserRole(groupAdmin.getSeq(), adminUser.getSeq(), PublicTadpoleDefine.USER_TYPE.ADMIN.toString(), PublicTadpoleDefine.YES_NO.YES.toString(), PublicTadpoleDefine.USER_TYPE.ADMIN.toString());
			TadpoleSystem_UserRole.newUserRole(groupTest.getSeq(), managerUser.getSeq(), PublicTadpoleDefine.USER_TYPE.MANAGER.toString(), PublicTadpoleDefine.YES_NO.YES.toString(), PublicTadpoleDefine.USER_TYPE.ADMIN.toString());
			TadpoleSystem_UserRole.newUserRole(groupTest.getSeq(), gusetUser.getSeq(), PublicTadpoleDefine.USER_TYPE.GUEST.toString(), PublicTadpoleDefine.YES_NO.YES.toString(), PublicTadpoleDefine.USER_TYPE.ADMIN.toString());
		
		} catch(Exception e) {
			logger.error(createMsg, e);
			throw e;

		} finally {
			try { javaConn.close(); } catch(Exception e){}
		}
	}

	/**
	 * Tadpole Engine db를 초기화 합니다.
	 * 
	 * @param dbServerPath 
	 */
	private static void initEngineDB(String dbServerPath) {
		tadpoleEngineDB = new UserDBDAO();
		
		// local db
		if("".equals(dbServerPath)) {
			
			tadpoleEngineDB.setDbms_types(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDBToString());
			tadpoleEngineDB.setUrl(String.format(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDB_URL_INFO(), DB_FILE_LOCATION + DB_NAME));
			tadpoleEngineDB.setDb(DB_INFORMATION);
			tadpoleEngineDB.setDisplay_name(DB_INFORMATION);
			tadpoleEngineDB.setPasswd(""); //$NON-NLS-1$
			tadpoleEngineDB.setUsers(""); //$NON-NLS-1$			
			
		} else {
			FileInputStream fis = null;
			try{
				// get file
				fis = new FileInputStream(dbServerPath);
				int available = fis.available();
				byte[] readData = new byte[available];
				fis.read(readData, 0, available);
				
				// decrypt
				String propData= EncryptiDecryptUtil.decryption(new String(readData));				
				InputStream is = new ByteArrayInputStream(propData.getBytes());
				
				// properties
				Properties prop = new Properties();
				prop.load(is);
			
				String whichDB 	= prop.getProperty("DB").trim();
				String ip		= prop.getProperty("ip").trim();
				String port		= prop.getProperty("port").trim();
				String database	= prop.getProperty("database").trim();
				String user		= prop.getProperty("user").trim();
				String passwd	= prop.getProperty("password").trim();
			
				// make userDB
				tadpoleEngineDB.setDbms_types(DBDefine.TADPOLE_SYSTEM_CUBRID_DEFAULT.getDBToString());
				tadpoleEngineDB.setUrl(String.format(DBDefine.TADPOLE_SYSTEM_CUBRID_DEFAULT.getDB_URL_INFO(), ip, port, database));
				tadpoleEngineDB.setDb(database);
				tadpoleEngineDB.setDisplay_name(DBDefine.TADPOLE_SYSTEM_CUBRID_DEFAULT.getDBToString());
				tadpoleEngineDB.setUsers(user);
				tadpoleEngineDB.setPasswd(passwd);
				
				if(logger.isDebugEnabled()) { 
					logger.debug("[which DB]" + whichDB);
					logger.debug(tadpoleEngineDB.toString());				
				}
				
			} catch(Exception ioe) {
				logger.error("File not found exception or file encrypt exception. check the exist file." + dbServerPath, ioe);
				System.exit(0);
			} finally {
				if(fis != null) try { fis.close(); } catch(Exception e) {}
			}
			
		}
	}
	
}
