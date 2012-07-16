package com.hangum.db.browser.rap.core.actions.adapter.resource;

import org.eclipse.ui.IActionFilter;

import com.hangum.db.dao.system.UserDBResourceDAO;

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
		System.out.println(target);
		
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
