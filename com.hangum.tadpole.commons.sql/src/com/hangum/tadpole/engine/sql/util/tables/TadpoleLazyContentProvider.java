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
package com.hangum.tadpole.engine.sql.util.tables;

import java.util.List;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Tadpole Lazy content provider
 * 
 * @author hangum
 *
 */
public class TadpoleLazyContentProvider implements ILazyContentProvider {
	private TableViewer tv;
	private List<?> elements;

	public TadpoleLazyContentProvider(TableViewer tv) {
		this.tv = tv;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.elements = (List<?>)newInput;
	}

	@Override
	public void updateElement(int index) {
		tv.replace(elements.get(index), index);
	}

}
