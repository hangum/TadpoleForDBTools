package com.hangum.tadpole.mongodb.core.editors.dbInfos;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.db.dao.system.UserDBDAO;

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
		return this.userDB.getDisplay_name();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return userDB.getDatabase();
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
}
