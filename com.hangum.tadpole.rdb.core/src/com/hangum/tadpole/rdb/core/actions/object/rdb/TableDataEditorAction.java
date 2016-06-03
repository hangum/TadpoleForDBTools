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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DML_TYPE;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.editors.objects.table.DBTableEditorInput;
import com.hangum.tadpole.rdb.core.editors.objects.table.TableInformationEditor;
import com.hangum.tadpole.rdb.core.util.GrantCheckerUtils;
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
	public static String ID = "com.hangum.tadpole.rdb.core.actions.object.rdb.TableDataEditorAction"; //$NON-NLS-1$

	public TableDataEditorAction(IWorkbenchWindow window, OBJECT_TYPE actionType) {
		super(window, actionType);
		
		setId(ID + actionType);
		setText(Messages.get().TableDataEditorAction_1);
	}
	
	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
//		try {
//			PublicTadpoleDefine.QUERY_DML_TYPE queryType = QUERY_DML_TYPE.INSERT;
//			if(queryType == QUERY_DML_TYPE.INSERT) {
//				if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getInsert_lock())) {
//					throw new Exception(Messages.get().MainEditor_21);
//				}
//			}
//			queryType = QUERY_DML_TYPE.UPDATE;
//			if(queryType == QUERY_DML_TYPE.UPDATE) {
//				if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getUpdate_lock())) {
//					throw new Exception(Messages.get().MainEditor_21);
//				}
//			}
//			queryType = QUERY_DML_TYPE.DELETE;
//			if(queryType == QUERY_DML_TYPE.DELETE) {
//				if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getDbAccessCtl().getDelete_locl())) {
//					throw new Exception(Messages.get().MainEditor_21);
//				}
//			}
//		} catch(Exception e) {
//			MessageDialog.openError(getWindow().getShell(), Messages.get().Error, e.getMessage());
//			return;
//		}
		
		try {
			if(!GrantCheckerUtils.ifExecuteQuery(userDB)) return;
		} catch (Exception e) {
			MessageDialog.openError(getWindow().getShell(), Messages.get().Error, e.getMessage());
			return;
		}
		
		TableDAO tableDAO = (TableDAO)selection.getFirstElement();
		try {
			// get the table columns
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			Map<String, String> mapParam = new HashMap<String, String>();
			mapParam.put("db", userDB.getDb()); //$NON-NLS-1$
			mapParam.put("table", tableDAO.getName()); //$NON-NLS-1$
			List showTableColumns = sqlClient.queryForList("tableColumnList", mapParam); //$NON-NLS-1$

			// Open the table director editor
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			
			DBTableEditorInput dbEditorInput = new DBTableEditorInput(tableDAO, userDB, showTableColumns);
			page.openEditor(dbEditorInput, TableInformationEditor.ID, false);
		} catch(Exception e) {
			logger.error("Load the table data", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().ExplorerViewer_39, errStatus); //$NON-NLS-1$
		}
	}

}
