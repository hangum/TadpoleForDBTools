/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.define;

/**
 * db operation type
 * 
 * @author hangum
 *
 */
public enum DBOperationType {
	REAL("Real Sever"), 
	DEVELOP("Develop Sever"), 
	OTHERS("Others Sever");

	private String typeName;
	
	private DBOperationType(String typeName) {
		this.typeName = typeName;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public static DBOperationType getNameToType(String name) {
		if(REAL.typeName.equals(name)) return REAL;
		else if(DEVELOP.typeName.equals(name)) return DEVELOP;
		else return OTHERS;
	}
}
