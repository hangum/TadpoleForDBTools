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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;

import com.hangum.tadpole.commons.sql.util.sqlscripts.DDLScriptManager;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;

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
	
	protected UserDBDAO userDB;
	protected List<InOutParameterDAO> listInParamValues;
	protected List<InOutParameterDAO> listOutParamValues;
	
	protected ProcedureFunctionDAO procedureDAO;

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
	 * executer
	 * 
	 * @param parameterList
	 * @return
	 */
	public abstract boolean exec(TableViewer viewer, List<InOutParameterDAO> parameterList);

}
