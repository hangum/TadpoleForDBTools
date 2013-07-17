package com.hangum.tadpole.commons.sql.util.sqlscripts.scripts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.mysql.TriggerDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * SQLite ddl script
 * 
 * @author hangum
 *
 */
public class SQLiteDDLScript extends AbstractRDBDDLScript {
	
	public SQLiteDDLScript(UserDBDAO userDB, PublicTadpoleDefine.DB_ACTION actionType) {
		super(userDB, actionType);
	}

	@Override
	public String getTableScript(TableDAO tableDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		return ""+client.queryForObject("getTableScript", tableDAO.getName());
	}

	@Override
	public String getViewScript(String strName) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		return ""+client.queryForObject("getViewScript", strName);
	}

	@Override
	public String getIndexScript(InformationSchemaDAO indexDAO)	throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("index_name", indexDAO.getINDEX_NAME());
		paramMap.put("table_name", indexDAO.getTABLE_NAME());
		
		return ""+client.queryForObject("getIndexScript", paramMap);
	}

	@Override
	public String getFunctionScript(ProcedureFunctionDAO functionDAO) throws Exception {
		throw new Exception("Not support Database");
	}

	@Override
	public String getProcedureScript(ProcedureFunctionDAO procedureDAO)
			throws Exception {
		throw new Exception("Not support Database");
	}
	
	@Override
	public String getTriggerScript(TriggerDAO triggerDAO) throws Exception {
		return triggerDAO.getStatement();
	}

	@Override
	public List<InOutParameterDAO> getProcedureInParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		throw new Exception("Not support Database");
	}

	@Override
	public List<InOutParameterDAO> getProcedureOutParamter(ProcedureFunctionDAO procedureDAO) throws Exception {
		throw new Exception("Not support Database");
	}

}
