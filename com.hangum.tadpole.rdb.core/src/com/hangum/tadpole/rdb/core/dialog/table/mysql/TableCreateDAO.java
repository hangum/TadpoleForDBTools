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
package com.hangum.tadpole.rdb.core.dialog.table.mysql;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;

/**
 * table create dao
 * 
 * @author hangum
 *
 */
public class TableCreateDAO {
	
	String name;
	String encoding;
	String collation;
	String type;
	
	public TableCreateDAO() {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the collation
	 */
	public String getCollation() {
		return collation;
	}

	/**
	 * @param collation the collation to set
	 */
	public void setCollation(String collation) {
		this.collation = collation;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getFullName(UserDBDAO userDB) {
		if(StringUtils.isBlank(userDB.getSchema())){
			return SQLUtil.makeIdentifierName(userDB, this.getName());
		}else{
			return String.format("%s.%s", SQLUtil.makeIdentifierName(userDB, userDB.getSchema()), SQLUtil.makeIdentifierName(userDB, this.getName()) );
		}
	}
	
}
