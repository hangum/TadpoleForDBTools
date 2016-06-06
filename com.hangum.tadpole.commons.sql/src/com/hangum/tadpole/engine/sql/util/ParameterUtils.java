/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Parameter Utils
 * @author hangum
 *
 */
public class ParameterUtils {

	private static final DateTimeFormatter  DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");;
	private static final DateTimeFormatter  TIMESTAMP_FORMATTER  = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * original code is  http://www.gotoquiz.com/web-coding/programming/java-programming/log-sql-statements-with-parameter-values-filled-in-spring-jdbc/
	 * 
	 * @param statement
	 * @param sqlArgs
	 * @return
	 */
	public static String fillParameters(String statement, Object[] sqlArgs) {
	    // initialize a StringBuilder with a guesstimated final length
	    StringBuilder completedSqlBuilder = new StringBuilder(Math.round(statement.length() * 1.2f));
	    int index, // will hold the index of the next ?
	        prevIndex = 0; // will hold the index of the previous ? + 1
	 
	    // loop through each SQL argument
	    for (Object arg : sqlArgs) {
	        index = statement.indexOf("?", prevIndex);
	        if (index == -1)
	            break; // bail out if there's a mismatch in # of args vs. ?'s
	 
	        // append the chunk of SQL coming before this ?
	        completedSqlBuilder.append(statement.substring(prevIndex, index));
	        if (arg == null)
	            completedSqlBuilder.append("NULL");
	        else if (arg instanceof String) {
	            // wrap the String in quotes and escape any quotes within
	            completedSqlBuilder.append('\'')
	               .append(arg.toString().replace("'", "''"))
	                .append('\'');
	        }
	        else if (arg instanceof Date) {
	            // convert it to a Joda DateTime
	            DateTime dateTime = new DateTime((Date)arg);
	            // test to see if it's a DATE or a TIMESTAMP
	            if (dateTime.getHourOfDay() == LocalTime.MIDNIGHT.getHourOfDay() &&
	                dateTime.getMinuteOfHour() == LocalTime.MIDNIGHT.getMinuteOfHour() &&
	                dateTime.getSecondOfMinute() == LocalTime.MIDNIGHT.getSecondOfMinute()) {
	                completedSqlBuilder.append("DATE '")
	                    .append(DATE_FORMATTER.print(dateTime))
	                    .append('\'');
	            }
	            else {
	                completedSqlBuilder.append("TIMESTAMP '")
	                    .append(TIMESTAMP_FORMATTER.print(dateTime))
	                    .append('\'');
	            }
	        }
	        else
	            completedSqlBuilder.append(arg.toString());
	 
	        prevIndex = index + 1;
	    }
	 
	    // add the rest of the SQL if any
	    if (prevIndex != statement.length())
	        completedSqlBuilder.append(statement.substring(prevIndex));
	 
	    return completedSqlBuilder.toString();
	}
}
