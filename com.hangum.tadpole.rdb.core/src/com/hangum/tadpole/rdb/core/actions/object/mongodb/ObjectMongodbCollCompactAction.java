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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.mongodb.core.dialogs.collection.CollectionCompactDialog;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;

/**
 * mongoDB collection compact
 * 
 * @author hangum
 *
 */
public class ObjectMongodbCollCompactAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectMongodbCollCompactAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.mongo.collection.compact";
	
	public ObjectMongodbCollCompactAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText(title);
	}

	@Override
	public void run() {
		if(null != this.sel) {
			TableDAO collDAO = (TableDAO)this.sel.getFirstElement();
			CollectionCompactDialog dialog = new CollectionCompactDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
									getUserDB(), 
									collDAO.getName()
								);
			dialog.open();
		}		
	}
	
}
