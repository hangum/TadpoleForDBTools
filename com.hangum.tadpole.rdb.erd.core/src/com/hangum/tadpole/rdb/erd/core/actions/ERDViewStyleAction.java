///*******************************************************************************
// * Copyright (c) 2014 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.erd.core.actions;
//
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.actions.RetargetAction;
//
///**
// * ERD view styled select action
// * 
// * @author hangum
// *
// */
//public class ERDViewStyleAction extends RetargetAction {//Action implements ISelectionListener, IWorkbenchAction {
//	
//	private final IWorkbenchWindow window;
//	private final static String ID = "com.hangum.tadpole.rdb.erd.actions.global.ERDViewStyleAction"; //$NON-NLS-1$
//	private IStructuredSelection sel;
//	
//	public ERDViewStyleAction(String actionID, String text) {
//		super(actionID, text);
//
//		
//	}
//
//	
////	public ERDViewStyleAction() {
////	}
////	
////	public ERDViewStyleAction(IWorkbenchWindow window) {
////		this.window = window;
////		
////		setId(ID);
////		setText("View Style Select");
////		setToolTipText("View Style Select");
////		setImageDescriptor( ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/connect.png"));
////		
////		window.getSelectionService().addPostSelectionListener(this);
////	}
//	
//	@Override
//	public void run() {
//		System.out.println("===========================================================");
//		
////		runConnectionDialog(sel);
//	}
//	
////	public void runConnectionDialog(IStructuredSelection sel) {
////		String selGroupName = "";
//////		
//////		if(sel != null) {
//////			if(sel.getFirstElement() instanceof ManagerListDTO) {
//////				ManagerListDTO mana = (ManagerListDTO)sel.getFirstElement();
//////				selGroupName = mana.getName();
//////			} else if(sel.getFirstElement() instanceof UserDBDAO) {
//////				UserDBDAO user =(UserDBDAO)sel.getFirstElement();
//////				selGroupName = user.getParent().getName();
//////			}
//////		}
//////		
//////		final DBLoginDialog dialog = new DBLoginDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), selGroupName);
//////		final int ret = dialog.open();
//////		
//////		final UserDBDAO userDB = dialog.getDTO();
//////		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);			
//////		
//////		Display.getCurrent().asyncExec(new Runnable() {
//////			@Override
//////			public void run() {
//////				if(ret == Dialog.OK) {
//////					if(userDB == null) managerView.init();
//////					else managerView.addUserDB(userDB, true);
//////				}
//////				else managerView.init();
//////			}
//////		});	// end display
////	}
////	
////
////	@Override
////	public void dispose() {
////		window.getSelectionService().removePostSelectionListener(this);
////	}
////
////	@Override
////	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
////		sel = (IStructuredSelection)selection;
////	}
//
//}
