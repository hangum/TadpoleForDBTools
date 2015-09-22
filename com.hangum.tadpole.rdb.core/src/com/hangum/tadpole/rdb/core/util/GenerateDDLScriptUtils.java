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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.TadpoleObjectQuery;

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
	
	
	public static String genTableScript(UserDBDAO userDB, TableDAO tableDAO, List<TableColumnDAO> showTableColumns) {
		if(showTableColumns == null) return "";
		if(showTableColumns.isEmpty()) return "";
		
		StringBuffer sbSQL = new StringBuffer();
		try {
			sbSQL.append("SELECT "); //$NON-NLS-1$
			for (int i=0; i<showTableColumns.size(); i++) {
				TableColumnDAO dao = showTableColumns.get(i);
				sbSQL.append(StringUtils.trim(dao.getSysName()));
				
				// 마지막 컬럼에는 ,를 않넣어주어야하니까 
				if(i < (showTableColumns.size()-1)) sbSQL.append(", ");  //$NON-NLS-1$
				else sbSQL.append(" "); //$NON-NLS-1$
			}
			
			
			sbSQL.append(PublicTadpoleDefine.LINE_SEPARATOR + " FROM "); //$NON-NLS-1$ 
			if("".equals(tableDAO.getSchema_name())) sbSQL.append(tableDAO.getSysName()); //$NON-NLS-2$
			else  sbSQL.append(tableDAO.getSchema_name() + "." + tableDAO.getSysName()); //$NON-NLS-2$
			sbSQL.append(PublicTadpoleDefine.SQL_DELIMITER);
			
		} catch(Exception e) {
			logger.error(Messages.GenerateSQLSelectAction_8, e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.GenerateSQLSelectAction_0, errStatus); //$NON-NLS-1$
		}
		
		return sbSQL.toString();
	}

	/** 
	 * table script
	 * 
	 * @param userDB
	 * @param tableDAO
	 * @return
	 */
	public static String genTableScript(UserDBDAO userDB, TableDAO tableDAO) {
		try {
			List<TableColumnDAO> showTableColumns = TadpoleObjectQuery.makeShowTableColumns(userDB, tableDAO);
			return genTableScript(userDB, tableDAO, showTableColumns);
		} catch(Exception e) {
			logger.error(Messages.GenerateSQLSelectAction_8, e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.GenerateSQLSelectAction_0, errStatus); //$NON-NLS-1$
		}
		
		return "";
	}
}
