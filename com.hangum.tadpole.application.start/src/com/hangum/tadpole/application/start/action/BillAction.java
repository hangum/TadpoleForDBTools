//package com.hangum.tadpole.application.start.action;
//
//import org.apache.log4j.Logger;
//import org.eclipse.core.runtime.IStatus;
//import org.eclipse.core.runtime.Status;
//import org.eclipse.jface.action.Action;
//import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.ui.ISelectionListener;
//import org.eclipse.ui.IWorkbenchPart;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.PartInitException;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
//
//import com.hangum.tadpole.application.start.BrowserActivator;
//import com.hangum.tadpole.application.start.Messages;
//import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
//import com.hangum.tadpole.commons.libs.core.define.DefineExternalPlguin;
//import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
//import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
//import com.hangum.tadpole.engine.query.dao.system.bill.UserBillEditorInput;
//import com.swtdesigner.ResourceManager;
//
///**
// * bill action
// * 
// * @author hangum
// *
// */
//public class BillAction  extends Action implements ISelectionListener, IWorkbenchAction {
//	private static final Logger logger = Logger.getLogger(BillAction.class);
//	private final static String ID = "com.hangum.tadpole.bill.core.actions.BillAction"; //$NON-NLS-1$
//	protected final IWorkbenchWindow window;
//	protected IStructuredSelection iss;
//	protected UserDBDAO userDB;
//	
//	public BillAction(IWorkbenchWindow window) {
//		this.window = window;
//		
//		setId(ID);
//		setText(Messages.get().ServiceBill);
//		setToolTipText(Messages.get().ServiceBill);
//		setImageDescriptor( ResourceManager.getPluginImageDescriptor(BrowserActivator.ID, "resources/icons/cart.png"));
//		setEnabled(true);
//		
//		window.getSelectionService().addPostSelectionListener(this);
//	}
//	
//	@Override
//	public void run() {
//		UserBillEditorInput mei = new UserBillEditorInput();
//		
//		try {
//			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(mei, DefineExternalPlguin.BILL_PLUGIN);
//		} catch (PartInitException e) {
//			logger.error("open editor", e); //$NON-NLS-1$
//			
//			Status errStatus = new Status(IStatus.ERROR, BrowserActivator.ID, e.getMessage(), e); //$NON-NLS-1$
//			ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, "Bill page open", errStatus); //$NON-NLS-1$
//		}
//	}
//	
//	@Override
//	public void dispose() {
//		window.getSelectionService().removePostSelectionListener(this);
//	}
//
//	@Override
//	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
//		IStructuredSelection sel = (IStructuredSelection)selection;
//
//		setEnabled(true);
//	}
//}
