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
package com.hangum.tadpole.mongodb.erd.core.part.tree;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.editparts.AbstractTreeEditPart;

import com.hangum.tadpole.mongodb.model.DB;

public class DBTreeEditPart extends AbstractTreeEditPart {
	private  DBAdapter adapter;
	
	public DBTreeEditPart() {
		super();
		adapter = new DBAdapter();
	}
	
	@Override
	protected List getModelChildren() {
		return ((DB)getModel()).getTables();
	}
	
	@Override
	protected void refreshVisuals() {
		DB db = (DB)getModel();
		setWidgetText(db.getId());
	}
	
	@Override
	public void activate() {
		if(!isActive()) {
			((DB)getModel()).eAdapters().add(adapter);
		}
		super.activate();
	}
	
	@Override
	public void deactivate() {
		if(isActive()) {
			((DB)getModel()).eAdapters().remove(adapter);
		}
		super.deactivate();
	}
	
	/**
	 * db event adapter
	 * 
	 * @author hangum
	 *
	 */
	public class DBAdapter implements Adapter {

		@Override
		public void notifyChanged(Notification notification) {
			refreshVisuals();
			refreshChildren();
		}

		@Override
		public Notifier getTarget() {
			return (DB)getModel();
		}

		@Override
		public void setTarget(Notifier newTarget) {
		}

		@Override
		public boolean isAdapterForType(Object type) {
			return type.equals(DB.class);
		}
		
	}
}
