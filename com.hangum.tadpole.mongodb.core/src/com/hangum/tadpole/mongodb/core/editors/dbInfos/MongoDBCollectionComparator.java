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
package com.hangum.tadpole.mongodb.core.editors.dbInfos;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import com.hangum.tadpole.commons.libs.core.utils.NullSafeComparator;
import com.hangum.tadpole.mongodb.core.dto.MongoDBCollectionInfoDTO;

/**
 * mongodb collection comparator
 * 
 * @author hangum
 *
 */
public class MongoDBCollectionComparator extends ViewerSorter {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public MongoDBCollectionComparator() {
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
		
		if(e1 instanceof MongoDBCollectionInfoDTO) {
			MongoDBCollectionInfoDTO m1 = (MongoDBCollectionInfoDTO)e1;
			MongoDBCollectionInfoDTO m2 = (MongoDBCollectionInfoDTO)e2;
			
			Double dbl1, dbl2;
			
			switch (propertyIndex) {
			case 0:
				rc = NullSafeComparator.compare(m1.getName(), m2.getName());
				break;
			case 1:
				
				dbl1 = Double.valueOf(""+m1.getCount());
				dbl2 = Double.valueOf(""+m2.getCount());
				
				rc = dbl1.compareTo(dbl2);
				break;
	
			case 2:
				dbl1 = Double.valueOf(""+m1.getSize());
				dbl2 = Double.valueOf(""+m2.getSize());
				
				rc = dbl1.compareTo(dbl2);
				break;
			case 3:
				dbl1 = Double.valueOf(""+m1.getStorage());
				dbl2 = Double.valueOf(""+m2.getStorage());
				
				rc = dbl1.compareTo(dbl2);
				break;
			case 4:
				dbl1 = Double.valueOf(""+m1.getIndex());
				dbl2 = Double.valueOf(""+m2.getIndex());
				
				rc = dbl1.compareTo(dbl2);
				break;
			case 5:
				dbl1 = Double.valueOf(""+m1.getAvgObj());
				dbl2 = Double.valueOf(""+m2.getAvgObj());
				
				rc = dbl1.compareTo(dbl2);
				break;
			case 6:
				dbl1 = Double.valueOf(""+m1.getPadding());
				dbl2 = Double.valueOf(""+m2.getPadding());
				
				rc = dbl1.compareTo(dbl2);
				break;
			default:
				break;
			}
			
			if(direction == DESCENDING) rc = -rc;
		}
		
		return rc;
	}
}
