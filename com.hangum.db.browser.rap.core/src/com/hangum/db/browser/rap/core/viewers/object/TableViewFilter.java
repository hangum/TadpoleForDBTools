package com.hangum.db.browser.rap.core.viewers.object;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * TABLE, VIEW의 FILTER
 * 
 * @author hangumNote
 *
 */
public class TableViewFilter extends ViewerFilter {
	private String searchString;
	
	public void setSearchText(String s) {
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		String str  = element.toString();
		if(str.toUpperCase().matches(searchString.toUpperCase())) return true;
		
		return false;
	}

}
