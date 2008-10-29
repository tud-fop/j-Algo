package org.jalgo.module.app.view.graph;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.LineMetrics;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.view.InterfaceConstants;

/**
 * The graphical representation of a node in the user interface. This class
 * wraps an object from type Node.
 */
public class NodeElement extends GraphElement {

	private static final long serialVersionUID = 1938758344902933851L;

	private Node node;
	private Ellipse2D ellipse;
	private Stroke stroke;
	private String label;
	
	private static boolean beamerMode;


	public NodeElement(Node node) {
		this.node = node;

		beamerMode = false;
		highlightState = HighlightState.NONE;
	}

	@Override
	public boolean contains(Point2D point) {
		return getShape().contains(point);
	}

	/*
	 * the mouseDragged method in NewEdgeElement doesn't find the contains
	 * method. So I use this.
	 */
	public boolean nodeContains(Point2D point) {
		return getShape().contains(point);
	}

	@Override
	public void paint(Graphics2D g) {

		ellipse = getShape();

		// Draw content
		g.setColor(InterfaceConstants
				.fillColorForHighlightStateNode(highlightState));
		g.fill(ellipse);

		// Draw border
		stroke = new BasicStroke(InterfaceConstants.getNodeBorderWidth(beamerMode));
		g.setStroke(stroke);
		g.setColor(InterfaceConstants
				.borderColorForHighlightState(highlightState));
		g.draw(ellipse);

		// Draw label
		Rectangle2D labelSize;
		LineMetrics metrics;
		Font font;

		label = Integer.toString(node.getId() + 1);

		font = InterfaceConstants.getGraphNodeFont(beamerMode);
		labelSize = font.getStringBounds(label, g.getFontRenderContext());
		metrics = font.getLineMetrics(label, g.getFontRenderContext());

		g.setColor(InterfaceConstants
				.textColorForHighlightStateNode(highlightState));
		g.setFont(font);

		g.drawString(label, (float) (ellipse.getCenterX() - labelSize
				.getWidth() / 2), (float) (ellipse.getCenterY()
				- metrics.getHeight() / 2 + metrics.getAscent()));
	}

	private Ellipse2D getShape() {
		Dimension2D dimension;
		Ellipse2D ellipse;
		Point2D location;

		ellipse = new Ellipse2D.Float();
		location = new Point2D.Float();

		location = component.getScaledLocation(node.getLocation());
		
		dimension = new Dimension((int) InterfaceConstants.getNodeDiameter(beamerMode),
				(int) InterfaceConstants.getNodeDiameter(beamerMode));

		location.setLocation(location.getX() - dimension.getWidth() / 2,
				location.getY() - dimension.getHeight() / 2);
		ellipse.setFrame(location, dimension);

		return ellipse;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
	public void updateBeamerMode(boolean beamerMode) {
		NodeElement.beamerMode = beamerMode;
	}
		
	public void mouseDragged(MouseEvent event) {
		component.setIgnoreEvents(true);
		
		/*if(event.getPoint().x <= component.getWidth() - 20
			&& event.getPoint().y <= component.getHeight() - 20
			&&event.getPoint().y >= component.getLocation().y + 20
			&&event.getPoint().x >= component.getLocation().x + 20)*/
			component.getGraphActionListener().alterNodePosition(node,
					component.getCoreLocation(event.getPoint())
					);
		
		component.setIgnoreEvents(false);
	}

	public void keyReleased(KeyEvent e) {
		component.setIgnoreEvents(true);
		
		if (e.getKeyCode() == KeyEvent.VK_DELETE
				|| e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			component.getGraphActionListener().removeNode(node);
		}
		
		component.setIgnoreEvents(false);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.app.view.graph.GraphElement#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		//Memorize node position for undo concerns.
		component.getGraphActionListener().setPreviousPosition(component.getCoreLocation(e.getPoint()));
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.app.view.graph.GraphElement#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		//Realize that the movement of a node has ended.
		component.getGraphActionListener().setFinalNodePosition(node, component.getCoreLocation(e.getPoint()));
		
			
			
		
	}
	
	
	
}
