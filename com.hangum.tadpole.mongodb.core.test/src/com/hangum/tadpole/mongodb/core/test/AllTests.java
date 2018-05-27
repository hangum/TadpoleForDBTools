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
