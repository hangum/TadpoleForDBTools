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
package com.hangum.tadpole.engine.sql.util.tables;

import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

/**
 * TableUtil에서 sorter의 columnindex를 지정하려고 만든 소터 
 * 
 * 
 * @author hangum
 *
 */
public class BasicViewerSorter extends ViewerSorter {
	/** 테이블을 처음으로 소트하고 싶지 않을 경우 상수로 사용하려고 
	 * 
	 *  해당 수는 테이블 컬럼의 인덱스 이므로 99999999가 넘어가면 문제가 됩니다.
	 *  컬럼수가 99999999가 넘어가는것은 설마.... - hangum, 10.09.13
	 */	
	public static final int FIRST_DISABLE_SORT = 99999999;
	
	protected int propertyIndex;
	protected static final int DESCENDING = 1;
	protected int direction = DESCENDING;

	public BasicViewerSorter() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			direction = 1 - direction;
		} else {
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}
	
	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}
	
}
