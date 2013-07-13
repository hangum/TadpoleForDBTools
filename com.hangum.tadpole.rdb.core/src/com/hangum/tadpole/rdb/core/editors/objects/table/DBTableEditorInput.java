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
package com.hangum.tadpole.rdb.core.editors.objects.table;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * TableEditor의 input
 * 
 * @author hangum
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
		return false;//(this.tableName != null);
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
