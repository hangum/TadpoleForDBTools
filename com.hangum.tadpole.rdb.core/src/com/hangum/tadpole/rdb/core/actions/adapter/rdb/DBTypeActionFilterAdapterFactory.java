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

import com.hangum.tadpole.commons.libs.core.utils.LicenseValidator;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

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
	
	/** munu 는 history hub는 보이지 않도록 합니다. */
	private static final String strProduct = LicenseValidator.getLicense().getProductType();

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if(adapterType == IActionFilter.class) {
			if(!PublicTadpoleDefine.PRODUCT_TYPE.TadpoleHistoryHub.name().equals(strProduct)) {
				return new DBTypeActionFilterAdapter();
			}
		}
		
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {IActionFilter.class};
	}

}
