/*******************************************************************************
 * Copyright (c) 2014 hangum.
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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine.DB_ACTION;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.dialog.table.AlterTableDialog;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 * Object Editor에서 사용하는 Table 직접 수정 action
 * 
 * @author hangum
 *
 */
public class AlterTableAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AlterTableAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.alterTable";
	
	public AlterTableAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Alter " + title);
		
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, DB_ACTION actionType) {
		TableDAO tc = (TableDAO)selection.getFirstElement();
		
		AlterTableDialog dialog = new AlterTableDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB, tc);
		dialog.open();
		
		
//		if(actionType == PublicTadpoleDefine.DB_ACTION.TABLES) {
//			
//			// others db
//			if(DBDefine.getDBDefine(userDB) != DBDefine.MONGODB_DEFAULT) {
//				
//				CreateTableAction cta = new CreateTableAction();
//				
//				// sqlite db인 경우 해당 테이블의 creation문으로 생성합니다.
//				if(DBDefine.getDBDefine(userDB) == DBDefine.SQLite_DEFAULT) {
//					TableDAO tc = (TableDAO)selection.getFirstElement();
//					if(tc == null) cta.run(userDB, actionType);
//					else cta.run(userDB, tc.getComment(), actionType);
//				} else {				
//					cta.run(userDB, actionType);
//				}
//				
//			// moongodb
//			} else if(DBDefine.getDBDefine(userDB) == DBDefine.MONGODB_DEFAULT) {				
//				NewCollectionDialog ncd = new NewCollectionDialog(Display.getCurrent().getActiveShell(), userDB);
//				if(Dialog.OK == ncd.open() ) {
//					refreshTable();
//				}
//			}
//			
//		} else if(actionType == PublicTadpoleDefine.DB_ACTION.VIEWS) {
//			CreateViewAction cva = new CreateViewAction();
//			cva.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.DB_ACTION.INDEXES) {
//			if(DBDefine.getDBDefine(userDB) != DBDefine.MONGODB_DEFAULT) {
//				CreateIndexAction cia = new CreateIndexAction();
//				cia.run(userDB, actionType);
//			// moongodb
//			} else if(DBDefine.getDBDefine(userDB) == DBDefine.MONGODB_DEFAULT) {
//				NewIndexDialog nid = new NewIndexDialog(Display.getCurrent().getActiveShell(), userDB);
//				if(Dialog.OK == nid.open()) {
//					refreshIndexes();
//				}
//			}
//		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PROCEDURES) {
//			CreateProcedureAction cia = new CreateProcedureAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.DB_ACTION.PACKAGES) {
//			CreatePackageAction cia = new CreatePackageAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.DB_ACTION.FUNCTIONS) {
//			CreateFunctionAction cia = new CreateFunctionAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.DB_ACTION.TRIGGERS) {
//			CreateTriggerAction cia = new CreateTriggerAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.DB_ACTION.JAVASCRIPT) {
//			CreateJavaScriptAction csa = new CreateJavaScriptAction();
//			csa.run(userDB, actionType);
//		}
	}
	
}
