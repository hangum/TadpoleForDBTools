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
package com.hangum.db.start;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import com.hangum.db.util.ApplicationArgumentUtils;
import com.hangum.db.util.ApplicationLock;

/**
 * <pre>
 * 시스템이 처음 시작했을때 해주어야 하는 일 정의 합니다.  
 * 이 클래스는 단 한번 실행합니다.
 * 
 * 1) statnalone mode일 경우 브라우저 시작해서 띄워줌.
 * 
 * </pre>
 * 
 * @author hangum
 * 
 */
public class TadpoleSystem {
	private static final Logger logger = Logger.getLogger(TadpoleSystem.class);

	public static void startInit() {
		if (ApplicationArgumentUtils.isStandaloneMode()) {
			
			try {
				
				boolean isLock = ApplicationLock.setLock("Tadpole For DB Tools application lock");
				if(logger.isDebugEnabled()) {
					logger.debug("############################################################");
					logger.debug("[Tadpole System is lock?] " + isLock);
					logger.debug("############################################################");
				}
				
				// 중복 실행 검사
				if(!isLock) {
					openURL();
					
					// browser 뛰우는 시간을 벌기위해 잠시 멈춥니다.
					try { Thread.sleep(3000); } catch(Exception e) {};
					
					// 프로젝트 이미 실행되어 있는지 검사합니다.
					System.exit(-1);
				} else {
					openURL();
				}
				
			} catch (Exception e) {
				logger.error("Tadpole System start error", e);
			}
			
		}
	}
	
	public static void stopInit() {
		if (ApplicationArgumentUtils.isStandaloneMode()) {
			try {
				Platform.getUserLocation().release();
			} catch(Exception e) {
				logger.error("Tadpole System stop error", e);
			}
					 
		}
	}

	public static void openURL() {
		TadpoleOpenBrowser openBrowser = new TadpoleOpenBrowser();
		
		Thread the = new Thread(openBrowser);
		the.start();
	}
	
}
