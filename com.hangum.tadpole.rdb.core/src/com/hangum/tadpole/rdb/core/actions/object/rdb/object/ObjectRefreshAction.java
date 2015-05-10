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
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangum
 *
 */
public class ObjectRefreshAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(ObjectRefreshAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.refresh";

	public ObjectRefreshAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Refresh " + title);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType) {
		if(actionType == PublicTadpoleDefine.DB_ACTION.TABLES) {
			refreshTable();
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.VIEWS) {
			refreshView();
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.SYNONYM) {
			refreshSynonym();
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.INDEXES) {
			refreshIndexes();
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
			refreshProcedure();
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PACKAGES) {
			refreshPackage();
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
			refreshFunction();
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
			refreshTrigger();
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.JAVASCRIPT) {
			refreshJS();
		}
	}

}
