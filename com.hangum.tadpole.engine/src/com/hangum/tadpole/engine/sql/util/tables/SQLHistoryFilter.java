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
package com.hangum.tadpole.engine.sql.util.tables;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;

/**
* SQLHistory filter
* 
* @author hangum
*
*/
public class SQLHistoryFilter extends ViewerFilter {
	String searchString;
	
	public void setSearchString(String s) {
		this.searchString = ".*" + s.toLowerCase() + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) {
			return true;
		}
			
		RequestResultDAO sqlDAO = (RequestResultDAO)element;
		
		if(StringUtils.deleteWhitespace(sqlDAO.getSql_text()).toLowerCase().matches(searchString)) return true;
		if((""+sqlDAO.getStartDateExecute()).toLowerCase().matches(searchString)) return true;
		if((""+sqlDAO.getEndDateExecute()).toLowerCase().matches(searchString)) return true;
		if(sqlDAO.getMesssage().toLowerCase().matches(searchString)) return true;
		if(sqlDAO.getResult().toLowerCase().matches(searchString)) return true;
		if((""+sqlDAO.getRows()).toLowerCase().matches(searchString)) return true;
		if((""+sqlDAO.getUserName()).toLowerCase().matches(searchString)) return true;
		if((""+sqlDAO.getDbName()).toLowerCase().matches(searchString)) return true;
		if((""+sqlDAO.getIpAddress()).toLowerCase().matches(searchString)) return true;
		return false;
	}
	
}