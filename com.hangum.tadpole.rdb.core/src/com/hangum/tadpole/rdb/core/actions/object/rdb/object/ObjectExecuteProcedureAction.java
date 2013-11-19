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
package com.hangum.tadpole.rdb.core.actions.object.rdb.object;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.dialog.procedure.ExecuteProcedureDialog;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.executer.ProcedureExecuterManager;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 *  Procedure를 실행합니다.
 * 
 * @author hangum
 *
 */
public class ObjectExecuteProcedureAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(ObjectExecuteProcedureAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.execute.procedure";

	public ObjectExecuteProcedureAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Execute "  + title);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType) {
		ProcedureFunctionDAO procedureDAO = (ProcedureFunctionDAO)selection.getFirstElement();
		
		ProcedureExecuterManager pm = new ProcedureExecuterManager(userDB, procedureDAO);
		if(pm.isExecuted(procedureDAO, userDB)) {
			ExecuteProcedureDialog epd = new ExecuteProcedureDialog(null, userDB, procedureDAO);
			epd.open();
		}
	}// end method
	
}
