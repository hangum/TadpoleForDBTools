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
