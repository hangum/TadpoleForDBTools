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
package com.hangum.tadpole.rdb.core.actions.adapter.resource;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

/*
 * resource type adapter factory
 * 
 */
public class ResourceTypeAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IActionFilter.class) {
			return new ResourceTypeActionFilterAdapter();
		}
		
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {IActionFilter.class};
	}

}
