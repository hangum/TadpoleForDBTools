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
package com.hangum.tadpole.engine.sql.util;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * sql convert char utils
 * 
 * @author hangum
 *
 */
public class SQLConvertCharUtil {
	private static final Logger logger = Logger.getLogger(SQLConvertCharUtil.class);
	
	/**
	 * convert to client
	 * 
	 * @param userDB
	 * @param strChar
	 * @return
	 */
	public static String toClient(final UserDBDAO userDB, final String strChar) {
		if(strChar == null) return strChar;
		
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_convert_data())) {
			try {
				String strConvChar = new String(strChar.getBytes(userDB.getChar_target()), userDB.getChar_source());
				if(logger.isDebugEnabled()) logger.debug("[toClient]conv char : " + strChar + "--->" + strConvChar);
				return strConvChar;
			} catch(Exception e) {
				// igonre exception
			}
		}
		
		return strChar;
	}
	
	/**
	 * convert to server
	 * 
	 * @param userDB
	 * @param strChar
	 * @return
	 */
	public static String toServer(final UserDBDAO userDB, final String strChar) {
		if(strChar == null) return strChar;
		
		if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_convert_data())) {
			try {
				String strConvChar = new String(strChar.getBytes(userDB.getChar_source()), userDB.getChar_target());
				if(logger.isDebugEnabled()) logger.debug("[toServer]conv char : " + strChar + "--->" + strConvChar);
				return strConvChar;
			} catch(Exception e) {
				// igonre exception
			}
		}
		
		return strChar;
	}

}  
