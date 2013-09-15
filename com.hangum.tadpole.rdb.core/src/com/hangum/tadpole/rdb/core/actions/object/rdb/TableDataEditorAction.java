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
package com.hangum.tadpole.rdb.core.actions.object.rdb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.editors.objects.table.DBTableEditorInput;
import com.hangum.tadpole.rdb.core.editors.objects.table.TableInformationEditor;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Table Data editor action
 * 
 * @author hangum
 *
 */
public class TableDataEditorAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableDataEditorAction.class);
	public static String ID = "com.hangum.tadpole.rdb.core.actions.object.rdb.TableDataEditorAction";

	public TableDataEditorAction(IWorkbenchWindow window, DB_ACTION actionType) {
		super(window, actionType);
		
		setId(ID + actionType);
		setText("Table Data Editor");
	}
	
	@Override
	public void run() {
		TableDAO tableDAO = (TableDAO)sel.getFirstElement();
		
		try {
			// get the table columns
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			Map<String, String> mapParam = new HashMap<String, String>();
			mapParam.put("db", userDB.getDb());
			mapParam.put("table", tableDAO.getName());
			List showTableColumns = sqlClient.queryForList("tableColumnList", mapParam); //$NON-NLS-1$

			// Open the table director editor
			DBTableEditorInput mei = new DBTableEditorInput(tableDAO.getName(), userDB, showTableColumns);
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.openEditor(mei, TableInformationEditor.ID);
		} catch(Exception e) {
			logger.error("Load the table data", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.ExplorerViewer_39, errStatus); //$NON-NLS-1$
		}
	}

}
