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
package com.hangum.tadpole.rdb.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.sql.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * generate ddl script utils
 * 
 * @author hangum
 *
 */
public class GenerateDDLScriptUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateDDLScriptUtils.class); 

	/** 
	 * table script
	 * 
	 * @param userDB
	 * @param tableDAO
	 * @return
	 */
	public static String genTableScript(UserDBDAO userDB, TableDAO tableDAO) {
		StringBuffer sbSQL = new StringBuffer();
		try {
			Map<String, String> parameter = new HashMap<String, String>();
			parameter.put("db", userDB.getDb());
			parameter.put("table", tableDAO.getName());
			
			List<TableColumnDAO> showTableColumns = null;
			if(userDB.getDBDefine() != DBDefine.TAJO_DEFAULT) {
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
				showTableColumns = sqlClient.queryForList("tableColumnList", parameter); //$NON-NLS-1$
			} else {
				showTableColumns = new TajoConnectionManager().tableColumnList(userDB, parameter);
			}
			
			sbSQL.append("SELECT "); //$NON-NLS-1$
			for (int i=0; i<showTableColumns.size(); i++) {
				TableColumnDAO dao = showTableColumns.get(i);
				sbSQL.append(dao.getField());
				
				// 마지막 컬럼에는 ,를 않넣어주어야하니까 
				if(i < (showTableColumns.size()-1)) sbSQL.append(", ");  //$NON-NLS-1$
				else sbSQL.append(" "); //$NON-NLS-1$
			}
			sbSQL.append(PublicTadpoleDefine.LINE_SEPARATOR + " FROM " + tableDAO.getName() + PublicTadpoleDefine.SQL_DILIMITER); //$NON-NLS-1$ //$NON-NLS-2$
			sbSQL.append(PublicTadpoleDefine.LINE_SEPARATOR);
			
		} catch(Exception e) {
			logger.error(Messages.GenerateSQLSelectAction_8, e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.GenerateSQLSelectAction_0, errStatus); //$NON-NLS-1$
		}
		
		return sbSQL.toString();
	}
}
