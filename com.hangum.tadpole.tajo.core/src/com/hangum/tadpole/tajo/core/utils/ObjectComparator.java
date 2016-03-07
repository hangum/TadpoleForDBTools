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
package com.hangum.tadpole.tajo.core.utils;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import com.hangum.tadpole.commons.libs.core.utils.NullSafeComparator;

/**
 * sort를 위한 최상위 클래서(기본으로 table 사용)
 * 
 * @author hangum
 *
 */
public class ObjectComparator extends ViewerSorter  {
	protected int propertyIndex;
	protected static final int DESCENDING = 1;
	protected static final int ASCENDING = -1;
	protected int direction = DESCENDING;
	
	public ObjectComparator() {
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
		String tb1 = e1.toString();
		String tb2 = e2.toString();
		
		int rc = NullSafeComparator.compare(tb1, tb2);		
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
