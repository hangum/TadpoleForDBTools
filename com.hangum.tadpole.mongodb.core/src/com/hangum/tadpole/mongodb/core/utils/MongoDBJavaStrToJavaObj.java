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
package com.hangum.tadpole.mongodb.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * java string to java object
 * @author hangum
 *
 */
public class MongoDBJavaStrToJavaObj {

	/**
	 *  java string to java object
	 *  
	 * @param type
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static Object convStrToObj(String type, String value) throws Exception {
		value = value.trim();
		
		if(type.equals("java.lang.String")) { //$NON-NLS-1$
			return value;
		} else if(type.equals("java.lang.Double")) { //$NON-NLS-1$
			return Double.parseDouble(value);
		} else if(type.equals("java.lang.Integer")) { //$NON-NLS-1$
			return Integer.parseInt(value);
		} else if(type.equals("java.util.Date")) { //$NON-NLS-1$
			
			try {
				DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				return sdFormat.parse(value);				
			} catch(Exception e) {}
			
			try {
				DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
				return sdFormat.parse(value);				
			} catch(Exception e) {}
			
			try {
				DateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
				return sdFormat.parse(value);				
			} catch(Exception e) {}
			
			return new Date(value);
		} else if(type.equals("java.lang.Boolean")) {
			return Boolean.parseBoolean(value);
		} else {
			throw new Exception("not define exception[type]" + type);
		}
	}
	
	
	/**
	 * java array string to java Object
	 * 
	 * @param type
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public static Object[] convStrToObj(String type, String[] values) throws Exception {
		Object[] objs = new Object[values.length];
		
		for (int i=0; i<values.length; i++) {
			objs[i] = convStrToObj(type, values[i]);
		}
		
		return objs;
	}
}
