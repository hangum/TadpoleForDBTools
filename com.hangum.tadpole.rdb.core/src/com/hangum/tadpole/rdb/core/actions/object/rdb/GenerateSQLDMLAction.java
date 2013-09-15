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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.dialog.dml.GenerateStatmentDMLDialog;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * generate sql dml statement     
 * 
 * @author hangum
 *
 */
public class GenerateSQLDMLAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSQLDMLAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateDMLtAction"; //$NON-NLS-1$
	
	public GenerateSQLDMLAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
	
		setId(ID + actionType.toString());
		setText(Messages.GenerateSQLSelectAction_1 + title);
		
		window.getSelectionService().addSelectionListener(this);
	}
	
	@Override
	public void run() {
		try {
			TableDAO tableDAO = (TableDAO)sel.getFirstElement();
			
			GenerateStatmentDMLDialog dialog = new GenerateStatmentDMLDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB, tableDAO.getName());
			if(Dialog.OK == dialog.open()) {
				FindEditorAndWriteQueryUtil.run(userDB, dialog.getDML());
			}
			
		} catch(Exception e) {
			logger.error(Messages.GenerateSQLSelectAction_8, e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.GenerateSQLSelectAction_0, errStatus); //$NON-NLS-1$
		}
	}
	
}
