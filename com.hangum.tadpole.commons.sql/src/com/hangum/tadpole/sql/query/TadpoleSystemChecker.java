/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.sql.query;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.sql.Messages;

/**
 * Tadpole System checker
 * 올챙이가 동작가능한 환경인지 검사합니다. 
 * 동작이 불가능한 환경이라면, 시스템 로그를 남기고 죽도록 합니다. 
 * 
 * 1. 맥일 경우 jvm 1.7이상 기타 시스템일 경우 1.6 이상 동작환경인지 검사합니다.
 *
 * @author hangum
 *
 */
public class TadpoleSystemChecker {

	private static final String OS = System.getProperty("os.name"); //$NON-NLS-1$
	private static final String JAVA_VERSION = System.getProperty("java.version"); //$NON-NLS-1$
	
	/**
	 * 올챙이가 동작 가능한 환경인지 검사합니다. 
	 * 
	 * @return
	 */
	public static void checker() throws Exception {
		double dblJavaVersion = Double.parseDouble(JAVA_VERSION.substring(0, 3));
		
		// mac 일경우 jvm 1.7 이상 동작 환경.
		if(isMac()) {
			if(dblJavaVersion < 1.7d) {
				throw new Exception(String.format(Messages.TadpoleSystemChecker_2, "1.7.x")); //$NON-NLS-2$
			}
			
		// 그 이외일 경우 1.6 이상인지 검사합니다. 
		} else {
			if(dblJavaVersion < 1.6d) {
				throw new Exception(String.format(Messages.TadpoleSystemChecker_2, "1.6.x")); //$NON-NLS-2$
			}
		}
	}

	/**
	 * is mac
	 * @return
	 */
	private static boolean isMac() {
		return StringUtils.containsIgnoreCase(OS, "mac"); //$NON-NLS-1$
	}
}
