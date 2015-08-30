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
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;

/**
 * sort를 위한 최상위 클래서(기본으로 table의 column 사용)
 * 
 * @author nilriri
 *
 */
public  class IndexColumnComparator extends ObjectComparator  {
	
	public IndexColumnComparator() {
		this.propertyIndex = 0;
		direction = ASCENDING;
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		InformationSchemaDAO tc1 = (InformationSchemaDAO)e1;
		InformationSchemaDAO tc2 = (InformationSchemaDAO)e2;
		
		int rc = 0;
		switch(propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(tc1.getSEQ_IN_INDEX(), tc2.getSEQ_IN_INDEX());
			break;
		case 1:
			rc = NullSafeComparator.compare(tc1.getCOLUMN_NAME(), tc2.getCOLUMN_NAME());
			break;
		case 2:
			rc = NullSafeComparator.compare(tc1.getCOMMENT(), tc2.getCOMMENT());
			break;		
		}
		
				
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
