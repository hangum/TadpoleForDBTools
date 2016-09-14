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
package com.hangum.tadpole.commons.admin.core.editors.useranddb;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import com.hangum.tadpole.commons.libs.core.utils.NullSafeComparator;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;

/**
 * {@code UserDAO} comparator
 * 
 * @author hangum
 *
 */
public class UserDAOComparator extends ViewerSorter {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public UserDAOComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			direction = 1 - direction;
		} else {
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int rc = 0;
		
		if(e1 instanceof UserDAO) {
			UserDAO m1 = (UserDAO)e1;
			UserDAO m2 = (UserDAO)e2;
			
			switch (propertyIndex) {
			case 0:
				rc = NullSafeComparator.compare(m1.getEmail(), m2.getEmail());
				break;
			case 1:
				rc = NullSafeComparator.compare(m1.getName(), m2.getName());
				break;
			case 2:
				rc = NullSafeComparator.compare(m1.getAllow_ip(), m2.getAllow_ip());
				break;
			case 3:
				rc = NullSafeComparator.compare(m1.getIs_regist_db(), m2.getIs_regist_db());
				break;
			case 4:
				rc = NullSafeComparator.compare(m1.getIs_shared_db(), m2.getIs_shared_db());
				break;
			case 5:
				rc = NullSafeComparator.compare(m1.getLimit_add_db_cnt(), m2.getLimit_add_db_cnt());
				break;
			case 6:
				rc = NullSafeComparator.compare(m1.getService_end().getTime(), m2.getService_end().getTime());
				break;
			case 7:
				rc = NullSafeComparator.compare(m1.getEmail_key(), m2.getEmail_key());
				break;
			case 8:
				rc = NullSafeComparator.compare(m1.getApproval_yn(), m2.getApproval_yn());
				break;
			case 9:
				rc = NullSafeComparator.compare(m1.getIs_email_certification(), m2.getIs_email_certification());
				break;
			case 10:
				rc = NullSafeComparator.compare(m1.getDelYn(), m2.getDelYn());
				break;
			case 11:
				rc = NullSafeComparator.compare(m1.getCreate_time(), m2.getCreate_time());
				break;
			default:
				break;
			}
			
			if(direction == DESCENDING) rc = -rc;
		}
		
		return rc;
	}
}
