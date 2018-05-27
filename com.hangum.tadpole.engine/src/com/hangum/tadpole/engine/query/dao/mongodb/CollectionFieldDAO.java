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
package com.hangum.tadpole.engine.query.dao.mongodb;

import java.util.ArrayList;
import java.util.List;

/**
 * mongodb collection field info
 * 
 * @author hangum
 *
 */
public class CollectionFieldDAO {
	String Field = "";
	String Type= "";
	String Key= "";
	
	String newIndex = "";
	
	List<CollectionFieldDAO> children = new ArrayList<CollectionFieldDAO>();
	
	public CollectionFieldDAO(String field, String type, String key) {
		this.Field = field;
		this.Type = type;
		this.Key = key;
	}

	public String getField() {
		return Field;
	}

	public void setField(String field) {
		Field = field;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public List<CollectionFieldDAO> getChildren() {
		return children;
	}

	public void setChildren(List<CollectionFieldDAO> children) {
		this.children = children;
	}
	
	public String getNewIndex() {
		return newIndex;
	}
	
	public void setNewIndex(String newIndex) {
		this.newIndex = newIndex;
	}

}
