package com.hangum.tadpold.commons.libs.core.utils;

import java.sql.Date;

/**
 * String null safe comparator 
 * 
 * @author hangum
 *
 */
public class NullSafeComparator {

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
		}

		if (str1 == null && str2 == null) {
			return 0;
		}

		return str1.compareToIgnoreCase(str2);
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
		}

		if (date1 == null && date2 == null) {
			return 0;
		}
		
		return date1.compareTo(date2);
	}

}
