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
 * MongoDB Index
 * 
 * @author hangum
 *
 */
public class MongoDBIndexDAO {

	String v;
	List<MongoDBIndexFieldDAO> listIndexField = new ArrayList<MongoDBIndexFieldDAO>();
	boolean isUnique;
	String ns;
	String name;
	
	public MongoDBIndexDAO() {
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public List<MongoDBIndexFieldDAO> getListIndexField() {
		return listIndexField;
	}

	public void setListIndexField(List<MongoDBIndexFieldDAO> listIndexField) {
		this.listIndexField = listIndexField;
	}

	public boolean isUnique() {
		return isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
