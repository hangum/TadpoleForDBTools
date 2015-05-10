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
package com.hangum.tadpole.manager.core.editor.transaction.connection;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.manager.transaction.TransactionDAO;

/**
 * Transaction Connection Manager Labelprovider
 * 
 * @author hangum
 *
 */
public class TransactionConnectionListLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TransactionDAO dto = (TransactionDAO)element;

		switch(columnIndex) {
		case 0: return dto.getUserDB().getDbms_type();
		case 1: return dto.getUserDB().getDisplay_name();
		case 2: return dto.getUserId();
		case 3: return dto.getStartTransaction().toLocaleString();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}