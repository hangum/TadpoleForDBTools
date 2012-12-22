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
package com.hangum.tadpole.mongodb.core.define;

/**
 * mongodb defien class
 * 
 * @author hangum
 *
 */
public class MongoDBDefine {

	/**
	 * ObjectID
	 */
	public static final int PRIMARY_ID_KEY = 99999;
	public static final String PRIMARY_ID_STRING = "__object_id__";
	
	public static final String[] SYSTEM_COLLECTION = {"system.namespaces", "system.indexes", "system.profile", "system.users"}; 
	
}
