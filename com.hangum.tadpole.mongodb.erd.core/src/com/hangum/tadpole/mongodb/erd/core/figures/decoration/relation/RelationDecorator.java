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
package com.hangum.tadpole.mongodb.erd.core.figures.decoration.relation;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transform;

import com.hangum.tadpole.mongodb.model.RelationKind;

/**
 * relation decoration
 * 
 * @author hangum
 *
 */
public class RelationDecorator extends Shape implements RotatableDecoration {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RelationDecorator.class);

	/** location */
	private Point location = new Point();
	
	/** relation kind */
	private PointList relation = new PointList();
	
	private Transform transform = new Transform();
	
	/** bend point */
	private PointList points;

	/** one or many */
	public static final PointList ONE_OR_MANY = new PointList();
	static {		
		ONE_OR_MANY.addPoint(0, 3);
		ONE_OR_MANY.addPoint(-8, 0);
		ONE_OR_MANY.addPoint(-8, 3);
		ONE_OR_MANY.addPoint(-8, 0);
		ONE_OR_MANY.addPoint(-8, -3);
		ONE_OR_MANY.addPoint(-8, 0);
		ONE_OR_MANY.addPoint(0, -3);
	}
	
	/** zero or many */
	public static final PointList ZERO_OR_MANY = new PointList();	
	static {
		ZERO_OR_MANY.addPoint(-8, 0);
		ZERO_OR_MANY.addPoint(0, 3);
		ZERO_OR_MANY.addPoint(-8, 0);
		ZERO_OR_MANY.addPoint(0, -3);
		ZERO_OR_MANY.addPoint(-8, 0);
		ZERO_OR_MANY.addPoint(-8, -1);
		ZERO_OR_MANY.addPoint(-9, -2);
		ZERO_OR_MANY.addPoint(-11, -2);
		ZERO_OR_MANY.addPoint(-12, -1);
		ZERO_OR_MANY.addPoint(-12, 1);
		ZERO_OR_MANY.addPoint(-11, 2);
		ZERO_OR_MANY.addPoint(-9, 2);
		ZERO_OR_MANY.addPoint(-8, 1);
		ZERO_OR_MANY.addPoint(-8, 0);
	}

	/** only one */
	public static final PointList ONE_ONLY = new PointList();
	static {
		ONE_ONLY.addPoint(-4, -3);
		ONE_ONLY.addPoint(-4, 0);
		ONE_ONLY.addPoint(-4, 3);
		ONE_ONLY.addPoint(-4, 0);
		ONE_ONLY.addPoint(-7, 0);
		ONE_ONLY.addPoint(-7, -3);
		ONE_ONLY.addPoint(-7, 3);
	}
	
	/** zero or one */
	public static final PointList ZERO_OR_ONE = new PointList();	
	static {
		ZERO_OR_ONE.addPoint(-4, 0);
		ZERO_OR_ONE.addPoint(-4, -3);
		ZERO_OR_ONE.addPoint(-4, 0);
		ZERO_OR_ONE.addPoint(-4, 3);
		ZERO_OR_ONE.addPoint(-4, 0);
		ZERO_OR_ONE.addPoint(-7, 0);
		ZERO_OR_ONE.addPoint(-7, -1);
		ZERO_OR_ONE.addPoint(-8, -2);
		ZERO_OR_ONE.addPoint(-10, -2);
		ZERO_OR_ONE.addPoint(-11, -1);
		ZERO_OR_ONE.addPoint(-11, 1);
		ZERO_OR_ONE.addPoint(-10, 2);
		ZERO_OR_ONE.addPoint(-8, 2);
		ZERO_OR_ONE.addPoint(-7, 1);
		ZERO_OR_ONE.addPoint(-7, 0);
	}
	
	/**
	 * relation decoreator
	 * 
	 * @param relationKindName
	 */
	public RelationDecorator(String relationKindName) {
		setRelationKind(RelationKind.getByName(relationKindName));
//		setBackgroundColor(ColorConstants.black);
	}

	public Rectangle getBounds() {
		if (bounds == null) {
			bounds = getPoints().getBounds().getExpanded(getLineWidth() / 2, getLineWidth() / 2);
		}
		
		return bounds;
	}

	protected void outlineShape(Graphics g) {
		g.drawPolyline(points);
	}

	public void primTranslate(int i, int j) {
	}

	public void setPoints(PointList points) {
		erase();
		this.points = points;
		bounds = null;
		
		firePropertyChange("points", null, points);
		repaint();
	}

	public PointList getPoints() {
	
		if (points == null) {
			points = new PointList();
			for (int i = 0; i < relation.size(); i++) {
				points.addPoint(transform.getTransformed(relation.getPoint(i)));
			}

		}
		
		return points;
	}

	public void setLocation(Point p) {
		points = null;
		bounds = null;
		location.setLocation(p);
		transform.setTranslation(p.x, p.y);
	}

	public void setReferencePoint(Point ref) {
		Point pt = Point.SINGLETON;
		pt.setLocation(ref);
		pt.negate().translate(location);
		setRotation(Math.atan2(pt.y, pt.x));
	}

	public void setRotation(double angle) {
		points = null;
		bounds = null;
		transform.setRotation(angle);
	}

//	public void paintFigure(Graphics graphics) {
//		graphics.setAntialias(1);
//		super.paintFigure(graphics);
//	}

	@Override
	protected void fillShape(Graphics graphics) {
	}

	private void setRelationKind(RelationKind relationKind) {
		
		switch(relationKind.getValue()) {
			case RelationKind.ONLY_ONE_VALUE:
				relation = ONE_ONLY;
				break;
			case RelationKind.ONE_OR_MANY_VALUE:
				relation = ONE_OR_MANY;
				break;
			case RelationKind.ZERO_OR_MANY_VALUE:
				relation = ZERO_OR_MANY;
				break;
			case RelationKind.ZERO_OR_ONE_VALUE:
				relation = ZERO_OR_ONE;
				break;
		}
		
		setPoints(null);
	}
}
