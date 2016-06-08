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
package com.hangum.tadpole.engine.sql.util.executer.procedure;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtils;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.sql.util.sqlscripts.DDLScriptManager;

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
	
	protected UserDBDAO userDB;
	protected List<InOutParameterDAO> listInParamValues = new ArrayList<InOutParameterDAO>();
	protected List<InOutParameterDAO> listOutParamValues = new ArrayList<InOutParameterDAO>();
	
	protected ProcedureFunctionDAO procedureDAO;
	
	/** result dao */
	protected List<ResultSetUtilDTO> resultDAO = new ArrayList<ResultSetUtilDTO>();
	
	/** dbms output - only oracle */
	protected String strOutput = "";
	
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
	 * make execut script
	 * @return
	 * @throws Exception
	 */
	public String getMakeExecuteScript() throws Exception {
		StringBuffer sbQuery = new StringBuffer();
		if ("FUNCTION".equalsIgnoreCase(procedureDAO.getType())){
			if(!"".equals(procedureDAO.getPackagename())){
				sbQuery.append("SELECT " + procedureDAO.getPackagename() + "." + procedureDAO.getName() + "(");
			}else{
				sbQuery.append("SELECT " + procedureDAO.getName() + "(");
			}
			
			List<InOutParameterDAO> inList = getInParameters();
			for(int i=0; i<inList.size(); i++) {
				InOutParameterDAO inOutParameterDAO = inList.get(i);
				if(i == (inList.size()-1)) sbQuery.append(String.format(":%s ", inOutParameterDAO.getName()));
				else sbQuery.append(String.format(":%s, ", inOutParameterDAO.getName()));
			}
			sbQuery.append(") from dual");
		} else {
			if(!"".equals(procedureDAO.getPackagename())){
				sbQuery.append("{call " + procedureDAO.getPackagename() + "." + procedureDAO.getName() + "(");
			}else{
				sbQuery.append("{call " + procedureDAO.getName() + "(");
			}
			// in script
			int intParamSize = getParametersCount();
			for (int i = 0; i < intParamSize; i++) {
				if (i == 0) sbQuery.append("?");
				else 		sbQuery.append(",?");
			}
			sbQuery.append(")}");
		}

		if(logger.isDebugEnabled()) logger.debug("Execute Procedure query is\t  " + sbQuery.toString());
		
		return sbQuery.toString();
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
			if (type.equals(listOutParamValues.get(i).getType()) && "OUT".equalsIgnoreCase(listOutParamValues.get(i).getType())) cnt++;
		}
		
		return cnt;
	}
	public int getParametersCount() throws Exception {
		int cnt = this.listInParamValues.size();
		
		for(int i=0;i< this.listOutParamValues.size();i++){
			if ("OUT".equalsIgnoreCase(listOutParamValues.get(i).getType())) cnt++;
		}
		
		return cnt;
	}
	
	/**
	 * 프로시저 결과가 cursor일때 결과를 담아줍니다.
	 * 
	 * @param rs
	 * @throws Exception
	 */
	protected void setResultCursor(String reqQuery, ResultSet rs, String strNullValue) throws Exception {
		Map<Integer, String> mapColumns = ResultSetUtils.getColumnName(rs);
		Map<Integer, String> mapTableColum = ResultSetUtils.getColumnTableName(userDB, rs);
		Map<Integer, Integer> mapColumnType = ResultSetUtils.getColumnType(rs.getMetaData()); 
		TadpoleResultSet sourceDataList = ResultSetUtils.getResultToList(rs, 1000, strNullValue);

		ResultSetUtilDTO resultSet = new ResultSetUtilDTO(
//				PublicTadpoleDefine.SQL_STATEMENTS_TYPE.PROCEDURE, 
				userDB, reqQuery, mapColumns, mapTableColum, mapColumnType, sourceDataList, strNullValue);
		addResultDAO(resultSet);
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
	protected void setResultNoCursor(String reqQuery, TadpoleResultSet sourceDataList, String strNullValue) throws Exception {
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
		
		Map<Integer, String> mapColumnTable = new HashMap<Integer, String>();
		mapColumnTable.put(0, "dumy");
		mapColumnTable.put(1, "dumy");
		mapColumnTable.put(2, "dumy");
		mapColumnTable.put(3, "dumy");
		mapColumnTable.put(4, "dumy");
		mapColumnTable.put(5, "dumy");
		
		ResultSetUtilDTO resultSet = new ResultSetUtilDTO(userDB, reqQuery, mapColumns, mapColumnTable, mapColumnType, sourceDataList, strNullValue);
		addResultDAO(resultSet);
	}
	
	protected void initResult() {
		this.resultDAO.clear();
	}
	
	/**
	 * @return the resultDAO
	 */
	public List<ResultSetUtilDTO> getResultDAO() {
		return resultDAO;
	}

	/**
	 * @param resultDAO the resultDAO to set
	 */
	public void addResultDAO(ResultSetUtilDTO resultDAO) {
		this.resultDAO.add(resultDAO);
	}

	/**
	 * executer
	 * 
	 * @param parameterList
	 * @return
	 */
	public abstract boolean exec(List<InOutParameterDAO> parameterList, String strNullValue) throws Exception ;
	
	/**
	 * @return the strOutput
	 */
	public String getStrOutput() {
		return strOutput;
	}

	/**
	 * @param strOutput the strOutput to set
	 */
	public void setStrOutput(String strOutput) {
		this.strOutput = strOutput;
	}

}
