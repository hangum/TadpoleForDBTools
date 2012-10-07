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
package com.hangum.tadpole.browser.rap.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.browser.rap.BrowserActivator;
import com.hangum.tadpole.browser.rap.Messages;
import com.hangum.tadpole.browser.rap.dialog.about.AboutDialog;
import com.swtdesigner.ResourceManager;

/**
 * Opens an &quot;About RAP&quot; message dialog.
 */
public class AboutAction extends Action {
	
	private final IWorkbenchWindow window;
	
	public AboutAction(IWorkbenchWindow window) {
		super(Messages.AboutAction_0);
		setId(this.getClass().getName());
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(BrowserActivator.ID, "resources/icons/about.png"));
		
		this.window = window;
	}
	
	public void run() {
		if(window != null) {	
//			String title = Messages.AboutAction_1;
//			
//			String msg =   Messages.AboutAction_2
//						 + Messages.AboutAction_3
//			             + Messages.AboutAction_4 
//			             + Messages.AboutAction_5;
//			MessageDialog.openInformation( window.getShell(), title, msg );
			AboutDialog ad = new AboutDialog(window.getShell());
			ad.open();
			
		}
	}
	
}
