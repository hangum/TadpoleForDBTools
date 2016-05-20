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
package com.hangum.tadpole.manager.core.editor.executedsql;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.USER_ROLE_TYPE;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.manager.core.Messages;

/**
 * Executed SQL Editor input
 * 
 * @author hangum
 *
 */
public class SQLAuditEditorInput implements IEditorInput {
	private UserDAO userDAO;
	private UserDBDAO userDBDAO;
	private USER_ROLE_TYPE roleType;

	public SQLAuditEditorInput() {
	}
	
	public SQLAuditEditorInput(UserDAO userDAO, USER_ROLE_TYPE roleType) {
		this(userDAO, null, roleType);
	}

	public SQLAuditEditorInput(UserDAO userDAO, UserDBDAO userDBDAO, USER_ROLE_TYPE roleType) {
		this.userDAO = userDAO;
		this.userDBDAO = userDBDAO;
		this.roleType = roleType;
	}
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof SQLAuditEditorInput) ) return false;
		
		return ((SQLAuditEditorInput)obj).getName().equals(getName());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		if(getUserDAO() != null) {
			return String.format("%s (%s)", Messages.get().SQLAudit, getUserDAO().getName());
		} else {
			return Messages.get().SQLAudit;
		}
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return Messages.get().SQLAudit;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public UserDBDAO getUserDBDAO() {
		return userDBDAO;
	}
	
	public USER_ROLE_TYPE getRoleType() {
		return roleType;
	}
}
