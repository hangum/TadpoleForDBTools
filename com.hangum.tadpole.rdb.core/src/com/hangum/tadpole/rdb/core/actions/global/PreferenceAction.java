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
package com.hangum.tadpole.rdb.core.actions.global;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.internal.OpenPreferencesAction;

import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.swtdesigner.ResourceManager;

public class PreferenceAction extends Action implements  IWorkbenchAction {
	private final IWorkbenchWindow window;
	public static final String ID = "com.hangum.db.browser.rap.core.actions.global.preference"; //$NON-NLS-1$

	public PreferenceAction(IWorkbenchWindow window) {
		this.window = window;

		setId(ID);
		setText(Messages.get().Menu_SettingsAndPreferences);
		setToolTipText(Messages.get().Menu_SettingsAndPreferences);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/preferences.png")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		IWorkbenchAction action = new OpenPreferencesAction(window);
		action.setId(getId());
		
		action.run();
	}

	@Override
	public void dispose() {
	}
}
