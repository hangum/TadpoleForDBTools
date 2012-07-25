package com.hangum.tadpole.mongodb.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.hangum.tadpole.mongodb.core.connection.MongoDBConnectionTest;
import com.hangum.tadpole.mongodb.core.utils.MongoDBJavaStrToJavaObjTest;

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
		
//		suite.addTestSuite(MongoSQLParserTest.class);
		
		//$JUnit-END$
		return suite;
	}

}
