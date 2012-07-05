package com.hangum.db.browser.rap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.hangum.db.browser.rap.core.editors.intro.IntroEditor;
import com.hangum.db.browser.rap.core.editors.intro.IntroEditorInput;
import com.hangum.db.start.TadpoleSystem;
import com.hangum.db.util.ApplicationArgumentUtils;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	private static final Logger logger = Logger.getLogger(ApplicationWorkbenchAdvisor.class);
	private static final String PERSPECTIVE_ID = "com.hangum.db.browser.rap.perspective";

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
			IntroEditorInput mei = new IntroEditorInput();
			page.openEditor(mei, IntroEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		};
	}
	
//	@Override
//	public boolean preShutdown() {
//		if( MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "확인", "종료하시겠습니까?") ) {
//			return super.preShutdown();
//		}
//		return false;
//	}
}
