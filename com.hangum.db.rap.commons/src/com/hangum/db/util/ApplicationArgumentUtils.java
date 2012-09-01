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
package com.hangum.db.util;

import org.apache.log4j.Logger;

import org.eclipse.core.runtime.Platform;

/**
 * 시스템 시작 유틸 
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
	 * check debug mode
	 */
	public static boolean isDebugMode() {
		return checkString("-debuglog");
	}
	
	/**
	 * runtime시에 argument의 value를 리턴합니다.
	 * 
	 * @param key
	 * @return
	 */
	private static String getValue(String key) throws Exception {
		String[] applicationArgs = Platform.getApplicationArgs();
		for(int i=0; i<applicationArgs.length; i++) {
			String arg = applicationArgs[i];
			if( arg.startsWith(key) ) {
				return applicationArgs[i+1];
			}
		}
	
		throw new Exception("Can't find argument. key is " + key);
	}
	
	/**
	 * runtime시에 argument가 있는지 검사합니다.
	 * 
	 * @param checkString
	 * @return
	 */
	private static boolean checkString(String checkString) {
		String[] applicationArgs = Platform.getApplicationArgs();
		
		for (String string : applicationArgs) {
			if( string.startsWith(checkString) ) return true;
		}
		
		return false;
	}
}
