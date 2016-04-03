/*******************************************************************************
 * Copyright (c) 2016 hangum.
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
import com.hangum.tadpole.engine.query.dao.rdb.OracleSynonymColumnDAO;

/**
 * synonymcolumn comparator
 * 
 * @author hangum
 * 
 */
public class SynonymColumnComparator extends ObjectComparator {

	public SynonymColumnComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		OracleSynonymColumnDAO tb1 = (OracleSynonymColumnDAO) e1;
		OracleSynonymColumnDAO tb2 = (OracleSynonymColumnDAO) e2;

		int rc = ASCENDING;
		switch (this.propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(tb1.getColumn_id(), tb2.getColumn_id());
			break;
		case 1:
			rc = NullSafeComparator.compare(tb1.getColumn_name(), tb2.getColumn_name());
			break;
		case 2:
			rc = NullSafeComparator.compare(tb1.getData_type(), tb2.getData_type());
			break;
		case 3:
			rc = NullSafeComparator.compare(tb1.getNullable(), tb2.getNullable());
			break;
		case 4:
			rc = NullSafeComparator.compare(tb1.getKey(), tb2.getKey());
			break;
		case 5:
			rc = NullSafeComparator.compare(tb1.getComments(), tb2.getComments());
			break;
		}
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
