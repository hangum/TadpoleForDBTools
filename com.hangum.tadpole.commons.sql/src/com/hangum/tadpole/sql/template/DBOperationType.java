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
package com.hangum.tadpole.sql.template;

/**
 * db operation type
 * 
 * @author hangum
 *
 */
public enum DBOperationType {
	PRODUCTION("Production Sever"), 
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
		if(PRODUCTION.typeName.equals(name)) return PRODUCTION;
		else if(DEVELOP.typeName.equals(name)) return DEVELOP;
		else return OTHERS;
	}
}
