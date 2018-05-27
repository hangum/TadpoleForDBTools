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

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.hangum.tadpole.commons.libs.core.utils.NullSafeComparator;

/**
 * default comparator
 * @author hangum
 *
 */
public class DefaultComparator extends ObjectComparator {
	
	public DefaultComparator() {
		super();
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		
		TableViewer tableViewer = (TableViewer)viewer;
		ITableLabelProvider tlprov = (ITableLabelProvider)tableViewer.getLabelProvider();
		
		String e1Val = tlprov.getColumnText(e1, propertyIndex) == null?"":tlprov.getColumnText(e1, propertyIndex);
		String e2Val = tlprov.getColumnText(e2, propertyIndex) == null?"":tlprov.getColumnText(e2, propertyIndex);
		
		int rc = NullSafeComparator.compare(e1Val, e2Val);
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

}
