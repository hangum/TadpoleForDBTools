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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.orapackage;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;

/**
 * Index를 구성하는 컬럼 정보
 * 
 * @author nilriri
 *
 */
public class PackageProcFuncLabelprovider extends LabelProvider implements ITableLabelProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ProcedureFunctionDAO tc = (ProcedureFunctionDAO) element;
			
			switch(columnIndex) {
			case 0: return tc.getType();
			case 1: return tc.getName();
			case 2: return tc.getOverload() +"";
			}
		return null;
	}

}
