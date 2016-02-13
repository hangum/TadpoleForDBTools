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
package com.hangum.tadpole.db.metadata.constants;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * basic constants
 * 
 * @author hangum
 *
 */
public class DefaultConstants extends SQLConstants {

	public DefaultConstants(UserDBDAO userDB) {
		super(userDB);
	}

	@Override
	public String keyword() {
		return "select|insert|update|delete|from|where|and|or|group|by|order|limit|offset|having|as|case|" +
				"when|else|end|type|left|right|join|on|outer|desc|asc|union" +
				"into|values";
	}

	@Override
	public String function() {
		return "count|min|max|avg|sum|rank|now|coalesce";
	}

	@Override
	public String constant() {
		return "true|false|null";
	}

	@Override
	public String variable() {
		return "";
	}

}
