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

import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.commons.libs.core.utils.NullSafeComparator;
import com.hangum.tadpole.engine.query.dao.mysql.SessionListDAO;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;

/**
 * SessionList tableview compare
 * 
 * @author hangum
 *
 */
public class MySQLSessionListTableCompare extends ObjectComparator  {
	
	public MySQLSessionListTableCompare() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		SessionListDAO sl1 = (SessionListDAO)e1;
		SessionListDAO sl2 = (SessionListDAO)e2;
		
		int rc = 0;
		switch(propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(sl1.getId(), sl2.getId());
			break;
		case 1:
			rc = NullSafeComparator.compare(sl1.getUser(), sl2.getUser());
			break;
		case 2:
			rc = NullSafeComparator.compare(sl1.getHost(), sl2.getHost());
			break;
		case 3:
			rc = NullSafeComparator.compare(sl1.getDb(), sl2.getDb());
			break;
		case 4:
			rc = NullSafeComparator.compare(sl1.getCommand(), sl2.getCommand());
			break;
		case 5:
			rc = NullSafeComparator.compare(sl1.getTime(), sl2.getTime());
			break;
		case 6:
			rc = NullSafeComparator.compare(sl1.getState(), sl2.getState());
			break;
		case 7:
			rc = NullSafeComparator.compare(sl1.getInfo(), sl2.getInfo());
			break;
		}
				
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
