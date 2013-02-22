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
package com.hangum.tadpole.application.start.action;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.widgets.ExternalBrowser;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.swtdesigner.ResourceManager;

public class BugIssueAction extends Action {
	private final IWorkbenchWindow window;
	
	public BugIssueAction(IWorkbenchWindow window) {
		super(Messages.BugIssueAction_0);
		setId(this.getClass().getName());
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(BrowserActivator.ID, "resources/icons/bugAndIssue.png"));
		setToolTipText("Github Issue");
		
		this.window = window;
	}
	
	public void run() {
		ExternalBrowser.open( "Issues", "https://github.com/hangum/TadpoleForDBTools/issues", ExternalBrowser.LOCATION_BAR | ExternalBrowser.NAVIGATION_BAR | ExternalBrowser.STATUS );
	}
}
