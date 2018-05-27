/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Date utils
 */
public class DateUtil {
	
	/**
	 * 현재 시간데이터를 yyyymmdd 형태로 넘겨준다. 
	 * 
	 * @return
	 */
	public static String getPretteYYYMMDDHHMM() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(new Date());
	}
	
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

	/**
	 * 현재 시간의 세컨드 
	 * @return
	 */
	public static long getDateSecond() {
		Calendar _cal = Calendar.getInstance();
		return (_cal.getTimeInMillis() % 1000) * 1000;
	}
	
	/**
	 * 현재 년월을 리턴합니다.
	 * 
	 * @return
	 */
	public static String getYearMonth() {
		Calendar _cal = Calendar.getInstance();
		return _cal.get(Calendar.YEAR) + "" + StringUtils.leftPad(""+(_cal.get(Calendar.MONTH) + 1), 2, "0"); 
	}
	
	/**
	 * 현재 년월을 리턴합니다.
	 * 
	 * @param longTimeMills
	 * @return
	 */
	public static String getYearMonth(long longTimeMills) {
		Calendar _cal = Calendar.getInstance();
		_cal.setTimeInMillis(longTimeMills);
		return _cal.get(Calendar.YEAR) + "" + StringUtils.leftPad(""+(_cal.get(Calendar.MONTH) + 1), 2, "0"); 
	}
	
	/**
	 * 현재 년월일을 리턴합니다.
	 * 
	 * @param longTimeMills
	 * @return
	 */
	public static String getYearMonthDay(long longTimeMills) {
		Calendar _cal = Calendar.getInstance();
		_cal.setTimeInMillis(longTimeMills);
		return _cal.get(Calendar.YEAR) + "" + StringUtils.leftPad(""+(_cal.get(Calendar.MONTH) + 1), 2, "0")  + "" + StringUtils.leftPad(""+(_cal.get(Calendar.DAY_OF_MONTH)), 2, "0"); 
	}
	
	/**
	 * 몇개월 후
	 * 
	 * @param month
	 * @return
	 */
	public static long afterMonthToMillsMonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, month);
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * 몇개월 전
	 * 
	 * @param month
	 * @return
	 */
	public static long beforeMonthToMillsMonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -month);
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * 몇일 후
	 * 
	 * @param day
	 * @return
	 */
	public static long afterMonthToMillis(int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, day);
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * 몇일 전
	 * 
	 * @param day
	 * @return
	 */
	public static long beforeMonthToMillis(int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -day);
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * 주어진 시간 기준으로 몇일 후
	 * 
	 * @param time
	 * @param intMaxDay
	 * @return
	 */
	public static long afterMonthToMillis(long time, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.add(Calendar.DAY_OF_YEAR, day);
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * 주어진 시간 기준으로 몇일 후
	 * 
	 * @param time
	 * @param intMaxDay
	 * @return
	 */
	public static long beforeMonthToMillis(long time, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.add(Calendar.DAY_OF_YEAR, -day);
		
		return cal.getTimeInMillis();
	}
 
    // List of all date formats that we want to parse.
    // Add your own format here.
    private static List<SimpleDateFormat> 
            dateFormats = new ArrayList<SimpleDateFormat>() {
		private static final long serialVersionUID = 1L; 
		{
            add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
            add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            add(new SimpleDateFormat("yyyy-MM-dd"));
            add(new SimpleDateFormat("HH:mm:ss"));
            add(new SimpleDateFormat("hh:mm:ss a"));
            add(new SimpleDateFormat("M/dd/yyyy"));
            add(new SimpleDateFormat("dd.M.yyyy"));
            add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss a"));
            add(new SimpleDateFormat("dd.MMM.yyyy"));
            add(new SimpleDateFormat("dd-MMM-yyyy"));
        }
    };
 
    /**
     * Convert String with various formats into java.util.Date
     * 
     * @param input
     *            Date as a string
     * @return java.util.Date object if input string is parsed 
     *          successfully else returns null
     */
    public static Date convertToDate(String input) {
        Date date = convertToDate(input, false);  
        if (date != null) {
            return date;
        }else{
        	return convertToDate(input, true);
        }
    }
    
    public static Date convertToDate(String input, boolean isLenient) {
        Date date = null;
        if(null == input) {
            return null;
        }
        for (SimpleDateFormat format : dateFormats) {
            try {
            	format.setLenient(isLenient);
                date = format.parse(input);                
            } catch (ParseException e) {
            }
            if (date != null) {
                break;
            }
        } 
        return date;
    }

}