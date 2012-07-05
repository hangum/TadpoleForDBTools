package com.hangum.tadpole.erd.core.figures.decoration.relation;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transform;

import com.hangum.tadpole.model.RelationKind;

/**
 * realation decoration
 * 
 * @author hangum
 *
 */
public class RelationDecorator extends Shape implements RotatableDecoration {
	
	public static final PointList ONE_OR_MANY;
	public static final PointList ZERO_OR_MANY;
	public static final PointList ONE_ONLY;
	public static final PointList ZERO_OR_ONE;
	
	private Point location;
	private PointList template;
	private Transform transform;
	
	private RelationKind relationKind;
	private PointList points;
	private static final Rectangle LINEBOUNDS;

	static {
		ONE_OR_MANY = new PointList();
		ZERO_OR_MANY = new PointList();
		ONE_ONLY = new PointList();
		ZERO_OR_ONE = new PointList();
		
		ONE_OR_MANY.addPoint(0, 3);
		ONE_OR_MANY.addPoint(-8, 0);
		ONE_OR_MANY.addPoint(-8, 3);
		ONE_OR_MANY.addPoint(-8, 0);
		ONE_OR_MANY.addPoint(-8, -3);
		ONE_OR_MANY.addPoint(-8, 0);
		ONE_OR_MANY.addPoint(0, -3);
		
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
		
		ONE_ONLY.addPoint(-4, -3);
		ONE_ONLY.addPoint(-4, 0);
		ONE_ONLY.addPoint(-4, 3);
		ONE_ONLY.addPoint(-4, 0);
		ONE_ONLY.addPoint(-7, 0);
		ONE_ONLY.addPoint(-7, -3);
		ONE_ONLY.addPoint(-7, 3);
		
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
		
		LINEBOUNDS = Rectangle.SINGLETON;
	}

	public RelationDecorator(String relationKindName) {
		location = new Point();
		template = ONE_OR_MANY;
		transform = new Transform();
		
		this.relationKind = RelationKind.getByName(relationKindName);
		
		points = new PointList();
		bounds = null;
		setMultiplicity(relationKind);
		setBackgroundColor(ColorConstants.black);
	}

	public void addPoint(Point pt) {
		points.addPoint(pt);
		bounds = null;
		repaint();
	}

	public boolean containsPoint(int x, int y) {
		int tolerance = lineWidth / 2 + 2;
		LINEBOUNDS.setBounds(getBounds());
		LINEBOUNDS.expand(tolerance, tolerance);
		if (!LINEBOUNDS.contains(x, y))
			return false;
		int ints[] = points.toIntArray();
		for (int index = 0; index < ints.length - 3; index += 2)
			if (lineContainsPoint(ints[index], ints[index + 1],
					ints[index + 2], ints[index + 3], x, y, tolerance))
				return true;

		List children = getChildren();
		for (int i = 0; i < children.size(); i++)
			if (((IFigure) children.get(i)).containsPoint(x, y))
				return true;

		return false;
	}

	private boolean lineContainsPoint(int x1, int y1, int x2, int y2, int px,
			int py, int tolerance) {
		LINEBOUNDS.setSize(0, 0);
		LINEBOUNDS.setLocation(x1, y1);
		LINEBOUNDS.union(x2, y2);
		LINEBOUNDS.expand(tolerance, tolerance);
		if (!LINEBOUNDS.contains(px, py))
			return false;
		int result = 0;
		if (x1 != x2 && y1 != y2) {
			int v1x = x2 - x1;
			int v1y = y2 - y1;
			int v2x = px - x1;
			int v2y = py - y1;
			int numerator = v2x * v1y - v1x * v2y;
			int denominator = v1x * v1x + v1y * v1y;
			result = (int) (((long) numerator * (long) numerator) / (long) denominator);
		}
		return result <= tolerance * tolerance;
	}

	protected void fillShape(Graphics g1) {
	}

	public Rectangle getBounds() {
		if (bounds == null)
			bounds = getPoints().getBounds().getExpanded(lineWidth / 2, lineWidth / 2);
		return bounds;
	}

	public Point getEnd() {
		return points.getLastPoint();
	}

	public Point getStart() {
		return points.getFirstPoint();
	}

	public void insertPoint(Point pt, int index) {
		bounds = null;
		points.insertPoint(pt, index);
		repaint();
	}

	public boolean isOpaque() {
		return false;
	}

	protected void outlineShape(Graphics g) {
		g.drawPolyline(points);
	}

	public void primTranslate(int i, int j) {
	}

	public void removeAllPoints() {
		erase();
		bounds = null;
		points.removeAllPoints();
	}

	public void removePoint(int index) {
		erase();
		bounds = null;
		points.removePoint(index);
	}

	public void setEnd(Point end) {
		if (points.size() < 2)
			addPoint(end);
		else
			setPoint(end, points.size() - 1);
	}

	public void setEndpoints(Point start, Point end) {
		setStart(start);
		setEnd(end);
	}

	public void setLineWidth(int w) {
		if (lineWidth == w)
			return;
		if (w < lineWidth)
			erase();
		bounds = null;
		super.setLineWidth(w);
	}

	public void setPoint(Point pt, int index) {
		erase();
		points.setPoint(pt, index);
		bounds = null;
		repaint();
	}

	public void setPoints(PointList points) {
		erase();
		this.points = points;
		bounds = null;
		firePropertyChange("points", null, points);
		repaint();
	}

	public void setStart(Point start) {
		if (points.size() == 0)
			addPoint(start);
		else
			setPoint(start, 0);
	}

	protected boolean useLocalCoordinates() {
		return false;
	}

	public PointList getPoints() {
		if (points == null) {
			points = new PointList();
			for (int i = 0; i < template.size(); i++)
				points.addPoint(transform.getTransformed(template.getPoint(i)));

		}
		return points;
	}

	public void setLocation(Point p) {
		points = null;
		bounds = null;
		location.setLocation(p);
		transform.setTranslation(p.x, p.y);
	}

	public void setTemplate(PointList pl) {
		erase();
		template = pl;
		points = null;
		bounds = null;
		repaint();
	}

	public void setScale(double x, double y) {
		points = null;
		bounds = null;
		transform.setScale(x, y);
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

	public void setMultiplicity(RelationKind reloationKind) {
		
		switch(relationKind.getValue()) {
			case RelationKind.ONLY_ONE_VALUE:
				template = ONE_ONLY;
				break;
			case RelationKind.ONE_OR_MANY_VALUE:
				template = ONE_OR_MANY;
				break;
			case RelationKind.ZERO_OR_MANY_VALUE:
				template = ZERO_OR_MANY;
				break;
			case RelationKind.ZERO_OR_ONE_VALUE:
				template = ZERO_OR_ONE;
				break;
		}
		
		setPoints(null);
	}

	public void paintFigure(Graphics graphics) {
		graphics.setAntialias(1);
		super.paintFigure(graphics);
	}

}
