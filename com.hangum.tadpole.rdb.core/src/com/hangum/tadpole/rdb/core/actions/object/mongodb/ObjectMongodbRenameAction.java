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
package com.hangum.tadpole.rdb.core.actions.object.mongodb;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;

/**
 * Object Explorer에서 사용하는 Mongodb rename action
 * 
 * @author hangum
 *
 */
public class ObjectMongodbRenameAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectMongodbRenameAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.mongo.rename"; //$NON-NLS-1$
	
	public ObjectMongodbRenameAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
//		String originalName = selection.getFirstElement().toString();
		TableDAO table = (TableDAO) selection.getFirstElement();
		String originalName = table.getName();
//		String newName = ""; //$NON-NLS-1$

		InputDialog inputDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Rename Collection", //$NON-NLS-1$
				"Enter new collection name", originalName, new RenameValidator(originalName));
		if (inputDialog.open() == Window.OK) {
			try {
				MongoDBQuery.renameCollection(userDB, originalName, inputDialog.getValue());

				// object explorer를 refresh
				refreshTable();
			} catch (Exception e) {
				logger.error("mongodb rename", e);

				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Rename Collection", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
}
/**
 * rename validattor
 */
class RenameValidator implements IInputValidator {
	String oldName = ""; //$NON-NLS-1$
	
	public RenameValidator(String oldName) {
		this.oldName = oldName;
	}

	public String isValid(String newText) {
		if(oldName.equals(newText)) {
			return Messages.get().ObjectMongodbRenameAction_7;
		}
	    int len = newText.length();
	
	    // Determine if input is too short or too long
	    if (len < 2) return Messages.get().ObjectMongodbRenameAction_8;
	
	    return null;
	}
}
