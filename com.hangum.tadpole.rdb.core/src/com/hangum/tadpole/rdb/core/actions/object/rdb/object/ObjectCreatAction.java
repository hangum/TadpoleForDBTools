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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.dialogs.collection.NewCollectionDialog;
import com.hangum.tadpole.mongodb.core.dialogs.collection.index.NewIndexDialog;
import com.hangum.tadpole.rdb.core.actions.connections.CreateConstraintsAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateFunctionAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateIndexAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateJavaScriptAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreatePackageAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateProcedureAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateTableAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateTriggerAction;
import com.hangum.tadpole.rdb.core.actions.connections.CreateViewAction;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.dialog.table.mysql.MySQLTaableCreateDialog;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangum
 *
 */
public class ObjectCreatAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(ObjectCreatAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.creat";
	
	public ObjectCreatAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		if(actionType == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
			
			// others db
			if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
				
				CreateTableAction cta = new CreateTableAction();
				
				// sqlite db인 경우 해당 테이블의 creation문으로 생성합니다.
				if(userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) {
					TableDAO tc = (TableDAO)selection.getFirstElement();
					if(tc == null) cta.run(userDB, actionType);
					else cta.run(userDB, tc.getComment(), actionType);
				} else if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
					MySQLTaableCreateDialog dialog = new MySQLTaableCreateDialog(window.getShell(), getUserDB());
					dialog.open();
				} else {				
					cta.run(userDB, actionType);
				}
				
			// moongodb
			} else if(userDB.getDBDefine() == DBDefine.MONGODB_DEFAULT) {				
				NewCollectionDialog ncd = new NewCollectionDialog(Display.getCurrent().getActiveShell(), userDB);
				if(Dialog.OK == ncd.open() ) {
					refreshTable();
				}
			}
			
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
			CreateViewAction cva = new CreateViewAction();
			cva.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.INDEXES | actionType == PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS) {
			if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
				CreateIndexAction cia = new CreateIndexAction();
				cia.run(userDB, actionType);
			// moongodb
			} else if(userDB.getDBDefine() == DBDefine.MONGODB_DEFAULT) {
				NewIndexDialog nid = new NewIndexDialog(Display.getCurrent().getActiveShell(), userDB);
				if(Dialog.OK == nid.open()) {
					refreshIndexes();
				}
			}
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS) {
			CreateConstraintsAction cca = new CreateConstraintsAction();
			cca.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
			CreateProcedureAction cia = new CreateProcedureAction();
			cia.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.PACKAGES) {
			CreatePackageAction cia = new CreatePackageAction();
			cia.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
			CreateFunctionAction cia = new CreateFunctionAction();
			cia.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
			CreateTriggerAction cia = new CreateTriggerAction();
			cia.run(userDB, actionType);
		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.JAVASCRIPT) {
			CreateJavaScriptAction csa = new CreateJavaScriptAction();
			csa.run(userDB, actionType);
		}
	}
	
}
