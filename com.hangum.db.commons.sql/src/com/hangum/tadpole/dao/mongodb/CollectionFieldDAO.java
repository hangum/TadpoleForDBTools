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
package com.hangum.tadpole.dao.mongodb;

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

}
