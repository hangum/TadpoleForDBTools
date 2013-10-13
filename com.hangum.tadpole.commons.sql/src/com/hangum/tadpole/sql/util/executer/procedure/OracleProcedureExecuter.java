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

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.RDBTypeToJavaTypeUtils;
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
	public boolean exec(List<InOutParameterDAO> parameterList)  throws Exception {
		initResult();
		
		java.sql.Connection javaConn = null;
		java.sql.CallableStatement cstmt = null;
		java.sql.PreparedStatement pstmt = null;

		try {
			if(listOutParamValues == null) getOutParameters();

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			// make the script
			String strExecuteScript = getMakeExecuteScript();
			
			if (strExecuteScript.startsWith("select")){
				// function execute...
				
				pstmt = javaConn.prepareStatement(strExecuteScript);
				
				for (InOutParameterDAO inOutParameterDAO : parameterList) {
					pstmt.setObject(inOutParameterDAO.getOrder(), inOutParameterDAO.getValue());
				}
	
				// Set the OUT Parameter
				for (int i = 0; i < listOutParamValues.size(); i++) {
					InOutParameterDAO dao = listOutParamValues.get(i);
					//pstmt.registerOutParameter(dao.getOrder(), RDBTypeToJavaTypeUtils.getJavaType(dao.getRdbType()));
					pstmt.setObject(dao.getOrder(), "");
				}
				ResultSet rs = pstmt.executeQuery();
				setResultCursor(rs);
			}else{
			
				// set prepare call
				cstmt = javaConn.prepareCall(strExecuteScript);
				
				// Set input value
				for (InOutParameterDAO inOutParameterDAO : parameterList) {
	//				if(logger.isDebugEnabled()) logger.debug("Parameter " + inOutParameterDAO.getOrder() + " Value is " + inOutParameterDAO.getValue());
	//				if (null==inOutParameterDAO.getValue() || "".equals(inOutParameterDAO.getValue())){
	//					MessageDialog.openError(null, "Error", inOutParameterDAO.getName() + " parameters are required.");
	//					return false;
	//				}
					cstmt.setObject(inOutParameterDAO.getOrder(), inOutParameterDAO.getValue());
				}
	
				// Set the OUT Parameter
				for (int i = 0; i < listOutParamValues.size(); i++) {
					InOutParameterDAO dao = listOutParamValues.get(i);
					
					if(logger.isDebugEnabled()) logger.debug("Out Parameter " + dao.getOrder() + " JavaType is " + RDBTypeToJavaTypeUtils.getJavaType(dao.getRdbType()));
					
					cstmt.registerOutParameter(dao.getOrder(), RDBTypeToJavaTypeUtils.getJavaType(dao.getRdbType()));
				}
				cstmt.execute();
				
				//
				// 결과 set
				//
				// 결과가 cursor가 아닌경우 결과를 담기위한 list
				
				// boolean is cursor
				boolean isCursor = false;
				for (int i = 0; i < listOutParamValues.size(); i++) {				
					InOutParameterDAO dao = listOutParamValues.get(i);
					logger.debug("Execute Procedure result " + dao.getName() + "=" + cstmt.getString(dao.getOrder()));
					
					Object obj = cstmt.getObject(dao.getOrder());
					// 실행결과가 String이 아닌경우 Type Cast가 필요함.... 현재는 무조건 String 으로...
					if (obj!=null){
						if ("SYS_REFCURSOR".equals(dao.getRdbType())){
							isCursor = true;
							ResultSet rs = (ResultSet)obj;
							setResultCursor(rs);
							// cursor의 결과 리턴은 항상 1개입니다.
						}else{
							dao.setValue(obj.toString());
						}
					}
					
				}
				
				if(!isCursor) {
					List<Map<Integer, Object>> sourceDataList = new ArrayList<Map<Integer,Object>>();
					Map<Integer, Object> tmpRow = null;
	
					for (int i = 0; i < listOutParamValues.size(); i++) {
						InOutParameterDAO dao = listOutParamValues.get(i);
						tmpRow = new HashMap<Integer, Object>();
						
						tmpRow.put(0, ""+dao.getOrder());
						tmpRow.put(1, ""+dao.getName());
						tmpRow.put(2, ""+dao.getType());
						tmpRow.put(3, ""+dao.getRdbType());
						tmpRow.put(4, ""+dao.getLength());
						tmpRow.put(5, ""+dao.getValue());
						
						sourceDataList.add(tmpRow);
					}
					
					setResultNoCursor(sourceDataList);
				}
			}
			
			return true;
		} catch (Exception e) {
			logger.error("ProcedureExecutor executing error", e);
			throw e;
		} finally {
			try { if(pstmt != null) pstmt.close(); } catch (Exception e) {  }
			try { if(cstmt != null) cstmt.close(); } catch (Exception e) {  }
			try { if(javaConn != null) javaConn.close(); } catch (Exception e) { }
		}
	}

}
