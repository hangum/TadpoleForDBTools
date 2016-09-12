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
package com.hangum.tadpole.manager.core.editor.transaction.connection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import com.hangum.tadpole.commons.libs.core.utils.NullSafeComparator;
import com.hangum.tadpole.engine.manager.DBCPInfoDAO;

/**
 * General connection pool table comparator
 * 
 * @author hangum
 *
 */
public class GeneralConnectionTableComparator extends ViewerSorter  {
	protected int propertyIndex;
	protected static final int DESCENDING = 1;
	protected static final int ASCENDING = -1;
	protected int direction = DESCENDING;
	
	public GeneralConnectionTableComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}
	
	public void setColumn(int column) {
		if(column == this.propertyIndex) {
			direction = 1 - direction;
		} else {
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		DBCPInfoDAO dao1 = (DBCPInfoDAO) e1;
		DBCPInfoDAO dao2 = (DBCPInfoDAO) e2;
		
		int rc = ASCENDING;
		switch (this.propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(dao1.getUser(), dao2.getUser());
			break;
		case 1:
			rc = NullSafeComparator.compare(dao1.getDbType(), dao2.getDbType());
			break;
		case 2:
			rc = NullSafeComparator.compare(dao1.getDisplayName(), dao2.getDisplayName());
			break;
		case 3:
			rc = NullSafeComparator.compare(dao1.getNumberActive(), dao2.getNumberActive());
			break;
			
		case 4:
			rc = NullSafeComparator.compare(dao1.getMaxActive(), dao2.getMaxActive());
			break;
			
		case 5:
			rc = NullSafeComparator.compare(dao1.getNumberIdle(), dao2.getNumberIdle());
			break;
			
		case 6:
			rc = NullSafeComparator.compare(dao1.getMaxWait(), dao2.getMaxWait());
			break;
		}
		
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}