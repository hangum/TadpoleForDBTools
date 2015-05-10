package com.hangum.tadpole.manager.core.editor.auth.provider;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.engine.query.dao.system.ext.UserGroupAUserDAO;

/**
* content provider
* 
* @author hangum
*
*/
public class UserContentProvider implements ITreeContentProvider {
	private static final Logger logger = Logger.getLogger(UserContentProvider.class);
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List<UserGroupAUserDAO>)inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		if(element == null) {
			return null;
		}
		
		return ((UserGroupAUserDAO) element).parent;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof ArrayList) {
			return ((ArrayList)element).size() > 0;			
		} else if(element instanceof UserGroupAUserDAO) {
			return ((UserGroupAUserDAO)element).child.size() > 0;
		}
		
		return false;
	}
	
}