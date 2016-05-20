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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dml.GenerateStatmentDMLDialog;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;

/**
 * generate sql dml statement     
 * 
 * @author hangum
 *
 */
public class GenerateSQLDMLAction extends GenerateSQLSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSQLDMLAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateDMLtAction"; //$NON-NLS-1$
	
	public GenerateSQLDMLAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType, title);
	}
	
	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		try {
			TableDAO tableDAO = (TableDAO)selection.getFirstElement();
			
			GenerateStatmentDMLDialog dialog = new GenerateStatmentDMLDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), true, userDB, tableDAO);
			if(Dialog.OK == dialog.open()) {
				FindEditorAndWriteQueryUtil.run(userDB, dialog.getDML(), actionType);
			}
			
		} catch(Exception e) {
			logger.error("Generate SQL statement", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().GenerateSQLSelectAction_0, errStatus); //$NON-NLS-1$
		}
	}
	
}
