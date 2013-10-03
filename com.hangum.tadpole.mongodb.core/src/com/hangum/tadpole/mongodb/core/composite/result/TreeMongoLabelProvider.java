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
package com.hangum.tadpole.mongodb.core.composite.result;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.mongodb.core.dto.MongodbTreeViewDTO;

/**
 * tree label provider
 * 
 * @author hangum
 *
 */
public class TreeMongoLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		MongodbTreeViewDTO mongoTree = (MongodbTreeViewDTO)element;

		switch(columnIndex) {
		case 0: return mongoTree.getKey();
		case 1: return NumberFormatUtils.commaFormat(mongoTree.getValue());
		case 2: return mongoTree.getType();
		}
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}
