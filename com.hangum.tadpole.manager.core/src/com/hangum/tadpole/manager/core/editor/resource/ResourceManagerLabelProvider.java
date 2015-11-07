/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.editor.resource;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;

/**
 * Resource manager label provider
 * 
 * @author hangum
 *
 */
public class ResourceManagerLabelProvider extends DefaultLabelProvider {

	public ResourceManagerLabelProvider(TableViewer tv) {
		super(tv);
	}
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		return null;
	}

}
