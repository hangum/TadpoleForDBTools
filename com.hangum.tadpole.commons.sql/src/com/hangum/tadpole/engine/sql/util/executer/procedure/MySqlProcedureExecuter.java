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

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * mysql procedure executer
 * 
 * <pre>
 * 
 * Procedure sample....

create procedure p_calc2(in pram1  int, in param2  int, inout param3 int, out ret  float) 
begin
  set @ret =   @param1 / @param2;
end;

create procedure p_calc3(in pram1  int, in param2  int, inout param3 int, out ret  float) 
begin
   select * from information_schema.tables;
end;

	</pre>
 * 
 * @author hangum
 * @author nilriri
 * 
 */

public class MySqlProcedureExecuter extends ProcedureExecutor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MySqlProcedureExecuter.class);

	/**
	 * 
	 * @param procedureDAO
	 * @param userDB
	 */
	public MySqlProcedureExecuter(ProcedureFunctionDAO procedureDAO, UserDBDAO userDB) {
		super(procedureDAO, userDB);
	}
	
	/**
	 * execute script
	 */
	public String getMakeExecuteScript() throws Exception {
		StringBuffer sbQuery = new StringBuffer();
		if ("FUNCTION".equalsIgnoreCase(procedureDAO.getType())){
			if(!"".equals(procedureDAO.getPackagename())){
				sbQuery.append("select " + procedureDAO.getPackagename() + "." + procedureDAO.getName() + "(");
			}else{
				sbQuery.append("select " + procedureDAO.getName() + "(");
			}
			
			List<InOutParameterDAO> inList = getInParameters();
			for(int i=0; i<inList.size(); i++) {
				InOutParameterDAO inOutParameterDAO = inList.get(i);
				if(i == (inList.size()-1)) sbQuery.append(String.format(":%s", inOutParameterDAO.getName()));
				else sbQuery.append(String.format(":%s, ", inOutParameterDAO.getName()));
			}
			sbQuery.append(");");
		}else{
			
			// output parameter 
			for (InOutParameterDAO inOutParameterDAO : getOutParameters()) {
				if(RDBTypeToJavaTypeUtils.isNumberType(inOutParameterDAO.getRdbType())) {
					sbQuery.append(String.format("set @%s = 0;\n", inOutParameterDAO.getName()));
				} else {
					sbQuery.append(String.format("set @%s = \"\";\n", inOutParameterDAO.getName()));
				}
			}
			
			// 프로시저 본체 만들기.
			if(!"".equals(procedureDAO.getPackagename())){
				sbQuery.append(String.format("call %s.%s(", procedureDAO.getPackagename(), procedureDAO.getName()));
			}else{
				sbQuery.append(String.format("call %s(", procedureDAO.getName()));
			}
			
			// in 설정
			List<InOutParameterDAO> inList = getInParameters();
			for(int i=0; i<inList.size(); i++) {
				InOutParameterDAO inOutParameterDAO = inList.get(i);
				if(i == (inList.size()-1)) sbQuery.append(String.format(":%s", inOutParameterDAO.getName()));
				else sbQuery.append(String.format(":%s, ", inOutParameterDAO.getName()));
			}
			
			// out 설정
			List<InOutParameterDAO> outList = getOutParameters();
			if(!inList.isEmpty() && !outList.isEmpty()) sbQuery.append(", ");
			for(int i=0; i<outList.size(); i++) {
				InOutParameterDAO inOutParameterDAO = outList.get(i);
				if(i != (outList.size()-1)) sbQuery.append(String.format("@%s,", inOutParameterDAO.getName()));
				else sbQuery.append(String.format("@%s",inOutParameterDAO.getName()));
			}
			sbQuery.append(");\n");

			// out 출력
			for (InOutParameterDAO inOutParameterDAO : getOutParameters()) {
				sbQuery.append(String.format("select @%s;\n", inOutParameterDAO.getName()));
			}
		}

		if(logger.isDebugEnabled()) logger.debug("Execute Procedure query is\t  " + sbQuery.toString());
		
		return sbQuery.toString();
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
			
			if (cstmt.getResultSet()!=null){
				setResultCursor(cstmt.getResultSet());
				isCursor = true;
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
				
				setResultNoCursor(new TadpoleResultSet(sourceDataList));
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
