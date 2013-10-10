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

import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;

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
			rc = tb1.getName().compareTo(tb2.getName());
			break;
		case 1:
			rc = tb1.getDefiner().compareTo(tb2.getDefiner());
			break;
		case 2:
			rc = tb1.getModified().compareTo(tb2.getModified());
			break;
		case 3:
			rc = tb1.getCreated().compareTo(tb2.getCreated());
			break;
		case 4:
			rc = tb1.getSecurity_type().compareTo(tb2.getSecurity_type());
			break;
		case 5:
			rc = tb1.getComment().compareTo(tb2.getComment());
			break;
		case 6:
			rc = tb1.getCharacter_set_client().compareTo(tb2.getCharacter_set_client());
			break;
		case 7:
			rc = tb1.getCollation_connection().compareTo(tb2.getCollation_connection());
			break;
		case 8:
			rc = tb1.getDatabase().compareTo(tb2.getDatabase());
			break;
		case 9:
			rc = tb1.getCollation().compareTo(tb2.getCollation());
			break;
		}

		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
