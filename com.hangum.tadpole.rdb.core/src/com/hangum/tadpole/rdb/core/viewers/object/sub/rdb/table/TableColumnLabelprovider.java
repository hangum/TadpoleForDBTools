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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.dao.mysql.TableColumnDAO;

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
