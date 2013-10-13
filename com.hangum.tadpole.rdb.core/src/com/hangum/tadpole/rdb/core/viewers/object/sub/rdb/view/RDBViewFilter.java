/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.view;

import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.commons.util.TadpoleViewrFilter;

/**
 * VIEW의 FILTER
 * 
 * @author hangum
 *
 */
public class RDBViewFilter extends TadpoleViewrFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		String strView = (String)element;
		if(strView.toUpperCase().matches(searchString.toUpperCase())) return true;
		
		return false;
	}

}
