/*******************************************************************************
 * Copyright (c) 2012 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.importdb.core.dialog.importdb.dao;

/**
 * table 수정 뷰에서 사용할 dao
 * 
 * @author hangum
 *
 */
public class ModTableDAO {
	boolean isModify = false;
	String name;
	boolean isExistOnDelete = false;
	String reName = "";
	
	public ModTableDAO() {
	}
	
	public ModTableDAO(String name) {
		this.name = name;
	}

	public boolean isModify() {
		return isModify;
	}

	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isExistOnDelete() {
		return isExistOnDelete;
	}

	public void setExistOnDelete(boolean isExistOnDelete) {
		this.isExistOnDelete = isExistOnDelete;
	}

	public String getReName() {
		return reName;
	}

	public void setReName(String reName) {
		this.reName = reName;
	}
	
	
}
