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
package com.hangum.tadpole.sql.query;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.Messages;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.UserGroupDAO;
import com.hangum.tadpole.sql.system.internal.initialize.TadpoleMySQLDDL;
import com.hangum.tadpole.sql.system.internal.initialize.TadpoleSQLIteDDL;
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

	public static String DEFAULT_DB_FILE_LOCATION = Platform.getInstallLocation().getURL().getFile() + "configuration/tadpole/db/";// //$NON-NLS-1$
	public static final String DB_NAME = "tadpole-system.db"; //$NON-NLS-1$
	private static final String DB_INFORMATION = Messages.TadpoleSystemConnector_2;

	/** default group */
	private static final String GROUP_NAME = "Default_Group";

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
	 * 시스템 시작환경이 디비를 공유모드로 사용할지 환경을 선택합니다. -dbServer 데이터베이스암호화 정보.
	 */
	static {
		String dbServerPath = "";

		// 로컬 디비를 사용 할 경우.
		if (!ApplicationArgumentUtils.isDBServer()) {

			try {
				if(ApplicationArgumentUtils.isDBPath()) DEFAULT_DB_FILE_LOCATION = ApplicationArgumentUtils.getDBPath() + File.separator;
				if (!new File(DEFAULT_DB_FILE_LOCATION).exists()) {
					if(!new File(DEFAULT_DB_FILE_LOCATION).mkdirs()) {
						throw new Exception("Can not create the Directory. " + DEFAULT_DB_FILE_LOCATION);
					}
				}
				if (logger.isDebugEnabled()) logger.debug(DEFAULT_DB_FILE_LOCATION + DB_NAME);
			} catch(Exception e) {
				logger.error("System DB Initialize exception", e);
				System.exit(1);
			}

			// 원격디비를 사용 할 경우.
		} else {
			try {
				dbServerPath = ApplicationArgumentUtils.getDbServer();
				if("".equals(dbServerPath) || null == dbServerPath) {
					throw new Exception("Not found dbServerPath values.");
				}
			} catch(Exception e) {
				logger.error("Tadpole Argument error. check ini file is -dbServer value. ");
				System.exit(1);
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
	 * 1. 테이블이 없다면 테이블 생성하고, 초기데이터를 만듭니다. 2. 테이블이 있다면 디비가 버전과 동일한지 검사합니다.
	 * 
	 * @throws Exception
	 */
	public static boolean initSystem() throws Exception {
		boolean isInitialize = false;
		
		if (!ApplicationArgumentUtils.isDBServer()) {
			if(new File(DEFAULT_DB_FILE_LOCATION + DB_NAME).canRead()) {
				isInitialize = true;
			}
		} else {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			List listUserTable = sqlClient.queryForList("isUserTable"); //$NON-NLS-1$
			isInitialize = listUserTable.size() == 0?false:true; 
		}
		
		if (!isInitialize) {
			createSystemTable();
			insertInitialData();
			
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
//		TadpoleSystemDAO tsdao = TadpoleSystemQuery.getSystemInfo();
//		if (SystemDefine.MAJOR_VERSION.equals(tsdao.getMajor_version()) && SystemDefine.SUB_VERSION.equals(tsdao.getSub_version())) {
//			return true;
//		} else {
//			 현재 동작하지 않으므로 블럭 처리 합니다. - hangum 14.03.07
//			logger.info("System migration start....");
//			
//			// 1.0.0 ~ 1.1.1까지의 버전을 마이그레이션 합니다.
//			if("1.0.0".equals(tsdao.getMajor_version()) || "1.0.1".equals(tsdao.getMajor_version()) || "1.1.0".equals(tsdao.getMajor_version()) || "1.1.1".equals(tsdao.getMajor_version())) {
//				SystemMigration migr = new SystemMigration100to111();
//				migr.migration(SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION);
//				
//				migr = new SystemMigration100to112();
//				migr.migration(SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION);
//			}
//
//			logger.info("System migration end....");

			return true;
//		}
	}

	/**
	 * system table의 존재 유무를 파악하여 생성합니다.
	 * 
	 */
	private static void createSystemTable() throws Exception {

		java.sql.Connection javaConn = null;
		Statement stmt = null;
		String createMsg = "";

		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
			javaConn = sqlClient.getDataSource().getConnection();

			// 테이블 생성
			stmt = javaConn.createStatement();
//			boolean boolResult = false;

			Object obj = null;
			if (ApplicationArgumentUtils.isDBServer()) {
				logger.info("*** Remote DB System Teable creation ***");
				obj = new TadpoleMySQLDDL();
			} else {
				logger.info("*** SQLite System Teable creation ***");
				obj = new TadpoleSQLIteDDL();
			}

			String targetDDL = "";
			for (java.lang.reflect.Field field : obj.getClass().getFields()) {
				targetDDL = obj.getClass().getField(field.getName()).get("").toString();
				//targetDDL = new String(targetDDL.getBytes(), "ISO-8859-1");
				createMsg = "System Table create [" + targetDDL + "]";
				stmt.execute(targetDDL);
				logger.info("\n System Table create ==>> " + field.getName());
			}

		} catch (Exception e) {
			logger.error(createMsg, e);
			throw e;

		} finally {
			try { stmt.close(); } catch(Exception e) {}
			try { javaConn.close(); } catch (Exception e) {}
		}
	}
	
	/**
	 * insert initialize data 
	 * 
	 * @throws Exception
	 */
	private static void insertInitialData() throws Exception {
		// 시스템 기본 정보를 입력합니다.
		TadpoleSystemQuery.newSystemInfo(SystemDefine.NAME, SystemDefine.MAJOR_VERSION, SystemDefine.SUB_VERSION, SystemDefine.INFORMATION);

		// add basic group
//		String createMsg = "AdminGroup crateing....";
		UserGroupDAO groupAdmin = TadpoleSystem_UserGroupQuery.newUserGroup("AdminGroup");

//		createMsg = "TestGroup crateing....";
		UserGroupDAO groupTest = TadpoleSystem_UserGroupQuery.newUserGroup("TestGroup");

		// add basic user
//		createMsg = ADMIN_EMAIL + " user creating....";
		UserDAO adminUser = TadpoleSystem_UserQuery.newUser(ADMIN_EMAIL, ADMIN_PASSWD, ADMIN_NAME, "en_us", PublicTadpoleDefine.YES_NO.YES.toString(),
				PublicTadpoleDefine.SecurityHint.QUESTION2.getKey(), "tadpole");

//		createMsg = MANAGER_EMAIL + " user creating....";
		UserDAO managerUser = TadpoleSystem_UserQuery.newUser(MANAGER_EMAIL, MANAGER_PASSWD, MANAGER_NAME, "en_us", PublicTadpoleDefine.YES_NO.YES.toString(),
				PublicTadpoleDefine.SecurityHint.QUESTION2.getKey(), "tadpole");

//		createMsg = GUEST_EMAIL + " user creating....";
		UserDAO gusetUser = TadpoleSystem_UserQuery.newUser(GUEST_EMAIL, GUEST_PASSWD, GUEST_NAME, "en_us", PublicTadpoleDefine.YES_NO.YES.toString(),
				PublicTadpoleDefine.SecurityHint.QUESTION2.getKey(), "tadpole");

		// add group_role
		TadpoleSystem_UserRole.newUserRole(groupAdmin.getSeq(), adminUser.getSeq(), PublicTadpoleDefine.USER_TYPE.ADMIN.toString(), PublicTadpoleDefine.YES_NO.NO.toString(), PublicTadpoleDefine.USER_TYPE.ADMIN.toString());
		TadpoleSystem_UserRole.newUserRole(groupTest.getSeq(), managerUser.getSeq(), PublicTadpoleDefine.USER_TYPE.MANAGER.toString(), PublicTadpoleDefine.YES_NO.NO.toString(), PublicTadpoleDefine.USER_TYPE.MANAGER.toString());
		TadpoleSystem_UserRole.newUserRole(groupTest.getSeq(), gusetUser.getSeq(), PublicTadpoleDefine.USER_TYPE.USER.toString(), PublicTadpoleDefine.YES_NO.NO.toString(), PublicTadpoleDefine.USER_TYPE.USER.toString());

	}

	/**
	 * Tadpole Engine db를 초기화 합니다.
	 * 
	 * @param dbServerPath
	 */
	private static void initEngineDB(String dbServerPath) {
		tadpoleEngineDB = new UserDBDAO();

		// local db
		if ("".equals(dbServerPath)) {

			tadpoleEngineDB.setDbms_types(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDBToString());
			tadpoleEngineDB.setUrl(String.format(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDB_URL_INFO(), DEFAULT_DB_FILE_LOCATION + DB_NAME));
			tadpoleEngineDB.setDb(DB_INFORMATION);
			tadpoleEngineDB.setDisplay_name(DB_INFORMATION);
			tadpoleEngineDB.setPasswd(""); //$NON-NLS-1$
			tadpoleEngineDB.setUsers(""); //$NON-NLS-1$			

		} else {
			try {

				// decrypt
				String propData = CipherManager.getInstance().decryption(dbServerPath);
				InputStream is = new ByteArrayInputStream(propData.getBytes());

				// properties
				Properties prop = new Properties();
				prop.load(is);

				String whichDB 	= prop.getProperty("DB").trim();
				String ip 		= prop.getProperty("ip").trim();
				String port 	= prop.getProperty("port").trim();
				String database = prop.getProperty("database").trim();
				String user 	= prop.getProperty("user").trim();
				String passwd 	= prop.getProperty("password").trim();
				
				// make userDB
				if("MYSQL".equalsIgnoreCase(whichDB)) {
					tadpoleEngineDB.setDbms_types(DBDefine.TADPOLE_SYSTEM_MYSQL_DEFAULT.getDBToString());
					tadpoleEngineDB.setUrl(String.format(DBDefine.TADPOLE_SYSTEM_MYSQL_DEFAULT.getDB_URL_INFO(), ip, port, database));
					tadpoleEngineDB.setDb(database);
					tadpoleEngineDB.setDisplay_name(DBDefine.TADPOLE_SYSTEM_MYSQL_DEFAULT.getDBToString());
					tadpoleEngineDB.setUsers(user);
					tadpoleEngineDB.setPasswd(passwd);
//				} else if("PGSQL".equalsIgnoreCase(whichDB)) {
//					tadpoleEngineDB.setDbms_types(DBDefine.TADPOLE_SYSTEM_PGSQL_DEFAULT.getDBToString());
//					tadpoleEngineDB.setUrl(String.format(DBDefine.TADPOLE_SYSTEM_PGSQL_DEFAULT.getDB_URL_INFO(), ip, port, database));
//					
//					String isSSL = prop.getProperty("isSSL");
//					if("true".equals(isSSL)) {
//						tadpoleEngineDB.setUrl( tadpoleEngineDB.getUrl() + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory");
//					}
//					
//					tadpoleEngineDB.setDb(database);
//					tadpoleEngineDB.setDisplay_name(DBDefine.TADPOLE_SYSTEM_PGSQL_DEFAULT.getDBToString());
//					tadpoleEngineDB.setUsers(user);
//					tadpoleEngineDB.setPasswd(passwd);
				}

			} catch (Exception ioe) {
				logger.error("File not found exception or file encrypt exception. check the exist file." + dbServerPath, ioe);
				System.exit(0);
			}

		} // is local db?
	}

}
