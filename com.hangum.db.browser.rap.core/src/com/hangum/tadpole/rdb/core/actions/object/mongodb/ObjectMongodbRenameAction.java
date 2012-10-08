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
package com.hangum.tadpole.rdb.core.actions.object.mongodb;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;

/**
 * Object Explorer에서 사용하는 Mongodb rename action
 * 
 * @author hangumNote
 *
 */
public class ObjectMongodbRenameAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectMongodbRenameAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.mongo.rename";
	
	public ObjectMongodbRenameAction(IWorkbenchWindow window, Define.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
		
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run() {
		if(null != this.sel) {
			String originalName = this.sel.getFirstElement().toString();
			String newName = "";
			
			InputDialog inputDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Rename Collection", "Enter new collection name", originalName, new LengthValidator());
			if(inputDialog.open() == Window.OK) {				
				try {
					MongoDBQuery.renameCollection(userDB, originalName, inputDialog.getValue());
					
					// object explorer를 refresh 
					refreshTable();
				} catch (Exception e) {
					logger.error("mongodb rename", e);
					
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(null, "Error","Rename Collection", errStatus); //$NON-NLS-1$
				}
				
			}
		}
		
	}
	
}
/**
 * rename validattor
 */
class LengthValidator implements IInputValidator {

	public String isValid(String newText) {
    int len = newText.length();

    // Determine if input is too short or too long
    if (len < 3) return "Too short";

    return null;
  }
}
