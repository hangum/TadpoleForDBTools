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
package com.hangum.tadpole.commons.sql.util.executer.procedure;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpold.commons.libs.core.dao.ResultSetTableViewerDAO;
import com.hangum.tadpole.commons.sql.util.ResultSetUtils;
import com.hangum.tadpole.commons.sql.util.sqlscripts.DDLScriptManager;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * rdb procedure executer.
 * 
 * @author hangum
 *
 */
public abstract class ProcedureExecutor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ProcedureExecutor.class);
	
//	/** Result Max row count */
//	protected int queryResultCount 	= GetPreferenceGeneral.getQueryResultCount();
	
	protected UserDBDAO userDB;
	protected List<InOutParameterDAO> listInParamValues;
	protected List<InOutParameterDAO> listOutParamValues;
	
	protected ProcedureFunctionDAO procedureDAO;
	
	/** result dao */
	protected ResultSetTableViewerDAO resultDAO;

	/**
	 * procedure executor
	 * 
	 * @param procedureDAO
	 * @param listParamValues
	 * @param userDB
	 */
	public ProcedureExecutor(ProcedureFunctionDAO procedureDAO, UserDBDAO userDB) {
		this.userDB = userDB;
		this.procedureDAO = procedureDAO;
	}

	/**
	 * Get in parameter.
	 * 
	 * @return
	 */
	public List<InOutParameterDAO> getInParameters() throws Exception {
		DDLScriptManager ddlScriptManager = new DDLScriptManager(userDB);
		listInParamValues = ddlScriptManager.getProcedureInParamter(procedureDAO);
		if(listInParamValues == null) listInParamValues = new ArrayList<InOutParameterDAO>();
		
		return listInParamValues;
	}
	
	
	/**
	 * Get out parameter.
	 * 
	 * @return
	 */
	public List<InOutParameterDAO> getOutParameters() throws Exception {
		DDLScriptManager ddlScriptManager = new DDLScriptManager(userDB);
		listOutParamValues = ddlScriptManager.getProcedureOutParamter(procedureDAO);
		if(listOutParamValues == null) listOutParamValues = new ArrayList<InOutParameterDAO>();
		
		return listOutParamValues;
	}

	/**
	 * Get parameter Count.
	 * 
	 * @return
	 */
	public int getParametersCount(String type) throws Exception {
		int cnt = 0;
		
		for(int i=0;i< this.listInParamValues.size();i++){
			if (type.equals(listInParamValues.get(i).getType())) cnt++;
		}
		for(int i=0;i< this.listOutParamValues.size();i++){
			if (type.equals(listOutParamValues.get(i).getType()) && "OUT".equals(listOutParamValues.get(i).getType())) cnt++;
		}
		
		return cnt;
	}
	public int getParametersCount() throws Exception {
		int cnt = this.listInParamValues.size();
		
		for(int i=0;i< this.listOutParamValues.size();i++){
			if ("OUT".equals(listOutParamValues.get(i).getType())) cnt++;
		}
		
		return cnt;
	}
	
	/**
	 * 프로시저 결과가 cursor일때 결과를 담아줍니다.
	 * 
	 * @param rs
	 * @throws Exception
	 */
	protected void setResultCursor(ResultSet rs) throws Exception {
		Map<Integer, String> mapColumns = ResultSetUtils.getColumnName(rs);
		Map<Integer, Integer> mapColumnType = ResultSetUtils.getColumnType(rs.getMetaData()); 
		List<Map<Integer, Object>> sourceDataList = ResultSetUtils.getResultToList(rs, 1000, true);

		ResultSetTableViewerDAO resultSet = new ResultSetTableViewerDAO(mapColumns, mapColumnType, sourceDataList);
		setResultDAO(resultSet);
	}
	
	/**
	 * 프로시저 결과가 cursor가 아닐 경우 결과를 설정합니다.
	 * 
	 * 	결과 set이 cursor이 아닌 경우에는 테이블에 데이터를 출력하기 위해 다음 3가지가 필요합니다. 
		column 이름.
		column 정렬 순서.
		결과 데이터 셋.
	 *  "Seq", "Name", "Type", "ParamType", "Length", "Value"
	 *  
	 *  @param List<Map<Integer, Object>> sourceDataList
	 */
	protected void setResultNoCursor(List<Map<Integer, Object>> sourceDataList) throws Exception {
		Map<Integer, String> mapColumns = new HashMap<Integer, String>();
		mapColumns.put(0, "Seq");
		mapColumns.put(1, "Name");
		mapColumns.put(2, "Type");
		mapColumns.put(3, "ParamType");
		mapColumns.put(4, "Length");
		mapColumns.put(5, "Value");
		
		Map<Integer, Integer> mapColumnType = new HashMap<Integer, Integer>();
		mapColumnType.put(0, java.sql.Types.VARCHAR);
		mapColumnType.put(1, java.sql.Types.VARCHAR);
		mapColumnType.put(2, java.sql.Types.VARCHAR);
		mapColumnType.put(3, java.sql.Types.VARCHAR);
		mapColumnType.put(4, java.sql.Types.DOUBLE);
		mapColumnType.put(5, java.sql.Types.VARCHAR);
		
		ResultSetTableViewerDAO resultSet = new ResultSetTableViewerDAO(mapColumns, mapColumnType, sourceDataList);
		setResultDAO(resultSet);
	}
	
	/**
	 * @return the resultDAO
	 */
	public ResultSetTableViewerDAO getResultDAO() {
		return resultDAO;
	}

	/**
	 * @param resultDAO the resultDAO to set
	 */
	public void setResultDAO(ResultSetTableViewerDAO resultDAO) {
		this.resultDAO = resultDAO;
	}

	/**
	 * executer
	 * 
	 * @param parameterList
	 * @return
	 */
	public abstract boolean exec(List<InOutParameterDAO> parameterList) throws Exception ;

}
