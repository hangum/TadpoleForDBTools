/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.dml;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;

/**
 * generate statement dml
 * 
 * @author hangum
 *
 */
public class IndexInformationLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		InformationSchemaDAO dao = (InformationSchemaDAO) element;

		switch (columnIndex) {
		case 0: return dao.getCOLUMN_NAME();
		case 1: return dao.getCOMMENT();
		case 2: return dao.getSEQ_IN_INDEX();
		case 3: return dao.getCHAR_LENGTH()+"";
		case 4: return dao.getTABLE_NAME();
		case 5: return dao.getTABLE_SCHEMA();
		} 

		return "*** not set column value ***"; //$NON-NLS-1$
	}

}