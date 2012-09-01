/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.db.browser.rap.core.actions.adapter.resource;

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
