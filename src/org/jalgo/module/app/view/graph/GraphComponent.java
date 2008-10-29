package org.jalgo.module.app.view.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jalgo.module.app.controller.GraphActionListener;
import org.jalgo.module.app.controller.GraphController;
import org.jalgo.module.app.controller.GraphObserver;
import org.jalgo.module.app.controller.GraphActionListener.EditMode;
import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.core.step.AtomicStep;
import org.jalgo.module.app.core.step.GroupStep;
import org.jalgo.module.app.core.step.RootStep;
import org.jalgo.module.app.view.InterfaceConstants;
import org.jalgo.module.app.view.run.StepHighlighting;

public class GraphComponent extends JPanel implements StepHighlighting,
		GraphObserver, MouseListener, MouseMotionListener, KeyListener, FocusListener {

	private static final long serialVersionUID = -1899916065578097190L;

	private Set<NodeElement> nodeElements;
	private Set<EdgeElement> edgeElements;
	private GraphElement focussedElement;

	private GraphActionListener graphActionListener;
	private GraphController graphCtrl;

	private boolean isDragging;
	private boolean ignoreClick;
	private boolean ignoreNextClick;

	private boolean isDynamicScaling;
	
	private AtomicStep highlightedStep;
	
	private boolean beamerMode;

	private class ScalingInformation {
		float offsX, offsY;
		float scaleWidth, scaleHeight;
	}
	
	public GraphComponent(GraphController graphCtrl) {
		nodeElements = new HashSet<NodeElement>();
		edgeElements = new HashSet<EdgeElement>();
		focussedElement = null;
		ignoreNextClick = false;
		isDynamicScaling = false;
		beamerMode = false;
		
		this.graphCtrl = graphCtrl;

		initInterface();
		
	}

	/**
	 * Creates all relevant Interface thing
	 */
	private void initInterface() {
		JPanel testPanel = new JPanel();

		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		this.add(testPanel, BorderLayout.CENTER);

		testPanel.setLayout(new BorderLayout());
		testPanel.setBackground(Color.WHITE);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.addFocusListener(this);

		this.setFocusable(true);
		this.setLayout(null);
	}

	/**
	 * Initializes the element lists from the initial graph
	 */
	private void initElements() {
		Graph graph;

		graph = graphActionListener.getGraph();

		if (graph != null) {

			for (Edge edge : graph.getEdges())
				edgeAdded(graph, edge);
			for (Node node : graph.getNodes())
				nodeAdded(graph, node);
		}

		graphUpdated();
	}

	/**
	 * Triggers the component to redraw
	 */
	public void update() {
		repaint();
		revalidate();
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D g;

		// Setup
		g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw background
		g.setColor(Color.WHITE);
		g.fill(this.getBounds());

		// Draw edges
		for (EdgeElement edge : edgeElements)
			edge.paint(g);
		if (focussedElement instanceof NewEdgeElement)
			focussedElement.paint(g);

		// Draw nodes
		for (NodeElement node : nodeElements)
			node.paint(g);

		paintChildren(g);
	}

	private void setFocussedElementInternal(GraphElement element) {
		if (highlightedStep != null)
			throw new UnsupportedOperationException();

		// Change internal selection
		if (focussedElement != null)
			focussedElement.setHighlightState(HighlightState.NONE);

		focussedElement = element;

		if (focussedElement != null)
			focussedElement.setHighlightState(HighlightState.CURRENT);
	}
		
	public void setFocussedElement(GraphElement element) {
		setFocussedElementInternal(element);
		
		if (focussedElement == null
				|| focussedElement instanceof NewEdgeElement) {
			graphActionListener.setSelectedEdge(null);
		} else if (focussedElement instanceof EdgeElement)
			graphActionListener.setSelectedEdge(((EdgeElement) focussedElement)
					.getEdge());
		else
			graphActionListener.setSelectedNode(((NodeElement) focussedElement)
					.getNode());
	}

	
	private ScalingInformation getStaticScaleFactors() {
		ScalingInformation scales = new ScalingInformation();
		
		scales.offsX = 0;
		scales.offsY = 0;
		scales.scaleWidth = this.getWidth() / 1000f;
		scales.scaleHeight = this.getHeight() / 1000f;
		
		return scales;
	}	
	
	private ScalingInformation getDynamicScaleFactors() {
		float minX, minY, maxX, maxY;
		float curHeight, curWidth;
		float spaceX, spaceY;
		
		ScalingInformation scales = new ScalingInformation();
		
		if (graphActionListener.getGraph().getNodes().size() == 0)
			return getStaticScaleFactors();
		
		curHeight = (float)this.getHeight();
		curWidth = (float)this.getWidth();
		
		maxX = 0;
		maxY = 0;
		minX = 1000;
		minY = 1000;
		
		for (Node node : graphActionListener.getGraph().getNodes() ) {
			if (minX > node.getLocation().getX())
				minX = (float)node.getLocation().getX();
			
			if (minY > node.getLocation().getY())
				minY = (float)node.getLocation().getY();
			
			if (maxX < node.getLocation().getX())
				maxX = (float)node.getLocation().getX();

			if (maxY < node.getLocation().getY())
				maxY = (float)node.getLocation().getY();
		}

		spaceX = InterfaceConstants.getNodeDiameter(beamerMode) / (curWidth / 1000f);
		spaceY = InterfaceConstants.getNodeDiameter(beamerMode) / (curHeight / 1000f);		
		
		scales.scaleWidth = curWidth / (maxX + 2 * spaceX - minX);
		scales.scaleHeight = curHeight / (maxY + 2 * spaceY - minY);
		
		scales.offsX = minX * scales.scaleWidth - InterfaceConstants.getNodeDiameter(beamerMode);
		scales.offsY = minY * scales.scaleHeight - InterfaceConstants.getNodeDiameter(beamerMode);
		
		
		return scales;
	}

	public boolean isDynamicScaling() {
		return isDynamicScaling;		
	}
	
	public void setDynamicScaling(boolean state) {
		isDynamicScaling = state;
	}
	
	public Point2D getScaledLocation(Point2D location) {
		ScalingInformation scales;
		
		if (isDynamicScaling)
			scales = getDynamicScaleFactors();
		else
			scales = getStaticScaleFactors();
		
		return new Point2D.Float(((float)location.getX() * scales.scaleWidth) - scales.offsX,
								 ((float)location.getY() * scales.scaleHeight) - scales.offsY
						        );
	}
	
	public Point2D getCoreLocation(Point2D location) {
		ScalingInformation scales;
		
		if (isDynamicScaling)
			scales = getDynamicScaleFactors();
		else
			scales = getStaticScaleFactors();
		
		return new Point2D.Float(((float)location.getX() / scales.scaleWidth) + scales.offsX,
								 ((float)location.getY() / scales.scaleHeight) + scales.offsY
						        );
	}
	
	/**
	 * 
	 */
	public GraphElement getFocussedElement() {
		return focussedElement;
	}
	
	public void updateBeamerMode(boolean beamerMode) {
		this.beamerMode = beamerMode;
		for(EdgeElement e : edgeElements){
			e.updateBeamerMode(beamerMode);
		}
		for(NodeElement n : nodeElements){
			n.updateBeamerMode(beamerMode);
		}
		repaint();
		revalidate();
	}
	
	private void setHighlightedStep(AtomicStep step) {
		// Remove any selection
		if (focussedElement != null)
			setFocussedElement(null);

		// Erase current highlighting
		if (highlightedStep != null) {
			for (EdgeElement edge : edgeElements)
				edge.setHighlightState(HighlightState.NONE);
			for (NodeElement node : nodeElements)
				node.setHighlightState(HighlightState.NONE);
		}

		highlightedStep = step;

		if (highlightedStep != null) {
			List<Integer> nodes;
			HighlightState st;
			int k, n, n2;

			k = highlightedStep.getK();
			nodes = new ArrayList<Integer>(highlightedStep.getNodes());
			nodes.add(0, highlightedStep.getU());
			nodes.add(highlightedStep.getV());

			st = HighlightState.PRE_PATH;
			for (int i = 0; i < nodes.size(); i++) {
				NodeElement elem;

				n = nodes.get(i);

				elem = null;
				for (NodeElement e : nodeElements) {
					if (e.getNode().getId() == n) {
						elem = e;
						break;
					}
				}

				if (elem == null)
					continue;

				if (n == k) {
					elem.setHighlightState(HighlightState.CURRENT);
					st = HighlightState.POST_PATH;
				} else {
					elem.setHighlightState(st);
				}
			}

			st = HighlightState.PRE_PATH;
			for (int i = 0; i < nodes.size() - 1; i++) {
				EdgeElement elem;

				n = nodes.get(i);
				n2 = nodes.get(i + 1);

				if (n == n2)
					continue;

				elem = null;
				for (EdgeElement e : edgeElements) {
					if (e.getEdge().getBegin().getId() == n
							&& e.getEdge().getEnd().getId() == n2) {
						elem = e;
						break;
					}
				}

				if (n == k)
					st = HighlightState.POST_PATH;
				if (elem != null)
					elem.setHighlightState(st);
			}
		}

		update();
	}

	public void setGraphActionListener(GraphActionListener listener) {
		graphActionListener = listener;
		graphActionListener.addGraphObserver(this);

		initElements();
		update();
	}

	public GraphActionListener getGraphActionListener() {
		return graphActionListener;
	}

	/**
	 * @return the nodeElements
	 */
	public Set<NodeElement> getNodeElements() {
		return nodeElements;
	}

	/**
	 * @return the edgeElements
	 */
	public Set<EdgeElement> getEdgeElements() {
		return edgeElements;
	}

	public void nodeAdded(Graph graph, Node node) {
		NodeElement n;
		
		n = new NodeElement(node);
		n.setComponent(this);
		n.updateBeamerMode(beamerMode);
		
		nodeElements.add(n);

		setFocussedElement(n);

	}

	public void nodeAltered(Graph graph, Node node) {
		update();
	}

	public void nodeRemoved(Graph graph, Node node) {
		NodeElement del;

		del = null;
		for (NodeElement elem : nodeElements) {
			if (elem.getNode() == node) {
				del = elem;
				break;
			}
		}

		if (del != null)
			nodeElements.remove(del);
		if (del == focussedElement) {
			setFocussedElement(null);
		}
	}

	public void edgeAdded(Graph graph, Edge edge) {
		EdgeElement e;

		e = new EdgeElement(edge);
		e.setComponent(this);
		e.updateBeamerMode(beamerMode);

		edgeElements.add(e);
		update();
	}

	public void edgeAltered(Graph graph, Edge edge) {
		// Nothing to do, just redraw
	}

	public void edgeRemoved(Graph graph, Edge edge) {
		EdgeElement del;

		del = null;
		for (EdgeElement elem : edgeElements) {
			if (elem.getEdge() == edge) {
				del = elem;
				break;
			}
		}

		if (del != null)
			edgeElements.remove(del);
		if (del == focussedElement)
			setFocussedElement(null);
	}

	public void graphUpdated() {
		update();
	}

	public void graphSelectionChanged() {
		update();

		for (EdgeElement e : edgeElements) {
			if (e.getEdge().equals(graphActionListener.getSelectedEdge()))
				setFocussedElementInternal(e);
		}
	}

	public void enterHighlightMode(RootStep rootStep) {
		setHighlightedStep(null);
	}

	public void leaveHighlightMode() {
		setHighlightedStep(null);
	}

	public void highlightLastStep(RootStep step) {
		setHighlightedStep(null);
	}

	public void highlightFirstStep(RootStep step) {
		setHighlightedStep(null);
	}

	public void highlightAtomicStep(AtomicStep step, boolean isForward) {
		setHighlightedStep(step);
	}

	public void highlightGroupStep(GroupStep step, boolean isForward) {
		setHighlightedStep(null);
	}

	public void setIgnoreEvents(boolean yorn) {
		ignoreClick = yorn;
	}

	public void mousePressed(MouseEvent event) {
		Point2D point;

		// Set the focus
		grabFocus();

		// Only while editing
		if (   graphActionListener.getEditMode() == EditMode.NONE 
		    || ignoreClick
		   )
			return;

		// Track the click
		point = event.getPoint();

		for (NodeElement node : nodeElements) {
			if (node.contains(point)) {
				node.mouseClicked(event);
				return;
			}
		}

		for (EdgeElement edge : edgeElements) {
			if (edge.contains(point)) {
				edge.mouseClicked(event);
				return;
			}
		}

		setFocussedElement(null);
	}

	public void mouseReleased(MouseEvent event) {
		if (graphActionListener.getEditMode() == EditMode.NONE || ignoreClick)
			return;

		if (    ((event.getX() >= this.getWidth()) || (event.getY() >= this.getHeight()))
			 || ((event.getX() <= 0) || (event.getY() <= 0))
		   ) 
		{
			isDragging = false;
			
			if (focussedElement instanceof NewEdgeElement)
				setFocussedElement(null);
			
			return;
		}
		
		if (isDragging) {
			isDragging = false;

			if (focussedElement != null)
				focussedElement.mouseReleased(event);

			return;
		}
		
		if (ignoreNextClick) {
			ignoreNextClick = false;
			return;
		}

		if (getFocussedElement() == null && graphActionListener != null
				&& graphActionListener.getEditMode() == EditMode.EDITING_NODES) {
			graphActionListener.addNode(getCoreLocation(event.getPoint()));
		}
	}

	public void mouseDragged(MouseEvent event) {
		Point2D point;

		if (graphActionListener.getEditMode() == EditMode.NONE || ignoreClick)
			return;

		if (    ((event.getX() >= this.getWidth()) || (event.getY() >= this.getHeight()))
			 || ((event.getX() <= 0) || (event.getY() <= 0))
		   )
				return;
		
		point = event.getPoint();
		
		if (graphActionListener.getEditMode() == EditMode.EDITING_EDGES
				&& !isDragging && point != null) {
			for (NodeElement n : nodeElements) {
				if (n.contains(point)) {
					NewEdgeElement newEdge;

					newEdge = new NewEdgeElement(n.getNode(), event.getPoint());
					newEdge.setComponent(this);

					setFocussedElement(newEdge);
					isDragging = true;
				}
			}

			return;
		}

		if (focussedElement != null
				&& (isDragging || focussedElement.contains(point)) && point != null) {
			
			focussedElement.mouseDragged(event);
			isDragging = true;
		}
	}

	public void keyReleased(KeyEvent event) {
		if (graphActionListener.getEditMode() == EditMode.NONE)
			return;

		if (focussedElement != null) {
			focussedElement.keyReleased(event);
		}
	}

	public void mouseClicked(MouseEvent event) {
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseMoved(MouseEvent event) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyTyped(KeyEvent event) {
	}

	public void focusGained(FocusEvent event) {
		if (   (event.getOppositeComponent() instanceof EdgeElement.EdgeTextField)
			&& (graphActionListener.getEditMode() == EditMode.EDITING_NODES)
		   )
			ignoreNextClick = true;
	}

	public void focusLost(FocusEvent event) {
	}
}