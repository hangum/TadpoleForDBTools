package com.hangum.tadpole.util;

import org.eclipse.jface.viewers.ViewerFilter;

/**
 * tadpole viewer filter
 * 
 * @author hangum
 *
 */
public abstract class TadpoleViewrFilter extends ViewerFilter {

	protected String searchString;
	
	public void setSearchText(String s) {
		this.searchString = ".*" + s + ".*";
	}

}
