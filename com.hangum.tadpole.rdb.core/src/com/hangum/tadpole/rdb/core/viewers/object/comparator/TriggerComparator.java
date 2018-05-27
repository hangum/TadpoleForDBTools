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
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;

/**
 * sort를 위한 최상위 클래서(기본으로 table 사용)
 * 
 * @author hangum
 * 
 */
public class TriggerComparator extends ObjectComparator {

	public TriggerComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		TriggerDAO tb1 = (TriggerDAO) e1;
		TriggerDAO tb2 = (TriggerDAO) e2;

		int rc = ASCENDING;
		switch (this.propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(tb1.getName(), tb2.getName());
			break;
		case 1:
			rc = NullSafeComparator.compare(tb1.getEvent(), tb2.getEvent());
			break;
		case 2:
			rc = NullSafeComparator.compare(tb1.getTable_name(), tb2.getTable_name());
			break;
		case 3:
			rc = NullSafeComparator.compare(tb1.getStatement(), tb2.getStatement());
			break;
		case 4:
			rc = NullSafeComparator.compare(tb1.getTiming(), tb2.getTiming());
			break;
		case 5:
			rc = NullSafeComparator.compare(tb1.getCreated(), tb2.getCreated());
			break;
		}
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
