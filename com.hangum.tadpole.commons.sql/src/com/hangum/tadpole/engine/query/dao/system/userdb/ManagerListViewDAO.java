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

import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO.DB_RESOURCE_TYPE;

/**
 * manager lsit view dao
 * 
 * @author hangum
 *
 */
public class ManagerListViewDAO {

	protected ManagerListDTO parent;

	/** list resource */
	protected List<ResourcesDAO> listResource = new ArrayList<ResourcesDAO>();
	
	public ManagerListDTO getParent() {
		return parent;
	}
	
	public void setParent(ManagerListDTO parent) {
		this.parent = parent;
	}

	/**
	 * @return the listResource
	 */
	public List<ResourcesDAO> getListResource() {
		return listResource;
	}

	/**
	 * @param listResource the listResource to set
	 */
	public void setListResource(List<ResourcesDAO> listResource) {
		this.listResource = listResource;
	}
	
	public ResourcesDAO findResource(DB_RESOURCE_TYPE  type) {
		for(ResourcesDAO dao : getListResource()) {
			if(dao.getType() == type) return dao;
		}
		
		return null;
	}

}
