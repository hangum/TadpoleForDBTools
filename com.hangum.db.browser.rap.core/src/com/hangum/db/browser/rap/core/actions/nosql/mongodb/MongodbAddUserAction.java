package com.hangum.db.browser.rap.core.actions.nosql.mongodb;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.dialogs.users.AddUserDialog;

/**
 * mongodb currentOp action
 * 	
 * @author hangum
 *
 */
public class MongodbAddUserAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongodbAddUserAction.class);

	private IStructuredSelection sel;

	public MongodbAddUserAction() {
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		AddUserDialog dialog = new AddUserDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}

}
