/*******************************************************************************
 * Copyright (c) 2020 hangum.
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
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.ModifyDBDialog;

/**
 * 데이터베이스 연결 수정 액션
 * 
 * @author hangum
 */
public class ModifyDBConnectionInfoAction implements IViewActionDelegate {
	private static final Logger logger = Logger.getLogger(ModifyDBConnectionInfoAction.class);

	private IStructuredSelection sel;
	
	public ModifyDBConnectionInfoAction() {
		super();
	}

	@Override
	public void run(IAction action) {
		final UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		ModifyDBDialog dialog = new ModifyDBDialog(null, userDB);
		dialog.open();
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}

}
