/*******************************************************************************
 * Copyright (c) 2015 hangum.
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

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;

/**
 * Table column rename action
 * 
 * @author hangum
 *
 */
public class TableColumnModifyAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableColumnModifyAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.tablecolumn.modify"; //$NON-NLS-1$

	public TableColumnModifyAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(Messages.get().TableColumnRenameAction); //$NON-NLS-1$
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		TableColumnDAO tableColumnDAO = (TableColumnDAO)selection.getFirstElement();
		TableDAO tableDAO = tableColumnDAO.getTableDao();
		
//		TableColumnRenameValidator fv = new TableColumnRenameValidator(tableColumnDAO.getField());
//		InputDialog dialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
//								Messages.get().TableColumnRenameAction_0, tableColumnDAO.getField(), tableColumnDAO.getField(), fv);
//		if(dialog.open() == Window.OK) {
//			String newTableNm = dialog.getValue();
//			
//			if(!MessageDialog.openConfirm(null, Messages.get().ObjectRenameAction_1, String.format(Messages.get().TableColumnRenameAction_1, newTableNm))) return;
//			try {
//				TableColumnObjectQuery.renameColumn(userDB, tableDAO, tableColumnDAO.getField(), newTableNm);
//				refreshTableColumn();
//			} catch (Exception e) {
//				logger.error("rename column name", e);
//
//				TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, e.getMessage());
//				errDialog.open();
//			}
//		}
	}
}

///**
// * Object rename validator
// * @author hangum
// *
// */
//class TableColumnRenameValidator implements IInputValidator {
//	private static final Logger logger = Logger.getLogger(TableColumnRenameValidator.class);
//	private String oldName;
//	private String objectName;
//	
//	public TableColumnRenameValidator(String oldName) {
//		super();
//		
//		this.oldName = oldName;
//	}
//	
//	@Override
//	public String isValid(String newText) {
//		if(oldName.equals(newText)) {
//			return Messages.get().TableColumnRenameValidator_0;
//		}
//		if(newText.length() < 2) return Messages.get().FileNameValidator_0;
//		objectName = newText;
//				
//		return null;
//	}
//	
//	public String getObjectName() {
//		return objectName;
//	}
//}
