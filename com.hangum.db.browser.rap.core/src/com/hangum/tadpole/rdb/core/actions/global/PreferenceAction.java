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
package com.hangum.tadpole.rdb.core.actions.global;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.internal.OpenPreferencesAction;

import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

public class PreferenceAction extends Action implements  IWorkbenchAction {
	private final IWorkbenchWindow window;
	public static final String ID = "com.hangum.db.browser.rap.core.actions.global.preference";

	public PreferenceAction(IWorkbenchWindow window) {
		this.window = window;

		setId(ID);
		setText("Preferences");
		setToolTipText("Preferences");
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/preferences.png"));
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
