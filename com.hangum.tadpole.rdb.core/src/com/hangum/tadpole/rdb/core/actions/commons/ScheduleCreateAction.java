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
//package com.hangum.tadpole.rdb.core.actions.commons;
//
//import org.apache.log4j.Logger;
//import org.eclipse.jface.action.IAction;
//import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.ui.IViewActionDelegate;
//import org.eclipse.ui.IViewPart;
//import org.eclipse.ui.PlatformUI;
//
//import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
//import com.hangum.tadpole.monitoring.core.dialogs.schedule.AddScheduleDialog;
//
///**
// * Schedule create action
// * 
// * @author hangum
// *
// */
//public class ScheduleCreateAction implements IViewActionDelegate {
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(ScheduleCreateAction.class);
//	private IStructuredSelection iss;
//	
//	public ScheduleCreateAction() {
//	}
//	
//	@Override
//	public void run(IAction action) {
//		if(!iss.isEmpty()) {
//			UserDBDAO userDB = (UserDBDAO)iss.getFirstElement();
//			
//			AddScheduleDialog dialog = new AddScheduleDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB);
//			dialog.open();
//		}
//	}
//
//
//	@Override
//	public void selectionChanged(IAction action, ISelection selection) {
//		iss = (IStructuredSelection)selection;
//	}
//
//	@Override
//	public void init(IViewPart view) {
//	}
//
//}
