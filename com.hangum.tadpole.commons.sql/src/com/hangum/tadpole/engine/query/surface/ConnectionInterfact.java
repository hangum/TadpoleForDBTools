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

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;

/**
 * 테드폴디비 허브에서 메타데이터 조회 쿼리로 가져오지 못하는 방식이고,
 * 일반적인 jdbc의 표준 api 에서 지원하는 디비 메타 데이터를 이용하여 조회하는 방식입니다.
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
	public void executeUpdate(final Connection javaConn, UserDBDAO userDB, String sqlQuery) throws Exception;
	
	/**
	 * select
	 * 
	 * @param userDB
	 * @param requestQuery
	 * @param statementParameter
	 * @param queryResultCount
	 * 
	 * @throws Exception
	 */
	public ResultSetUtilDTO select(final Connection javaConn, UserDBDAO userDB, String requestQuery, Object[] statementParameter, int queryResultCount) throws Exception;
	
	/**
	 * 연결 테스트 합니다.
	 * 
	 * @param userDB
	 */
	public void connectionCheck(final Connection javaConn, UserDBDAO userDB) throws Exception ;
	
	/**
	 * table 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @throws Exception
	 */
	public List<TableDAO> tableList(final Connection javaConn, UserDBDAO userDB) throws Exception;

	/**
	 * table의 컬컴 정보를 리턴합니다.
	 * 
	 * @param userDB
	 * @param tbName
	 * @throws Exception
	 */
	public List<TableColumnDAO> tableColumnList(final Connection javaConn, UserDBDAO userDB, Map<String, String> mapParam) throws Exception;
}
