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
package com.hangum.tadpole.commons.sql.util.executer;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.util.sqlscripts.DDLScriptManager;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * rdb procedure executer.
 * 
 * @author hangum
 *
 */
public class ProcedureExecutor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ProcedureExecutor.class);
	
	private UserDBDAO userDB;
	private List<InOutParameterDAO> listInParamValues;
	private List<InOutParameterDAO> listOutParamValues;
	
	private ProcedureFunctionDAO procedureDAO;

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
	 * exec procedure
	 * 
	 * @return
	 */
	public boolean exec(List<InOutParameterDAO> parameterList) {
		java.sql.Connection javaConn = null;
		java.sql.CallableStatement cstmt = null;

		try {
			if(listOutParamValues == null) getInParameters();

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			// make the script
			StringBuffer query = new StringBuffer("{call " + procedureDAO.getName() + "(");
			StringBuffer params = new StringBuffer();

			// in script
			for (int i = 0; i < listInParamValues.size(); i++) {
				if (i == 0) {
					params.append("?");
				} else {
					params.append(",?");
				}
			}

			query.append(params.toString() + ")}");
			cstmt = javaConn.prepareCall(query.toString());
			
			if(logger.isDebugEnabled()) {
				logger.debug("[make procedure query]" + query.toString());
			}
			
			// set the value
			for (InOutParameterDAO inOutParameterDAO : parameterList) {
				cstmt.setObject(inOutParameterDAO.getOrder(), inOutParameterDAO.getValue());
			}

			// Set the OUT Param
			for (int i = 0; i < listOutParamValues.size(); i++) {
				InOutParameterDAO dao = listInParamValues.get(i);

//				if (StringUtils.equalsIgnoreCase(dao.getType(), "OUT")) {
					cstmt.registerOutParameter(dao.getOrder(), Types.VARCHAR);
//				} else if (StringUtils.equalsIgnoreCase(dao.getType(), "IN OUT")) {
//					cstmt.setObject(dao.getOrder(), dao.getValue());
//					cstmt.registerOutParameter(dao.getOrder(), Types.VARCHAR);
//				} else {
//					cstmt.setObject(dao.getOrder(), dao.getValue());
//				}
			}

			logger.debug("Execute Procedure query is " + query.toString());

			cstmt.execute();
			javaConn.commit();

			for (int i = 0; i < listOutParamValues.size(); i++) {
				InOutParameterDAO dao = listOutParamValues.get(i);

				if (StringUtils.contains(dao.getType(), "OUT")) {
					// dao.getType에 따라서 분리해야함.
					logger.debug("Execute Procedure result " + dao.getName() + "=" + cstmt.getString(dao.getOrder()));
				}
			}
			MessageDialog.openInformation(null, "Information", "Execute Compete.");

			return true;

		} catch (Exception e) {
			logger.error("ProcedureExecutor executing error", e);
			MessageDialog.openError(null, "Error", e.getMessage());
			return false;
		} finally {
			try {
				cstmt.close();
			} catch (Exception e) {
			}
			try {
				javaConn.close();
			} catch (Exception e) {
			}
		}
	}

}
