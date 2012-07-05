package com.hangum.tadpole.mongodb.core.test;

import com.hangum.tadpole.mongodb.core.connection.MongoDBConnectionTest;
import com.hangum.tadpole.mongodb.core.utils.MongoDBJavaStrToJavaObjTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * mongodb의 test suite
 * 
 * @author hangum
 *
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(MongoDBConnectionTest.class);
		
		suite.addTestSuite(MongoDBJavaStrToJavaObjTest.class);
		
		//$JUnit-END$
		return suite;
	}

}
