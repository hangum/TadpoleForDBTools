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
package com.hangum.tadpole.util.tables;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * sql select의 sorter
 * 
 * @author hangum
 *
 */
public class SQLResultSorter extends ViewerSorter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLResultSorter.class);

	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public SQLResultSorter() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}
	
	public SQLResultSorter(int firstPropetyIndex) {
		super();
		propertyIndex = firstPropetyIndex;
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
		HashMap<Integer, String> p1 = (HashMap<Integer, String>)e1;
		HashMap<Integer, String> p2 = (HashMap<Integer, String>)e2;
		
		String search1 = ""+p1.get(propertyIndex);
		String search2 = ""+p2.get(propertyIndex);
		
		int rc=0;
		try {
			Long long1 = Long.parseLong(search1);
			Long long2 = Long.parseLong(search2);
			
			rc = long1.compareTo(long2);
		} catch(NumberFormatException nfe) {
			rc = search1.toLowerCase().compareTo(search2.toLowerCase());
		}
		

		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
