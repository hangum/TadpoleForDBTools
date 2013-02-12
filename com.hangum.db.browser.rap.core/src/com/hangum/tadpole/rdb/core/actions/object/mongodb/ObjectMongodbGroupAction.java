/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.editors.group.MongoDBGroupEditor;
import com.hangum.tadpole.mongodb.core.editors.group.MongoDBGroupEditorInput;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;

/**
 * Object Explorer에서 사용하는 Mongodb group
 * 
 * @author hangumNote
 *
 */
public class ObjectMongodbGroupAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectMongodbGroupAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.mongo.group";
	
	public ObjectMongodbGroupAction(IWorkbenchWindow window, Define.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
	}

	@Override
	public void run() {
		if(null != this.sel) {
			TableDAO tableDAO = (TableDAO)this.sel.getFirstElement();

			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
			try {
				MongoDBGroupEditorInput input = new MongoDBGroupEditorInput(userDB, tableDAO.getName());
				page.openEditor(input, MongoDBGroupEditor.ID, false);
				
			} catch (PartInitException e) {
				logger.error("Mongodb group", e);
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", "Group Open Exception", errStatus); //$NON-NLS-1$
			}
			
		}		
	}
	
}