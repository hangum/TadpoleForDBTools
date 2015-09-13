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

import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.manager.core.Messages;

/**
 * Executed SQL Editor input
 * 
 * @author hangum
 *
 */
public class ExecutedSQLEditorInput implements IEditorInput {
	private UserDAO userDAO;
	private UserDBDAO userDBDAO;

	public ExecutedSQLEditorInput(UserDAO selectUserDAO) {
		this.userDAO = selectUserDAO;
	}
	
	public ExecutedSQLEditorInput() {
	}

	public ExecutedSQLEditorInput(UserDBDAO userDBDAO) {
		this.userDBDAO = userDBDAO;
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
		if( !(obj instanceof ExecutedSQLEditorInput) ) return false;
		return ((ExecutedSQLEditorInput)obj).getName().equals(getName());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		return Messages.ExecutedSQLEditorInput_0;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return Messages.ExecutedSQLEditorInput_1;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public UserDBDAO getUserDBDAO() {
		return userDBDAO;
	}
}
