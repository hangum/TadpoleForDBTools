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

import com.hangum.tadpole.commons.libs.core.utils.NullSafeComparator;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;

/**
 * sort를 위한 최상위 클래서(기본으로 table의 column 사용)
 * 
 * @author hangum
 *
 */
public  class TableColumnComparator extends ObjectComparator  {
	
	public TableColumnComparator() {
		this.propertyIndex = -1;
		direction = ASCENDING;
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		TableColumnDAO tc1 = (TableColumnDAO)e1;
		TableColumnDAO tc2 = (TableColumnDAO)e2;
		
		int rc = 0;
		switch(propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(tc1.getName(), tc2.getName());
			break;
		case 1:
			rc = NullSafeComparator.compare(tc1.getType(), tc2.getType());
			break;
		case 2:
			rc = NullSafeComparator.compare(tc1.getKey(), tc2.getKey());
			break;
		case 3:
			rc = NullSafeComparator.compare(tc1.getComment(), tc2.getComment());
			break;
		case 4:
			rc = NullSafeComparator.compare(tc1.getNull(), tc2.getNull());
			break;
		case 5:
			rc = NullSafeComparator.compare(tc1.getDefault(), tc2.getDefault());
			break;
		case 6:
			rc = NullSafeComparator.compare(tc1.getExtra(), tc2.getExtra());
		}
		
				
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
