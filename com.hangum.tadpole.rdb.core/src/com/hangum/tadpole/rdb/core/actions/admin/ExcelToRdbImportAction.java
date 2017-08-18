package com.hangum.tadpole.rdb.core.actions.admin;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.importexport.core.dialogs.CsvToRDBImportDialog;
import com.hangum.tadpole.rdb.core.Messages;

public class ExcelToRdbImportAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CsvToRdbImportAction.class);

	private IStructuredSelection sel;
	
	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		if(DBGroupDefine.ALTIBASE_GROUP == userDB.getDBGroup()) {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), CommonMessages.get().Information, Messages.get().MainEditor_DoesnotSupport);
		} else {
			boolean isPossible = false;
			if(PermissionChecker.isDBAdminRole(userDB)) isPossible = true;
			else {
				if(!PermissionChecker.isProductBackup(userDB)) isPossible = true;
			}
				
			if(isPossible) {
				CsvToRDBImportDialog dialog = new CsvToRDBImportDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), userDB);
				dialog.open();
			} else {
				MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), CommonMessages.get().Information, Messages.get().MainEditor_21);
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}
}