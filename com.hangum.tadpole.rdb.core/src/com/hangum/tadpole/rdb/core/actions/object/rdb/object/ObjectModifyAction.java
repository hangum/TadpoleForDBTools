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

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.rdb.core.actions.connections.CreateFunctionAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateIndexAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateJavaScriptAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateProcedureAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateTableAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateTriggerAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateViewAction;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangum
 *
 */
public class ObjectModifyAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectModifyAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.modify"; //$NON-NLS-1$

	public ObjectModifyAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Alert " + title); //$NON-NLS-1$
	}

	@Override
	public void run() {
		if(actionType == PublicTadpoleDefine.DB_ACTION.TABLES) {
			
			CreateTableAction cta = new CreateTableAction();
			
			// sqlite db인 경우 해당 테이블의 creation문으로 생성합니다.
			if(DBDefine.getDBDefine(userDB.getDelYn()) == DBDefine.SQLite_DEFAULT) {
				TableDAO tc = (TableDAO)sel.getFirstElement();
				if(tc == null) cta.run(userDB, actionType);
				else cta.run(userDB, tc.getComment());
			} else {				
				cta.run(userDB, actionType);
			}
				
			
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.VIEWS) {
			CreateViewAction cva = new CreateViewAction();
			cva.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.INDEXES) {
			CreateIndexAction cia = new CreateIndexAction();
			cia.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
			CreateProcedureAction cia = new CreateProcedureAction();
			cia.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
			CreateFunctionAction cia = new CreateFunctionAction();
			cia.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
			CreateTriggerAction cia = new CreateTriggerAction();
			cia.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.DB_ACTION.JAVASCRIPT) {
			CreateJavaScriptAction csa = new CreateJavaScriptAction();
			csa.run(userDB, actionType);
		}
	}
}
