package org.jalgo.module.bfsdfs.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * Collection of helper methods to draw different graph elements.
 * @author Anselm Schmidt
 *
 */
public abstract class GraphDrawing {	
	/**
	 * Draw an arrowhead at the specified point with the specified direction.
	 * NOTE: The arrowhead direction vector has to be an unit vector.
	 * Color and alpha values have to be set already.
	 * @param g Used <code>Graphics</code> instance.
	 * @param position The position of the arrowhead.
	 * @param directionX The x coordinate of the arrowhead direction vector.
	 * @param directionY The y coordinate of the arrowhead direction vector.
	 * @param width The width of the arrowhead.
	 * Use <code>GUIConstants.EDGE_WIDTH</code> as default value.
	 * @param length The length of the arrowhead.
	 * Use <code>GUIConstants.EDGE_LENGTH</code> as default value.
	 * @param drawMiddle <code>True</code>, if the arrow middle has to be drawn, too.
	 * For example, if the arrow has another color than the rest of the edge.
	 * <code>False</code>, otherwise. NOTE: If <code>alpha</code> is lower than
	 * <code>255</code>, it should be set to <code>False</code> to keep the right
	 * transparency.
	 * @author Anselm Schmidt
	 */
	private static void drawArrowhead(Graphics g, Point position,
			double directionX, double directionY, int width, int length) {		
		// calculate vector with arrow length and direction 
		double lengthX = directionX * length;
		double lengthY = directionY * length;
		
		// calculate position back from arrow end by arrow length
		double backX = (double) position.x - lengthX;
		double backY = (double) position.y - lengthY;
		
		// calculate right-handed normal by rotating the direction vector
		double normalRightX = directionY;
		double normalRightY = - directionX;
			
		// calculate left-handed normal by rotating the direction vector
		double normalLeftX = - directionY;
		double normalLeftY = directionX;
		
		// calculate right side point by sizing the right-handed normal
		double rightX = backX + normalRightX * width;
		double rightY = backY + normalRightY * width;
			
		// calculate left side point by sizing the right-handed normal
		double leftX = backX + normalLeftX * width;
		double leftY = backY + normalLeftY * width;
		
		// draw right side line of arrow
		g.drawLine(position.x, position.y, (int) Math.round(rightX),
				(int) Math.round(rightY)); 
		
		// draw left side line of arrow
		g.drawLine(position.x, position.y, (int) Math.round(leftX),
				(int) Math.round(leftY));
	}
	
	/**
	 * Draw a clipped circle edge with one arrowhead.
	 * Circular clipping will be performed at the begin and end of the circle edge.
	 * If <code>clippingRadius</code> is set to <code>0</code>,
	 * no clipping will be performed.
	 * The circle edge will always be drawn above the position.
	 * @param g Used <code>Graphics</code> instance.
	 * @param position Begin and end of the circle edge.
	 * @param lineWidth The line width of the circle edge.
	 * @param clippingRadius The radius of the clipping circle.
	 * @param color The color of the circle edge.
	 * @param arrowWidth The width of the arrowhead.
	 * Use <code>GUIConstants.EDGE_WIDTH</code> as default value.
	 * @param arrowLength The length of the arrowhead.
	 * Use <code>GUIConstants.EDGE_LENGTH</code> as default value.
	 * @param circleEdgeSize The diameter of the circle edge.
	 * Use <code>GUIConstants.EDGE_CIRCLE_SIZE</code> as default value.
	 * @param alpha Alpha value for drawing.
	 * Use <code>255</code> as default value.
	 * @author Anselm Schmidt
	 */
	private static void drawClippedCircleEdge(Graphics g, Point position,
			int lineWidth, int clippingRadius, Color color, int arrowWidth,
			int arrowLength, int circleEdgeSize, int alpha) {
		if(alpha == 0) {
			return;
		}
		
		// set transparency
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		
		// calculate circle edge radius	
		int radius = (int) Math.round((float) circleEdgeSize / 2f);
		
		// calculate circle edge center
		Point center = new Point(position.x, position.y - radius);
		
		// calculate distance between circle intersections
		double intersectionDist =
			GraphCalculation.calcCircleCircleIntersecDist(position.x, position.y,
					clippingRadius, center.x, center.y, radius);
		
		// calculate angle between circle intersections,
		// relative to circle edge center
		double intersectionAngle = Math.acos(1 -
				(intersectionDist * intersectionDist) / (2 * radius * radius));
		
		// convert angle from radians to degrees and round it to int
		int angle = (int) Math.round(Math.toDegrees(intersectionAngle));
		
		// set color
		g.setColor(color);
		
		// set line width
		((Graphics2D) g).setStroke(new BasicStroke(lineWidth));
		
		// draw arc of circle edge
		g.drawArc(center.x - radius, center.y - radius, circleEdgeSize,
				circleEdgeSize, 270 + (int) Math.round((float) angle / 2f), 360 -
				angle);
				
		// calculate arrowhead position, relative to circle edge center
		Point arrowPos = new Point((int) Math.round(Math.sin(intersectionAngle /
				2d) * radius), (int) Math.round(Math.cos(intersectionAngle / 2d) *
						radius));
		
		// convert the arrowhead position vector into unit vector
		double length = Math.sqrt(arrowPos.x * arrowPos.x + arrowPos.y * arrowPos.y);
		double unitX = (double) arrowPos.x / length;
		double unitY = (double) arrowPos.y / length;
		
		// create unit arrow direction vector by rotating this unit vector by alpha
		double angleAlpha = Math.toRadians(70d);
		double arrowDirX = unitX * Math.cos(angleAlpha) - unitY *
			Math.sin(angleAlpha);
		double arrowDirY = unitX * Math.sin(angleAlpha) + unitY *
			Math.cos(angleAlpha);
		
		// draw arrowhead
		drawArrowhead(g, new Point(center.x + arrowPos.x, center.y + arrowPos.y),
				arrowDirX, arrowDirY, arrowWidth, arrowLength);				
	}
	
	/**
	 * Enable Anti-Aliasing for specified <code>Graphics</code> instance.
	 * @param g The used <code>Graphics</code> instance.
	 * @author Anselm Schmidt
	 */
	public static void enableAntiAliasing(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	/**
	 * Draw a node.
	 * @param g Used <code>Graphics</code> instance.
	 * @param label Text that will be shown in the center of the node.
	 * This should be the id of the node.
	 * If it is <code>null</code>, no label will be shown.
	 * @param position The position of the node.
	 * @param font The font that will be used for the text in the center of the node.
	 * Use <code>GUIConstants.NODE_FONT</code> as default value.
	 * @param size The diameter of the node.
	 * Use <code>GUIConstants.NODE_SIZE</code> as default value.
	 * @param borderSize The width of the node border.
	 * Use <code>GUIConstants.NODE_BORDER_SIZE</code> as default value.
	 * @param borderColor The color of the node border.
	 * Use <code>GUIConstants.NODE_BORDER_COLOR</code> as default value.
	 * @param labelColor The color of the text in the center of the node.
	 * Use <code>GUIConstants.NODE_LABEL_COLOR</code> as default value.
	 * @param topColor The background color at the top of the node.
	 * Use <code>GUIConstants.NODE_COLOR_TOP</code> as default value.
	 * @param bottomColor The background color at the bottom of the node.
	 * Use <code>GUIConstants.NODE_COLOR_BOTTOM</code> as default value.
	 * @param gradientHeight Height of the color gradient at the top of the inner circle
	 * Use <code>GUIConstants.NODE_GRADIENT_HEIGHT</code> as default value.  
	 * @param alpha Alpha value for drawing.
	 * @param numberFont Used font for the number near the node.
	 * Will be ignored, if <code>number</code> is <code>0</code>.
	 * @param numberColor Used color for the number near the node.
	 * Will be ignored, if <code>number</code> is <code>0</code>.
	 * @param number Number to write near the node.
	 * Will be ignored, if it is <code>0</code>.
	 * <code>Integer.MAX_VALUE</code> will be interpreted as infinite. 
	 * @author Anselm Schmidt
	 */
	public static void drawNode(Graphics g, String label, Point position,
			Font font, int size, int borderSize, Color borderColor,
			Color labelColor, Color topColor, Color bottomColor, int alpha,
			int gradientHeight, Font numberFont, Color numberColor, int number) {
		if(alpha == 0) {
			return;
		}
		
		Graphics2D g2 = (Graphics2D) g;
		
		// set transparency
		topColor = new Color(topColor.getRed(), topColor.getGreen(), topColor.getBlue(),
				alpha);
		bottomColor = new Color(bottomColor.getRed(), bottomColor.getGreen(),
				bottomColor.getBlue(), alpha);
		borderColor = new Color(borderColor.getRed(), borderColor.getGreen(),
				borderColor.getBlue(), alpha);
		if(labelColor != null) { 
			labelColor = new Color(labelColor.getRed(), labelColor.getGreen(),
					labelColor.getBlue(), alpha);
		}
		if(numberColor != null) {		
			numberColor = new Color(numberColor.getRed(), numberColor.getGreen(),
					numberColor.getBlue(), alpha);
		}
		
		// calculate the node radius
		int radius = (int) Math.round((float) size / 2f);
		
		// calculate the coordinates of the top left node rectangle corner
		int topLeftX = position.x - radius;
		int topLeftY = position.y - radius;
		
		// calculate inner circle attributes
		int circleX = topLeftX + borderSize - 2;
		int circleY = topLeftY + borderSize - 2;
		int circleWidth = size - 2 * borderSize + 4;
		int circleHeight = size - 2 * borderSize + 4;
		
		// save old paint settings
		Paint oldPaint = g2.getPaint();
		
		// draw color gradient
		Paint gradPaint = new GradientPaint(circleX, circleY, topColor, circleX,
				circleY + gradientHeight, bottomColor);
		g2.setPaint(gradPaint);
		g.fillOval(circleX, circleY, circleWidth, circleHeight);
		
		// set color and stroke of the border
		g2.setPaint(oldPaint);
		g.setColor(borderColor);
		g2.setStroke(new BasicStroke(borderSize));
		
		// draw the border
		g.drawOval(topLeftX, topLeftY, size - borderSize / 2, size - borderSize / 2);
		
		if(label != null && label.length() > 0) {
			// set font and color
			g.setFont(font);
			g.setColor(labelColor);
			
			// finally write the label on the node
			g.drawString(label, topLeftX + GraphCalculation.calcTextCenter(g, label,
					font, size).x, topLeftY + GraphCalculation.calcTextCenter(g, label,
					font, size).y);
		}
		
		if(number > 0) {
			// get string to write
			String string = String.valueOf(number);
			
			if(number == Integer.MAX_VALUE) {
				string = "inf";
			}
			
			// calc text position 
			Point pos = new Point(position.x + radius, position.y - radius);
			
			// set font and color
			g.setFont(numberFont);
			g.setColor(numberColor);
			
			// draw number
			g.drawString(string, pos.x, pos.y);
		}
	}
	
	/**
	 * Draw a node without small number next to it.
	 * @param g Used <code>Graphics</code> instance.
	 * @param label Text that will be shown in the center of the node.
	 * This should be the id of the node.
	 * If it is <code>null</code>, no label will be shown.
	 * @param position The position of the node.
	 * @param font The font that will be used for the text in the center of the node.
	 * Use <code>GUIConstants.NODE_FONT</code> as default value.
	 * @param size The diameter of the node.
	 * Use <code>GUIConstants.NODE_SIZE</code> as default value.
	 * @param borderSize The width of the node border.
	 * Use <code>GUIConstants.NODE_BORDER_SIZE</code> as default value.
	 * @param borderColor The color of the node border.
	 * Use <code>GUIConstants.NODE_BORDER_COLOR</code> as default value.
	 * @param labelColor The color of the text in the center of the node.
	 * Use <code>GUIConstants.NODE_LABEL_COLOR</code> as default value.
	 * @param topColor The background color at the top of the node.
	 * Use <code>GUIConstants.NODE_COLOR_TOP</code> as default value.
	 * @param bottomColor The background color at the bottom of the node.
	 * Use <code>GUIConstants.NODE_COLOR_BOTTOM</code> as default value.
	 * @param alpha Alpha value for drawing.
	 * @param gradientHeight Height of the color gradient at the top of the inner circle
	 * Use <code>GUIConstants.NODE_GRADIENT_HEIGHT</code> as default value.
	 * @author Anselm Schmidt
	 */
	public static void drawNode(Graphics g, String label, Point position,
			Font font, int size, int borderSize, Color borderColor,
			Color labelColor, Color topColor, Color bottomColor, int alpha,
			int gradientHeight) {		
		// draw a node without small number next to it
		drawNode(g, label, position, font, size, borderSize, borderColor, labelColor,
				topColor, bottomColor, alpha, gradientHeight, null, null, 0);
	}
	
	/**
	 * Draw a clipped edge.
	 * Clipping will be performed at the begin and the end of the edge.
	 * The clipping values can be set separately.
	 * If a clipping value is <code>0</code>, no clipping will be performed at
	 * this position.
	 * The position of the arrows will be changed by the performed clipping.
	 * If the edge ends at the same position as it starts,
	 * a circle edge will be drawn.
	 * NOTE: The edge has to be long enough for the clipping to be performed! 
	 * @param g Used <code>Graphics</code> instance.
	 * @param start Start point of the edge. Clipping of the begin will start here.
	 * @param end End point of the edge. Clipping of the end will end here.
	 * @param width The width of the edge line.
	 * Use <code>GUIConstants.EDGE_WIDTH</code> as default value.
	 * @param bidirectional If <code>True</code>, arrows will be drawn on both sides.
	 * If <code>False</code>, only one arrow will be drawn at the end of the edge.
	 * @param clipBegin Number of pixels that will be removed at the begin of the
	 * edge line.
	 * @param clipEnd Number of pixels that will be removed at the end of the edge
	 * line.
	 * @param startColor The color of the edge start.
	 * Use <code>GUIConstants.EDGE_COLOR</code> as default value.
	 * Will be ignored, when <code>bidirectional</code> is <code>False</code>.
	 * @param endColor The color of the edge end.
	 * Use <code>GUIConstants.EDGE_COLOR</code> as default value.
	 * @param edgeColor The color of the edge middle.
	 * Use <code>GUIConstants.EDGE_COLOR</code> as default value.
	 * @param arrowWidth Width of the arrow(s) of the edge.
	 * Use <code>GUIConstants.EDGE_ARROW_WIDTH</code> as default value.
	 * @param arrowLength Length of the arrow(s) of the edge.
	 * Use <code>GUIConstants.EDGE_ARROW_LENGTH</code> as default value.
	 * @param circleEdgeSize The diameter of a circle edge.
	 * Use <code>GUIConstants.EDGE_CIRCLE_SIZE</code> as default value.
	 * @param alpha Alpha value for drawing.
	 * Use <code>255</code> as default value.
	 * @author Anselm Schmidt
	 */
	public static void drawClippedEdge(Graphics g, Point start, Point end,
			int width, boolean bidirectional, int clipBegin, int clipEnd,
			Color startColor, Color endColor, Color edgeColor, int arrowWidth,
			int arrowLength, int circleEdgeSize, int alpha) {
		if(alpha == 0) {
			return;
		}
		
		// check if edge is a circle
		if(start.equals(end)) {
			// draw circle edge instead of line edge
			drawClippedCircleEdge(g, start, width, clipBegin, edgeColor, arrowWidth,
					arrowLength, circleEdgeSize, alpha);
		}
		else {			
			// set transparency
			startColor = new Color(startColor.getRed(), startColor.getGreen(),
					startColor.getBlue(), alpha);
			endColor = new Color(endColor.getRed(), endColor.getGreen(),
					endColor.getBlue(), alpha);
			edgeColor = new Color(edgeColor.getRed(), edgeColor.getGreen(),
					edgeColor.getBlue(), alpha);
			
			// set line width
			((Graphics2D) g).setStroke(new BasicStroke(width));
			
			// calculate edge vector
			double edgeVectorX = end.x - start.x;
			double edgeVectorY = end.y - start.y;
			double edgeVectorLength = Math.sqrt(edgeVectorX * edgeVectorX +
					edgeVectorY * edgeVectorY);
			
			// create unit vector from edge vector
			double unitVectorX = edgeVectorX / edgeVectorLength;
			double unitVectorY = edgeVectorY / edgeVectorLength;
			
			// edge length is okay: change length of edge vector into clipping size
			edgeVectorX = unitVectorX * clipBegin;
			edgeVectorY = unitVectorY * clipBegin;
			
			// calculate the coordinates of the line start point
			Point newStart = new Point(start.x + (int) Math.round(edgeVectorX),
					start.y + (int) Math.round(edgeVectorY));
			
			// change length of edge vector into end clipping size
			edgeVectorX = unitVectorX * clipEnd;
			edgeVectorY = unitVectorY * clipEnd;
			
			// calculate the coordinates of the line end point
			Point newEnd = new Point(end.x - (int) Math.round(edgeVectorX), 
					end.y - (int) Math.round(edgeVectorY));
			
			// draw edge end with specified color
			g.setColor(endColor);
			drawArrowhead(g, newEnd, unitVectorX, unitVectorY, arrowWidth,
					arrowLength);
			
			if(bidirectional) {
				// edge is bidirectional: draw edge start with specified color
				g.setColor(startColor);
				drawArrowhead(g, newStart, -unitVectorX, -unitVectorY, arrowWidth,
						arrowLength);
				
				newStart.x += (int) Math.round(unitVectorX * width);
				newStart.y += (int) Math.round(unitVectorY * width);
			}
			
			// correct end position
			newEnd.x -= (int) Math.round(unitVectorX * width);
			newEnd.y -= (int) Math.round(unitVectorY * width);
			
			// set color to edge line color
			g.setColor(edgeColor);
							
			// draw edge line
			g.drawLine(newStart.x, newStart.y, newEnd.x, newEnd.y);
		}
	}
	
	/**
	 * Draw a node cursor over the border of a node.
	 * @param position The position of the selected node.
	 * @param nodeSize The size of the node.
	 * Use <code>GUIConstants.NODE_SIZE</code> as default value.
	 * @param sizeFactor
	 * The size of the cursor will be <code>sizeFactor</code> times the size of a node.
	 * @param borderSize The size of the border.
	 * Use <code>GUIConstants.NODE_BORDER_SIZE</code> as default value.
	 * @param color The color of the node cursor.
	 * Use <code>GUIConstants.NODE_CURSOR_COLOR</code> as default value.
	 * @param alpha The alpha value of the node cursor.
	 * @return The rectangle around the node cursor.
	 * @author Anselm Schmidt
	 */
	public static Rectangle drawNodeCursor(Graphics g, Point position, int nodeSize,
			float sizeFactor, int borderSize, Color color, int alpha) {
		// scale node size
		int scaledNodeSize = (int) Math.round((double) nodeSize * sizeFactor);
		
		// calculate node cursor radius
		int radius = (int) Math.round((double) scaledNodeSize / 2);
		
		// calculate the coordinates of the top left node cursor rectangle corner
		int topLeftX = position.x - radius;
		int topLeftY = position.y - radius;
		
		if(alpha > 0) {
			// set transparency
			color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
			
			// set color and stroke of the node cursor
			g.setColor(color);
			((Graphics2D) g).setStroke(new BasicStroke(borderSize));
			
			// draw node cursor circle
			g.drawOval(topLeftX, topLeftY, scaledNodeSize - borderSize / 2,
					scaledNodeSize - borderSize / 2);
		}
		
		return new Rectangle(topLeftX - 1, topLeftY - 1, scaledNodeSize + 2,
				scaledNodeSize + 2);
	}
}