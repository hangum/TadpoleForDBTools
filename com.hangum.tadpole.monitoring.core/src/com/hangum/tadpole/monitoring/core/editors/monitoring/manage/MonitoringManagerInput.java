package com.hangum.tadpole.monitoring.core.editors.monitoring.manage;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * monitoring manage input
 * 
 * @author hangum
 *
 */
public class MonitoringManagerInput  implements IEditorInput {

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof MonitoringManagerInput) ) return false;
		return ((MonitoringManagerInput)obj).getName().equals(getName());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		return "Monitoring manage";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Monitoring manage";
	}

}