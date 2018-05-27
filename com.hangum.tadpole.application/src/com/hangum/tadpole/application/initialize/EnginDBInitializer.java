/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.application.initialize;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.mails.SendEmails;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.initialize.TadpoleEngineUserDB;
import com.hangum.tadpole.engine.manager.TadpoleApplicationContextManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.SystemDefine;

/**
 * <pre>
 * engine db를 설정한다.
 * 
 * 엔진의 런타임 옵션은targetProject/docs/engine argument options.txt 를 참고합니다.
 * </pre>
 * 
 * @author hangum
 * 
 */
public class EnginDBInitializer {
	private static final Logger logger = Logger.getLogger(EnginDBInitializer.class);
	
	public static String DEFAULT_DB_FILE_LOCATION = "";// //$NON-NLS-1$
	public static final String DB_NAME = "tadpole-system.db"; //$NON-NLS-1$

	/**
	 * 시스템 시작환경이 디비를 공유모드로 사용할지 환경을 선택합니다. -dbServer 데이터베이스암호화 정보.
	 */
	static {
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

		// 엔진 디비를 초기화합니다.
		initEngineDB();
	}

	/**
	 * system 초기화 합니다.
	 * 
	 * 1. 테이블이 없다면 테이블 생성하고, 초기데이터를 만듭니다. 2. 테이블이 있다면 디비가 버전과 동일한지 검사합니다.
	 * 
	 * @throws Exception
	 */
	public static boolean initSystem() throws Exception {
		
		// Is SQLite?
		if(!new File(DEFAULT_DB_FILE_LOCATION + DB_NAME).exists()) {
			if(logger.isInfoEnabled()) logger.info("Createion Engine DB. Type is SQLite.");
			ClassLoader classLoader = EnginDBInitializer.class.getClassLoader();
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
		
		// initialize email information
		GetAdminPreference.getSessionSMTPINFO();
		SendEmails.getInstance();
		
		return TadpoleApplicationContextManager.getSystemAdmin() == null?false:true;
	}

	/**
	 * Tadpole Engine db를 초기화 합니다.
	 * 
	 * @param dbServerPath
	 */
	private static void initEngineDB() {
		UserDBDAO tadpoleEngineDB = new UserDBDAO();

		tadpoleEngineDB.setDbms_type(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDBToString());
		tadpoleEngineDB.setUrl(String.format(DBDefine.TADPOLE_SYSTEM_DEFAULT.getDB_URL_INFO(), DEFAULT_DB_FILE_LOCATION + DB_NAME));
		tadpoleEngineDB.setDb("SQLite"); //$NON-NLS-1$
		tadpoleEngineDB.setDisplay_name("Tadpole Engine DB"); //$NON-NLS-1$
		tadpoleEngineDB.setPasswd(""); //$NON-NLS-1$
		tadpoleEngineDB.setUsers(""); //$NON-NLS-1$
		tadpoleEngineDB.setTdbUserID(PublicTadpoleDefine.DB_USER_ROLE_TYPE.SYSTEM_ADMIN.name());
		
		// setting engin db
		TadpoleEngineUserDB.setUserDB(tadpoleEngineDB);
	}
}
