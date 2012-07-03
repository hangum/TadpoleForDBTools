package com.hangum.db.browser.rap.core.actions.global;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.dialog.dbconnect.DBLoginDialog;
import com.hangum.db.browser.rap.core.viewers.connections.ManagerViewer;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.ManagerListDTO;
import com.hangum.db.dao.system.UserDBDAO;
import com.swtdesigner.ResourceManager;

/**
 * 전체 영역에서 사용하기 위한 action 
 * 
 * @author hangum
 *
 */
public class ConnectDatabaseAction extends Action implements ISelectionListener, IWorkbenchAction {
	private final IWorkbenchWindow window;
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.ConnectDatabaseAction"; //$NON-NLS-1$
	private IStructuredSelection sel;
	
	public ConnectDatabaseAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.ConnectDatabaseAction_1);
		setToolTipText(Messages.ConnectDatabaseAction_2);
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/connect.png"));
		
		window.getSelectionService().addPostSelectionListener(this);
	}
	
	@Override
	public void run() {
		runConnectionDialog(sel);
	}
	
	public void runConnectionDialog(IStructuredSelection sel) {
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
	public void dispose() {
		window.getSelectionService().removePostSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		sel = (IStructuredSelection)selection;

	}

}
