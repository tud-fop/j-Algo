package org.jalgo.module.app.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jalgo.module.app.controller.undoRedo.UndoManager;
import org.jalgo.module.app.core.Matrix;
import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.view.graph.EditToolbar;
import org.jalgo.module.app.view.graph.GraphComponent;
import org.jalgo.module.app.view.graph.GraphTextComponent;
import org.jalgo.module.app.view.run.MatrixPreviewComponent;

/**
 * Controls the three Panels, which are on the right and left panel of the User
 * Interface. The <code>GraphController</code> initializes the four components
 * which are involved in these Panels.
 */
public class GraphController implements GraphActionListener {

	/**
	 * Connection to the MainController to communicate with the Core and the
	 * InterfaceController.
	 */
	private MainController mainCtrl;

	// Components, in which the process of editing the graph is shown.
	private EditToolbar editToolbar;
	private MatrixPreviewComponent matrixPreviewComponent;
	private GraphTextComponent graphTextComponent;
	private GraphComponent graphComponent;

	/**
	 * To remember the state of the components
	 */
	private boolean isSetUp;
	private boolean notificationGroupingActive;
	private boolean groupedChangeDone;

	private EditMode editMode;
	private EditMode oldEditMode;
	private Node selectedNode;
	private Edge selectedEdge;

	// The Panels, which are initialized by the InterfaceController
	private JPanel textualGraphPanel, graphPanel, graphToolPanel;

	private Set<GraphObserver> observers;

	private UndoManager undoManager;
	
	private boolean beamerMode;
	private InterfaceMode interfaceMode;
	
	Point2D previousPosition;

	/**
	 * Instantiates the three Panels, which are in the right panel of the User
	 * Interface (in <code>InterfaceMode.GRAPH_EDITING</code>. He also
	 * initializes the four components involved in the panels.
	 */
	public GraphController(MainController mainController,
			JPanel textualGraphPanel, JPanel graphPanel, JPanel graphToolPanel) {
		mainCtrl = mainController;

		// Init properties
		this.textualGraphPanel = textualGraphPanel;
		this.graphPanel = graphPanel;
		this.graphToolPanel = graphToolPanel;

		this.observers = new HashSet<GraphObserver>();
		mainController.getInterfaceController().getModeSwitchComponent()
				.setGraphActionListener(this);

		this.isSetUp = false;
		this.beamerMode = false;
		this.notificationGroupingActive = false;
		this.editMode = EditMode.EDITING_NODES;

		this.undoManager = new UndoManager(this);

		// Init interface
		initInterface();
	}

	/**
	 * Initializes the graph editing View with components for the graph, the
	 * textual representation, the toolbar and the matrix preview.
	 */
	private void initInterface() {
		BorderLayout layout;

		editToolbar = new EditToolbar(this);
		matrixPreviewComponent = new MatrixPreviewComponent(this);
		graphTextComponent = new GraphTextComponent(this);
		graphComponent = new GraphComponent(this);

		layout = new BorderLayout();
		textualGraphPanel.setLayout(layout);
		textualGraphPanel.add(graphTextComponent, BorderLayout.CENTER);
		textualGraphPanel.add(matrixPreviewComponent, BorderLayout.SOUTH);

		matrixPreviewComponent.setBorder(BorderFactory.createMatteBorder(1, -1,
				1, 1, Color.LIGHT_GRAY));

		graphPanel.add(graphComponent);
		graphToolPanel.add(editToolbar);

		mainCtrl.getAlgorithmController().addStepHighlighting(graphComponent);
	}

	/**
	 * Changes the look of the components owned by this controller based upon
	 * the <code>InterfaceMode</code>. If interfaceMode is set to
	 * <code>InterfaceMode.GRAPH_EDITING</code>, the calculation will be
	 * started and the components will be made visible.
	 * 
	 * @param interfaceMode
	 *            the current mode of the Interface
	 */
	public void updateDisplay(InterfaceMode interfaceMode) {
		this.interfaceMode = interfaceMode;
		
		if (interfaceMode == InterfaceMode.GRAPH_EDITING && !isSetUp) {
			DataType dataType;
			isSetUp = true;

			try {
				dataType = mainCtrl.getCalculation().getSemiring().getType()
						.newInstance();
				editToolbar.setSpecialCharButtons(dataType
						.getSpecialCharacter());
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}

			editToolbar.setGraphActionListener(this);
			graphTextComponent.setGraphActionListener(this);
			graphComponent.setGraphActionListener(this);
		}

		// Remember the last edit mode but disable editing while the algorithm
		// runs
		if (interfaceMode == InterfaceMode.GRAPH_EDITING) {
			if (oldEditMode == null || oldEditMode == EditMode.NONE)
				oldEditMode = EditMode.EDITING_NODES;

			setEditMode(oldEditMode);
			graphComponent.highlightAtomicStep(null, false);
		} else {
			oldEditMode = getEditMode();
			setEditMode(EditMode.NONE);
		}

		for (GraphObserver observer : observers) {
			observer.graphUpdated();
		}
				
		updateDynamicScaling();
	}

	/**
	 * Gets the current graph from the calculation in the core.
	 * 
	 * @return the current Graph.
	 */
	public Graph getGraph() {
		return mainCtrl.getCalculation().getGraph();
	}

	/**
	 * Gets the data type of all edge weights in the graph.
	 * 
	 * @return the weight data type
	 */
	public Class<? extends DataType> getDataType() {
		return mainCtrl.getCalculation().getSemiring().getType();
	}

	/**
	 * Gets the initial Matrix (adjacency matrix). This matrix is needed for
	 * displaying it in the graph editing mode.
	 * 
	 * @return the initial Matrix (adjacency matrix).
	 */
	public Matrix getInitialMatrix() {
		return mainCtrl.getCalculation().calculateInitialMatrix();
	}

	/**
	 * Adds <code>newObserver</code> to the list of graph observers, which is
	 * managed in this controller.
	 * 
	 * @param newObserver
	 *            the observer to be added.
	 */
	public void addGraphObserver(GraphObserver newObserver) {
		observers.add(newObserver);
	}

	/**
	 * Enables/Disables the <code>editToolbar</code> (to make it
	 * visible/invisible) in the User Interface. The use of this method depends
	 * on the currentDisplaymode.
	 * 
	 * @param state
	 *            <code>true</code> to enable it, <code>false</code> to
	 *            disable it.
	 * @param redo
	 *            The state of the undo button.
	 * @param undo
	 *            The state of the redo button.
	 */
	public void setToolbarEnabled(boolean state, boolean undo, boolean redo) {
		editToolbar.setEnabled(state, undo, redo);
	}

	/**
	 * If the <code>graphTextComponent</code> currently has focus,
	 * <code>true</code> is returned, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code>, if the <code>graphTextComponent</code>
	 *         currently has focus.
	 */
	public boolean getGraphTextComponentFocusState() {
		return graphTextComponent.isFocusOwner();
	}

	/**
	 * Changes the font of the current GraphTextComponent by calling
	 * graphTextComponent.setTextAreaFont().
	 * 
	 * @see GraphTextComponent#setTextAreaFont()
	 */
	public void changeTextComponentFont() {
		graphTextComponent.setTextAreaFont();
	}

	public EditMode getEditMode() {
		return editMode;
	}

	public void setEditMode(EditMode mode) {
		editMode = mode;
	}

	private void updateDynamicScaling() {
		if (interfaceMode == InterfaceMode.ALGORITHM_DISPLAY && beamerMode)
			graphComponent.setDynamicScaling(true);
		else
			graphComponent.setDynamicScaling(false);			
	}
	
	/**
	 * Turns the beamer mode on or off.
	 * 
	 * @param beamerMode A boolean which should be true to enable the beamer mode.
	 */
	public void setBeamerMode(boolean beamerMode){
		this.beamerMode = beamerMode;
		updateDynamicScaling();	
		
		graphComponent.updateBeamerMode(beamerMode);
		graphTextComponent.updateBeamerMode(beamerMode);
		matrixPreviewComponent.updateBeamerMode(beamerMode);
	}

	public Node getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(Node node) {
		selectedNode = node;
		selectedEdge = null;

		for (GraphObserver o : observers)
			o.graphSelectionChanged();
		
		editToolbar.updateButtons();
	}

	public Edge getSelectedEdge() {
		return selectedEdge;
	}

	public void setSelectedEdge(Edge edge) {
		selectedEdge = edge;
		selectedNode = null;

		for (GraphObserver o : observers)
			o.graphSelectionChanged();

		editToolbar.updateButtons();
	}

	public void beginEditing() {
		if (!notificationGroupingActive) {
			notificationGroupingActive = true;
			groupedChangeDone = false;
		}
	}

	public void endEditing() {
		if (notificationGroupingActive && groupedChangeDone) {
			notificationGroupingActive = false;

			for (GraphObserver o : observers)
				o.graphUpdated();
		}
	}

	public Node addNode() {
		Point position = getAppropriateNodePosition();
		return addNode(position);
	}

	/**
	 * Finds a place for a new node with enough distance to other nodes and the
	 * borders of the graph component.
	 * 
	 * @return an appropriate Position for a new Node in the graph component.
	 */
	private Point getAppropriateNodePosition() {
		int paintAreaDimensions = 900;
		int distanceBetweenNodes = 60;
		int fieldWidth = 300;
		boolean positionFound = false;
		int newX = 0;
		int newY = 0;

		// divide the paint area into a 3 x 3 field of subareas
		int[][] area = { { 20, 20 }, { 170, 20 }, { 320, 20 }, { 320, 170 },
				{ 320, 320 }, { 170, 320 }, { 20, 320 }, { 20, 170 },
				{ 170, 170 } };
		Point pos = new Point();
		Set<Node> nodes = getGraph().getNodes();

		// Find an empty area
		if (nodes.isEmpty()) {
			newX = (int) (Math.random() * fieldWidth + area[0][0]);
			newY = (int) (Math.random() * fieldWidth + area[0][1]);
		} else {
			int i = 0;
			while (i < area.length && !positionFound) {
				boolean areaTaken = false;
				for (Node existingNode : nodes) {
					if (((existingNode.getLocation().getX() > area[i][0]) && existingNode
							.getLocation().getX() < (area[i][0] + fieldWidth))
							&& (existingNode.getLocation().getY() > area[i][1])
							&& existingNode.getLocation().getY() < (area[i][1] + fieldWidth)) {
						areaTaken = true;
						break;
					}
				}
				if (!areaTaken) {
					newX = (int) (Math.random() * fieldWidth + area[i][0]);
					newY = (int) (Math.random() * fieldWidth + area[i][1]);
					positionFound = true;
				}
				i++;
			}
		}
		// if no empty area found
		if (!positionFound) {
			for (int i = 0; i < 10; i++) {
				for (Node existingNode : nodes) {
					if (newX > (existingNode.getLocation().getX() - distanceBetweenNodes)
							|| newX < (existingNode.getLocation().getX() + distanceBetweenNodes)) {
						newX = (int) (Math.random() * paintAreaDimensions + 20);
					}
					if (newY > (existingNode.getLocation().getY() - distanceBetweenNodes)
							|| newY < (existingNode.getLocation().getY() + distanceBetweenNodes)) {
						newY = (int) (Math.random() * paintAreaDimensions + 20);
					}
				}
			}
		}
		pos.x = newX;
		pos.y = newY;
		return pos;
	}

	/**
	 * Adds a node to the model, without any checking and undo tracking. Informs
	 * all observers of change.
	 * 
	 * @param node
	 *            the node to be added.
	 */
	public void addNode(Node node) {
		// set the runToggle enabled
		if (getGraph().getNodes().isEmpty())
			mainCtrl.getInterfaceController().getModeSwitchComponent()
					.setCurrentModeButtonEnable(
							mainCtrl.getInterfaceController().getDisplayMode(),
							false);

		getGraph().addNode(node);

		setSelectedNode(node);

		for (GraphObserver o : observers) {
			o.nodeAdded(getGraph(), node);
			o.graphUpdated();
			o.graphSelectionChanged();
		}
		mainCtrl.setChangesToSave();
	}

	/**
	 * This method is called by undo actions. Adds a node to the model, adjusts
	 * the node IDs. Informs all observers of change.
	 */
	public void addUndoNode(Node node) {

		// consider that this is the special undo adding
		getGraph().addUndoNode(node);

		for (GraphObserver o : observers) {
			o.nodeAdded(getGraph(), node);
			o.graphUpdated();
		}
		mainCtrl.setChangesToSave();
	}

	/**
	 * Adds the node on a specific position. Also adds an Undo-Action.
	 */
	public Node addNode(Point2D position) {
		Node node;

		node = getGraph().newNode(false);
		node.setLocation(position);

		addNode(node);

		undoManager.addUndoAddAction(node);
		editToolbar.setUndoState();

		return node;
	}

	/**
	 * Changes the position of the node.
	 * 
	 * @param node
	 *            the node of which the position should be changed.
	 * @param position
	 *            the new coordinates for <code>node</code>.
	 */
	public void alterNodePosition(Node node, Point2D position) {
		setNodePosition(node, position);
	}

	/**
	 * Method which is called when a dragged node has reached its actual
	 * position.
	 * 
	 * @param node
	 *            the dragged node.
	 * @param position
	 *            the final position after dragging <code>node</code>.
	 */
	public void setFinalNodePosition(Node node, Point2D position) {
		setNodePosition(node, position);

		undoManager.addUndoNodePositionAlteredAction(node, previousPosition);
		editToolbar.setUndoState();
	}

	/**
	 * Sets the node position by undo actions.
	 * 
	 * @param node
	 * @param position
	 */
	public void alterUndoNodePosition(Node node, Point2D position) {
		setNodePosition(node, position);
	}

	/**
	 * Sets the position of the node. Informs all graphObservers of this change.
	 * 
	 * @param node
	 *            the node.
	 * @param position
	 *            the new position.
	 */
	public void setNodePosition(Node node, Point2D position) {
		node.setLocation(position);

		for (GraphObserver o : observers) {
			o.nodeAltered(getGraph(), node);

			if (!notificationGroupingActive)
				o.graphUpdated();
			else
				groupedChangeDone = true;
		}
		mainCtrl.setChangesToSave();
	}

	public void removeNode(Node node) {
		Graph graph = getGraph();

		Set<Edge> relatedEdges = new HashSet<Edge>();

		for (Edge e : graph.getEdges()) {
			if (e.getBegin() == node || e.getEnd() == node) {
				relatedEdges.add(e);
			}
		}

		doRemoveNode(node);
		undoManager.addUndoRemoveAction(node, relatedEdges);
		editToolbar.setUndoState();
	}

	/**
	 * Removes a node without affecting the undo/redo manager.
	 * 
	 * @param node
	 *            The node to remove.
	 */
	public void doRemoveNode(Node node) {
		Set<Edge> oldEdges;
		Graph graph;

		graph = getGraph();

		// Remember edges that will be removed
		oldEdges = new HashSet<Edge>();
		for (Edge e : graph.getEdges()) {
			if (e.getBegin() == node || e.getEnd() == node) {
				oldEdges.add(e);
			}
		}

		// Actually remove the node
		// if (node == selectedNode)
		graph.removeNode(node);

		if (node == selectedNode) {
			selectedNode = null;
			// setSelectedNode(graph.getNode(graph.getBiggestID()));
		}

		// set save status
		if (graph.getNodes().isEmpty()) {
			mainCtrl.setNothingToSave();
		} else {
			mainCtrl.setChangesToSave();
		}

		// Inform all observers
		for (GraphObserver o : observers) {
			for (Edge e : oldEdges) {
				o.edgeRemoved(graph, e);
			}
			o.nodeRemoved(graph, node);

			if (!notificationGroupingActive)
				o.graphUpdated();
			else
				groupedChangeDone = true;
		}

		// set the runToggle disable, when there is no edge or node
		if (graph.getEdges().isEmpty())
			mainCtrl.getInterfaceController().getModeSwitchComponent()
					.setCurrentModeButtonEnable(
							mainCtrl.getInterfaceController().getDisplayMode(),
							true);

	}

	public Edge addEdge(Node start, Node end) {
		DataType weight;

		weight = null;

		try {
			weight = getDataType().newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

		return addEdge(start, end, weight);
	}

	/**
	 * Method called by the undo actions.
	 */
	public void addEdge(Edge edge) {
		// set the runToggle enabled
		if (getGraph().getEdges().isEmpty())
			mainCtrl.getInterfaceController().getModeSwitchComponent()
					.setCurrentModeButtonEnable(
							mainCtrl.getInterfaceController().getDisplayMode(),
							false);

		getGraph().addEdge(edge);

		for (GraphObserver o : observers) {
			o.edgeAdded(getGraph(), edge);

			if (!notificationGroupingActive)
				o.graphUpdated();
			else
				groupedChangeDone = true;
		}
		mainCtrl.setChangesToSave();
	}

	public Edge addEdge(Node start, Node end, DataType weight) {
		Edge edge;

		edge = getGraph().newEdge(start, end, weight, false);
		addEdge(edge);

		undoManager.addUndoAddAction(edge);
		editToolbar.setUndoState();

		return edge;
	}

	public void alterEdgeWeight(Edge edge, DataType weight) {
		DataType previousWeight = edge.getWeight();
		setEdgeWeight(edge, weight);

		undoManager.addUndoWeightAlteredAction(edge, previousWeight);
		editToolbar.setUndoState();
	}

	/**
	 * Sets the edge weight by undo actions.
	 * 
	 * @param edge
	 *            the edge where the weight should be changed.
	 * @param weight
	 *            the new weight.
	 */
	public void alterUndoEdgeWeight(Edge edge, DataType weight) {
		setEdgeWeight(edge, weight);
	}

	/**
	 * Sets the weight of the edge.
	 * 
	 * @param edge
	 *            the edge where the weight should be changed.
	 * @param weight
	 *            the new weight of type <code>DataType</code>.
	 */
	public void setEdgeWeight(Edge edge, DataType weight) {
		edge.setWeight(weight);

		for (GraphObserver o : observers) {
			o.edgeAltered(getGraph(), edge);

			if (!notificationGroupingActive)
				o.graphUpdated();
			else
				groupedChangeDone = true;
		}
		mainCtrl.setChangesToSave();
	}

	public void removeEdge(Edge edge) {

		undoManager.addUndoRemoveAction(edge);
		editToolbar.setUndoState();

		doRemoveEdge(edge);
	}

	/**
	 * Removes an edge without affecting the undo/redo manager.
	 * 
	 * @param edge
	 *            The edge to remove.
	 */
	public void doRemoveEdge(Edge edge) {
		Graph graph;

		graph = getGraph();

		// removes the edge
		graph.removeEdge(edge);
		if (edge == selectedEdge)
			selectedEdge = null;

		mainCtrl.setChangesToSave();

		// Inform all observers
		for (GraphObserver o : observers) {
			o.edgeRemoved(graph, edge);

			if (!notificationGroupingActive)
				o.graphUpdated();
			else
				groupedChangeDone = true;
		}

		// set the runToggle disable, when there is no Edge or Node
		if (graph.getNodes().isEmpty() || graph.getEdges().isEmpty())
			mainCtrl.getInterfaceController().getModeSwitchComponent()
					.setCurrentModeButtonEnable(
							mainCtrl.getInterfaceController().getDisplayMode(),
							true);

	}

	/**
	 * Gets the current undoManager.
	 * 
	 * @return the current undoManager.
	 */
	public UndoManager getUndoManager() {
		return undoManager;
	}

	/**
	 * Sets the global UndoManager.
	 * 
	 * @param undoManager
	 *            the new UndoManager.
	 */
	public void setUndoManager(UndoManager undoManager) {
		this.undoManager = undoManager;
	}

	/**
	 * Sets the position of a node before dragging to remember for undo/redo.
	 * 
	 * @param previousPosition
	 *            the previousPosition to set
	 */
	public void setPreviousPosition(Point2D previousPosition) {
		this.previousPosition = previousPosition;
	}

}