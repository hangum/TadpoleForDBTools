package com.hangum.tadpole.engine.sql.util.sqlscripts.scripts;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Altibase ddl script
 * 
 * @author hangum
 *
 */
public class AltibaseDDLScript extends MySqlDDLScript {

	public AltibaseDDLScript(UserDBDAO userDB, OBJECT_TYPE actionType) {
		super(userDB, actionType);
	}

	
	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getViewScript(java.lang.String)
	 */
	@Override
	public String getViewScript(String strName) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> parameters = new HashMap<String, String>(2);
		parameters.put("object_definer", StringUtils.substringBefore(strName, "."));
		parameters.put("object_name", StringUtils.substringAfter(strName, "."));
		
		Map srcList = (HashMap)client.queryForObject("getViewScript", parameters);
		String strSource = ""+srcList.get("Create View");
		
		return strSource;
	}
	
	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getFunctionScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getFunctionScript(ProcedureFunctionDAO functionDAO) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> parameters = new HashMap<String, String>(2);
		parameters.put("object_definer", functionDAO.getDefiner());
		parameters.put("object_name", functionDAO.getName());
		
		Map srcList = (HashMap)client.queryForObject("getFunctionScript", parameters);
		return ""+srcList.get("Create Function");
	}
	
	/* (non-Javadoc)
	 * @see com.hangum.tadpole.rdb.core.editors.objects.table.scripts.RDBDDLScript#getProcedureScript(com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO)
	 */
	@Override
	public String getProcedureScript(ProcedureFunctionDAO procedureDAO)	throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> parameters = new HashMap<String, String>(2);
		parameters.put("object_definer", procedureDAO.getDefiner());
		parameters.put("object_name", procedureDAO.getName());
		
		Map srcList = (HashMap)client.queryForObject("getProcedureScript", parameters);
		return ""+srcList.get("Create Procedure");
	}
}
