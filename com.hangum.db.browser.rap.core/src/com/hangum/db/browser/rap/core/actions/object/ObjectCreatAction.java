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
package com.hangum.db.browser.rap.core.actions.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.db.browser.rap.core.actions.connections.CreateFunctionAction;
import com.hangum.db.browser.rap.core.actions.connections.CreateIndexAction;
import com.hangum.db.browser.rap.core.actions.connections.CreateProcedureAction;
import com.hangum.db.browser.rap.core.actions.connections.CreateTableAction;
import com.hangum.db.browser.rap.core.actions.connections.CreateTriggerAction;
import com.hangum.db.browser.rap.core.actions.connections.CreateViewAction;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.define.Define;
import com.hangum.db.define.Define.DB_ACTION;
import com.hangum.tadpole.mongodb.core.dialogs.collection.NewCollectionDialog;

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
		
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run() {
		if(actionType == DB_ACTION.TABLES) {
			
			// others db
			if(DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {
				CreateTableAction cta = new CreateTableAction();
				cta.run(userDB, actionType);
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
			CreateIndexAction cia = new CreateIndexAction();
			cia.run(userDB, actionType);
		} else if(actionType == DB_ACTION.PROCEDURES) {
			CreateProcedureAction cia = new CreateProcedureAction();
			cia.run(userDB, actionType);
		} else if(actionType == DB_ACTION.FUNCTIONS) {
			CreateFunctionAction cia = new CreateFunctionAction();
			cia.run(userDB, actionType);
		} else if(actionType == DB_ACTION.TRIGGERS) {
			CreateTriggerAction cia = new CreateTriggerAction();
			cia.run(userDB, actionType);
		}
	}
	
}
