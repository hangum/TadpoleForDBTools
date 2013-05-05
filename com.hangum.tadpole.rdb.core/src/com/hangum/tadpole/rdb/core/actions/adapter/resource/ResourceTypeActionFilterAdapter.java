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

import org.eclipse.ui.IActionFilter;

import com.hangum.tadpole.dao.system.UserDBResourceDAO;

/**
 * resource type adapter
 * 
 * @author hangum
 *
 */
public class ResourceTypeActionFilterAdapter implements IActionFilter {
	private static final Object MYOBJECT_TYPE = "type";
	
	@Override
	public boolean testAttribute(Object target, String name, String value) {
//		System.out.println(target);
		
		if (target instanceof UserDBResourceDAO) {
//			UserDBResourceDAO obj = (UserDBResourceDAO) target;
//
//			if(MYOBJECT_TYPE.equals(name)) {
//				return true;
//			}
			return true;			
		}
		
		return false;
	}

}
