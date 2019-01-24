package org.jalgo.module.bfsdfs.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * Collection of helper methods to solve different graph calculations.
 * @author Anselm Schmidt
 *
 */
public abstract class GraphCalculation {
	/**
	 * Calculate the distance between the intersections of two circles.
	 * @param x1 X coordinate of the first circle center.
	 * @param y1 Y coordinate of the first circle center.
	 * @param r1 Radius of the first circle.
	 * @param x2 X coordinate of the second circle center.
	 * @param y2 Y coordinate of the second circle center.
	 * @param r2 Radius of the second circle.
	 * @author Anselm Schmidt
	 */
	public static double calcCircleCircleIntersecDist(double x1, double y1,
			double r1, double x2, double y2, double r2) {
		// calculate distance between circles
		double dx = x2 - x1;
		double dy = y2 - y1;
        double d = Math.sqrt(dx * dx + dy * dy);
        
        // calculate distance between first circle center and intersections line
        double a = (r1 * r1 - r2 * r2 + d * d) / (2 * d);
        
        // calculate distance between intersections
        return 2 * Math.sqrt(r1 * r1 - a * a);
	}
	
	/**
	 * Calculate the center point of a text.
	 * @param g Used <code>Graphics</code> instance.  
	 * @param label Used text.
	 * @return Correct coordinates to write the text.
	 * @author Florian Dornbusch
	 */
	public static Point calcTextCenter(Graphics g, String label, Font font,
			int size) {
		Integer textHeight, textWidth, x, y;
		Rectangle2D rect;

		FontMetrics fm = g.getFontMetrics(font);
		rect = fm.getStringBounds(label, g);
		textHeight = (int) (rect.getHeight());
		textWidth = (int) (rect.getWidth());
		x = (size - textWidth) / 2;
		y = (size - textHeight) / 2 + fm.getAscent();

		return new Point(x, y);
	}
	
	/**
	 * Calculate the size of a text.
	 * @param g Used <code>Graphics</code> instance.  
	 * @param label Used text.
	 * @return Correct coordinates to write the text.
	 * @author Florian Dornbusch
	 */
	public static int calcTextWidth(Graphics g, String label, Font font) {
		Rectangle2D rect;

		FontMetrics fm = g.getFontMetrics(font);
		rect = fm.getStringBounds(label, g);
		
		return (int) rect.getWidth();
	}

	/**
	 * Calculate the squared distance between two points
	 * 
	 * @param x1
	 *            X coordinate of the first point.
	 * @param y1
	 *            Y coordinate of the first point.
	 * @param x2
	 *            Y coordinate of the second point.
	 * @param y2
	 *            Y coordinate of the second point.
	 * @result The squared distance between the two points.
	 * @author Anselm Schmidt
	 */
	public static double calcPointPointDistSqr(double x1, double y1, double x2,
			double y2) {
		// calculate distance vector
		double distX = x2 - x1;
		double distY = y2 - y1;

		// return size of the distance vector
		return distX * distX + distY * distY;
	}
	
	/**
	 * Calculate the squared distance between two points
	 * 
	 * @param p1 First point.
	 * @param p2 Second point.
	 * @result The squared distance between the two points.
	 * @author Anselm Schmidt
	 */
	public static double calcPointPointDistSqr(Point p1, Point p2) {
		return calcPointPointDistSqr(p1.x, p1.y, p2.x, p2.y);
	}

	/**
	 * Calculate the squared distance between a point and a line.
	 * 
	 * @param x1
	 *            The x coordinate of the first point on the line.
	 * @param y1
	 *            The y coordinate of the first point on the line.      
	 * @param x2
	 *            The x coordinate of the second point on the line.
	 * @param y2
	 *            The y coordinate of the second point on the line.
	 * @param x3
	 *            The x coordinate of the point.
	 * @param y3
	 *            The y coordinate of the point.
	 * @result The squared distance between the specified point and the line.
	 * @author Anselm Schmidt
	 */
	public static double calcPointLineDistSqr(int x1, int y1, int x2, int y2, int x3,
			int y3) {
		// calculate the squared length of the line vector
		double sqrLength = calcPointPointDistSqr(x1, y1, x2, y2);

		// calculate factor of the direction vector
		double u = (double) ((x3 - x1) * (x2 - x1) + (y3 - y1) * (y2 - y1))
			/ sqrLength;

		// calculate point of intersection of the tangent
		double x = (double) x1 + u * (x2 - x1);
		double y = (double) y1 + u * (y2 - y1);

		// calculate the distance from there to p3
		return calcPointPointDistSqr(x, y, x3, y3);
	}
	
	/**
	 * Check, which part of a bidirectional edge has been hit by a mouse click.
	 * 
	 * @param x1
	 *            The x coordinate of one end of the edge.
	 * @param y1
	 *            The y coordinate of one end of the edge.
	 * @param x2
	 *            The x coordinate of the other end of the edge.
	 * @param y2
	 *            The y coordinate of the other end of the edge.
	 * @param hit
	 *            Position of the mouse click.
	 * @return <code>1</code>, if the edge has been hit at the first end.
	 *         <code>2</code>, if the edge has been hit at the other end.
	 *         <code>0</code> if the edge has been hit somewhere else.
	 * @author Anselm Schmidt
	 */
	public static short getBidirectionalEdgeHitArea(int x1, int y1, int x2, int y2,
			Point hit, int arrowLength, int nodeSize) {
		// calculate the squared distance between point and line
		double sqrDistance = calcPointLineDistSqr(x1, y1, x2, y2, hit.x, hit.y);

		// calculate the squared distances between edge ends and hitting point
		double sqrHitDist1 = calcPointPointDistSqr(x1, y1, hit.x, hit.y);
		double sqrHitDist2 = calcPointPointDistSqr(x2, y2, hit.x, hit.y);

		// calculate the distances between edge ends and line hitting point
		double dist1 = Math.sqrt(sqrHitDist1 - sqrDistance);
		double dist2 = Math.sqrt(sqrHitDist2 - sqrDistance);

		// check the shortest distance
		if (dist1 < dist2) {
			if (dist1 < 2 * arrowLength + nodeSize / 2) {
				// edge 1 has been hit
				return 1;
			}
		} else {
			if (dist2 < 2 * arrowLength + nodeSize / 2) {
				// edge 2 has been hit
				return 2;
			}
		}

		return 0;
	}
}
