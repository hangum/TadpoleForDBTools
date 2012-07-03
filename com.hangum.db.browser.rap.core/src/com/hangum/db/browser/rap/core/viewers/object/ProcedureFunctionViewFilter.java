package com.hangum.db.browser.rap.core.viewers.object;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.hangum.db.dao.mysql.ProcedureFunctionDAO;

/**
 * Procedure Function 의 FILTER
 * 
 * @author hangumNote
 *
 */
public class ProcedureFunctionViewFilter extends ViewerFilter {
	private String searchString;
	
	public void setSearchText(String s) {
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		ProcedureFunctionDAO dao  = (ProcedureFunctionDAO)element;
		if(dao.getName().toUpperCase().matches(searchString.toUpperCase())) return true;
		
		return false;
	}

}
