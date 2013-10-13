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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.editors.mapreduce.MapReduceEditor;
import com.hangum.tadpole.mongodb.core.editors.mapreduce.MapReduceEditorInput;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;

/**
 * Object Explorer에서 사용하는 Mongodb MapReduce
 * 
 * @author hangum
 *
 */
public class ObjectMongodbMapReduceAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ObjectMongodbMapReduceAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.mongo.mapreduce";
	
	public ObjectMongodbMapReduceAction(IWorkbenchWindow window, PublicTadpoleDefine.DB_ACTION actionType, String title) {
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
				MapReduceEditorInput input = new MapReduceEditorInput(userDB, tableDAO.getName());
				page.openEditor(input, MapReduceEditor.ID, false);
				
			} catch (PartInitException e) {
				logger.error("Mongodb MapReduce", e);
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(null, "Error", "MapReduce Open Exception", errStatus); //$NON-NLS-1$
			}
			
		}		
	}
	
}
