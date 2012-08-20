package com.hangum.db.browser.rap.core.actions.adapter.rdb;

import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.IActionFilter;

import com.hangum.db.dao.system.UserDBDAO;

/**
 * rdb db popup
 * 
 * @author hangum
 *
 */
public class DBTypeActionFilterAdapter implements IActionFilter {	
	private static final String DB_TYPE = "db_type";
	
	@Override
	public boolean testAttribute(Object target, String name, String value) {
		UserDBDAO userDB = null;
		
		if (target instanceof UserDBDAO) {
			userDB = (UserDBDAO) target;
			
			if(DB_TYPE.equals(name)) {
				String[] dbTypes = StringUtils.split(value, ",");
				for (String dbType : dbTypes) {
					if(userDB.getTypes().toLowerCase().equals(dbType)) {
						return true;
					}
				}				
			}			
		}
		
		return false;
	}

}
