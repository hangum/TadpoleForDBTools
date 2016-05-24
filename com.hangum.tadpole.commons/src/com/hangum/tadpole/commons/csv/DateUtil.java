package com.hangum.tadpole.commons.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * csv loader 
 * 
 * @see original file location is (http://viralpatel.net/blogs/java-load-csv-file-to-database/)
 */
public class DateUtil {
	
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