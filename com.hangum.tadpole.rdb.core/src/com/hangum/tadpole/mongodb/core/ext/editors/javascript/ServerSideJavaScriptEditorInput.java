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
package com.hangum.tadpole.mongodb.core.ext.editors.javascript;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.engine.query.dao.mongodb.MongoDBServerSideJavaScriptDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

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

	public ServerSideJavaScriptEditorInput(UserDBDAO userDB) {
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
		if(javascriptDAO == null) return " JavaScript" + userDB.getDb();
		else return "JavaScript " + userDB.getDb() + "[" + javascriptDAO.getName() + "]";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		if(javascriptDAO == null) return " JavaScript" + userDB.getDb();
		else return "JavaScript " + userDB.getDb() + "[" + javascriptDAO.getName() + "]";
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public MongoDBServerSideJavaScriptDAO getJavascriptDAO() {
		return javascriptDAO;
	}

}
