/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.paremeter;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.restful.RESTFULUnsupportedEncodingException;
import com.hangum.tadpole.engine.restful.RESTFulArgumentNotMatchException;
import com.hangum.tadpole.engine.restful.RESTfulAPIUtils;
import com.hangum.tadpole.engine.sql.paremeter.lang.OracleStyleSQLNamedParameterUtil;
import com.hangum.tadpole.engine.sql.util.SQLUtil;

/**
 * SQL을 분석해서 사용할 SQL과 SQL bind parameter로 만든다. 
 */
public class NamedParameterUtil {

	/**
	 * SQL을 분석해서 사용할 SQL과 SQL bind parameter로 만든다.
	 *  
	 * @param userDB 
	 * @param strLastSQL
	 * @param strParameter
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws RESTFulArgumentNotMatchException
	 */
	public static NamedParameterDAO parseParameterUtils(UserDBDAO userDB, String strLastSQL, String strParameter) throws RESTFULUnsupportedEncodingException, RESTFulArgumentNotMatchException {
		NamedParameterDAO returnDao = new NamedParameterDAO();
		
		strLastSQL = SQLUtil.makeExecutableSQL(userDB, strLastSQL);
		OracleStyleSQLNamedParameterUtil oracleNamedParamUtil = new OracleStyleSQLNamedParameterUtil();
		String strOracleStyleSQL = oracleNamedParamUtil.parse(strLastSQL);
		
		Map<Integer, String> mapIndex = oracleNamedParamUtil.getMapIndexToName();
		if(!mapIndex.isEmpty()) {
			returnDao.setStrSQL(strOracleStyleSQL);
			returnDao.setListParam(RESTfulAPIUtils.makeArgumentToOracleList(mapIndex, strParameter));
		} else {
			returnDao.setStrSQL(strLastSQL);
			returnDao.setListParam(RESTfulAPIUtils.makeArgumentToJavaList(strParameter));
		}
		
		return returnDao;
	}
	
	
}
