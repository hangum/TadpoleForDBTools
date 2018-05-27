/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.help.core.views.sub.shortcutkey;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * shortcut dialog labelprovider
 * 
 * @author hangum
 *
 */
public class ShortcutLabelProvider extends LabelProvider implements ITableLabelProvider {
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ShortcutHelpDAO dao = (ShortcutHelpDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getName();
		case 1: return dao.getKey();
		}
		
		return "*** not set column ***";
	}
	
}
