package com.hangum.db.browser.rap.core.actions.connections;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.editors.main.MainEditorInput;
import com.hangum.db.browser.rap.core.viewers.connections.ManagerViewer;
import com.hangum.db.browser.rap.core.viewers.object.ExplorerViewer;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.ManagerListDTO;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * database disconnect
 * 
 * @author hangumNote
 *
 */
public class DisconnectDBAction implements IViewActionDelegate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DisconnectDBAction.class);

	private IStructuredSelection sel;
	
	public DisconnectDBAction() {
		super();
	}

	@Override
	public void run(IAction action) {

		final UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();

		// 실제 db connection 삭제 
		try {
			TadpoleSQLManager.removeInstance(userDB);			
		} catch (Exception e) {
			logger.error("remove Instnce db", e);
		}
		
		// Connection tree 삭제
		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				managerView.removeTreeList(userDB.getType(), userDB);
			}
		});	// end display
		
		// editor 삭제
		MainEditorInput mei = new MainEditorInput(userDB);		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorReference[] editroRefreence = page.getEditorReferences();

		String titleName = mei.getName();
		for (IEditorReference iEditorReference : editroRefreence) {
			if(iEditorReference.getTitle().indexOf(titleName) != -1) {
				page.closeEditor(iEditorReference.getEditor(false), true);
			}
		}
		
		// object view를 초기화합니다.
		final ExplorerViewer explorerView = (ExplorerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ExplorerViewer.ID);
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				explorerView.initObjectHead(new ManagerListDTO(userDB.getDisplay_name(), DBDefine.getDBDefine(userDB.getType()) ));
			}
		});	// end display
		
		// realdb disconnect
		try {
			TadpoleSQLManager.removeInstance(userDB);			
		} catch (Exception e) { 
			logger.error("disconnection exception", e);			
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", "Disconnection Exception", errStatus); //$NON-NLS-1$
		}
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		sel = (IStructuredSelection)selection;
	}

	@Override
	public void init(IViewPart view) {
	}

}
