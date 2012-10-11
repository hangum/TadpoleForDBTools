/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.define.Define.DB_ACTION;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.dialogs.collection.NewDocumentDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.connections.QueryEditorAction;
import com.ibatis.sqlmap.client.SqlMapClient;

public class GenerateSQLInsertAction extends GenerateSQLSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSQLInsertAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateSQLInsertAction"; //$NON-NLS-1$

	public GenerateSQLInsertAction(IWorkbenchWindow window, DB_ACTION actionType, String title) {
		super(window, actionType, title);
	}
	
	@Override
	public void run() {
		TableDAO tableDAO = (TableDAO)sel.getFirstElement();
		
		if(DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {
			StringBuffer sbSQL = new StringBuffer();
			try {
				Map<String, String> parameter = new HashMap<String, String>();
				parameter.put("db", userDB.getDb());
				parameter.put("table", tableDAO.getName());
				
				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
				List<TableColumnDAO> showTableColumns = sqlClient.queryForList("tableColumnList", parameter); //$NON-NLS-1$
				
				sbSQL.append(" INSERT INTO " + tableDAO.getName() + " \r\n ("); //$NON-NLS-1$ //$NON-NLS-2$
				for (int i=0; i<showTableColumns.size(); i++) {
					TableColumnDAO dao = showTableColumns.get(i);
					sbSQL.append(dao.getField());
					
					// 마지막 컬럼에는 ,를 않넣어주어야하니까 
					if(i < (showTableColumns.size()-1)) sbSQL.append(", ");  //$NON-NLS-1$
					else sbSQL.append(") "); //$NON-NLS-1$
				}
				sbSQL.append("\r\n VALUES \r\n ( "); //$NON-NLS-1$
				for (int i=0; i<showTableColumns.size(); i++) {
					if(i < (showTableColumns.size()-1)) sbSQL.append("?, ");  //$NON-NLS-1$
					else sbSQL.append("? ); "); //$NON-NLS-1$
				}
				
				//
				QueryEditorAction qea = new QueryEditorAction();
				qea.run(userDB, sbSQL.toString());
			} catch(Exception e) {
				logger.error(Messages.GenerateSQLInsertAction_9, e);
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", Messages.GenerateSQLInsertAction_0, errStatus); //$NON-NLS-1$
			}
		// mongo db
		} else if(DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MONGODB_DEFAULT) {
			
			NewDocumentDialog dialog = new NewDocumentDialog(Display.getCurrent().getActiveShell(), userDB, tableDAO.getName());
			dialog.open();
			
		}

	}

}
