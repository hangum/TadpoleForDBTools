package com.hangum.db.browser.rap.core.actions.global;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.actions.erd.ERDDeleteAction;
import com.hangum.db.dao.system.UserDBResourceDAO;
import com.swtdesigner.ResourceManager;

/**
 * 현재 선택된 디비의 릴레이션 화면을 오픈합니다.
 * 
 * @author hangum
 *
 */
public class DeleteResourceAction extends Action implements ISelectionListener, IWorkbenchAction {
	private final IWorkbenchWindow window;
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.DeleteResourceAction"; //$NON-NLS-1$
	private IStructuredSelection iss;
	
	public DeleteResourceAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.DeleteResourceAction_0);
		setToolTipText(Messages.DeleteResourceAction_0);
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/delete-relation.png")); //$NON-NLS-1$
		setEnabled(false);
		
		window.getSelectionService().addPostSelectionListener(this);
	}
	
	@Override
	public void run() {
		UserDBResourceDAO userResourceDB = (UserDBResourceDAO)iss.getFirstElement();
		if(!MessageDialog.openConfirm(window.getShell(), Messages.DeleteResourceAction_3, userResourceDB.getFilename() + Messages.DeleteResourceAction_4)) return;
		
		ERDDeleteAction erdDelete = new ERDDeleteAction();
		erdDelete.run(userResourceDB);
	}
	

	@Override
	public void dispose() {
		window.getSelectionService().removePostSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IStructuredSelection sel = (IStructuredSelection)selection;
		if(sel != null) {
			if( sel.getFirstElement() instanceof UserDBResourceDAO ) {
				iss = sel;
				
				setEnabled(true);
			} else setEnabled(false);
		} else setEnabled(false);
	}

}
