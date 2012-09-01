/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.erd.core.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.dao.system.UserDBResourceDAO;

public class TadpoleEditorInput implements IEditorInput {
	private String name = null;
	private UserDBDAO userDB;
	private UserDBResourceDAO userDBERD;
	private boolean allTable = false;
	
	/**
	 * 신규로 작성
	 * 
	 * @param name
	 * @param userDB
	 * @param allTable 모든 테이블 유무
	 */
	public TadpoleEditorInput(String name, UserDBDAO userDB, boolean allTable) {
		this.name = name;
		this.userDB = userDB;
		this.allTable = allTable;
	}

	/**
	 * 기존 erd 불러오기
	 * @param name
	 * @param userDBErd
	 */
	public TadpoleEditorInput(String name, UserDBResourceDAO userDBErd) {
		this.name = userDBErd.getFilename();
		this.userDB = userDBErd.getParent();
		this.userDBERD = userDBErd;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return (this.name != null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof TadpoleEditorInput) ) return false;
		return ((TadpoleEditorInput)obj).getName().equals(getName());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		return allTable?"All "+ this.name:this.name;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return userDB.getDisplay_name();
	}
	
	public UserDBDAO getUserDBDAO() {
		return userDB;
	}
	
	public UserDBResourceDAO getUserDBERD() {
		return userDBERD;
	}
	
	public boolean isAllTable() {
		return allTable;
	}

}
