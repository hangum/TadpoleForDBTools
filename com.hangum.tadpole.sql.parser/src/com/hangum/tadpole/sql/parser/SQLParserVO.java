/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.sql.parser;

import java.util.HashMap;
import java.util.Map;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.QUERY_TYPE;

/**
 * SQLparser value object
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 8.
 *
 */
public class SQLParserVO {

	// query type
	private PublicTadpoleDefine.QUERY_TYPE queryType = QUERY_TYPE.UNKNOWN;

	// parameter
	private Map<Integer, String> mapParameter = new HashMap<>();

	// table name
	// column name
	// where pr

	/**
	 * @return the queryType
	 */
	public PublicTadpoleDefine.QUERY_TYPE getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType
	 *            the queryType to set
	 */
	public void setQueryType(PublicTadpoleDefine.QUERY_TYPE queryType) {
		this.queryType = queryType;
	}

	/**
	 * @return the mapParameter
	 */
	public Map<Integer, String> getMapParameter() {
		return mapParameter;
	}

	/**
	 * @param mapParameter
	 *            the mapParameter to set
	 */
	public void setMapParameter(Map<Integer, String> mapParameter) {
		this.mapParameter = mapParameter;
	}

}
