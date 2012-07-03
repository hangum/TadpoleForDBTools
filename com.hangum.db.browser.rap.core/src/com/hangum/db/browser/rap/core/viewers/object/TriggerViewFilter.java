package com.hangum.db.browser.rap.core.viewers.object;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.hangum.db.dao.mysql.TriggerDAO;

/**
 * Trigger의 FILTER
 * 
 * @author hangumNote
 *
 */
public class TriggerViewFilter extends ViewerFilter {
	private String searchString;
	
	public void setSearchText(String s) {
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		TriggerDAO dao  = (TriggerDAO)element;
		if(dao.getTrigger().matches(searchString)) return true;
		
		return false;
	}

}
