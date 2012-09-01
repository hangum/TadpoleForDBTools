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
