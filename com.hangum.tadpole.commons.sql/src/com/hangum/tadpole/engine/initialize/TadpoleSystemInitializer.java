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
package com.hangum.tadpole.engine.initialize;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.TadpoleEngineActivator;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleApplicationContextManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

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

	public static String DEFAULT_DB_FILE_LOCATION = "";// //$NON-NLS-1$
	public static final String DB_NAME = "tadpole-system.db"; //$NON-NLS-1$

	/**
	 * 시스템 시작환경이 디비를 공유모드로 사용할지 환경을 선택합니다. -dbServer 데이터베이스암호화 정보.
	 */
	static {
		String dbServerPath = "";
		
		// jdbc driver 파일을 옮긴다.

		// 로컬 디비를 사용 할 경우.
		if (!ApplicationArgumentUtils.isDBServer()) {

			try {
				DEFAULT_DB_FILE_LOCATION = SystemDefine.getConfigureRoot() + "configuration/tadpole/db/";// //$NON-NLS-1$
				
				if(ApplicationArgumentUtils.isDBPath()) DEFAULT_DB_FILE_LOCATION = ApplicationArgumentUtils.getDBPath() + File.separator;
				if (!new File(DEFAULT_DB_FILE_LOCATION).exists()) {
					if(!new File(DEFAULT_DB_FILE_LOCATION).mkdirs()) {
						throw new Exception("Can not create the Directory. " + DEFAULT_DB_FILE_LOCATION);
					}
				}
				if (logger.isDebugEnabled()) logger.debug(DEFAULT_DB_FILE_LOCATION + DB_NAME);
			} catch(Exception e) {
				logger.error("System DB Initialize exception", e);
				System.exit(0);
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
	 * 1. 테이블이 없다면 테이블 생성하고, 초기데이터를 만듭니다. 2. 테이블이 있다면 디비가 버전과 동일한지 검사합니다.
	 * 
	 * @throws Exception
	 */
	public static boolean initSystem() throws Exception {
		
		// move to driver file
		if(!TadpoleApplicationContextManager.isSystemInitialize()) initJDBCDriver();
		
		// Is SQLite?
		if (!ApplicationArgumentUtils.isDBServer()) {
			if(!new File(DEFAULT_DB_FILE_LOCATION + DB_NAME).exists()) {
				if(logger.isInfoEnabled()) logger.info("Createion Engine DB. Type is SQLite.");
				ClassLoader classLoader = TadpoleSystemInitializer.class.getClassLoader();
				InputStream is = classLoader.getResourceAsStream("com/hangum/tadpole/engine/initialize/TadpoleEngineDBEngine.sqlite");
				
				byte[] dataByte = new byte[1024];
				int len = 0;
				
				FileOutputStream fos = new FileOutputStream(DEFAULT_DB_FILE_LOCATION + DB_NAME);
				while((len = is.read(dataByte)) > 0) {
					fos.write(dataByte, 0, len);
				}
				
				fos.close();
				is.close();
			}
		}
		
		return TadpoleApplicationContextManager.getSystemAdmin() == null?false:true;
	}
	
	/**
	 * initialize jdbc driver
	 */
	private static void initJDBCDriver() {
		final String strJDBCDir = ApplicationArgumentUtils.JDBC_RESOURCE_DIR;
		File jdbcLocationDir = new File(strJDBCDir);
		logger.info("######### TDB JDBC Driver local Path : " + strJDBCDir);
		if(!jdbcLocationDir.exists()) {
			logger.info("\t##### Copying initialize JDBC Driver........");
			try {
				jdbcLocationDir.mkdirs();
				
				File fileEngine = FileLocator.getBundleFile(TadpoleEngineActivator.getDefault().getBundle());
				String filePath = fileEngine.getAbsolutePath() + "/libs/driver";
				logger.info("##### TDB JDBC URI: " + filePath);
				
				FileUtils.copyDirectory(new File(filePath), new File(strJDBCDir));
			} catch(Exception e) {
				logger.error("Initialize JDBC driver file", e);
			}
		}
		
		// driver loading
		try {
			JDBCDriverLoader.addJARDir(strJDBCDir);
		} catch(Exception e) {
			logger.error("JDBC driver loading", e);
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
		if ("".equals(dbServerPath)) {

			tadpoleEngineDB.setDbms_type(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDBToString());
			tadpoleEngineDB.setUrl(String.format(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDB_URL_INFO(), DEFAULT_DB_FILE_LOCATION + DB_NAME));
			tadpoleEngineDB.setDb("SQLite"); //$NON-NLS-1$
			tadpoleEngineDB.setDisplay_name("Tadpole Engine DB"); //$NON-NLS-1$
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
					tadpoleEngineDB.setDbms_type(DBDefine.TADPOLE_SYSTEM_MYSQL_DEFAULT.getDBToString());
					tadpoleEngineDB.setUrl(String.format(DBDefine.TADPOLE_SYSTEM_MYSQL_DEFAULT.getDB_URL_INFO(), ip, port, database));
					tadpoleEngineDB.setDb(database);
					tadpoleEngineDB.setDisplay_name(DBDefine.TADPOLE_SYSTEM_MYSQL_DEFAULT.getDBToString());
					tadpoleEngineDB.setUsers(user);
					tadpoleEngineDB.setPasswd(passwd);
				}

			} catch (Exception ioe) {
				logger.error("File not found exception or file encrypt exception. check the exist file." + dbServerPath, ioe);
				System.exit(0);
			}

		} // is local db?
	}
	
}
