package com.hangum.db.browser.rap.core.actions.connections;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.browser.rap.core.dialog.dbconnect.DBLoginDialog;
import com.hangum.db.browser.rap.core.viewers.connections.ManagerViewer;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.ManagerListDTO;
import com.hangum.db.dao.system.UserDBDAO;

/**
 * db 연결 action
 * @author hangumNote
 *
 */
public class ConnectDatabase implements IViewActionDelegate {
	public static final String ID = "com.hangum.db.browser.rap.core.action.connect.database";
	private IStructuredSelection sel;
//	private IWorkbenchWindow window;

	@Override
	public void run(IAction action) {
		DBDefine selDbType = null;
		
		if(sel != null) {
			if(sel.getFirstElement() instanceof ManagerListDTO) {
				ManagerListDTO mana = (ManagerListDTO)sel.getFirstElement();
				selDbType = mana.getDbType();
			} else if(sel.getFirstElement() instanceof UserDBDAO) {
				UserDBDAO user =(UserDBDAO)sel.getFirstElement();
				selDbType = user.getParent().getDbType();
			}
		}
		
		final DBLoginDialog dialog = new DBLoginDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), selDbType);
		int ret = dialog.open();
		
		if(ret == Dialog.OK) {
			final UserDBDAO userDB = dialog.getDTO();
			final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
					
			Display.getCurrent().asyncExec(new Runnable() {
				@Override
				public void run() {
					managerView.addUserDB(userDB.getType(), userDB, true);
				}
			});	// end display
				
		}	// end fi
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection)selection;
		
	}

	@Override
	public void init(IViewPart view) {
	}
}
