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
package com.hangum.tadpole.engine.query.surface;

import java.util.List;
import java.util.Map;

import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;

/**
 * 올챙이에서 jdbc로 연결하지 못하는 데이터베이스의 Connection의 interface를 정합니다.
 * 
 * @author hangum
 *
 */
public interface ConnectionInterfact {

	/**
	 * not select 
	 * 
	 * @param userDB
	 * @param sqlQuery
	 * @return
	 */
	public void executeUpdate(UserDBDAO userDB, String sqlQuery) throws Exception;
	
	/**
	 * select
	 * 
	 * @param userDB
	 * @param requestQuery
	 * @param queryResultCount
	 * 
	 * @throws Exception
	 */
	public ResultSetUtilDTO select(UserDBDAO userDB, String requestQuery, int queryResultCount, String strNullValue) throws Exception;
	
	/**
	 * 연결 테스트 합니다.
	 * 
	 * @param userDB
	 */
	public void connectionCheck(UserDBDAO userDB) throws Exception ;
	
	/**
	 * table 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public List<TableDAO> tableList(UserDBDAO userDB) throws Exception;

	/**
	 * table의 컬컴 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @param tbName
	 * @throws Exception
	 */
	public List<TableColumnDAO> tableColumnList(UserDBDAO userDB, Map<String, String> mapParam) throws Exception;
}
