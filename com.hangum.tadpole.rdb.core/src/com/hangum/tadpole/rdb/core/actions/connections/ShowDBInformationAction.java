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
package com.hangum.tadpole.rdb.core.actions.connections;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.MongoDBInfosEditor;
import com.hangum.tadpole.mongodb.core.editors.dbInfos.MongoDBInfosInput;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.RDBDBInfoEditorInput;
import com.hangum.tadpole.rdb.core.editors.dbinfos.RDBDBInfosEditor;

/**
 * Show db information action
 * 
 * @author hangum
 *
 */
public class ShowDBInformationAction extends AbstractQueryAction{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ShowDBInformationAction.class);

	public ShowDBInformationAction() {
		super();
	}
	
	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
			try {
				RDBDBInfoEditorInput editorInput = new RDBDBInfoEditorInput(userDB);
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(editorInput, RDBDBInfosEditor.ID);
			} catch (PartInitException e) {
				logger.error("open DB Information editor", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().AbstractQueryAction_1, errStatus); //$NON-NLS-1$
			}
		} else if(userDB.getDBDefine() == DBDefine.MONGODB_DEFAULT) {
			MongoDBInfosInput mongoInput = new MongoDBInfosInput(userDB, MongoDBInfosEditor.PAGES.INSTANCE_INFORMATION);
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mongoInput, MongoDBInfosEditor.ID);
			} catch (PartInitException e) {
				logger.error("open editor", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().AbstractQueryAction_1, errStatus); //$NON-NLS-1$
			}
		}
	}

}
