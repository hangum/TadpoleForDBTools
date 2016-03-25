/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.mysql;

/**
 * commons schema object
 * 
 * @author hangum
 *
 */
public abstract class StructObjectDAO {
	protected String schema_name = "";
	
	/**
	 * @return the schema_name
	 */
	public String getSchema_name() {
		return schema_name;
	}

	/**
	 * @param schema_name the schema_name to set
	 */
	public void setSchema_name(String schema_name) {
		this.schema_name = schema_name;
	}
}
