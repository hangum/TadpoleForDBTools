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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.trigger;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

/**
 * Trigger 의 컬럼 정보
 * 
 * @author hangum
 *
 */
public class TriggerLabelProvicer extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		TriggerDAO triggerDao = (TriggerDAO) element;

		switch(columnIndex) {
		case 0: 
			if (triggerDao.isValid()){
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/state/normalcy.png");
			}else{
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/state/warning.png");
			}
		}
		
		return null;
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		TriggerDAO tc = (TriggerDAO) element;

		switch(columnIndex) {
		case 0: return tc.getTrigger();
		case 1: return tc.getEvent();
		case 2: return tc.getTable_name();
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
