package com.hangum.db.browser.rap.core.actions.nosql.mongodb;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.ext.dialog.ServerStatusDialog;

/**
 * mongodb server status action
 * @author hangum
 *
 */
public class MongodbDBStatusAction implements IViewActionDelegate {
	private IStructuredSelection sel;

	public MongodbDBStatusAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(IAction action) {
		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
		
		ServerStatusDialog dialog = new ServerStatusDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB);
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
