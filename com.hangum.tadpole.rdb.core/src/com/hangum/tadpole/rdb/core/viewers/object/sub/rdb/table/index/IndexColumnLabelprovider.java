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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.index;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;

/**
 * Index를 구성하는 컬럼 정보
 * 
 * @author nilriri
 *
 */
public class IndexColumnLabelprovider extends LabelProvider implements ITableLabelProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		InformationSchemaDAO tc = (InformationSchemaDAO) element;
			
			switch(columnIndex) {
			case 0: return tc.getSEQ_IN_INDEX();
			case 1: return tc.getCOLUMN_NAME();
			case 2: return tc.getCOMMENT();
			}
		return null;
	}

}
