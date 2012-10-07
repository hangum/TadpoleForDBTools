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
package com.hangum.tadpole.browser.rap.core.editors.table;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * TableEditor의 input
 * @author hangumNote
 *
 */
public class DBTableEditorInput implements IEditorInput {
	String tableName = "";
	UserDBDAO userDB;
	List<TableColumnDAO> showTableColumns;
	
	public DBTableEditorInput(String tableName, UserDBDAO userDB, List showTableColumns) {
		this.tableName = tableName;
		this.userDB = userDB;
		this.showTableColumns = showTableColumns;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return (this.tableName != null);
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
		return tableName;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return userDB.getDb() + "[" + tableName + "]";
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public List<TableColumnDAO> getShowTableColumns() {
		return showTableColumns;
	}
}
