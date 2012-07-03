package com.hangum.tadpole.erd.stanalone;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.tadpole.erd.core.editor.TadpoleEditor;
import com.hangum.tadpole.erd.core.editor.TadpoleEditorInput;

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
			TadpoleEditorInput input = new TadpoleEditorInput("Standalone Test", loginInfo, true);
			page.openEditor(input, TadpoleEditor.ID, false);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
