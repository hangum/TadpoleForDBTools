/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * VIEW의 FILTER
 * 
 * @author hangumNote
 *
 */
public class RDBViewFilter extends ViewerFilter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RDBViewFilter.class);

	private String searchString;
	
	public void setSearchText(String s) {
		this.searchString = ".*" + s + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		String strView = (String)element;
		if(strView.toUpperCase().matches(searchString.toUpperCase())) return true;
		
		return false;
	}

}
