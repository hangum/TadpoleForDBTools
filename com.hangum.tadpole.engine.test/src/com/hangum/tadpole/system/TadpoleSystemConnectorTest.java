///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.system;
//
//import java.io.File;
//
//import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
//
//import junit.framework.TestCase;
//
///**
// * {@link com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer 시스템엔진디비 초기화}
// * 
// * @author hangum
// *
// */
//public class TadpoleSystemConnectorTest extends TestCase {
//
//	/**
//	 * {@link com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer#createSystemTable() 시스템엔진디비 초기화}
//	 * 
//	 */
//	public void testCreateSystemTable() {
//	
//		try {
//			// 기존 디비 파일을 삭제 하고 제대로 생성되는지 검사합니다.
//			new File(TadpoleSystemInitializer.DEFAULT_DB_FILE_LOCATION + TadpoleSystemInitializer.DB_NAME).delete(); 
//
//			//
//			TadpoleSystemInitializer.initSystem();
//			
//		} catch (Exception e) {
//			fail("fail System Connection " + e.getMessage());
//		}
//	}
//
//}
