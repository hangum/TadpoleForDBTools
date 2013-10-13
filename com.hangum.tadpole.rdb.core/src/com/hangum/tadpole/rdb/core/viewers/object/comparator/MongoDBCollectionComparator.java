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

import com.hangum.tadpole.sql.dao.mysql.TableDAO;

/**
 * sort를 위한 최상위 클래서(기본으로 table 사용)
 * 
 * @author hangum
 *
 */
public class MongoDBCollectionComparator extends ObjectComparator  {
	
	public MongoDBCollectionComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		TableDAO tb1 = (TableDAO)e1;
		TableDAO tb2 = (TableDAO)e2;
		
		int rc = 0;
		switch(propertyIndex) {
		case 0:
			rc = tb1.getName().compareToIgnoreCase(tb2.getName());
			break;
		case 1:
			rc = tb1.getRows() > tb2.getRows()?1:-1;
			break;
		case 2:
			rc = tb1.getSize() > tb2.getSize()?1:-1;
			break;		
		}
				
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
