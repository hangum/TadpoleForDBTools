/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.ext.editors.javascript;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * Server Side Java script editor input
 * 
 * @author hangum
 *
 */
public class ServerSideJavaScriptEditorInput implements IEditorInput {
	private UserDBDAO userDB;
	private MongoDBServerSideJavaScriptDAO javascriptDAO;
	
	public ServerSideJavaScriptEditorInput(UserDBDAO userDB, MongoDBServerSideJavaScriptDAO mjsDAO) {
		this.userDB = userDB;
		this.javascriptDAO = mjsDAO;
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
		if(javascriptDAO == null) return userDB.getDb() + " JavaScript";
		else return userDB.getDb() + "[" + javascriptDAO.getName() + "] " + " JavaScript";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		if(javascriptDAO == null) return userDB.getDb() + " JavaScript";
		else return userDB.getDb() + "[" + javascriptDAO.getName() + "] " + " JavaScript";
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public MongoDBServerSideJavaScriptDAO getJavascriptDAO() {
		return javascriptDAO;
	}

}
