/*******************************************************************************
 * Copyright (c) 2014 hangum.
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.dialog.table.AlterTableDialog;

/**
 * Object Editor에서 사용하는 Table 직접 수정 action
 * 
 * @author hangum
 *
 */
public class AlterTableAction extends AbstractObjectSelectAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4892198101737925406L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AlterTableAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.alterTable";
	
	public AlterTableAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
		
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		TableDAO tc = (TableDAO)selection.getFirstElement();
		
		final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			AlterTableDialog dialog = new AlterTableDialog(shell, userDB, tc);
			dialog.open();
		} else {
			MessageDialog.openInformation(shell, "Confirm", "Not support this function. But soon. wait for our team.");
		}
	}
	
}
