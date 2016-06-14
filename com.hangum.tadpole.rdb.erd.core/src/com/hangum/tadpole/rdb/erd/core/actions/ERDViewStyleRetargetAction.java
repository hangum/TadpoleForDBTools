/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.erd.core.actions;

import org.eclipse.ui.actions.RetargetAction;

import com.hangum.tadpole.rdb.erd.core.Messages;
import com.hangum.tadpole.rdb.erd.stanalone.Activator;
import com.swtdesigner.ResourceManager;

/**
 * view style select 
 * 
 * @author hangum
 *
 */
public class ERDViewStyleRetargetAction extends RetargetAction {
	
	public ERDViewStyleRetargetAction() {
		super(ERDViewStyleAction.ID, Messages.get().ShowColumn);
		setToolTipText(Messages.get().ShowColumn);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/setting_gear_-16.png"));
	}

}
