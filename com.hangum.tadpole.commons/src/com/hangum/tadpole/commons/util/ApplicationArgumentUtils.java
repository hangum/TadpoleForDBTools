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
package com.hangum.tadpole.commons.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.SystemDefine;

/**
 * System application argument utils 
 * 
 * @author hangum
 *
 */
public class ApplicationArgumentUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ApplicationArgumentUtils.class);

	/**
	 * engine에서 사용하는 패스워드.
	 * 
	 * @return
	 */
	public static String getPasswd() {
		String passwd = "";
		
		try {
			passwd = getValue("-passwd");
		} catch(Exception e) {
			passwd = PublicTadpoleDefine.SYSTEM_DEFAULT_PASSWORD;
		}
		
		return passwd;
	}

	/**
	 * 리소스 디렉토리 루트 정보를 리턴합니다.
	 * @return
	 */
	public static String getResourcesDir() {
		String strResourceDir = "";
		
		try {
			strResourceDir = getValue("-resourcesDir");
		} catch(Exception e) {
			strResourceDir = FileUtils.getUserDirectoryPath() + IOUtils.DIR_SEPARATOR + "tadpole";
		}
		
		return strResourceDir + IOUtils.DIR_SEPARATOR;
	}
	
	/**
	 * 시스템의 테이블 초기 데이터를 초기화 루틴을 탈것인지 검사합니다.
	 * @return
	 * @throws Exception
	 */
	public static boolean isForceSystemInitialize() {
		return checkString("-forceSystemInitialize");
	}
	
	/**
	 * 엔진이 외부 디비를 사용 할 것인지?
	 * @return
	 * @throws Exception
	 */
	public static boolean isDBServer() {
		return checkString("-dbServer");
	}
	
	/**
	 * <pre>
	 * 	엔진이 디비를 공유 디비정보를 가져온다.
	 * </pre>
	 * 
	 * @return
	 */
	public static String getDbServer() throws Exception {
		return getValue("-dbServer");
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isDBPath() {
		return checkString("-dbPath");
	}
	
	/**
	 * 엔진디비를 로컬디비를 사용했을때 path를 적어 줍니다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getDBPath() throws Exception {
		return getValue("-dbPath");
	}
	
	/**
	 * 사용자 디비를 사용합니까?
	 * @return
	 */
	public static boolean isUseDB() {
		return checkString("-useDB");
	}
	
	/**
	 * 사용자 디비 목록을 가져옵니다.
	 * @return
	 * @throws Exception
	 */
	public static String getUseDB() throws Exception {
		return getValue("-useDB");
	}
	
	public static boolean isDefaultDB() {
		return checkString("-defaultDB");
	}
	public static String getDefaultDB() throws Exception {
		return getValue("-defaultDB");
	}

	/**
	 * <pre>
	 * application 시작 모드
	 * 
	 * application argement에 -standalone가 있으면 standalone 모드
	 * <pre>
	 * 
	 * @return
	 */
	public static boolean isStandaloneMode() {
		return checkString("-standalone");
	}
	
	/**
	 * test 모드이면
	 * 
	 * @return
	 */
	public static boolean isTestMode() {
		return checkString("-test");
	}
	
	/**
	 * testDB 모드이면
	 * 
	 * @return
	 */
	public static boolean isTestDBMode() {
		return checkString("-testDB");
	}
	
	/**
	 * check debug mode
	 */
	public static boolean isDebugMode() {
		return checkString("-debuglog");
	}
	
	/**
	 * IS Google Analytics use?
	 * 
	 * @return
	 */
	public static boolean isGAON() {
		return checkString("-GAON");
	}
	
	/**
	 * 신규 유저는 어드민의 허락이 필요한지?
	 * @return
	 */
	public static boolean isNewUserPermit() {
		return checkString("-newUserPermit");
	}
	/**
	 * 옵션이 정의되어 있지 않다면 어드민 허락이 필요없다.
	 * 
	 * @return
	 */
	public static boolean getNewUserPermit() {
		if(isNewUserPermit()) {
			try {
				return PublicTadpoleDefine.YES_NO.YES.name().equals(getValue("-newUserPermit"))?true:false;
			} catch (Exception e) {
				logger.error("check option : -newUserPermit exeception ", e);
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * runtime시에 argument의 value를 리턴합니다.
	 * 
	 * @param key
	 * @return
	 */
	private static String getValue(String key) throws Exception {
		String[] applicationArgs = getArguments();
		for(int i=0; i<applicationArgs.length; i++) {
			String arg = applicationArgs[i];
			if( arg.startsWith(key) ) {
				return applicationArgs[i+1];
			}
		}
	
		throw new Exception("Can't find argument. Find key is " + key);
	}
	/**
	 * runtime시에 argument가 있는지 검사합니다.
	 * 
	 * @param checkString
	 * @return
	 */
	private static boolean checkString(String checkString) {
		String[] applicationArgs = getArguments();
		
		for (String string : applicationArgs) {
			if( string.equalsIgnoreCase(checkString) ) return true;
		}
		
		return false;
	}
	
	/**
	 * application argument
	 * 
	 * @return
	 */
	private static String[] getArguments() {
		String[] applicationArgs = null;
		
		/* is osgi */
		if(SystemDefine.isOSGIRuntime()) {
			applicationArgs = Platform.getApplicationArgs();
		/* is web server and single  */
		} else {
			applicationArgs = new String[4];
			
			applicationArgs[0] = "-dbServer";
			applicationArgs[1] = System.getProperty("dbServer");
			if(applicationArgs[1] == null) logger.error("**** System Initialize exception : Not found Tadpole engine db");
				
			applicationArgs[2] = "-passwd";
			applicationArgs[3] = System.getProperty("passwd");
			if(applicationArgs[3] == null) applicationArgs[3] = PublicTadpoleDefine.SYSTEM_DEFAULT_PASSWORD;
		}
		
		return applicationArgs;
	}
}
