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
package com.hangum.tadpole.browser.rap.core.viewers.object.comparator;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

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
		
		int rc = e1Val.compareTo(e2Val);
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

}
