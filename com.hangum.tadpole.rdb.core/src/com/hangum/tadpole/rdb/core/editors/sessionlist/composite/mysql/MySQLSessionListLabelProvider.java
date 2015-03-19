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
package com.hangum.tadpole.rdb.core.editors.sessionlist.composite.mysql;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.mysql.SessionListDAO;

/**
 * mysql, maridb session list label provider
 * 
 * @author hangum
 *
 */
public class MySQLSessionListLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		SessionListDAO sl = (SessionListDAO)element;
		
		switch(columnIndex) {
		case 0: return sl.getId();
		case 1: return sl.getUser();
		case 2: return sl.getHost();
		case 3: return sl.getDb();
		case 4: return sl.getCommand();
		case 5: return sl.getTime();
		case 6: return sl.getState();
		case 7: return sl.getInfo();
		}
		
		return "*** not set column ***";
	}
	
}
