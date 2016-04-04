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
package com.hangum.tadpole.engine.sql.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * Object compile util
 * 
 * @author hangum
 *
 */
public class ObjectCompileUtil {
	private static final Logger logger = Logger.getLogger(ObjectCompileUtil.class);
	/**
	 * object 컴파일 후 - 오브젝트 상태를 표시해 줄수 있도록 합니다. 
	 * 
	 * @param userDB
	 * @param ddlType
	 * @param objectName
	 * @return
	 */
	public static String validateObject(UserDBDAO userDB, QUERY_DDL_TYPE ddlType, String objectName) {
		if(StringUtils.isEmpty(objectName)) return "";
		String retMsg = ""; //$NON-NLS-1$
		
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT | 
				userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT
		) {
			try {
				String strObjectName = StringUtils.upperCase(objectName);
				if(ddlType == QUERY_DDL_TYPE.PROCEDURE) {
					retMsg = OracleObjectCompileUtils.otherObjectCompile(QUERY_DDL_TYPE.PROCEDURE, "PROCEDURE", strObjectName, userDB); //$NON-NLS-1$
				} else if(ddlType == QUERY_DDL_TYPE.PACKAGE) {
					retMsg = OracleObjectCompileUtils.packageCompile(strObjectName, userDB);
				} else if(ddlType == QUERY_DDL_TYPE.FUNCTION) {
					retMsg = OracleObjectCompileUtils.otherObjectCompile(QUERY_DDL_TYPE.FUNCTION, "FUNCTION", strObjectName, userDB);			 //$NON-NLS-1$
				} else if(ddlType == QUERY_DDL_TYPE.TRIGGER) {
					retMsg = OracleObjectCompileUtils.otherObjectCompile(QUERY_DDL_TYPE.TRIGGER, "TRIGGER",  strObjectName, userDB);			 //$NON-NLS-1$
				} else if(ddlType == QUERY_DDL_TYPE.SYNONYM) {
					retMsg = OracleObjectCompileUtils.otherObjectCompile(QUERY_DDL_TYPE.SYNONYM, "SYNONYM",  strObjectName, userDB);			 //$NON-NLS-1$
				}
			} catch(Exception e) {
				logger.error("object compile", e);
			}
		}
		
		return retMsg;
	}
}
