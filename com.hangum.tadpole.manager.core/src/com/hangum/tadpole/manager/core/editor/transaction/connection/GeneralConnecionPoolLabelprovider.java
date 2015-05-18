/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
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

import com.hangum.tadpole.engine.manager.DBCPInfoDAO;
import com.hangum.tadpole.manager.core.Messages;

/**
 * general connection pool lnfo
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 18.
 *
 */
public class GeneralConnecionPoolLabelprovider extends LabelProvider implements ITableLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		DBCPInfoDAO dao = (DBCPInfoDAO)element;
		String[] names = {Messages.TransactionConnectionListEditor_13, Messages.TransactionConnectionListEditor_14,
				"Active Connections", "Max Active Count", "Idle Count", "Max wait"};
		
		switch(columnIndex) {
		case 0: return dao.getDbType();
		case 1: return dao.getDisplayName();
		case 2: return ""+dao.getNumberActive();
		case 3: return ""+dao.getMaxActive();
		case 4: return ""+dao.getNumberIdle();
		case 5: return ""+dao.getMaxWait();
		}
		
		return "** not set column **";
	}

}
