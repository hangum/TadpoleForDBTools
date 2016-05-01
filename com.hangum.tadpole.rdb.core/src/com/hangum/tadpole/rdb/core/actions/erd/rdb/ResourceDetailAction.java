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
package com.hangum.tadpole.rdb.core.actions.erd.rdb;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.rdb.core.dialog.resource.ResourceDetailDialog;

/**
 * Resource history action
 * 
 * @author hangum
 *
 */
public class ResourceDetailAction implements IViewActionDelegate {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ResourceDetailAction.class);
	private IStructuredSelection sel;
	
	public ResourceDetailAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBResourceDAO resourceDB = (UserDBResourceDAO)sel.getFirstElement();

		ResourceManagerDAO managerDao = new ResourceManagerDAO();
		managerDao.setResource_seq(resourceDB.getResource_seq());
		managerDao.setDb_seq(resourceDB.getDb_seq());
		managerDao.setUser_seq(resourceDB.getUser_seq());
		
		managerDao.setName(resourceDB.getName());
		managerDao.setResource_types(resourceDB.getResource_types());
		managerDao.setDescription(resourceDB.getDescription());
		managerDao.setResource_seq(resourceDB.getResource_seq());
		managerDao.setCreate_time(resourceDB.getCreate_time() == null?resourceDB.getSqliteCreate_time():resourceDB.getCreate_time().toLocaleString());
		managerDao.setUser_name(resourceDB.getUsernames());
		
		try {
			String defaultStr = TadpoleSystem_UserDBResource.getResourceData(resourceDB);
			resourceDB.setDataString(defaultStr);
		} catch(Exception e) {
			logger.error("user resource data", e);
		}
		
		ResourceDetailDialog dialog = new ResourceDetailDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), managerDao, resourceDB);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}

}
