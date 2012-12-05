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
package com.hangum.tadpole.mongodb.core.connection;

/**
 * mongodb not found exception
 * 
 * @author hangum
 * 
 */
public class MongoDBNotFoundException extends Exception {
	
	public MongoDBNotFoundException(String msg) {
		super(msg);
	}
	
}
