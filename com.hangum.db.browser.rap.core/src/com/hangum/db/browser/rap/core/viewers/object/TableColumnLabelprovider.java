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
package com.hangum.db.browser.rap.core.viewers.object;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.db.dao.mysql.TableColumnDAO;

/**
 * TABLE, VIEW의 컬럼 정보
 * 
 * @author hangumNote
 *
 */
public class TableColumnLabelprovider extends LabelProvider implements ITableLabelProvider {
	
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TableColumnDAO tc = (TableColumnDAO) element;
		
			
			switch(columnIndex) {
			case 0: return tc.getField();
			case 1: return tc.getType();
			case 2: return tc.getKey();
			case 3: return tc.getComment();
			case 4: return tc.getNull();
			case 5: return tc.getDefault();
			case 6: return tc.getExtra();
			}
		return null;
	}

}
