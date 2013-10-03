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

/**
 * Time utils
 * 
 * @author hangum
 * 
 */
public class TimeUtils {

	/**
	 * 
	 * @param initSeconds
	 * @return ? hour ? minute ? sec
	 */
	public static String getHoureMinSecString(long longMillis) {
		String strRetValue = "00:00:00";

		if (longMillis > 0) {
			longMillis = longMillis / 1000;
			long longViewHour = longMillis / 3600;
			long longCalcHour = longMillis % 3600;
			long longMinute = longCalcHour / 60;
			long longSec = longCalcHour % 60;

			strRetValue = (longViewHour < 10 ? "0" : "")+ String.valueOf(longViewHour) + ":" + 
						  (longMinute < 10 ? "0" : "") + String.valueOf(longMinute) + ":" + 
						  (longSec < 10 ? "0" : "") + String.valueOf(longSec);
		}

		return strRetValue;
	}
}
