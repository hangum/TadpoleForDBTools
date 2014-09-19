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
package com.hangum.tadpole.rdb.core.actions.admin;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.hangum.tadpole.sql.dao.system.UserDBDAO;

/**
 *  CSV file Import
 * 
 * @author hangum
 *
 */
public class CsvToRdbImportAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CsvToRdbImportAction.class);

	private IStructuredSelection sel;
	
	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
//		CsvToRDBImportDialog dialog = new CsvToRDBImportDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB);
		
//		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();		
//		try {
//			MongoDBImportEditorInput input = new MongoDBImportEditorInput(userDB);
//			page.openEditor(input, MongoDBImportEditor.ID, false);
//			
//		} catch (PartInitException e) {
//			logger.error("Mongodb import", e);
//			
//			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
//			ExceptionDetailsErrorDialog.openError(null, "Error", "MongoDB Import Exception", errStatus); //$NON-NLS-1$
//		}
		logger.debug("===> CVS to RDB DB import");
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}
}
