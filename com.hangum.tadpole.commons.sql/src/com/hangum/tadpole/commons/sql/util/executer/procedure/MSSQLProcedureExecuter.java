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

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * oracle procedure executer
 * 
<pre>
Procedure sample....

create procedure p_test1( @param1   int ,@param2    int  = 2 OUTPUT, @ret   float out) as
   begin
      SET @ret = @param1 / @param2;
   END;

create procedure p_test2( @param1   int ,@param2    int  = 2 OUTPUT, @ret   float out) as
   begin      
      select * from sysobjects;      
   END;

CREATE PROCEDURE p_test3(@param1  INT,@param2 INT = 2 OUTPUT, @ret FLOAT OUT) AS
BEGIN
   SELECT    *      from sysobjects;      
   SELECT    *      FROM sysobjects;
end;

CREATE PROCEDURE p_test4 AS
BEGIN
   SELECT    *      from sysobjects;      
end;

</pre>
 * 
 * @author hangum
 * @author nilriri
 * 
 */

public class MSSQLProcedureExecuter extends ProcedureExecutor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MSSQLProcedureExecuter.class);

	/**
	 * 
	 * @param procedureDAO
	 * @param userDB
	 */
	public MSSQLProcedureExecuter(ProcedureFunctionDAO procedureDAO, UserDBDAO userDB) {
		super(procedureDAO, userDB);
	}
	
	@Override
	public boolean exec(List<InOutParameterDAO> parameterList)  throws Exception {
		initResult();
		
		java.sql.Connection javaConn = null;
		java.sql.CallableStatement cstmt = null;

		try {
			if(listOutParamValues == null) getOutParameters();

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			// make the script
			StringBuffer sbQuery = new StringBuffer("{call " + procedureDAO.getName() + "(");
			// in script
			int intParamSize = this.getParametersCount();
			for (int i = 0; i < intParamSize; i++) {
				if (i == 0) sbQuery.append("?");
				else 		sbQuery.append(",?");
			}
			sbQuery.append(")}");
			if(logger.isDebugEnabled()) logger.debug("Execute Procedure query is\t  " + sbQuery.toString());
			
			// set prepare call
			cstmt = javaConn.prepareCall(sbQuery.toString());
			
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
			ResultSet rs = cstmt.getResultSet();
			if (rs != null){
				setResultCursor(rs);
				isCursor = true;
				
				// mssql은 result set이 여러개 리턴될 수 있음.
				while(cstmt.getMoreResults()){
					setResultCursor(cstmt.getResultSet());
				}
			}else{
				for (int i = 0; i < listOutParamValues.size(); i++) {				
					InOutParameterDAO dao = listOutParamValues.get(i);
					
					Object obj = cstmt.getObject(dao.getOrder());
					// 실행결과가 String이 아닌경우 Type Cast가 필요함.... 현재는 무조건 String 으로...
					if (obj!=null){
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
			
			return true;
		} catch (Exception e) {
			logger.error("ProcedureExecutor executing error", e);
			throw e;
		} finally {
			try { if(cstmt != null) cstmt.close(); } catch (Exception e) {  }
			try { if(javaConn != null) javaConn.close(); } catch (Exception e) { }
		}
	}

}
