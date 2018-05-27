/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.permission;

import java.util.List;

import com.hangum.tadpole.engine.define.TDBResultCodeDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.restful.TadpoleException;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.QUERY_DML_TYPE;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.SQL_TYPE;

/**
 * SQL permission check
 * 
 * @author hangum
 *
 */
public class AcessControlUtils {
	
	/**
	 * SQL 권한검사. 
	 * 
	 * @param errMsg
	 * @param listReqQuery
	 * @throws TadpoleException
	 */
	public static void SQLPermissionCheck(final String errMsg, final List<RequestQuery> listReqQuery) throws TadpoleException {
		if(listReqQuery.isEmpty()) return;
		
		for (RequestQuery reqQuery : listReqQuery) {
			SQLPermissionCheck(errMsg, reqQuery);
		}
	}

	/**
	 * SQL 권한검사.
	 * 
	 * @param errMsg
	 * @param reqQuery
	 * @throws TadpoleException
	 */
	public static void SQLPermissionCheck(String errMsg, final RequestQuery reqQuery) throws TadpoleException 
	{
		final UserDBDAO userDB = reqQuery.getUserDB();
		PermissionChecker.isExecute(reqQuery);
		
		if(reqQuery.getSqlType() == SQL_TYPE.DDL) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDdl_lock())) {
				throw new TadpoleException(TDBResultCodeDefine.FORBIDDEN, errMsg);
			}
		}
		PublicTadpoleDefine.QUERY_DML_TYPE queryType = reqQuery.getSqlDMLType();
		if(queryType == QUERY_DML_TYPE.INSERT) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getInsert_lock())) {
				throw new TadpoleException(TDBResultCodeDefine.FORBIDDEN, errMsg);
			}
		}
		if(queryType == QUERY_DML_TYPE.UPDATE) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getUpdate_lock())) {
				throw new TadpoleException(TDBResultCodeDefine.FORBIDDEN, errMsg);
			}
		}
		if(queryType == QUERY_DML_TYPE.DELETE) {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDelete_locl())) {
				throw new TadpoleException(TDBResultCodeDefine.FORBIDDEN, errMsg);
			}
		}
	
	}

}
