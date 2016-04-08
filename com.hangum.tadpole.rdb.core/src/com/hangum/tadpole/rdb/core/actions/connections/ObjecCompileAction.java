/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.connections;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;

/**
 * Open object compile window
 * 
 * @author hangum
 *
 */
public class ObjecCompileAction implements IViewActionDelegate {
	protected IStructuredSelection sel;
	
	public ObjecCompileAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		final UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		FindEditorAndWriteQueryUtil.run(userDB, 
				"", 
				PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
		// TODO Auto-generated method stub

	}

}
