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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.dialogs.collection.FindAndModifyDialog;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;

/**
 * Collection Find and modify action
 * 
 * @author hangum
 *
 */
public class ObjectMongodbCollFindAndModifyAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(ObjectMongodbCollFindAndModifyAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.mongo.collection.findAndModify";
	
	public ObjectMongodbCollFindAndModifyAction(IWorkbenchWindow window, PublicTadpoleDefine.OBJECT_TYPE actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
	}

	@Override
	public void run(IStructuredSelection selection, UserDBDAO userDB, OBJECT_TYPE actionType) {
		TableDAO collDAO = (TableDAO) selection.getFirstElement();
		FindAndModifyDialog dialog = new FindAndModifyDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB, collDAO.getName());
		dialog.open();
	}
	
}