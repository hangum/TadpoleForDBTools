package com.hangum.db.browser.rap.core.editors.table;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.db.dao.mysql.TableColumnDAO;
import com.hangum.db.dao.system.UserDBDAO;

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
		return userDB.getDatabase() + "[" + tableName + "]";
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public List<TableColumnDAO> getShowTableColumns() {
		return showTableColumns;
	}
}
