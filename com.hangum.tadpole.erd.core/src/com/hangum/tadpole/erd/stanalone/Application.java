package com.hangum.tadpole.erd.stanalone;

import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint {

//	public Object start(IApplicationContext context) throws Exception {
//		Display display = PlatformUI.createDisplay();
//		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
//		return PlatformUI.createAndRunWorkbench(display, advisor);
//	}

	public void stop() {
		// Do nothing
	}

	@Override
	public int createUI() {
		Display display = PlatformUI.createDisplay();
		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
		return PlatformUI.createAndRunWorkbench(display, advisor);
	}
}
