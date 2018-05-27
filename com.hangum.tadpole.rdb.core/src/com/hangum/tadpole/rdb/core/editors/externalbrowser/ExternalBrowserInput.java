/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.externalbrowser;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * External browser input
 * 
 * @author hangum
 *
 */
public class ExternalBrowserInput implements IEditorInput {
	private UserDBDAO userDB;
	private List<ExternalBrowserInfoDAO> listExternalBrowser;

	public ExternalBrowserInput(UserDBDAO userDB, List<ExternalBrowserInfoDAO> listExternalBrowser) {
		this.userDB = userDB;
		this.listExternalBrowser = listExternalBrowser;
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
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return userDB.getDisplay_name() + " External Browser";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return userDB.getDisplay_name() + " External Browser";
	}
	
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public List<ExternalBrowserInfoDAO> getListExternalBrowser() {
		return listExternalBrowser;
	}
}
