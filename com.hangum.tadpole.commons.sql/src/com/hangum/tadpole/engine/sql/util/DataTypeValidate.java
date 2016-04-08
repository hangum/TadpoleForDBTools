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
package com.hangum.tadpole.engine.sql.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;


/**
 * <pre>
 * 각 디비의 데이터 타입을 정의 합니다.
 * 
 * 현재는 디비의 number, datetime 으로만 구분하도록 합니다.
 * 
 * </pre>
 */
public class DataTypeValidate {
	private static final Logger logger = Logger.getLogger(DataTypeValidate.class);
	
	/**
	 * 디비의 type에 따른 입력 값이 올바른지 검증 합니다.
	 * 
	 * @param userDB
	 * @param type
	 * @param value
	 * @return
	 */
	public static boolean isValid(UserDBDAO userDB, String type, String value) {
		type = type.toLowerCase();
		if(logger.isDebugEnabled()) logger.debug("[type]" + type + "[value]" + value);
				
		if(DBDefine.MYSQL_DEFAULT == userDB.getDBDefine()) {
			if(type.equals("datetime") || type.indexOf("timestamp") >= 0) {
				return strToData(value);
			}
			if(type.indexOf("int") >= 0 || type.equals("decimal")) {
				return strToNumber(value);
			}
			
		} else if(DBDefine.ORACLE_DEFAULT == userDB.getDBDefine()) {
			
			if(type.equals("date") || type.equals("timestamp")) {
				return strToData(value);
			}
			if(type.indexOf("number") >= 0 || type.equals("integer")) {
				return strToNumber(value);
			}
			
		} else if(DBDefine.SQLite_DEFAULT == userDB.getDBDefine()) {
			
			if(type.equals("date") || type.equals("datetime")) {
				return strToData(value);
			}
			if(type.indexOf("number") >= 0 || type.equals("int") || type.equals("numeric")) {
				return strToNumber(value);
			}
		
		} else if(DBDefine.CUBRID_DEFAULT == userDB.getDBDefine()) {
			
			if(type.equals("date") || type.equals("datetime")) {
				return strToData(value);
			}
			if(type.indexOf("number") >= 0 || type.equals("integer") || type.equals("numeric")) {
				return strToNumber(value);
			}
			
		} else if(DBDefine.MONGODB_DEFAULT == userDB.getDBDefine()) {
			if(type.equals("java.lang.Double")) {
				return strToNumber(value);
			} else if(type.equals("java.lang.Integer")) {
				return strToInteger(value);
			} else if(type.equals("java.util.Date")) {
				return strToData(value);
			}
		}
		
		return true;
	}
	
	/**
	 * 입력값이 무자열인지 비교한다.
	 * @param vauel
	 * @return
	 */
	private static boolean strToNumber(String vauel) {
		try {
			double intVal = Double.parseDouble(vauel);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * 입력값이 무자열인지 비교한다.
	 * @param vauel
	 * @return
	 */
	private static boolean strToInteger(String vauel) {
		try {
			int intVal = Integer.parseInt(vauel);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * 입력값이 date인지 검사합니다.
	 *  yyyy-MM-dd HH:mm:ss, yyyy-MM-dd 포멧이 맞는지 비교한다.
	 * 
	 * @param val
	 * @return
	 */
	private static boolean strToData(String val) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.parse(val);
			
			return true;
		} catch(ParseException pe) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				format.parse(val);
				
				return true;
			} catch(ParseException ee) {
				return false;				
			}
		}
	}
	
}
