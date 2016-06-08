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

import org.apache.log4j.Logger;

import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;

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
				sbQuery.append("SELECT " + procedureDAO.getPackagename() + "." + procedureDAO.getName() + "(");
			}else{
				sbQuery.append("SELECT " + procedureDAO.getName() + "(");
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
					sbQuery.append(String.format("SET @%s = 0;\n", inOutParameterDAO.getName()));
				} else {
					sbQuery.append(String.format("SET @%s = \"\";\n", inOutParameterDAO.getName()));
				}
			}
			
			// 프로시저 본체 만들기.
			if(!"".equals(procedureDAO.getPackagename())){
				sbQuery.append(String.format("CALL %s.%s(", procedureDAO.getPackagename(), procedureDAO.getName()));
			}else{
				sbQuery.append(String.format("CALL %s(", procedureDAO.getName()));
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
				sbQuery.append(String.format("SELECT @%s;\n", inOutParameterDAO.getName()));
			}
		}

		if(logger.isDebugEnabled()) logger.debug("Execute Procedure query is\t  " + sbQuery.toString());
		
		return sbQuery.toString();
	}
	
	@Override
	public boolean exec(List<InOutParameterDAO> parameterList, String strNullValue)  throws Exception {
		throw new Exception("Do now use the method");
	}

}
