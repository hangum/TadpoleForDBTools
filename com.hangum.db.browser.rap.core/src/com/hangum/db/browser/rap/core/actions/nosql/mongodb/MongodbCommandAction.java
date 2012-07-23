//package com.hangum.db.browser.rap.core.actions.nosql.mongodb;
//
//import org.eclipse.jface.action.IAction;
//import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.ui.IViewActionDelegate;
//import org.eclipse.ui.IViewPart;
//import org.eclipse.ui.PlatformUI;
//
//import com.hangum.db.dao.system.UserDBDAO;
//import com.hangum.tadpole.mongodb.core.ext.dialog.ShellCommandDialog;
//
///**
// * mongodb shell command action
// * 
// * @author hangum
// *
// */
//public class MongodbCommandAction implements IViewActionDelegate {
//	private IStructuredSelection sel;
//	
//	public MongodbCommandAction() {
//	}
//
//	@Override
//	public void run(IAction action) {
//		UserDBDAO userDB = (UserDBDAO)sel.getFirstElement();
//		
//		ShellCommandDialog dialog = new ShellCommandDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB);
//		dialog.open();
//	}
//
//	@Override
//	public void selectionChanged(IAction action, ISelection selection) {
//		sel = (IStructuredSelection)selection;
//	}
//
//	@Override
//	public void init(IViewPart view) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
