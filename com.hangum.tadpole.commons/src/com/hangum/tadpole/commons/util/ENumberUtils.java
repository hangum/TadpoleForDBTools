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
package com.hangum.tadpole.commons.util;

import org.apache.commons.lang.math.NumberUtils;

/**
 * Number utils
 * 
 * @author hangum
 *
 */
public class ENumberUtils {

	/**
	 * Object to java.lang.int
	 * 
	 * @param obj
	 * @return
	 */
	public static int toInt(Object obj) {
		if(obj == null) return 0;
		
		try {
			return NumberUtils.toInt(obj.toString());
		} catch(Exception nfe) {
			return 0;
		}
	}
}
