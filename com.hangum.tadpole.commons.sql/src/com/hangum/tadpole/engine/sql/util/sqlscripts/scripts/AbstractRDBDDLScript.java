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
package com.hangum.tadpole.engine.sql.util.sqlscripts.scripts;

import java.util.List;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TriggerDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;

/**
 * 각 디비별 object script 를 가져오기 위한 클래스.
 * 
 * @author hangum
 *
 */
public abstract class AbstractRDBDDLScript {
	protected UserDBDAO userDB;
	protected OBJECT_TYPE actionType;

	/**'
	 * 각 디비별로 데이터를 가져옵니다.
	 * 
	 * @param userDB
	 * @param actionType
	 */
	public AbstractRDBDDLScript(UserDBDAO userDB, PublicTadpoleDefine.OBJECT_TYPE actionType) {
		this.userDB = userDB;
		this.actionType = actionType;
	}
	
	/**
	 * table script
	 * 
	 * @param tableDAO
	 * @return
	 * @throws Exception
	 */
	public abstract String getTableScript(TableDAO tableDAO) throws Exception;
	
	/**
	 * view script
	 * 
	 * @param strName
	 * @return
	 * @throws Exception
	 */
	public abstract String getViewScript(String strName) throws Exception;
	
	/**
	 * index script
	 * 
	 * @param indexDAO
	 * @return
	 * @throws Exception
	 */
	public abstract String getIndexScript(InformationSchemaDAO indexDAO) throws Exception;
	
	/**
	 * function script
	 * 
	 * @param functionDAO
	 * @return
	 * @throws Exception
	 */
	public abstract String getFunctionScript(ProcedureFunctionDAO functionDAO) throws Exception;
	
	/**
	 * procedure script
	 * 
	 * @param procedureDAO
	 * @return
	 * @throws Exception
	 */
	public abstract String getProcedureScript(ProcedureFunctionDAO procedureDAO) throws Exception;
	
	/**
	 * Define procedure in parameter
	 * 
	 * @param procedureDAO
	 * @return
	 * @throws Exception
	 */
	public abstract List<InOutParameterDAO> getProcedureInParamter(ProcedureFunctionDAO procedureDAO) throws Exception;
	
	/**
	 * Define procedure  out parameter
	 * 
	 * @param procedureDAO
	 * @return
	 * @throws Exception
	 */
	public abstract List<InOutParameterDAO> getProcedureOutParamter(ProcedureFunctionDAO procedureDAO) throws Exception;
	
	/**
	 * trigger script
	 * 
	 * @param triggerDAO
	 * @return
	 * @throws Exception
	 */
	public abstract String getTriggerScript(TriggerDAO triggerDAO) throws Exception;
	
}
