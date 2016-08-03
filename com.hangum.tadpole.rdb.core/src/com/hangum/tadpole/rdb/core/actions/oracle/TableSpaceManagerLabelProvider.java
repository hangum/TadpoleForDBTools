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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.swtdesigner.ResourceManager;

/**
 * Tablespace manager label provider
 * 
 * @author nilriri
 *
 */
public class TableSpaceManagerLabelProvider extends DefaultLabelProvider {

	public TableSpaceManagerLabelProvider(TableViewer tv) {
		super(tv);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) {
			return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/tablespace.png");
		} else {
			return null;
		}
	}

}
