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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.trigger;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.dao.mysql.TriggerDAO;

/**
 * Trigger 의 컬럼 정보
 * 
 * @author hangumNote
 *
 */
public class TriggerLabelProvicer extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		TriggerDAO tc = (TriggerDAO) element;

		switch(columnIndex) {
		case 0: return tc.getTrigger();
		case 1: return tc.getEvent();
		case 2: return tc.getTable();
		case 3: return tc.getStatement();
		case 4: return tc.getTiming();
		case 5: return tc.getCreated();
		
		case 6: return tc.getSql_mode();
		case 7: return tc.getDefiner();
		case 8: return tc.getCharacter_set_client();
		case 9: return tc.getCollation_connection();
		case 10: return tc.getDatabase();
		case 11: return tc.getCollation();
		}
		
		return "** not set column **";
	}

}
