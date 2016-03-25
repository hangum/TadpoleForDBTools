package com.hangum.tadpole.commons.admin.core.editors.useranddb.provider;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.hangum.tadpole.engine.query.dao.system.UserDAO;

/**
* User composite filter
* 
* @author hangum
*
*/
public class UserCompFilter extends ViewerFilter {
	String searchString;
	
	public void setSearchString(String s) {
		this.searchString = ".*" + s.toLowerCase() + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) {
			return true;
		}
			
		UserDAO user = (UserDAO)element;
		if(user.getEmail().toLowerCase().matches(searchString)) return true;
		if(user.getName().toLowerCase().matches(searchString)) return true;
		if(user.getEmail_key().toLowerCase().matches(searchString)) return true;
		
		return false;
	}
	
}