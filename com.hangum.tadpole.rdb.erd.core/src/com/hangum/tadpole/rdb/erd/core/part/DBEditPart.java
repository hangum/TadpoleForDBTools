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
package com.hangum.tadpole.rdb.erd.core.part;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.hangum.tadpole.rdb.erd.core.figures.DBFigure;
import com.hangum.tadpole.rdb.erd.core.policies.TableXYLayoutPolicy;
import com.hangum.tadpole.rdb.model.DB;
import com.hangum.tadpole.rdb.model.Table;

public class DBEditPart extends AbstractGraphicalEditPart implements LayerConstants  {
	private static final Logger logger = Logger.getLogger(DBEditPart.class);
	private  DBAdapter adapter;
	
	public DBEditPart() {
		super();
		adapter = new DBAdapter();
	}

	@Override
	protected IFigure createFigure() {
		IFigure figure = new DBFigure();
		return figure;
	}
	
	@Override
	protected void refreshVisuals() {
		DBFigure figure = (DBFigure)getFigure();
		DB model = (DB)getModel();
		
		figure.setLabelDBType(model.getDbType());
		figure.setLabelID(model.getId());
		figure.setLabelURL(model.getUrl());
		
		// connection router 조절
		ConnectionLayer cLayer = (ConnectionLayer)getLayer(CONNECTION_LAYER);
		cLayer.setForegroundColor(ColorConstants.lightGray);
		cLayer.setConnectionRouter(new ShortestPathConnectionRouter(getFigure()));
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TableXYLayoutPolicy());
	}

	@Override
	protected List<Table> getModelChildren() {
		List<Table> retVal = new ArrayList<Table>();

		DB db = (DB) getModel();
		retVal.addAll(db.getTables());

		return retVal;
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
			DB db = (DB) getModel();
//			logger.debug("\t ######################## [DB] " + db.getSid());
			
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
