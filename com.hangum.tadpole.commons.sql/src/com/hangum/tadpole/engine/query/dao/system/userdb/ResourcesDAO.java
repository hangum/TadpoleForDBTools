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
package com.hangum.tadpole.engine.query.dao.system.userdb;

import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * resource 
 * 
 * @author hangum
 *
 */
public class ResourcesDAO {
	public enum DB_RESOURCE_TYPE {USER_RESOURCE, SCHEMAS, PG_EXTENSION}; 
	
	protected String name = "Resources";
	protected DB_RESOURCE_TYPE type = DB_RESOURCE_TYPE.USER_RESOURCE;
	protected List listResource = new ArrayList();
	private UserDBDAO userDBDAO;
	
	public ResourcesDAO(UserDBDAO userDB) {
		this.userDBDAO = userDB;
	}
	
	/**
	 * @return the userDBDAO
	 */
	public UserDBDAO getUserDBDAO() {
		return userDBDAO;
	}

	/**
	 * @param userDBDAO the userDBDAO to set
	 */
	public void setUserDBDAO(UserDBDAO userDBDAO) {
		this.userDBDAO = userDBDAO;
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
	 * @return the type
	 */
	public DB_RESOURCE_TYPE getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DB_RESOURCE_TYPE type) {
		this.type = type;
	}

	/**
	 * @return the listResource
	 */
	public List getListResource() {
		return listResource;
	}

	/**
	 * @param listResource the listResource to set
	 */
	public void setListResource(List listResource) {
		this.listResource = listResource;
	}

	
}
