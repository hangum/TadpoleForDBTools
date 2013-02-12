/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.erd.stanalone;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.erd.core.editor.TadpoleMongoDBEditorInput;
import com.hangum.tadpole.mongodb.erd.core.editor.TadpoleMongoDBERDEditor;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "com.hangum.tadpole.erd.core.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}
	
	@Override
	public void postStartup() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		try {
			UserDBDAO loginInfo = new UserDBDAO();
			loginInfo.setDisplay_name("newdb");
			loginInfo.setUrl("127.0.0.1:27017");
			loginInfo.setHost("127.0.0.1:27017");
			loginInfo.setDb("newdb");
			
			TadpoleMongoDBEditorInput input = new TadpoleMongoDBEditorInput("Standalone Test", loginInfo, true);
			page.openEditor(input, TadpoleMongoDBERDEditor.ID, false);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
