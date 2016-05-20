/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.object.rdb.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.dialog.table.mysql.MySQLTableColumnDialog;

/**
 * Table column modify action
 * 
 * @author hangum
 *
 */
public class TableColumnModifyAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableColumnModifyAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.tablecolumn.modify"; //$NON-NLS-1$

	public TableColumnModifyAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(Messages.get().ModifyColumn); //$NON-NLS-1$
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		TableColumnDAO tableColumnDAO = (TableColumnDAO)selection.getFirstElement();
		TableDAO tableDAO = tableColumnDAO.getTableDao();
		
		MySQLTableColumnDialog dialog = new MySQLTableColumnDialog(window.getShell(), getUserDB(), tableDAO, tableColumnDAO);
		dialog.open();
	}
}
