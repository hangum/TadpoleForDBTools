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
package com.hangum.tadpole.rdb.core.editors.main.parameter;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * SQLResult의 LabelProvider
 * 
 * @author hangum
 *
 */
public class ParamLabelProvider extends LabelProvider implements ITableLabelProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ParamLabelProvider.class);

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getColumnText(Object element, int columnIndex) {
		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
		
		return rsResult.get(columnIndex).toString();
	}
	
}
