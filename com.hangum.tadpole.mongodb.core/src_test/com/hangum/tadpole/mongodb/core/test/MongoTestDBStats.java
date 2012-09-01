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

import com.hangum.db.util.JSONUtil;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * db.stats();
 * 
 * @author hangum
 * 
 */
public class MongoTestDBStats  {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ConAndAuthentication testMongoCls = new ConAndAuthentication();
		Mongo mongo = testMongoCls.connection(ConAndAuthentication.serverurl, ConAndAuthentication.port);
		DB db = mongo.getDB("test");
		
		CommandResult cr = db.getStats();
			
		System.out.println("[ok]" + cr.ok() );
		if(!cr.ok()) System.out.println("[Exception ]" + cr.getException().toString());
		System.out.println("[toString]" + JSONUtil.getPretty(cr.toString()));
		System.out.println("[size]" + cr.size() );
		
		mongo.close();
	}

}
