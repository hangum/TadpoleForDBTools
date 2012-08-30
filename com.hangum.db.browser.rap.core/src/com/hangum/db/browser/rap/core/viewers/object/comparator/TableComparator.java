package com.hangum.db.browser.rap.core.viewers.object.comparator;

import org.eclipse.jface.viewers.Viewer;

import com.hangum.db.dao.mysql.TableDAO;

/**
 * sort를 위한 최상위 클래서(기본으로 table 사용)
 * 
 * @author hangum
 *
 */
public  class TableComparator extends ObjectComparator  {
	
	public TableComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		TableDAO tb1 = (TableDAO)e1;
		TableDAO tb2 = (TableDAO)e2;
		
		int rc = tb1.getName().compareTo(tb2.getName());		
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
