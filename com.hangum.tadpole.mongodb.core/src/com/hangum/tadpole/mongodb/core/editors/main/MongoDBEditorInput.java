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
package com.hangum.tadpole.mongodb.core.editors.main;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * mongodb기본 editor의 editorinput
 * 
 * @author hangum
 *
 */
public class MongoDBEditorInput implements IEditorInput {
	/** db info */
	private UserDBDAO userDB;

	/** collection name */
	private String collectionName = "";
	
	/** collection column info */
	private List showTableColumns;
	
	public MongoDBEditorInput(String collectionName, UserDBDAO userDB, List showTableColumns) {
		this.collectionName = collectionName;
		this.userDB = userDB;
		this.showTableColumns = showTableColumns;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return (this.collectionName != null);
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof MongoDBEditorInput) ) return false;
		return ((MongoDBEditorInput)obj).getName().equals(getName());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		return userDB.getDb() + " [" + collectionName + "]";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return userDB.getDb() + " [" + collectionName + "]";
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public List getShowTableColumns() {
		return showTableColumns;
	}
	
	public String getCollectionName() {
		return collectionName;
	}
}
