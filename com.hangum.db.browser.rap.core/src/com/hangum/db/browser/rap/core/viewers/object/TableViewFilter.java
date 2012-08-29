package com.hangum.db.browser.rap.core.viewers.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.hangum.db.dao.mysql.TableDAO;

/**
 * TABLE, VIEW의 FILTER
 * 
 * @author hangumNote
 *
 */
public class TableViewFilter extends ViewerFilter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableViewFilter.class);

	private String searchString;
	
	public void setSearchText(String s) {
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		TableDAO dao = (TableDAO)element;
		if(dao.getName().toUpperCase().matches(searchString.toUpperCase())) return true;
		
		return false;
	}

}
