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

import java.io.File;

import junit.framework.TestCase;

/**
 * {@link com.hangum.db.system.TadpoleSystemConnector 시스템엔진디비 초기화}
 * 
 * @author hangum
 *
 */
public class TadpoleSystemConnectorTest extends TestCase {

	/**
	 * {@link com.hangum.db.system.TadpoleSystemConnector#createSystemTable() 시스템엔진디비 초기화}
	 * 
	 */
	public void testCreateSystemTable() {
	
		try {
			// 기존 디비 파일을 삭제 하고 제대로 생성되는지 검사합니다.
			new File(TadpoleSystemConnector.DB_FILE_LOCATION + TadpoleSystemConnector.DB_NAME).delete(); 

			//
			TadpoleSystemConnector.createSystemTable();
			
		} catch (Exception e) {
			fail("fail System Connection " + e.getMessage());
		}
	}

}
