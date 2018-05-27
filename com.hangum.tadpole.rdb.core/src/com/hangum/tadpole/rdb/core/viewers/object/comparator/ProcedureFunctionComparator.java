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
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;

/**
 * sort를 위한 최상위 클래서(기본으로 table 사용)
 * 
 * @author hangum
 * 
 */
public class ProcedureFunctionComparator extends ObjectComparator {

	public ProcedureFunctionComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		ProcedureFunctionDAO tb1 = (ProcedureFunctionDAO) e1;
		ProcedureFunctionDAO tb2 = (ProcedureFunctionDAO) e2;

		int rc = ASCENDING;
		switch (propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(tb1.getName(), tb2.getName());
			break;
		case 1:
			rc = NullSafeComparator.compare(tb1.getDefiner(), tb2.getDefiner());
			break;
		case 2:
			rc = NullSafeComparator.compare(tb1.getModified(), tb2.getModified());
			break;
		case 3:
			rc = NullSafeComparator.compare(tb1.getCreated(), tb2.getCreated());
			break;
		case 4:
			rc = NullSafeComparator.compare(tb1.getSecurity_type(), tb2.getSecurity_type());
			break;
		case 5:
			rc = NullSafeComparator.compare(tb1.getComment(), tb2.getComment());
			break;
		case 6:
			rc = NullSafeComparator.compare(tb1.getCharacter_set_client(), tb2.getCharacter_set_client());
			break;
		case 7:
			rc = NullSafeComparator.compare(tb1.getCollation_connection(), tb2.getCollation_connection());
			break;
		case 8:
			rc = NullSafeComparator.compare(tb1.getDatabase(), tb2.getDatabase());
			break;
		case 9:
			rc = NullSafeComparator.compare(tb1.getCollation(), tb2.getCollation());
			break;
		}

		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
