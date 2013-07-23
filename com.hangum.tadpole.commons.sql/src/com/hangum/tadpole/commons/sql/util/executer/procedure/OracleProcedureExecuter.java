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
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * oracle procedure executer
 * 
 * <pre>
 * 
 * Procedure sample....

	CREATE OR REPLACE PROCEDURE p_add (rowcnt        IN     INT
	                                  ,retcode          OUT INT
	                                  ,retmsg           OUT VARCHAR2
	                                  ,cursorParam      OUT SYS_REFCURSOR)
	IS
	BEGIN
	   OPEN cursorParam FOR
	      SELECT table_name
	        FROM user_tables
	       WHERE ROWNUM <= rowcnt;
	
	   retcode := SQLCODE;
	   retmsg := SQLERRM;
	END;
	</pre>
 * 
 * @author hangum
 * @author nilriri
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
	public boolean exec(TableViewer viewer, List<InOutParameterDAO> parameterList) {
		java.sql.Connection javaConn = null;
		java.sql.CallableStatement cstmt = null;

		try {
			if(listOutParamValues == null) getOutParameters();

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			// make the script
			StringBuffer sbQuery = new StringBuffer("{call " + procedureDAO.getName() + "(");
			// in script
			int intParamSize = this.getParametersCount();// listOutParamValues.size() + listInParamValues.size();
			for (int i = 0; i < intParamSize; i++) {
				if (i == 0) sbQuery.append("?");
				else 		sbQuery.append(",?");
			}
			sbQuery.append(")}");
			if(logger.isDebugEnabled()) logger.debug("Execute Procedure query is\t  " + sbQuery.toString());
			
			
			cstmt = javaConn.prepareCall(sbQuery.toString());
			
			// Set input value
			for (InOutParameterDAO inOutParameterDAO : parameterList) {
				if(logger.isDebugEnabled()) logger.debug("Parameter " + inOutParameterDAO.getOrder() + " Value is " + inOutParameterDAO.getValue());
				if (null==inOutParameterDAO.getValue() || "".equals(inOutParameterDAO.getValue())){
					MessageDialog.openError(null, "Error", inOutParameterDAO.getName() + " parameters are required.");
					return false;
				}
				cstmt.setObject(inOutParameterDAO.getOrder(), inOutParameterDAO.getValue());
			}

			// Set the OUT Parameter
			for (int i = 0; i < listOutParamValues.size(); i++) {
				InOutParameterDAO dao = listOutParamValues.get(i);
				
				if(logger.isDebugEnabled()) logger.debug("Out Parameter " + dao.getOrder() + " JavaType is " + RDBTypeToJavaTypeUtils.getJavaType(dao.getRdbType()));
				
				cstmt.registerOutParameter(dao.getOrder(), RDBTypeToJavaTypeUtils.getJavaType(dao.getRdbType()));
				
			}
			cstmt.execute();

			for (int i = 0; i < listOutParamValues.size(); i++) {
				InOutParameterDAO dao = listOutParamValues.get(i);
				logger.debug("Execute Procedure result " + dao.getName() + "=" + cstmt.getString(dao.getOrder()));
				
				Object obj = cstmt.getObject(dao.getOrder());
				// 실행결과가 String이 아닌경우 Type Cast가 필요함.... 현재는 무조건 String 으로...
				if (obj!=null){
					if ("SYS_REFCURSOR".equals(dao.getRdbType())){
						ResultSet rs = (ResultSet)obj;
						while(rs.next()){
							logger.debug("Result Set = " + rs.getString(1));				
						}
					}else{
						dao.setValue(obj.toString());
					}
				}
			}
			
			viewer.setInput(listOutParamValues);
			viewer.refresh();
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
