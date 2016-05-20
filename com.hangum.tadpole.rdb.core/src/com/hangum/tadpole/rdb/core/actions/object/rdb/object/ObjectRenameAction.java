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
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectSelectAction;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * Table rename action
 * 
 * @author hangum
 *
 */
public class ObjectRenameAction extends AbstractObjectSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectRenameAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.modify"; //$NON-NLS-1$

	public ObjectRenameAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title); //$NON-NLS-1$
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		TableDAO dao = (TableDAO)selection.getFirstElement();
		
		ObjectRenameValidator fv = new ObjectRenameValidator(dao.getName());
		final Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		InputDialog dialog = new InputDialog(activeShell, Messages.get().ObjectRenameAction_0, dao.getName(), dao.getName(), fv);
		if(dialog.open() == Window.OK) {
			String newTableNm = dialog.getValue();
			
			if(!MessageDialog.openConfirm(null, Messages.get().Confirm, String.format(Messages.get().ObjectRenameAction_2, newTableNm))) return;
			try {
				TadpoleObjectQuery.renameTable(userDB, dao, newTableNm);
				refreshTable(newTableNm);
			} catch (Exception e) {
				logger.error("rename table", e);

				TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, e.getMessage());
				errDialog.open();
			}
		}
	}
}

/**
 * Object rename validator
 * @author hangum
 *
 */
class ObjectRenameValidator implements IInputValidator {
	private static final Logger logger = Logger.getLogger(ObjectRenameValidator.class);
	private String oldName;
	
	public ObjectRenameValidator(String oldName) {
		super();
		
		this.oldName = oldName;
	}
	
	@Override
	public String isValid(String newText) {
		int len = newText.length();
		if(oldName.equals(newText)) {
			return Messages.get().ObjectRenameValidator_0;
		}
		if(len < 2) return Messages.get().FileNameValidator_0;
				
		return null;
	}
}
