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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.dialog.table.AlterTableDialog;

/**
 * Object Editor에서 사용하는 Table 직접 수정 action
 * 
 * @author hangum
 *
 */
public class AlterTableAction extends AbstractObjectSelectAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4892198101737925406L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AlterTableAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.alterTable";
	
	public AlterTableAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
		
		window.getSelectionService().addSelectionListener(this);
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		setEnabled(false);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		TableDAO tc = (TableDAO)selection.getFirstElement();
		
		AlterTableDialog dialog = new AlterTableDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB, tc);
		dialog.open();
		
		
//		if(actionType == PublicTadpoleDefine.OBJECT_TYPE.TABLES) {
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
//		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.VIEWS) {
//			CreateViewAction cva = new CreateViewAction();
//			cva.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.INDEXES) {
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
//		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.PROCEDURES) {
//			CreateProcedureAction cia = new CreateProcedureAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.PACKAGES) {
//			CreatePackageAction cia = new CreatePackageAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS) {
//			CreateFunctionAction cia = new CreateFunctionAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS) {
//			CreateTriggerAction cia = new CreateTriggerAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == PublicTadpoleDefine.OBJECT_TYPE.JAVASCRIPT) {
//			CreateJavaScriptAction csa = new CreateJavaScriptAction();
//			csa.run(userDB, actionType);
//		}
	}
	
}
