/*******************************************************************************
 * Copyright Tadplehub (c) 2016 hangum.
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system.bill;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.engine.Messages;

/**
 * 서비스 결제 
 * 
 * @author hangum
 *
 */
public class UserBillEditorInput implements IEditorInput {

	public UserBillEditorInput() {
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof UserBillEditorInput) ) return false;
		return ((UserBillEditorInput)obj).getName().equals(getName());
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
		return Messages.get().ServiceBill;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return Messages.get().ServiceBill;
	}
	
}
