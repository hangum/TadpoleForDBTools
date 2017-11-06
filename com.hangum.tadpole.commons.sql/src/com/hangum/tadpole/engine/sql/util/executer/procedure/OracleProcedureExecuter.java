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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.OracleDbmsOutputUtil;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;

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
	public boolean exec(final List<InOutParameterDAO> parameterList)  throws Exception {
		initResult();
		
		java.sql.Connection javaConn = null;
		java.sql.CallableStatement cstmt = null;
		java.sql.PreparedStatement pstmt = null;
		ResultSet rs = null;

		OracleDbmsOutputUtil dbmsOutput = null;
		try {
			if(listOutParamValues == null) getOutParameters();

			javaConn = TadpoleSQLManager.getConnection(userDB);
			
			try {
				dbmsOutput = new OracleDbmsOutputUtil( javaConn );
				dbmsOutput.enable(1000000);
			} catch(SQLException e) { 
				logger.error("dbmsoutput exception", e); 
			}
			 
			// make the script
			String strExecuteScript = getMakeExecuteScript(parameterList);
			
			if (StringUtils.startsWithIgnoreCase(strExecuteScript, "SELECT")){
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
				rs = pstmt.executeQuery();
				setResultCursor(userDB, strExecuteScript, rs);
			}else{
			
				// set prepare call
				cstmt = javaConn.prepareCall(strExecuteScript);
				
				// Set input value
				for (InOutParameterDAO inOutParameterDAO : parameterList) {
					
					if(logger.isDebugEnabled()) logger.debug("Input Parameter " + inOutParameterDAO.getOrder() + " JavaType is " + RDBTypeToJavaTypeUtils.getJavaType(inOutParameterDAO.getRdbType()));
					if(logger.isDebugEnabled()) logger.debug("Input Parameter " + inOutParameterDAO.getOrder() + " Value is " + inOutParameterDAO.getValue() );
					
					if (RDBTypeToJavaTypeUtils.isCharType( inOutParameterDAO.getRdbType() )){
						cstmt.setObject(inOutParameterDAO.getOrder(), inOutParameterDAO.getValue() );						
					}else if (RDBTypeToJavaTypeUtils.isNumberType( inOutParameterDAO.getRdbType() )){
						try{
							float v = Float.valueOf(inOutParameterDAO.getValue());
							cstmt.setObject(inOutParameterDAO.getOrder(), v );
						}catch(Exception e){
							// 숫자 타입일 경우 형변환 오류가 생기거나 기타 다른 오류가 발생할 경우 0을 기본값으로 사용하도록 한다.
							cstmt.setObject(inOutParameterDAO.getOrder(), 0 );
						}						
					}else if (RDBTypeToJavaTypeUtils.isDateType( inOutParameterDAO.getRdbType() )){
						//사용자가 인수값을 입력했을때만 처리한다.
						if ( StringUtils.isNotEmpty(inOutParameterDAO.getValue()) ){						
							//TODO:Date타입의 포맷은 사용자 환경설정값을 이용해야 하나? 
							SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date to = transFormat.parse(inOutParameterDAO.getValue());
							if(logger.isDebugEnabled()) logger.debug("Input Parameter " + inOutParameterDAO.getOrder() + " Value is " + to );
							cstmt.setObject(inOutParameterDAO.getOrder(), new java.sql.Date(to.getTime()) );
						}
					}//isDateType
				}//for 
	
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
					if(logger.isDebugEnabled()) logger.debug("Execute Procedure result " + dao.getName() + "=" + cstmt.getString(dao.getOrder()));
					
					Object obj = cstmt.getObject(dao.getOrder());
					// 실행결과가 String이 아닌경우 Type Cast가 필요함.... 현재는 무조건 String 으로...
					if (obj!=null){
						if ("SYS_REFCURSOR".equals(dao.getRdbType())){
							isCursor = true;
							rs = (ResultSet)obj;
							setResultCursor(userDB, strExecuteScript, rs);
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
					
					setResultNoCursor(strExecuteScript, new TadpoleResultSet(sourceDataList));
				}
			}
			try { dbmsOutput.show(); } catch(SQLException e) { logger.error("dbmsoutput exception", e); }
			setStrOutput(dbmsOutput.getOutput());
			
			return true;
		} catch (Exception e) {
			logger.error("ProcedureExecutor executing error", e);
			throw e;
		} finally {
			try { if(rs != null) rs.close(); } catch (Exception e) {  }
			try { if(pstmt != null) pstmt.close(); } catch (Exception e) {  }
			try { if(cstmt != null) cstmt.close(); } catch (Exception e) {  }
			try { if(dbmsOutput != null) dbmsOutput.close(); } catch (Exception e) {  }
			try { if(javaConn != null) javaConn.close(); } catch (Exception e) { }
		}
	}

}
