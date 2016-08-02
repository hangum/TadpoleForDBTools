/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.object.rdb.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJavaDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleJobDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.connections.CreateJavaAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateJobsAction;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.util.GrantCheckerUtils;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author nilriri
 *
 */
public class ObjectAlterAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectAlterAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.alter"; //$NON-NLS-1$

	public ObjectAlterAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);

		setId(ID + actionType);
		setText(title);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		try {
			if (!GrantCheckerUtils.ifExecuteQuery(userDB))
				return;
		} catch (Exception e) {
			MessageDialog.openError(getWindow().getShell(), Messages.get().Error, e.getMessage());
			return;
		}

		if (actionType == PublicTadpoleDefine.OBJECT_TYPE.JOBS) {
			OracleJobDAO dao = (OracleJobDAO) selection.getFirstElement();

			CreateJobsAction cia = new CreateJobsAction(dao);
			cia.run(userDB, actionType);

			refreshJobs();

		} else if (actionType == PublicTadpoleDefine.OBJECT_TYPE.JAVA) {
			OracleJavaDAO dao = (OracleJavaDAO) selection.getFirstElement();

			CreateJavaAction cia = new CreateJavaAction(dao);
			cia.run(userDB, actionType);

			refreshJava();

		}
	} // end method

}
