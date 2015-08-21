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
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;

/**
 * sort를 위한 최상위 클래서(기본으로 table 사용)
 * 
 * @author hangum
 * 
 */
public class TableComparator extends ObjectComparator {

	public TableComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		TableDAO tb1 = (TableDAO) e1;
		TableDAO tb2 = (TableDAO) e2;

		int rc = ASCENDING;
		switch (this.propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(tb1.getName(), tb2.getName());
			break;
		case 1:
			rc = NullSafeComparator.compare(tb1.getComment(), tb2.getComment());
			break;
		}
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
