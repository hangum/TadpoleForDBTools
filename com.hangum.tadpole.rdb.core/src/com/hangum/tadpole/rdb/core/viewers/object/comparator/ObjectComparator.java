/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.comparator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

/**
 * sort를 위한 최상위 클래서(기본으로 table 사용)
 * 
 * @author hangum
 *
 */
public  class ObjectComparator extends ViewerSorter  {
	protected int propertyIndex;
	protected static final int DESCENDING = 1;
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
		
		int rc = tb1.compareTo(tb2);		
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
