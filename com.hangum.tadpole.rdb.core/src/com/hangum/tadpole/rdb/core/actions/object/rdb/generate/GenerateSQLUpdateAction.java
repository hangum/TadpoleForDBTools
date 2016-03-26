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
package com.hangum.tadpole.rdb.core.actions.object.rdb.generate;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * dml update action
 * 
 * @author hangum
 *
 */
public class GenerateSQLUpdateAction extends GenerateSQLSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSQLUpdateAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateSQLUpdateAction"; //$NON-NLS-1$

	public GenerateSQLUpdateAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType, title);
	}
	
	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		StringBuffer sbSQL = new StringBuffer();
		try {
			TableDAO tableDAO = (TableDAO)selection.getFirstElement();
			
			List<TableColumnDAO> showTableColumns = TadpoleObjectQuery.getTableColumns(userDB, tableDAO);
			sbSQL.append("UPDATE " + SQLUtil.getTableName(userDB, tableDAO) + PublicTadpoleDefine.LINE_SEPARATOR + "\tSET "); //$NON-NLS-1$ //$NON-NLS-2$
			for (int i=0; i<showTableColumns.size(); i++) {
				TableColumnDAO dao = showTableColumns.get(i);
				sbSQL.append(dao.getSysName());
				
				// 마지막 컬럼에는 ,를 않넣어주어야하니까 
				if(i < (showTableColumns.size()-1)) sbSQL.append("= ?, ");  //$NON-NLS-1$
				else sbSQL.append("= ? "); //$NON-NLS-1$
			}

			sbSQL.append(PublicTadpoleDefine.LINE_SEPARATOR + "WHERE " + PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
			int cnt = 0;
			for (int i=0; i<showTableColumns.size(); i++) {
				TableColumnDAO dao = showTableColumns.get(i);
				if(PublicTadpoleDefine.isKEY(dao.getKey())) {
					if(cnt == 0) sbSQL.append("\t\t" + dao.getField() + " = ? " + PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
					else sbSQL.append("\tAND " + dao.getField() + " = ? " + PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
					cnt++;
				}				
			}

			FindEditorAndWriteQueryUtil.run(userDB, sbSQL.toString(), actionType);
		} catch(Exception e) {
			logger.error("Generate SQL Statement Error", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().GenerateSQLUpdateAction_13, errStatus); //$NON-NLS-1$
		}
	}

}
