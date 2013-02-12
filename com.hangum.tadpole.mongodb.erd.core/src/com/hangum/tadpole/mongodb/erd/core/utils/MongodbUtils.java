/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
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
