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
package com.hangum.tadpole.mongodb.core.dto;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBObject;


/**
 * mongodb tree view dto
 * 
 * @author hangum
 *
 */
public class MongodbTreeViewDTO {
	/**
	 * 리얼 데이터
	 */
	private DBObject dbObject;
	private String realKey;
	
	private String key;
	private String value;
	private String type;
	
	private List<MongodbTreeViewDTO> children = new ArrayList<MongodbTreeViewDTO>();
	/**
	 * parent
	 */
	private MongodbTreeViewDTO parent;
	
	public MongodbTreeViewDTO() {	
	}
	
	public MongodbTreeViewDTO(DBObject dbObject, String key, String value, String type) {
		this.dbObject = dbObject;
		
		this.key = key;
		this.value = value;
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	public String getRealKey() {
		return realKey;
	}

	public void setRealKey(String realKey) {
		this.realKey = realKey;
	}

	public List<MongodbTreeViewDTO> getChildren() {
		return children;
	}
	
	public void setChildren(List<MongodbTreeViewDTO> children) {
		this.children = children;
		
		for (MongodbTreeViewDTO mongodbTreeViewDTO : children) {
			mongodbTreeViewDTO.parent = this;
		}
	}

	public DBObject getDbObject() {
		return dbObject;
	}

	public void setDbObject(DBObject dbObject) {
		this.dbObject = dbObject;
	}
	
	public MongodbTreeViewDTO getParent() {
		return parent;
	}
	
}
