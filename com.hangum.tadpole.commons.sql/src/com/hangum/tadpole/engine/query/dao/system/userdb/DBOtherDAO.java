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

/**
 * UserDBDAO 의 DB아래 포함된 특수 오브젝트를 지정한다. 
 * 
 * @author hangum
 *
 */
public class DBOtherDAO {
	private ResourcesDAO parent;
	
	private String name;
	private String comment = "";
	private Object userObject;

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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the userObject
	 */
	public Object getUserObject() {
		return userObject;
	}

	/**
	 * @param userObject the userObject to set
	 */
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	/**
	 * @return the parent
	 */
	public ResourcesDAO getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(ResourcesDAO parent) {
		this.parent = parent;
	}
}
