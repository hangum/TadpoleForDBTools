package com.hangum.tadpole.engine.sql.util.sqlscripts.scripts;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
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
	public String getViewScript(TableDAO tableDao) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		
		Map<String, String> parameters = new HashMap<String, String>(2);
		parameters.put("object_definer", StringUtils.substringBefore(tableDao.getName(), "."));
		parameters.put("object_name", StringUtils.substringAfter(tableDao.getName(), "."));
		
		Map srcList = (HashMap)client.queryForObject("getViewScript", parameters);
		String strSource = ""+srcList.get("Create View");
		if(StringUtils.isBlank(StringUtils.trimToEmpty(strSource))) strSource = strMSG_BlankScript;
		
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
		String strSource = ""+srcList.get("Create Function");
		
		if(StringUtils.isBlank(StringUtils.trimToEmpty(strSource))) strSource = strMSG_BlankScript;
		return strSource;
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
		String strSource = ""+srcList.get("Create Procedure");
		
		if(StringUtils.isBlank(StringUtils.trimToEmpty(strSource))) strSource = strMSG_BlankScript;
		return strSource;
	}
}
