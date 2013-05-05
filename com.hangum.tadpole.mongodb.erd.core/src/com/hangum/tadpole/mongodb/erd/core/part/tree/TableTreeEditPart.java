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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.editparts.AbstractTreeEditPart;

import com.hangum.tadpole.mongodb.erd.stanalone.Activator;
import com.hangum.tadpole.mongodb.model.Table;
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
