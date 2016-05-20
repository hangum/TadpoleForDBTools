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
package com.hangum.tadpole.manager.core.editor.transaction.connection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import com.hangum.tadpole.commons.libs.core.utils.NullSafeComparator;
import com.hangum.tadpole.engine.manager.transaction.TransactionDAO;

/**
 * Transaction table comparator
 * 
 * @author hangum
 *
 */
public class TransactioonTableComparator extends ViewerSorter  {
	protected int propertyIndex;
	protected static final int DESCENDING = 1;
	protected static final int ASCENDING = -1;
	protected int direction = DESCENDING;
	
	public TransactioonTableComparator() {
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
		TransactionDAO dao1 = (TransactionDAO) e1;
		TransactionDAO dao2 = (TransactionDAO) e2;
		
		int rc = ASCENDING;
		switch (this.propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(dao1.getUserDB().getDbms_type(), dao2.getUserDB().getDbms_type());
			break;
		case 1:
			rc = NullSafeComparator.compare(dao1.getUserDB().getDisplay_name(), dao2.getUserDB().getDisplay_name());
			break;
		case 2:
			rc = NullSafeComparator.compare(dao1.getUserId(), dao2.getUserId());
			break;
		case 3:
			rc = NullSafeComparator.compare(dao1.getStartTransaction().getTime(), dao2.getStartTransaction().getTime());
			break;
		}
		
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}