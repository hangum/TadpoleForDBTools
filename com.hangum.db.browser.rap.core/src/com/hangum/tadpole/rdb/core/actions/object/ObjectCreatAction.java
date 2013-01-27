/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.define.Define.DB_ACTION;
import com.hangum.tadpole.mongodb.core.dialogs.collection.NewCollectionDialog;
import com.hangum.tadpole.mongodb.core.dialogs.collection.index.NewIndexDialog;
import com.hangum.tadpole.rdb.core.actions.connections.CreateFunctionAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateIndexAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateJavaScriptAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateProcedureAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateTableAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateTriggerAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateViewAction;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangumNote
 *
 */
public class ObjectCreatAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectCreatAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.creat";
	
	public ObjectCreatAction(IWorkbenchWindow window, Define.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Create " + title);
	}

	@Override
	public void run() {
		if(actionType == DB_ACTION.TABLES) {
			
			// others db
			if(DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {
				
				CreateTableAction cta = new CreateTableAction();
				
				// sqlite db인 경우 해당 테이블의 creation문으로 생성합니다.
				if(DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.SQLite_DEFAULT) {
					TableDAO tc = (TableDAO)sel.getFirstElement();
					cta.run(userDB, tc.getComment());
				} else {				
					cta.run(userDB, actionType);
				}
				
			// moongodb
			} else if(DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MONGODB_DEFAULT) {				
				NewCollectionDialog ncd = new NewCollectionDialog(Display.getCurrent().getActiveShell(), userDB);
				if(Dialog.OK == ncd.open() ) {
					refreshTable();
				}
			}
			
		} else if(actionType == DB_ACTION.VIEWS) {
			CreateViewAction cva = new CreateViewAction();
			cva.run(userDB, actionType);
		} else if(actionType == DB_ACTION.INDEXES) {
			if(DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {
				CreateIndexAction cia = new CreateIndexAction();
				cia.run(userDB, actionType);
			// moongodb
			} else if(DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MONGODB_DEFAULT) {
				NewIndexDialog nid = new NewIndexDialog(Display.getCurrent().getActiveShell(), userDB);
				if(Dialog.OK == nid.open()) {
					refreshIndexes();
				}
			}
		} else if(actionType == DB_ACTION.PROCEDURES) {
			CreateProcedureAction cia = new CreateProcedureAction();
			cia.run(userDB, actionType);
		} else if(actionType == DB_ACTION.FUNCTIONS) {
			CreateFunctionAction cia = new CreateFunctionAction();
			cia.run(userDB, actionType);
		} else if(actionType == DB_ACTION.TRIGGERS) {
			CreateTriggerAction cia = new CreateTriggerAction();
			cia.run(userDB, actionType);
		} else if(actionType == DB_ACTION.JAVASCRIPT) {
			CreateJavaScriptAction csa = new CreateJavaScriptAction();
			csa.run(userDB, actionType);
		}
	}
	
}
