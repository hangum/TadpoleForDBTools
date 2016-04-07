/*******************************************************************************
 * Copyright (c) 2016 hangum.
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
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.dialog.dml.GenerateStatmentDMLDialog;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * dialog util
 * @author hangum
 *
 */
public class DialogUtil {
	private static final Logger logger = Logger.getLogger(DialogUtil.class);
	
	/**
	 * dialog util
	 * @param userDB
	 * @param strObject
	 */
	public static void popupDMLDialog(UserDBDAO userDB, String strObject) {
		try {
			TableDAO tableDao = null;
			List<TableDAO> listTable = userDB.getListTable();
			if(listTable.isEmpty()) { 
				if(DBDefine.POSTGRE_DEFAULT != userDB.getDBDefine()) { 
					tableDao = TadpoleObjectQuery.getTable(userDB, StringUtils.trim(strObject));
				} else {
					tableDao = new TableDAO(strObject, "");
				}
			} else {
				for (TableDAO tmpTableDAO : listTable) {
					if(strObject.equalsIgnoreCase(tmpTableDAO.getName())) {
						tableDao = tmpTableDAO;
						break;
					}
				}
			}
			
			if(tableDao != null) {
				GenerateStatmentDMLDialog dialog = new GenerateStatmentDMLDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), false, 
						userDB, tableDao);
				dialog.open();
			}
			
		} catch (Exception e) {
			logger.error("f4 function", e);
		}
	}

}
