package com.hangum.tadpole.manager.core.editor.auth.provider;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

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
			
//		UserGroupAUserDAO user = (UserGroupAUserDAO)element;
//		if(user.getUser_group_name().toLowerCase().matches(searchString)) return true;
//		if(user.getEmail().toLowerCase().matches(searchString)) return true;
//		if(user.getName().toLowerCase().matches(searchString)) return true;
//		if(user.getRole_type().toLowerCase().matches(searchString)) return true;
//		if(user.getApproval_yn().toLowerCase().matches(searchString)) return true;
		
		return false;
	}
	
}