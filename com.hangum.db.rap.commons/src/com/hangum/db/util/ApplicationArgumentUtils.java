package com.hangum.db.util;

import org.eclipse.core.runtime.Platform;

/**
 * 시스템 시작 유틸 
 * 
 * @author hangum
 *
 */
public class ApplicationArgumentUtils {

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
	 * runtime시에 실행 모드를 검사합니다.
	 * 
	 * @param checkString
	 * @return
	 */
	public static boolean checkString(String checkString) {
		String[] applicationArgs = Platform.getApplicationArgs();
		
		for (String string : applicationArgs) {
			if( string.startsWith(checkString) ) return true;
		}
		
		return false;
	}
}
