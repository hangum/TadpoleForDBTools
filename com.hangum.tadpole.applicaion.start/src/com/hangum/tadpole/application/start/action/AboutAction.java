/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.application.start.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.application.start.dialog.about.AboutDialog;
import com.swtdesigner.ResourceManager;

/**
 * Opens an &quot;About RAP&quot; message dialog.
 */
public class AboutAction extends Action {
	
	private final IWorkbenchWindow window;
	
	public AboutAction(IWorkbenchWindow window) {
		super(Messages.get().AboutAction_0);
		setId(this.getClass().getName());
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(BrowserActivator.APPLICTION_ID, "resources/icons/about.png"));
		setToolTipText(Messages.get().AboutAction_TDB);
		
		this.window = window;
	}
	
	public void run() {
		if(window != null) {	
			AboutDialog ad = new AboutDialog(window.getShell());
			ad.open();
			
		}
	}
	
}
