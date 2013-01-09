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
package com.hangum.tadpole.mongodb.core.editors.dbInfos;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * mongodb info기본 editor의 editorinput
 * 
 * @author hangum
 *
 */
public class MongoDBInfosInput implements IEditorInput {
	/** db info */
	private UserDBDAO userDB;

	public MongoDBInfosInput(UserDBDAO userDB) {
		this.userDB = userDB;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return (this.userDB.getDisplay_name() != null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof MongoDBInfosInput) ) return false;
		return ((MongoDBInfosInput)obj).getName().equals(getName());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		return "All Collections [" + this.userDB.getDisplay_name() + "@" + this.userDB.getDb() + "]";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "All Collections [" + this.userDB.getDisplay_name() + "@" + this.userDB.getDb() + "]";
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
}
