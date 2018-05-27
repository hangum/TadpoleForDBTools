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
package com.hangum.tadpole.mongodb.core.editors.group;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Group editor input
 * 
 * @author hangum
 *
 */
public class MongoDBGroupEditorInput implements IEditorInput {
	private UserDBDAO userDB;
	private String colname;
	
	public MongoDBGroupEditorInput(UserDBDAO userDB, String colName) {
		this.userDB = userDB;
		this.colname = colName;		
	}

	public MongoDBGroupEditorInput(UserDBDAO userDB) {
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
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		return "Group " + userDB.getDb() + "[" + colname + "]";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Group " + userDB.getDb() + "[" + colname + "]";
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public String getColname() {
		return colname;
	}

}
