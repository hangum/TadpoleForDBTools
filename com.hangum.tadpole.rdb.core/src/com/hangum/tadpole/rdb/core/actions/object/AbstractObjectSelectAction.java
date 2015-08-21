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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * object select되어야 일을 하는 action
 * 
 * @author hangum
 *
 */
public abstract class AbstractObjectSelectAction extends AbstractObjectAction {

	public AbstractObjectSelectAction(IWorkbenchWindow window, DB_ACTION actionType) {
		super(window, actionType);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		
		if(ExplorerViewer.ID.equals(part.getSite().getId())) {
			this.selection = (IStructuredSelection)selection;
			
			UserDBDAO userDB = this.userDB;
			if(userDB != null) {
				if(!this.selection.isEmpty()) {
					setEnabled(true);
					return;
				}
			}
		}
		
		setEnabled(false);
	}
}
