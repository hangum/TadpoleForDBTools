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
package com.hangum.tadpole.importexport.core.editors.mongodb.composite;

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
