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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.procedure;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

/**
 * procedure function 의 컬럼 정보
 * 
 * @author hangum
 *
 */
public class ProcedureFunctionLabelProvicer extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		ProcedureFunctionDAO procDao = (ProcedureFunctionDAO) element;

		switch(columnIndex) {
		case 0: 
			if (procDao.isValid()){
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/state/normalcy.png");
			}else{
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/state/warning.png");
			}
		}
		
		return null;
	}
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		ProcedureFunctionDAO tc = (ProcedureFunctionDAO) element;

		switch(columnIndex) {
		case 0: return SQLUtil.getProcedureName(tc);
		case 1: return tc.getDefiner();
		case 2: return tc.getModified();
		case 3: return tc.getCreated();
		case 4: return tc.getSecurity_type();
		case 5: return tc.getComment();
		case 6: return tc.getCharacter_set_client();
		case 7: return tc.getCollation_connection();
		case 8: return tc.getDatabase();
		case 9: return tc.getCollation();
		}
		
		return "** not set column **";
	}

}
