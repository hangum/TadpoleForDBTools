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
package com.hangum.tadpole.sql.util.executer.procedure;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.ResultSetUtilDTO;
import com.hangum.tadpole.sql.util.ResultSetUtils;
import com.hangum.tadpole.sql.util.sqlscripts.DDLScriptManager;

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
	protected List<ResultSetUtilDTO> resultDAO = new ArrayList<ResultSetUtilDTO>();

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
	protected String getMakeExecuteScript() throws Exception {
		StringBuffer sbQuery = new StringBuffer();
		if ("FUNCTION".equals(procedureDAO.getType())){
			if(!"".equals(procedureDAO.getPackagename())){
				sbQuery.append("select " + procedureDAO.getPackagename() + "." + procedureDAO.getName() + "(");
			}else{
				sbQuery.append("select " + procedureDAO.getName() + "(");
			}
			
			int intParamSize = getParametersCount();
			for (int i = 0; i < intParamSize; i++) {
				if (i == 0) sbQuery.append("?");
				else 		sbQuery.append(",?");
			}
			sbQuery.append(") as result from dual ");
		}else{
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

		ResultSetUtilDTO resultSet = new ResultSetUtilDTO(mapColumns, mapColumnType, sourceDataList);
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
		
		ResultSetUtilDTO resultSet = new ResultSetUtilDTO(mapColumns, mapColumnType, sourceDataList);
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
	public abstract boolean exec(List<InOutParameterDAO> parameterList) throws Exception ;

}
