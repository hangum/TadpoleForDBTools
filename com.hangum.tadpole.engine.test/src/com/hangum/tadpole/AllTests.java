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
package com.hangum.tadpole;

import com.hangum.tadpole.commons.sql.map.SQLMapTest;
import com.hangum.tadpole.system.TadpoleSystemCommonsTest;
import com.hangum.tadpole.system.TadpoleSystem_UserInfoDataTest;
import com.hangum.tadpole.system.TadpoleSystem_UserQueryTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		
		//$JUnit-BEGIN$
		suite.addTestSuite(TadpoleSystemCommonsTest.class);
		
//		suite.addTestSuite(TadpoleSystemConnectorTest.class);
//		suite.addTestSuite(TadpoleSystem_UserGroupQueryTest.class);
		suite.addTestSuite(TadpoleSystem_UserQueryTest.class);
//		suite.addTestSuite(TadpoleSystem_UserDBQueryTest.class);
//		suite.addTestSuite(TadpoleSystem_UserDBResourceTest.class);
		suite.addTestSuite(TadpoleSystem_UserInfoDataTest.class);
		
		suite.addTestSuite(SQLMapTest.class);
		
		//$JUnit-END$
		
		return suite;
	}

}
