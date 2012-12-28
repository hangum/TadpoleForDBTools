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
package com.hangum.tadpole.rdb.core.viewers.object.sub.index;

import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.util.TadpoleViewrFilter;

/**
 * Indexes의 FILTER
 * 
 * @author hangumNote
 *
 */
public class IndexesViewFilter extends TadpoleViewrFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		InformationSchemaDAO dao  = (InformationSchemaDAO)element;
		if(dao.getTABLE_NAME().toUpperCase().matches(searchString.toUpperCase())) return true;
		
		return false;
	}

}
