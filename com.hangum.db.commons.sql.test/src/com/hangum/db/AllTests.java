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
package com.hangum.db;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.hangum.db.commons.sql.map.SQLMapTest;
import com.hangum.db.system.TadpoleSystemCommonsTest;
import com.hangum.db.system.TadpoleSystemConnectorTest;
import com.hangum.db.system.TadpoleSystem_UserDBQueryTest;
import com.hangum.db.system.TadpoleSystem_UserDBResourceTest;
import com.hangum.db.system.TadpoleSystem_UserGroupQueryTest;
import com.hangum.db.system.TadpoleSystem_UserInfoDataTest;
import com.hangum.db.system.TadpoleSystem_UserQueryTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		
		//$JUnit-BEGIN$
		suite.addTestSuite(TadpoleSystemCommonsTest.class);
		
		suite.addTestSuite(TadpoleSystemConnectorTest.class);
		suite.addTestSuite(TadpoleSystem_UserGroupQueryTest.class);
		suite.addTestSuite(TadpoleSystem_UserQueryTest.class);
		suite.addTestSuite(TadpoleSystem_UserDBQueryTest.class);
		suite.addTestSuite(TadpoleSystem_UserDBResourceTest.class);
		suite.addTestSuite(TadpoleSystem_UserInfoDataTest.class);
		
		suite.addTestSuite(SQLMapTest.class);
		
		//$JUnit-END$
		
		return suite;
	}

}
