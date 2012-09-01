/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.system;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import com.hangum.db.Messages;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.define.Define;
import com.hangum.db.util.ApplicationArgumentUtils;
import com.hangum.db.util.secret.EncryptiDecryptUtil;
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
public class TadpoleSystemConnector {
	private static final Logger logger = Logger.getLogger(TadpoleSystemConnector.class);
	
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
				
				boolean boolResult = false;
				
				if(!ApplicationArgumentUtils.isDBServer()) {
					
					// group
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_group_create);
					logger.info("Group" + (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7) );
					
					// user
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_table_create );
					logger.info(Messages.TadpoleSystemConnector_5+ (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7) );
					
//					// ext account
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_external_account );
					logger.info("external account table "+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
					
					// user db
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_table_create);
					logger.info(Messages.TadpoleSystemConnector_9+ (!boolResult?Messages.TadpoleSystemConnector_10:Messages.TadpoleSystemConnector_11) );
					
					// user resource
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_resource_create	);
					logger.info(Messages.TadpoleSystemConnector_13+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
					
					// user resource data
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_resource_data_create	);
					logger.info("user_db_resource_data Tables" + (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
					
					// user info data
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_info_data);
					logger.info("user_info_data"+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
					
				// default is cubrid
				} else {
				
					// group
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_group_create_CUBRID);
					logger.info("Group" + (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7) );
					
					// user
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_table_create_CUBRID );
					logger.info(Messages.TadpoleSystemConnector_5+ (!boolResult?Messages.TadpoleSystemConnector_6:Messages.TadpoleSystemConnector_7) );
					
					// ext account
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_external_account_CUBRID );
					logger.info("external account table "+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
					
					// user db
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_table_create_CUBRID );
					logger.info(Messages.TadpoleSystemConnector_9+ (!boolResult?Messages.TadpoleSystemConnector_10:Messages.TadpoleSystemConnector_11) );
					
					// user resource
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_resource_create_CUBRID );
					logger.info(Messages.TadpoleSystemConnector_13+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
					
					// user resource data
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_db_resource_data_create_CUBRID );
					logger.info("user_db_resource_data Tables" + (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
					
					// user info data
					boolResult = stmt.execute( Messages.TadpoleSystemConnector_user_info_data_CUBRID );
					logger.info("user_info_data"+ (!boolResult?Messages.TadpoleSystemConnector_14:Messages.TadpoleSystemConnector_15) );
					
				}
				
				// 기본 그룹
				int seqAdm = TadpoleSystem_UserGroupQuery.newUserGroup("AdminGroup");
				int seqTest = TadpoleSystem_UserGroupQuery.newUserGroup("TestGroup");
				
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

	/**
	 * Tadpole Engine db를 초기화 합니다.
	 * 
	 * @param dbServerPath 
	 */
	private static void initEngineDB(String dbServerPath) {
		tadpoleEngineDB = new UserDBDAO();
		
		// local db
		if("".equals(dbServerPath)) {
			
			tadpoleEngineDB.setTypes(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDBToString());
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
				tadpoleEngineDB.setTypes(DBDefine.TADPOLE_SYSTEM_CUBRID_DEFAULT.getDBToString());
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
