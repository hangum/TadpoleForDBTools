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

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * oracle procedure executer
 * 
 * @author hangum
 *
 */
public class OracleProcedureExecuter extends ProcedureExecutor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OracleProcedureExecuter.class);

	/**
	 * 
	 * @param procedureDAO
	 * @param userDB
	 */
	public OracleProcedureExecuter(ProcedureFunctionDAO procedureDAO, UserDBDAO userDB) {
		super(procedureDAO, userDB);
	}
	
	@Override
	public boolean exec(List<InOutParameterDAO> parameterList) {
		java.sql.Connection javaConn = null;
		java.sql.CallableStatement cstmt = null;

		try {
			if(listOutParamValues == null) getOutParameters();

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			// make the script
			StringBuffer sbQuery = new StringBuffer("{call " + procedureDAO.getName() + "(");
			// in script
			int intParamSize = listOutParamValues.size() + listInParamValues.size();
			for (int i = 0; i < intParamSize; i++) {
				if (i == 0) sbQuery.append("?");
				else 		sbQuery.append(",?");
			}
			sbQuery.append(")}");
			if(logger.isDebugEnabled()) logger.debug("Execute Procedure query is\t  " + sbQuery.toString());
			cstmt = javaConn.prepareCall(sbQuery.toString());
			
			// Set input value
			for (InOutParameterDAO inOutParameterDAO : parameterList) {
				cstmt.setObject(inOutParameterDAO.getOrder(), inOutParameterDAO.getValue());
			}

			// Set the OUT Parameter
			for (int i = 0; i < listOutParamValues.size(); i++) {
				InOutParameterDAO dao = listInParamValues.get(i);
				cstmt.registerOutParameter(dao.getOrder(), RDBTypeToJavaTypeUtils.getJavaType(dao.getJavaType()));
			}

			cstmt.execute();

			for (int i = 0; i < listOutParamValues.size(); i++) {
				InOutParameterDAO dao = listOutParamValues.get(i);
				logger.debug("Execute Procedure result " + dao.getName() + "=" + cstmt.getString(dao.getOrder()));
			}
			MessageDialog.openInformation(null, "Information", "Execute Compete.");

			return true;
		} catch (Exception e) {
			logger.error("ProcedureExecutor executing error", e);
			MessageDialog.openError(null, "Error", e.getMessage());
			return false;
		} finally {
			try { if(cstmt != null) cstmt.close(); } catch (Exception e) {  }
			try { if(javaConn != null) javaConn.close(); } catch (Exception e) { }
		}
	}

}
