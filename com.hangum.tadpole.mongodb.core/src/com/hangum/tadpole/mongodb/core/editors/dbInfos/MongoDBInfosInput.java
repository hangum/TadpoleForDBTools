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
package com.hangum.tadpole.mongodb.core.editors.dbInfos;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * mongodb info기본 editor의 editorinput
 * 
 * @author hangum
 *
 */
public class MongoDBInfosInput implements IEditorInput {
	private MongoDBInfosEditor.PAGES defaultPage;
	
	/** db info */
	private UserDBDAO userDB;

	public MongoDBInfosInput(UserDBDAO userDB, MongoDBInfosEditor.PAGES defaultPage) {
		this.userDB = userDB;
		this.defaultPage = defaultPage;
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
		return this.userDB.getDisplay_name() + "@" + this.userDB.getDb();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return this.userDB.getDisplay_name() + "@" + this.userDB.getDb();
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public MongoDBInfosEditor.PAGES getDefaultPage() {
		return defaultPage;
	}
}
