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
package com.hangum.tadpole.rdb.core.actions.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * object select되어야 일을 하는 action
 * 
 * @author hangum
 *
 */
public abstract class AbstractObjectSelectAction extends AbstractObjectAction {
	private static final Logger logger = Logger.getLogger(AbstractObjectSelectAction.class);
	
	public AbstractObjectSelectAction(IWorkbenchWindow window, OBJECT_TYPE actionType) {
		super(window, actionType);
	}
	
	protected void exeMessage(String msgHead, Exception e) {
		TDBErroDialog errDialog = new TDBErroDialog(null, msgHead + Messages.get().ObjectDeleteAction_25, e.getMessage());
		errDialog.open();
	}
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		
		if(ExplorerViewer.ID.equals(part.getSite().getId())) {
			this.selection = (IStructuredSelection)selection;
			if(this.selection.isEmpty()) {
				setEnabled(false);
			} else {
				UserDBDAO userDB = this.userDB;
				if(userDB != null){
					if(userDB.is_isUseEnable()) setEnabled(true);
					else setEnabled(false);
				}
			}
		}
	}	// public void selectionChanged
}
