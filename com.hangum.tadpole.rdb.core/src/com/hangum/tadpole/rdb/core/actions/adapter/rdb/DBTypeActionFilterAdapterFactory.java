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
package com.hangum.tadpole.rdb.core.actions.adapter.rdb;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;

/**
 * action filter 
 * 
 * @author hangum
 *
 */
public class DBTypeActionFilterAdapterFactory implements IAdapterFactory {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DBTypeActionFilterAdapterFactory.class);

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IActionFilter.class) {
			return new DBTypeActionFilterAdapter();
		}
		
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {IActionFilter.class};
	}

}
