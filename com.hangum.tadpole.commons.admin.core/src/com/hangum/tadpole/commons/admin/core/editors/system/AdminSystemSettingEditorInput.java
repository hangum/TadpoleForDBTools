/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.editors.system;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.commons.admin.core.Messages;

/**
 * Admin System setting editor input
 * 
 * @author hangum
 *
 */
public class AdminSystemSettingEditorInput implements IEditorInput {

	public AdminSystemSettingEditorInput() {
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof AdminSystemSettingEditorInput) ) return false;
		return ((AdminSystemSettingEditorInput)obj).getName().equals(getName());
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return Messages.get().SystemSetting;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return Messages.get().SystemSetting;//"System Setting";
	}	
}
