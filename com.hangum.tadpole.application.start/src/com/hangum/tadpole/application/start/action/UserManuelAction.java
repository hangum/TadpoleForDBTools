/*******************************************************************************
 * Copyright (c) 2016 hangum.
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
 * user manuel action
 * @author hangum
 *
 */
public class UserManuelAction extends Action {
//	private final IWorkbenchWindow window;
	
	public UserManuelAction(IWorkbenchWindow window) {
		super(Messages.get().OpenUserManuel);
		setId(this.getClass().getName());
		setImageDescriptor( ResourceManager.getPluginImageDescriptor(BrowserActivator.ID, "resources/icons/document.png")); //$NON-NLS-1$
		setToolTipText(Messages.get().OpenUserManuel);
		
//		this.window = window;
	}
	
	public void run() {
		UrlLauncher launcher = RWT.getClient().getService( UrlLauncher.class );
		launcher.openURL("https://tadpoledbhub.atlassian.net/wiki/pages/viewpage.action?pageId=20578325");
	}
}
