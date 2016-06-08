/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.utils;

import java.sql.Date;

/**
 * String null safe comparator 
 * 
 * @author hangum
 *
 */
public class NullSafeComparator {
	/**
	 * int compare
	 * 
	 * @param int1
	 * @param int2
	 * @return
	 */
	public static int compare(final long long1, final long long2) {
		return long1 > long2?0:1;
	}
	
	/**
	 * int compare
	 * 
	 * @param int1
	 * @param int2
	 * @return
	 */
	public static int compare(final int int1, final int int2) {
		return int1 > int2?0:1;
	}

	/**
	 * String compare
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int compare(final String str1, final String str2) {
		if (str1 == null ^ str2 == null) {
			return (str1 == null)?-1:1;
		} else if (str1 == null && str2 == null) {
			return 0;
		} else {
			if(str1 != null) {
				return str1.compareToIgnoreCase(str2);
			} else {
				return 0;
			}
		}
	}

	/**
	 * java.sql.Date compare
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compare(Date date1, Date date2) {
		if (date1 == null ^ date2 == null) {
			return (date1 == null) ? -1 : 1;
		} else if (date1 == null && date2 == null) {
			return 0;
		} else {
			if(date1 != null) {
				return date1.compareTo(date2);
			} else {
				return 0;
			}
		}
	}

}
