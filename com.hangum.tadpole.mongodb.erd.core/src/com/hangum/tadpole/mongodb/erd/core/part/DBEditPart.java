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
package com.hangum.tadpole.mongodb.erd.core.part;

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
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.rulers.RulerProvider;

import com.hangum.tadpole.mongodb.erd.core.figures.DBFigure;
import com.hangum.tadpole.mongodb.erd.core.policies.TableXYLayoutPolicy;
import com.hangum.tadpole.mongodb.model.DB;
import com.hangum.tadpole.mongodb.model.Table;
import com.swtdesigner.SWTResourceManager;

/**
 * DB edit part
 * 
 * @author hangum
 *
 */
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
	public Object getAdapter(Class key) {
		if (key == SnapToHelper.class) {
			List<SnapToHelper> snaps = new ArrayList<SnapToHelper>();
			
			Boolean bool = (Boolean)getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
			if(bool != null && bool) snaps.add(new SnapToGuides(this));

			bool = (Boolean)getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
			if (bool != null && bool) snaps.add(new SnapToGeometry(this));
			
			bool = (Boolean) getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
			if (bool != null && bool) snaps.add(new SnapToGrid(this));
			
			if (snaps.size() == 0) return null;
			if (snaps.size() == 1) return snaps.get(0);
			
			SnapToHelper[] ss = snaps.toArray(new SnapToHelper[0]);
			
			return new CompoundSnapToHelper(ss);
		}

		return super.getAdapter(key);
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
		cLayer.setForegroundColor(SWTResourceManager.getColor(255, 102, 102));//ColorConstants.red());//lightBlue());
		cLayer.setConnectionRouter(new ShortestPathConnectionRouter(getFigure()));
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TableXYLayoutPolicy());
	}

	@Override
	protected List<Table> getModelChildren() {
		List<Table> retVal = new ArrayList<Table>();

		DB db = (DB)getModel();
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
//			DB db = (DB) getModel();
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
