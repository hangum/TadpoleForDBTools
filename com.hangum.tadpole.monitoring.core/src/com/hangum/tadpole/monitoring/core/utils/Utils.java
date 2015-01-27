package com.hangum.tadpole.monitoring.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronExpression;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;

/**
 * cron exp utils
 * 
 * @author hangum
 *
 */
public class Utils {

	/**
	 * show cron expression
	 */
	public static String showExp(String strExp) throws ParseException {
		StringBuffer sbStr = new StringBuffer();
//		try {
			CronExpression exp = new CronExpression(strExp);
			java.util.Date showDate = new java.util.Date();
//			sbStr.append(showDate.toString() + PublicTadpoleDefine.LINE_SEPARATOR);
	        
	        for (int i=0; i<=5; i++) {
	          showDate = exp.getNextValidTimeAfter(showDate);
	          sbStr.append(convPretty(showDate) + PublicTadpoleDefine.LINE_SEPARATOR);
	          showDate = new java.util.Date(showDate.getTime() + 1000);
	        }
	        
	        return sbStr.toString();
//		} catch (ParseException e) {
//			MessageDialog.openError(null, Messages.AddScheduleDialog_20, Messages.AddScheduleDialog_12);
//			textCronExp.setFocus();
//		}
	}
	
	private static String convPretty(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		return sdf.format(date);
	}

}
