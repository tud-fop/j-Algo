package org.jalgo.module.dijkstra.gui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;

import org.jalgo.module.dijkstra.gfx.NewEdgeLine;
import org.jalgo.module.dijkstra.gfx.Visual;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.event.GraphDisplayActionHandler;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.model.Node;
import org.jalgo.module.dijkstra.model.State;

/**
 * The class <code>GraphDisplay</code> is responsible for drawing the current
 * graph to the screen. Furthermore it contains methods to find the graph
 * element under a specific screen point.
 * 
 * @author Alexander Claus
 */
public class GraphDisplay
extends JComponent
implements Observer {

	private Controller controller;
	private GraphDisplayActionHandler actionHandler;

	//the offscreen context
	private BufferedImage offI;
	private Graphics2D offG;

	private Graph graph;
	private NewEdgeLine newEdgeLine;

	private static Dimension screenSize = new Dimension();

	public GraphDisplay(Controller controller) {
		super();
		this.controller = controller;
		offI = new BufferedImage(1600, 1600, BufferedImage.TYPE_INT_RGB);
		offG = offI.createGraphics();
		offG.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		actionHandler = new GraphDisplayActionHandler(this, controller);
		addMouseListener(actionHandler);
		addMouseMotionListener(actionHandler);
		controller.addObserver(this);
	}

	/**
	 * Paints the graph.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		offG.setColor(Color.WHITE);
		offG.fillRect(0, 0, getWidth(), getHeight());

		if (controller.getEditingMode() == Controller.MODE_ALGORITHM) {
			State dj = controller.getState(controller.getCurrentStep());
			if (dj != null) graph = dj.getGraph();
			else graph = controller.getGraph();
		}
		else graph = controller.getGraph();

		for (Edge edge : graph.getEdgeList())
			edge.getVisual().draw(offG, screenSize);
		if (newEdgeLine != null) {
			offG.setColor(Visual.RED);
			offG.setStroke(new BasicStroke(7));
			offG.draw(newEdgeLine);
		}
		for (Node node : graph.getNodeList())
			node.getVisual().draw(offG, screenSize);

		g.drawImage(offI, 0, 0, this);
	}

	/**
	 * This method is overridden to rescale the graph, when component is resized.
	 */
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		screenSize.width = width;
		screenSize.height = height;

		// update positions of all elements
		if (controller.getEditingMode() != Controller.MODE_ALGORITHM)
			graph = controller.getGraph();
		else {
			State dj = controller.getState(controller.getCurrentStep());
			if (dj != null) graph = dj.getGraph();
			else graph = controller.getGraph();
		}
		for (Edge edge : graph.getEdgeList())
			edge.getVisual().updateLocation(screenSize);
		for (Node node : graph.getNodeList())
			node.getVisual().updateLocation(screenSize);

		repaint();
	}

	/**
	 * Updates the contents.
	 */
	public void update(Observable o, Object arg) {
		if (controller.getEditingMode() != Controller.MODE_ALGORITHM)
			graph = controller.getGraph();
		else {
			State dj = controller.getState(controller.getCurrentStep());
			if (dj != null) graph = dj.getGraph();
			else graph = controller.getGraph();
		}
		repaint();
	}

	/**
	 * Finds the <code>Node</code> under the given point.
	 * 
	 * @param point the screen coordinates to test
	 * @return the underlying node, or <code>null</code>, if there is no such
	 */
	public Node findNode(Point point) {
		Graph graph = controller.getGraph();
		for (Node node : graph.getNodeList())
			if (node.getVisual().hit(screenSize, point)) return node;
		return null;
	}

	/**
	 * Finds the <code>Edge</code> under the given point.
	 * 
	 * @param point the screen coordinates to test
	 * @return the underlying edge, or <code>null</code>, if there is no such
	 */
	public Edge findEdge(Point point) {
		Graph graph = controller.getGraph();
		for (Edge edge : graph.getEdgeList())
			if (edge.getVisual().hit(screenSize, point)) return edge;
		return null;
	}

	public void setNewEdgeLine(NewEdgeLine line) {
		newEdgeLine = line;
	}

	public NewEdgeLine getNewEdgeLine() {
		return newEdgeLine;
	}

	/**
	 * Currently for easy creation of NodeVisuals
	 */
	public static Dimension getScreenSize() {
		return screenSize;
	}
}