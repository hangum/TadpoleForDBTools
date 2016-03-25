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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.commons.util.TadpoleViewrFilter;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;

/**
 * TABLE의 FILTER
 * 
 * @author hangum
 *
 */
public class TableFilter extends TadpoleViewrFilter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableFilter.class);
	private UserDBDAO userDB;
	
	public TableFilter(UserDBDAO userDB) {
		this.userDB = userDB;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		TableDAO dao = (TableDAO)element;
		if(SQLUtil.getTableName(userDB, dao).toUpperCase().matches(searchString.toUpperCase())) return true;
		
		return false;
	}

}
