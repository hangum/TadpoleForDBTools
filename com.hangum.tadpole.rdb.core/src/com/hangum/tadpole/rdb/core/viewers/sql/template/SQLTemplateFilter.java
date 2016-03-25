/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.sql.template;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.commons.util.TadpoleViewrFilter;
import com.hangum.tadpole.engine.query.dao.system.SQLTemplateDAO;

/**
 * SQLTemplate의 FILTER
 * 
 * @author hangum
 *
 */
public class SQLTemplateFilter extends TadpoleViewrFilter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLTemplateFilter.class);

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		if(element instanceof SQLTemplateDAO) {
			SQLTemplateDAO dao = (SQLTemplateDAO)element;
			if(dao.getName().toUpperCase().matches(searchString.toUpperCase())) return true;
			else if(dao.getDescription().toUpperCase().matches(searchString.toUpperCase())) return true;
			else if(dao.getContent().toUpperCase().matches(searchString.toUpperCase())) return true;
			else return false;
				
		}
		
		return true;
	}

}
