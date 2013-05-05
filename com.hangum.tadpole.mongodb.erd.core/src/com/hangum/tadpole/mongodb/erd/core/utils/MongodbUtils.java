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
package com.hangum.tadpole.mongodb.erd.core.utils;

import org.apache.commons.lang.StringUtils;

/**
 * MongoDB Utils
 * 
 * @author hangum
 *
 */
public class MongodbUtils {

	/**
	 * Is mongodb reference key
	 * 
	 * @param type
	 * @param field
	 * @return
	 */
	public static boolean isReferenceKey(String type, String field) {
		if("ObjectId".equals(type) && 
				!StringUtils.startsWith(field, "_id") && 
				StringUtils.endsWith(field, "_id")) {
			return true;			
		}
		
		return false;
	}
	
}
