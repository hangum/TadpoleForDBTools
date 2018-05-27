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
package com.hangum.tadpole.engine.sql.paremeter.lang;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.paremeter.NamedParameterDAO;

/**
 * Elasticsearch parameter util
 * 
 * @author hangum
 *
 */
public class ElasticsearchParameterUtil {
	private static final Logger logger = Logger.getLogger(ElasticsearchParameterUtil.class);

	/**
	 * 
	 * @param userDB
	 * @param query elasticsearch query dsl
	 * @param mapParameter
	 * @return
	 */
	public static NamedParameterDAO parser(UserDBDAO userDB, String query, Map<String, Object> mapParameter) {
		NamedParameterDAO returnDao = new NamedParameterDAO();
		
		Set<String> keys = mapParameter.keySet();
		for (String strKey : keys) {
			String strValue = (String)mapParameter.get(strKey);
			query = StringUtils.replace(query, ":" + strKey, strValue);
		}
		returnDao.setStrSQL(query);
		if(logger.isDebugEnabled()) logger.debug("====last query is \n" + query);
		
		return returnDao;
		
	}
}
