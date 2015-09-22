/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.editor.restfulapi.dao;

import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;

/**
 * RESTFulAPI DAO
 * 
 * @author hangum
 *
 */
public class RESTFulAPIDAO {
	private String strURL = "";
	private ResourceManagerDAO resourceManagerDao;
	
	private List<RESTFulAPIDAO> listChildren = new ArrayList<>();
	
//	private RESTFulAPIDAO parent;

	public RESTFulAPIDAO() {}
	
	public RESTFulAPIDAO(String strURL) {
		this.strURL = strURL;
	}
	
	public RESTFulAPIDAO(String strURL, ResourceManagerDAO dao) {
		this.strURL = strURL;
		this.resourceManagerDao = dao;
	}

	public String getStrURL() {
		return strURL;
	}

	public void setStrURL(String strURL) {
		this.strURL = strURL;
	}

	public ResourceManagerDAO getResourceManagerDao() {
		return resourceManagerDao;
	}

	public void setResourceManagerDao(ResourceManagerDAO resourceManagerDao) {
		this.resourceManagerDao = resourceManagerDao;
	}

	public List<RESTFulAPIDAO> getListChildren() {
		return listChildren;
	}

	public void setListChildren(List<RESTFulAPIDAO> listChildren) {
		this.listChildren = listChildren;
	}
	
		
}
