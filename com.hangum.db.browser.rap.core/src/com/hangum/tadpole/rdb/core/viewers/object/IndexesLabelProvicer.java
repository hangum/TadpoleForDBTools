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
package com.hangum.tadpole.rdb.core.viewers.object;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.dao.mysql.InformationSchemaDAO;

/**
 * index의 컬럼 정보
 * 
 * @author hangumNote
 *
 */
public class IndexesLabelProvicer extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		InformationSchemaDAO tc = (InformationSchemaDAO) element;
		
		switch(columnIndex) {
		case 0: return tc.getTABLE_NAME();
		case 1: return tc.getINDEX_NAME();
		case 2: return tc.getNON_UNIQUE();
		case 3: return tc.getINDEX_SCHEMA();
		case 4: return tc.getSEQ_IN_INDEX();
		case 5: return tc.getCOLUMN_NAME();
		
		case 6: return tc.getCOLLATION();
		case 7: return tc.getCARDINALITY();
		case 8: return tc.getSUB_PART();
		case 9: return tc.getPACKED();
		case 10: return tc.getNULLABLE();
		case 11: return tc.getINDEX_TYPE();
		
		case 12: return tc.getCOMMENT();
		}
		
		return "** not set column **";
	}

}
