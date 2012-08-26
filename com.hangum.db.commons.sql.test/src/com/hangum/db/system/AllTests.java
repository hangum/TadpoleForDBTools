package com.hangum.db.system;

import junit.framework.Test;
import junit.framework.TestSuite;

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
		
		//$JUnit-END$
		
		return suite;
	}

}
