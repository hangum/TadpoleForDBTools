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
package com.hangum.tadpole.rdb.core.viewers.object.comparator;

import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.sql.dao.mysql.TableColumnDAO;

/**
 * sort를 위한 최상위 클래서(기본으로 table의 column 사용)
 * 
 * @author hangum
 *
 */
public  class TableColumnComparator extends ObjectComparator  {
	
	public TableColumnComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		TableColumnDAO tc1 = (TableColumnDAO)e1;
		TableColumnDAO tc2 = (TableColumnDAO)e2;
		
		int rc = 0;
		switch(propertyIndex) {
		case 0:
			rc = tc1.getName().compareToIgnoreCase(tc2.getName());
			break;
		case 1:
			rc = tc1.getType().compareToIgnoreCase(tc2.getType());
			break;
		case 2:
			rc = tc1.getKey().compareToIgnoreCase(tc2.getKey());
			break;
		case 3:
			rc = tc1.getComment().compareToIgnoreCase(tc2.getComment());
			break;
		case 4:
			rc = tc1.getNull().compareToIgnoreCase(tc2.getNull());
			break;
		case 5:
			rc = tc1.getDefault().compareToIgnoreCase(tc2.getDefault());
			break;
		case 6:
			rc = tc1.getExtra().compareToIgnoreCase(tc2.getExtra());
		}
		
				
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
