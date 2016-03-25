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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * oracle procedure executer
 * 
 * ex) http://www.postgresql.org/docs/9.1/static/xfunc-sql.html
 * 
 * @author hangum
 * @author nilriri
 */

public class PostgreSQLProcedureExecuter extends ProcedureExecutor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PostgreSQLProcedureExecuter.class);

	/**
	 * 
	 * @param procedureDAO
	 * @param userDB
	 */
	public PostgreSQLProcedureExecuter(ProcedureFunctionDAO procedureDAO, UserDBDAO userDB) {
		super(procedureDAO, userDB);
	}
	
	/**
	 * execute script
	 */
	public String getMakeExecuteScript() throws Exception {
		StringBuffer sbQuery = new StringBuffer();
		if(!"".equals(procedureDAO.getPackagename())){
			sbQuery.append("SELECT " + procedureDAO.getPackagename() + "." + procedureDAO.getSysName() + "(");
		}else{
			sbQuery.append("SELECT " + procedureDAO.getSysName() + "(");
		}
		
		List<InOutParameterDAO> inList = getInParameters();
		InOutParameterDAO inOutParameterDAO = inList.get(0);
		String[] inParams = StringUtils.split(inOutParameterDAO.getRdbType(), ",");
		for(int i=0; i<inParams.length; i++) {
			String name = StringUtils.trimToEmpty(inParams[i]);
			
			if(i == (inParams.length-1)) sbQuery.append(String.format(":%s", name));
			else sbQuery.append(String.format(":%s, ", name));
		}
		sbQuery.append(");");

		if(logger.isDebugEnabled()) logger.debug("Execute Procedure query is\t  " + sbQuery.toString());
		
		return sbQuery.toString();
	}
	
	@Override
	public boolean exec(List<InOutParameterDAO> parameterList)  throws Exception {
		throw new Exception("Do now use the method");
	}
	
//	@Override
//	public boolean exec(List<InOutParameterDAO> parameterList)  throws Exception {
//		initResult();
//		
//		java.sql.Connection javaConn = null;
//		java.sql.CallableStatement cstmt = null;
//
//		try {
//			if(listOutParamValues == null) getOutParameters();
//
//			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
//			javaConn = client.getDataSource().getConnection();
//			
//			
//			// make the script
//			String[] arrProcedureName = StringUtils.split(procedureDAO.getName(), ".");
//			String strProcName = "[" + arrProcedureName[0] + "].[" + arrProcedureName[1] + "]";
//			
//			StringBuffer sbQuery = new StringBuffer("{call " + strProcName + "(");
//			// in script
//			int intParamSize = this.getParametersCount();
//			for (int i = 0; i < intParamSize; i++) {
//				if (i == 0) sbQuery.append("?");
//				else 		sbQuery.append(",?");
//			}
//			sbQuery.append(")}");
//			if(logger.isDebugEnabled()) logger.debug("Execute Procedure query is\t  " + sbQuery.toString());
//			
//			// set prepare call
//			cstmt = javaConn.prepareCall(sbQuery.toString());
//			
//			// Set input value
//			for (InOutParameterDAO inOutParameterDAO : parameterList) {
////				if(logger.isDebugEnabled()) logger.debug("Parameter " + inOutParameterDAO.getOrder() + " Value is " + inOutParameterDAO.getValue());
////				if (null==inOutParameterDAO.getValue() || "".equals(inOutParameterDAO.getValue())){
////					MessageDialog.openError(null, Messages.get().Error, inOutParameterDAO.getName() + " parameters are required.");
////					return false;
////				}
//				cstmt.setObject(inOutParameterDAO.getOrder(), inOutParameterDAO.getValue());
//			}
//
//			// Set the OUT Parameter
//			for (int i = 0; i < listOutParamValues.size(); i++) {
//				InOutParameterDAO dao = listOutParamValues.get(i);
//				
//				if(logger.isDebugEnabled()) logger.debug("Out Parameter " + dao.getOrder() + " JavaType is " + RDBTypeToJavaTypeUtils.getJavaType(dao.getRdbType()));
//				
//				cstmt.registerOutParameter(dao.getOrder(), RDBTypeToJavaTypeUtils.getJavaType(dao.getRdbType()));
//			}
//			cstmt.execute();
//			
//			//
//			// 결과 set
//			//
//			// 결과가 cursor가 아닌경우 결과를 담기위한 list
//			
//			// boolean is cursor
//			boolean isCursor = false;
//			ResultSet rs = cstmt.getResultSet();
//			if (rs != null){
//				setResultCursor(rs);
//				isCursor = true;
//				
//				// mssql은 result set이 여러개 리턴될 수 있음.
//				while(cstmt.getMoreResults()){
//					setResultCursor(cstmt.getResultSet());
//				}
//			}else{
//				for (int i = 0; i < listOutParamValues.size(); i++) {				
//					InOutParameterDAO dao = listOutParamValues.get(i);
//					
//					Object obj = cstmt.getObject(dao.getOrder());
//					// 실행결과가 String이 아닌경우 Type Cast가 필요함.... 현재는 무조건 String 으로...
//					if (obj!=null){
//						dao.setValue(obj.toString());
//					}
//					
//				}
//			}
//				
//			if(!isCursor) {
//				List<Map<Integer, Object>> sourceDataList = new ArrayList<Map<Integer,Object>>();
//				Map<Integer, Object> tmpRow = null;
//
//				for (int i = 0; i < listOutParamValues.size(); i++) {
//					InOutParameterDAO dao = listOutParamValues.get(i);
//					tmpRow = new HashMap<Integer, Object>();
//					
//					tmpRow.put(0, ""+dao.getOrder());
//					tmpRow.put(1, ""+dao.getName());
//					tmpRow.put(2, ""+dao.getType());
//					tmpRow.put(3, ""+dao.getRdbType());
//					tmpRow.put(4, ""+dao.getLength());
//					tmpRow.put(5, ""+dao.getValue());
//					
//					sourceDataList.add(tmpRow);
//				}
//				
//				setResultNoCursor(new TadpoleResultSet(sourceDataList));
//			}
//			
//			return true;
//		} catch (Exception e) {
//			logger.error("ProcedureExecutor executing error", e);
//			throw e;
//		} finally {
//			try { if(cstmt != null) cstmt.close(); } catch (Exception e) {  }
//			try { if(javaConn != null) javaConn.close(); } catch (Exception e) { }
//		}
//	}

}
