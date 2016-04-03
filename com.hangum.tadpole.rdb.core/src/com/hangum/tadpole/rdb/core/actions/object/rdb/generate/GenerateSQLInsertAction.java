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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.mongodb.core.dialogs.collection.NewDocumentDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * insert action
 * @author hangum
 *
 */
public class GenerateSQLInsertAction extends GenerateSQLSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSQLInsertAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateSQLInsertAction"; //$NON-NLS-1$

	public GenerateSQLInsertAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType, title);
	}
	
	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		TableDAO tableDAO = (TableDAO)selection.getFirstElement();
		
		if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
			StringBuffer sbSQL = new StringBuffer();
			try {
				List<TableColumnDAO> showTableColumns = TadpoleObjectQuery.getTableColumns(userDB, tableDAO);
				
				sbSQL.append("INSERT INTO " + SQLUtil.getTableName(userDB, tableDAO) + PublicTadpoleDefine.LINE_SEPARATOR + "	("); //$NON-NLS-1$ //$NON-NLS-2$
				for (int i=0; i<showTableColumns.size(); i++) {
					TableColumnDAO dao = showTableColumns.get(i);
					sbSQL.append(dao.getSysName());
					
					// 마지막 컬럼에는 ,를 않넣어주어야하니까 
					if(i < (showTableColumns.size()-1)) sbSQL.append(", ");  //$NON-NLS-1$
					else sbSQL.append(") "); //$NON-NLS-1$
				}
				sbSQL.append(PublicTadpoleDefine.LINE_SEPARATOR + "VALUES " + PublicTadpoleDefine.LINE_SEPARATOR + "	( "); //$NON-NLS-1$
				for (int i=0; i<showTableColumns.size(); i++) {
					if(i < (showTableColumns.size()-1)) sbSQL.append("?, ");  //$NON-NLS-1$
					else sbSQL.append("? )"); //$NON-NLS-1$
				}
				
				FindEditorAndWriteQueryUtil.run(userDB, sbSQL.toString(), actionType);
			} catch(Exception e) {
				logger.error("Generate SQL Statement", e);
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().GenerateSQLInsertAction_0, errStatus); //$NON-NLS-1$
			}
		// mongo db
		} else if(userDB.getDBDefine() == DBDefine.MONGODB_DEFAULT) {
			
			NewDocumentDialog dialog = new NewDocumentDialog(Display.getCurrent().getActiveShell(), userDB, tableDAO.getName());
			dialog.open();
		}
	}

}
