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
package com.hangum.tadpole.engine.utils;

import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * {@code RequestQuery} utils
 * 
 * @author hangum
 *
 */
public class RequestQueryUtil {
	
	/**
	 * simple request query
	 * 	ps) invalid thread 익셥션이 나오는 곳에서는 사용하면 안된다.
	 * 
	 * @param userDB
	 * @param connectID
	 * @param strQuery
	 * @return
	 */
	public static RequestQuery simpleRequestQuery(final UserDBDAO userDB, final String connectID, final String strSQL) {
		return new RequestQuery(connectID, userDB, strSQL, PublicTadpoleDefine.OBJECT_TYPE.TABLES, 
				EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.BLOCK, true);
	}

	/**
	 * simple request query
	 * 	ps) invalid thread 익셥션이 나오는 곳에서는 사용하면 안된다.
	 * 
	 * @param userDB
	 * @param strQuery
	 * @return
	 */
	public static RequestQuery simpleRequestQuery(final UserDBDAO userDB, final String strSQL) {
		return new RequestQuery(Utils.getUniqueID(), userDB, strSQL, PublicTadpoleDefine.OBJECT_TYPE.TABLES, 
				EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.BLOCK, true);
	}
	
	/**
	 * simple request query
	 * 
	 * @param loginIp
	 * @param sqlHead
	 * @param userDB
	 * @param strQuery
	 * @return
	 */
	public static RequestQuery simpleRequestQuery(final String loginIp, final String sqlHead, final UserDBDAO userDB, final String strSQL) {
		return new RequestQuery(loginIp, sqlHead, Utils.getUniqueID(), userDB, strSQL, PublicTadpoleDefine.OBJECT_TYPE.TABLES, 
				EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.BLOCK, true);
	}

}
