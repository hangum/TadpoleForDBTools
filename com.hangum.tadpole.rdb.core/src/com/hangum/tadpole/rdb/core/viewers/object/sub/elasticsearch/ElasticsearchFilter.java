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
package com.hangum.tadpole.rdb.core.viewers.object.sub.elasticsearch;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.commons.util.TadpoleViewrFilter;
import com.hangum.tadpole.engine.query.dao.elasticsearch.ElastIndexDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Elasticsearch의 FILTER
 * 
 * @author hangum
 *
 */
public class ElasticsearchFilter extends TadpoleViewrFilter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ElasticsearchFilter.class);
	private UserDBDAO userDB;
	
	public ElasticsearchFilter(UserDBDAO userDB) {
		this.userDB = userDB;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		ElastIndexDAO dao = (ElastIndexDAO)element;
		if(dao.getIndex().toUpperCase().matches(searchString.toUpperCase())) return true;
		else if(StringUtils.upperCase(dao.getHealth()).matches(searchString.toUpperCase())) return true;
		else if(StringUtils.upperCase(dao.getStatus()).matches(searchString.toUpperCase())) return true;
		
		return false;
	}

}
