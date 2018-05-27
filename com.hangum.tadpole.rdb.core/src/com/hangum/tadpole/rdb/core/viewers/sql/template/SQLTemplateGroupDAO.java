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
package com.hangum.tadpole.rdb.core.viewers.sql.template;

import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.engine.query.dao.system.SQLTemplateDAO;

/**
 * SQL Template group dao
 * 
 * @author hangum
 *
 */
public class SQLTemplateGroupDAO {
	String name;
	List<SQLTemplateDAO> childList = new ArrayList<SQLTemplateDAO>(); 

	public SQLTemplateGroupDAO() {
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
	 * @return the childList
	 */
	public List<SQLTemplateDAO> getChildList() {
		return childList;
	}

	/**
	 * @param childList the childList to set
	 */
	public void setChildList(List<SQLTemplateDAO> childList) {
		this.childList = childList;
	}

	
}
