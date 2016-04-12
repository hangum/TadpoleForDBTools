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
package com.hangum.tadpole.db.bander.oracle;

import java.sql.PreparedStatement;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.PartQueryUtil;

/**
 * oracle execute plan
 * 
 * @author hangum
 *
 */
public class OracleExecutePlanUtils {

	/**
	 * oracle query plan을 실행합니다. 
	 * 
	 * @param userDB
	 * @param sql
	 * @param planTableName
	 * @throws Exception
	 */
	public static void plan(UserDBDAO userDB, String sql, String planTableName, java.sql.Connection javaConn, PreparedStatement stmt, String statement_id  ) throws Exception {
			
		String query = PartQueryUtil.makeExplainQuery(userDB,  sql);
		query = StringUtils.replaceOnce(query, PublicTadpoleDefine.STATEMENT_ID, statement_id);
		query = StringUtils.replaceOnce(query, PublicTadpoleDefine.DELIMITER, planTableName);
		
		stmt = javaConn.prepareStatement( query );
		stmt.execute();
		
	}
}
