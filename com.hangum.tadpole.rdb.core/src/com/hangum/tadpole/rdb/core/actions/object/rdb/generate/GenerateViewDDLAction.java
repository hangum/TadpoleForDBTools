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

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.sqlscripts.DDLScriptManager;

/**
 * generate ddl view
 * 
 * @author hangum
 * 
 */
public class GenerateViewDDLAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateViewDDLAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateViewDDLAction"; //$NON-NLS-1$

	/**
	 * generated view ddl
	 * 
	 * @param window
	 * @param actionType
	 * @param target
	 */
	public GenerateViewDDLAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String target) {
		super(window, actionType);

		setId(ID + actionType.toString());
		setText(target + " DDL");
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType) {
		try {
			DDLScriptManager scriptManager = new DDLScriptManager(userDB, actionType);
			FindEditorAndWriteQueryUtil.run(userDB, scriptManager.getScript(selection.getFirstElement()), true);
		} catch (Exception e) {
			logger.error("view ddl", e);
			MessageDialog.openError(null, "Confirm", "Not support this function.");
		}
	}

}
