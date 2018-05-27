/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
//import org.joda.time.DateTime;
//import org.joda.time.DateTimeZone;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * utc로 저장되어 있는 시간을 사용자가 원하는 시간대로 바꾸어 보여줍니다.
 * 
 * @author hangum
 *
 */
public class TimeZoneUtil {
	private static final Logger logger = Logger.getLogger(TimeZoneUtil.class);
	
	/**
	 * timezone list
	 * 
	 * @return
	 */
	public static Set<String> getTimezoneList() {
		return DateTimeZone.getAvailableIDs();
	}
	
	/**
	 * db의 timezone 이 있다면 사용자의 타임존으로 바꿔준다.
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date date) {
		String dbTimeZone = GetAdminPreference.getDBTimezone();

		// db의 timezone 없다면 기본 시간으로 바꾼다.
		if(StringUtils.isEmpty(dbTimeZone)) { 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
	 	} else {
	 		// 서버 타임 UTC를 로컬 타임존으로 변경합니다. 
	 		DateTime targetDateTime = new DateTime(date).withZone(DateTimeZone.forID(SessionManager.getTimezone()));
	 		String strPretty = targetDateTime.toString(prettyDateTimeFormater());
	 		
//	 		if(logger.isDebugEnabled()) {
//	 			logger.debug(String.format("[SessionManager dbTimezone] %s => %s", SessionManager.getTimezone(), targetDateTime));
//	 			logger.debug(String.format("[strPretty] %s", strPretty));
//	 			logger.debug("===============================================================");
//	 		}
	 		
	 		return strPretty;
	 	}
	}
	
	/**
	 * 사용자 로컬 타임존을 서버의 타임존으로 바꿔준다.
	 * 
	 * @param localTimeMills
	 * @return
	 */
	public static long chageTimeZone(long localTimeMills) {
		String dbTimeZone = GetAdminPreference.getDBTimezone();
		// db의 timezone 없다면 기본 시간으로 바꾼다.
		if(StringUtils.isEmpty(dbTimeZone)) { 
			return localTimeMills;
	 	} else {
	 		// 서버 타임 UTC를 로컬 타임존으로 변경합니다. 
	 		DateTime sourceDateTime = new DateTime(localTimeMills, DateTimeZone.forID(SessionManager.getTimezone()));
	 		DateTime targetDateTime = sourceDateTime.withZone(DateTimeZone.forID(dbTimeZone));
			 		if(logger.isDebugEnabled()) {
			 			logger.debug(String.format("[SessionManager dbTimezone] %s => %s => %s", SessionManager.getTimezone(), localTimeMills, sourceDateTime.toString()));
			 			logger.debug(String.format("[targetDateTime] %s => %s", targetDateTime.getMillis(), targetDateTime.toString()));
			 			logger.debug("===============================================================");
			 		}
	 		
	 		return targetDateTime.getMillis();
	 	}
	}
	
	/**
	 * prettyDate
	 * @param date
	 * @return
	 */
	public static DateTimeFormatter prettyDateTimeFormater() {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		return dtf;
	}
	
//	/**
//	 * sample date time
//	 * @param args
//	 */
//	public static void main(String[] args) {
////		DateTimeZone timeZoneUTC = DateTimeZone.forID( "UTC" );
////		DateTimeZone timeZoneLondon = DateTimeZone.forID( "Europe/London" );
////		DateTimeZone timeZoneKolkata = DateTimeZone.forID( "Asia/Kolkata" );
////		DateTimeZone timeZoneNewYork = DateTimeZone.forID( "America/New_York" );
//		DateTimeZone timeAsiaSeoul = DateTimeZone.forID( "Asia/Seoul" );
//		
//		DateTime nowSeoul = DateTime.now( timeAsiaSeoul );
//		System.out.println(String.format("=%s:%s", "nowSeoul", nowSeoul) );
//
//		
//		DateTime nowUtcnowSeoul = nowSeoul.withZone( DateTimeZone.UTC );  // Built-in constant for UTC.
//		System.out.println(String.format("=%s:%s", "nowUtcnowSeoul", nowUtcnowSeoul) );
//	}
	
//	/**
//	 * defaultti
//	 * @param date
//	 * @return
//	 */
//	private static String defaultTime(Date date) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		return sdf.format(date);
//	}

}
