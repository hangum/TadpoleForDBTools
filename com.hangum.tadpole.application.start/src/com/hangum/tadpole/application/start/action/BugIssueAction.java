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
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.UrlLauncher;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.swtdesigner.ResourceManager;

/**
 * bug issue action
 * @author hangum
 *
 */
public class BugIssueAction extends Action {
	private final IWorkbenchWindow window;
	
	public BugIssueAction(IWorkbenchWindow window) {
		super(Messages.get().BugIssueAction_0);
		setId(this.getClass().getName());
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(BrowserActivator.ID, "resources/icons/bugAndIssue.png")); //$NON-NLS-1$
		setToolTipText(Messages.get().BugIssueAction_0);
		
		this.window = window;
	}
	
	public void run() {
		UrlLauncher launcher = RWT.getClient().getService( UrlLauncher.class );
		launcher.openURL("https://github.com/hangum/TadpoleForDBTools/issues");
	}
}
