/*******************************************************************************
 * Copyright (c) 2016 nilriri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.oracle;

import java.util.Map;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Tablespace manager detail information labelProvider
 * 
 * @author nilriri
 *
 */
public class TablespaceExtInfoLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Map<String, String> map = (Map<String, String>) element;

		switch (columnIndex) {
		case 0: return map.get("key");
		case 1: return map.get("value");
		} 

		return "*** not set column value ***"; //$NON-NLS-1$
	}

}