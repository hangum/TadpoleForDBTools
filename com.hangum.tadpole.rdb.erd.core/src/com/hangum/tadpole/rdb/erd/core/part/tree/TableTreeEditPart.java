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
package com.hangum.tadpole.rdb.erd.core.part.tree;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.editparts.AbstractTreeEditPart;

import com.hangum.tadpole.rdb.erd.stanalone.Activator;
import com.hangum.tadpole.rdb.model.Table;
import com.swtdesigner.ResourceManager;

public class TableTreeEditPart extends AbstractTreeEditPart {

	private TableAdapter adapter;
	public TableTreeEditPart() {
		super();
		adapter = new TableAdapter();
	}
	
	@Override
	protected void refreshVisuals() {
		Table table = (Table)getModel();
		setWidgetText(table.getName());
		setWidgetImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/database_table.png"));
	}
	
	@Override
	public void activate() {
		if(!isActive()) {
			((Table)getModel()).eAdapters().add(adapter);
		}
		super.activate();
	}
	
	@Override
	public void deactivate() {
		if(isActive()) {
			((Table)getModel()).eAdapters().remove(adapter);
		}
		super.deactivate();
	}
	
	public class TableAdapter implements Adapter {

		@Override
		public void notifyChanged(Notification notification) {
			refreshVisuals();
			refreshChildren();
		}

		@Override
		public Notifier getTarget() {
			return (Table)getModel();
		}

		@Override
		public void setTarget(Notifier newTarget) {
		}

		@Override
		public boolean isAdapterForType(Object type) {
			return type.equals(Table.class);
		}

	}
}
