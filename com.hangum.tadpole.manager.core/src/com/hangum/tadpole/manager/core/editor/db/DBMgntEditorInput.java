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
package com.hangum.tadpole.manager.core.editor.db;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.manager.core.Messages;

/**
 * user management input
 * 
 * @author hangum
 *
 */
public class DBMgntEditorInput implements IEditorInput {
	private PublicTadpoleDefine.USER_ROLE_TYPE roleType;
	private UserDAO userDAO;
	
	public DBMgntEditorInput(PublicTadpoleDefine.USER_ROLE_TYPE roleType) {
		this.roleType = roleType;
	}

	public DBMgntEditorInput(PublicTadpoleDefine.USER_ROLE_TYPE roleType, UserDAO userDAO) {
		this.roleType = roleType;
		this.userDAO = userDAO;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof DBMgntEditorInput) ) return false;
		return ((DBMgntEditorInput)obj).getName().equals(getName());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		if(getUserDAO() != null) {
			return String.format("%s (%s)", Messages.get().UserPermissionAction_2, getUserDAO().getName());
		} else {
			return Messages.get().UserPermissionAction_2;
		}
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return Messages.get().UserPermissionAction_2;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	public PublicTadpoleDefine.USER_ROLE_TYPE getRoleType() {
		return roleType;
	}
}
