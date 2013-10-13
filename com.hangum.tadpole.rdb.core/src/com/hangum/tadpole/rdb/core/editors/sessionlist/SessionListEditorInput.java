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
package com.hangum.tadpole.rdb.core.editors.sessionlist;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.rdb.core.editors.objects.table.DBTableEditorInput;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * Session list editor input
 * 
 * @author hangum
 *
 */
public class SessionListEditorInput implements IEditorInput {
	private UserDBDAO userDB;

	public SessionListEditorInput(UserDBDAO userDB) {
		this.userDB = userDB;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return (this.userDB != null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof DBTableEditorInput) ) return false;
		return ((DBTableEditorInput)obj).getName().equals(getName());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		return "Session List" + " [" + userDB.getDb() +  "]";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Session List" + " [" + userDB.getDb() +  "]";
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
}
