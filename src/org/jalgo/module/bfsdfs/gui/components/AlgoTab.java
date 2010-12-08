package org.jalgo.module.bfsdfs.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.algorithms.Algo;
import org.jalgo.module.bfsdfs.algorithms.stack.NodeStatus;
import org.jalgo.module.bfsdfs.algorithms.stack.StackObserver;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.graph.GraphObserver;
import org.jalgo.module.bfsdfs.gui.ComponentUtility;
import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;
import org.jalgo.module.bfsdfs.gui.StatusMouseAdapter;
import org.jalgo.module.bfsdfs.gui.event.RandomAction;
import org.jalgo.module.bfsdfs.gui.graphview.GraphView;
import org.jalgo.module.bfsdfs.gui.graphview.InteractiveGraphView;
import org.jalgo.module.bfsdfs.gui.graphview.TreeView;
import org.jalgo.module.bfsdfs.undo.UndoableObserver;

/**
 * AlgoTab is the abstract super class of {@linkplain DFSTab} and
 * {@linkplain BFSTab}. It contains every component both tabs have in common.
 * <br>
 * The main components are:
 * <ul>
 * <li>chooserPane : 	The panel where the user can choose the start node,
 * 						the order of the successors of the current node and
 * 						if the algorithm is or is not deterministic therefore
 * 						lets you choose the successor order or not.</li>
 * <li>nodeViewPane : 	The panel where the queue respectively the stack is
 * 						shown. </li>
 * <li>scrollPane : 	The panel where the {@linkplain GraphView} is embedded.
 * 						Actually {@linkplain TreeView} is drawn above the
 * 						the {@linkplain InteractiveGraphView}, which is grayed
 * 						out.</li>
 * </ul>
 * 
 * @author Florian Dornbusch
 * @author Anselm Schmidt
 */
public abstract class AlgoTab
extends JPanel
implements ComponentListener,  MouseListener, GraphObserver,
UndoableObserver, StackObserver, GUIConstants {
	private static final long serialVersionUID = 7334524852619774015L;
	
	// general references
	protected GUIController gui;
	private GraphController graph = null;
	private GraphView graphView;
	private TreeView treeView;
	
	// main components
	protected JScrollPane scrollPane;
	protected JScrollPane stackScrollPane;
	protected JPanel controlPane;
	protected JPanel chooserPane;
	protected JPanel nodeViewPane;
	private JPanel scrollPanel;
	
	// components of chooserPane
	private JLabel startNodeLabel;
	private JLabel successorLabel;
	private JLabel randomLabel;
	private JComboBox startNodeChooser;
	private JPanel successorPane;
	private SuccessorChooser successorChooser;
	private JScrollPane successorScrollPane;
	private JCheckBox randomBox;
	private ScrollArea leftScrollArea;
	private ScrollArea rightScrollArea;

	// components of nodeViewPane
	protected NodeStackView nodeStackView;
	private JLabel nodeViewLabel;
	
	// actions
	private AbstractAction resetAction;
	private AbstractAction stepBackAction;
	private AbstractAction stepForwardAction;
	private AbstractAction playAction;
	private AbstractAction cancelAction;
	private AbstractAction randomAction;
	
	/** Determines if the successorChooser is enabled or not. */
	private boolean successorsAvailable = false;
	
	/**
	 * Stores the current successors incoming from
	 * {@linkplain #onNodesAdded(List)}.
	 */
	private List<Integer> currentSuccessors;
	
	/** The diameter of a node. */
	protected int nodeSize;
	
	/**
	 * Constructor.
	 * @author Florian Dornbusch
	 */
	public AlgoTab(GUIController gui, GraphController graph, Algo algo) {
		this.graph=graph;
		this.gui = gui;
		
		currentSuccessors = new LinkedList<Integer>();
		
		gui.addGraphObserver(this);
		
		this.setLayout(new BorderLayout());
			
		// create components
		treeView = new TreeView(algo, graph);
		
		startNodeLabel = new JLabel(
				Messages.getString("bfsdfs", "AlgoTab.startNodeLabel"));
		
		startNodeChooser = new JComboBox();
		startNodeChooser.setRenderer(new NodeListRenderer());
		startNodeChooser.setMaximumRowCount(MAX_ROW_COUNT);	
		startNodeChooser.setToolTipText(Messages.getString("bfsdfs",
				"AlgoTab.startNodeChooserStatus"));
		
		successorLabel = new JLabel(
				Messages.getString("bfsdfs", "AlgoTab.successorLabel"));
		
		successorChooser = new SuccessorChooser(gui, this);
		
		successorScrollPane = new JScrollPane(successorChooser,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		leftScrollArea = new ScrollArea(
				successorScrollPane.getHorizontalScrollBar(), true, this);
		rightScrollArea = new ScrollArea(
				successorScrollPane.getHorizontalScrollBar(), false, this);
		
		successorPane = new JPanel();
		successorPane.setLayout(new BorderLayout());
		successorPane.add(leftScrollArea, BorderLayout.WEST);
		successorPane.add(rightScrollArea, BorderLayout.EAST);
		successorPane.add(successorScrollPane, BorderLayout.CENTER);
		
		randomLabel = new JLabel(
				Messages.getString("bfsdfs", "AlgoTab.randomLabel"));
		
		randomAction = new RandomAction(this, gui);
		
		randomBox = new JCheckBox();
		randomBox.setAction(randomAction);
		
		nodeViewLabel = new JLabel(Messages.getString("bfsdfs",
				this.getClass().getSimpleName()+".nodeViewLabel"));
		
		nodeStackView = new NodeStackView();
		
		stackScrollPane = new JScrollPane(nodeStackView,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		stackScrollPane.setPreferredSize(new Dimension(0, 0));
		
		nodeViewPane = new JPanel();
		nodeViewPane.setLayout(new BorderLayout());
		nodeViewPane.setBorder(BorderFactory.createEtchedBorder());
		nodeViewPane.add(nodeViewLabel, BorderLayout.NORTH);
		nodeViewPane.add(stackScrollPane, BorderLayout.CENTER);
		
		controlPane = new JPanel();
		
		setupChooserPane();
		setupScrollArea();
		handleEvents();
		adjustLayout();
		toggleBeamerMode();
	}
	
	/**
	 * Hook method that adjusts all additional content of the window.
	 * @author Florian Dornbusch
	 */
	protected abstract void adjustLayout();

	/**
	 * Additionally adjusts the size of the start node chooser because the size
	 * of the drop down button differentiates in different layouts.
	 * @author Florian Dornbusch
	 */
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if(aFlag) {
			int width = startNodeChooser.getComponents()[0].getWidth() + 50;
			startNodeChooser.setSize(new Dimension(width,50));
			startNodeChooser.setPreferredSize(new Dimension(width,50));
		}
	}
	
	/**
	 * Returns {@linkplain #successorsAvailable}.
	 * @author Florian Dornbusch
	 */
	public boolean getSuccessorsAvailable() {
		return successorsAvailable;
	}
	
	/**
	 * Returns the used check box to toggle the non-determinism.
	 * @author Florian Dornbusch
	 */
	public JCheckBox getRandomBox() {
		return randomBox;
	}
	
	/**
	 * Returns the used {@linkplain SuccessorChooser}.
	 * @author Florian Dornbusch
	 */
	public SuccessorChooser getSuccessorChooser() {
		return successorChooser;
	}
	
	/**
	 * Returns the left {@linkplain ScrollArea} of the
	 * {@linkplain SuccessorChooser}.
	 * @author Florian Dornbusch
	 */
	public ScrollArea getLeftScrollArea() {
		return leftScrollArea;
	}
	
	/**
	 * Returns the right {@linkplain ScrollArea} of the
	 * {@linkplain SuccessorChooser}.
	 * @author Florian Dornbusch
	 */
	public ScrollArea getRightScrollArea() {
		return rightScrollArea;
	}
	
	/**
	 * Returns true if the check box, which toggles the non-determinism, is
	 * selected.
	 * @author Florian Dornbusch
	 */
	public boolean isRandomActive() {
		return successorsAvailable && randomBox.isSelected();
	}
	
	/**
	 * Returns a random permutation of the current available successors.
	 * @author Florian Dornbusch
	 */
	public List<Integer> getRandomPermutation() {
		List<Integer> result = new LinkedList<Integer>();
		// copy the currentSuccessors list to a new list
		List<Integer> s = new LinkedList<Integer>();
		s.addAll(currentSuccessors);
		
		while(!s.isEmpty()) {
			// calculate a random entry, add it to the result and remove it
			// from the copied list
			int random = (int)(Math.random()*s.size());
			result.add(s.get(random));
			s.remove(random);
		}
		return result;
	}
	
	/**
	 * Returns the {@linkplain GraphController}.
	 * @author Florian Dornbusch
	 */
	public GraphController getGraphController() {
		return graph;
	}
	
	/**
	 * Returns the used {@linkplain TreeView}.
	 * @author Florian Dornbusch
	 */
	public TreeView getTreeView() {
		return treeView;
	}
	
	/**
	 * Saves the viewport position of the scroll pane.
	 * @author Florian Dornbusch
	 */
	public void saveViewportPosition() {
		mouseReleased(null);
	}
	
	/**
	 * Loads the viewport position of the scroll pane.
	 * @author Florian Dornbusch
	 */
	public void loadViewportPosition() {
		componentResized(null);
	}
	
	/**
	 * Returns true if there is no available start node.
	 * @author Florian Dornbusch
	 */
	public boolean startNodeChooserEmpty() {
		return startNodeChooser.getItemCount() == 0;
	}
	
	/**
	 * Returns the {@linkplain NodeStackView}.
	 * @author Florian Dornbusch
	 */
	public NodeStackView getNodeStackView() {
		return nodeStackView;
	}
	
	/**
	 * This method changes the content of this component depending on
	 * {@linkplain ComponentUtility#BEAMER_MODE}.
	 * @author Florian Dornbusch
	 */
	public void toggleBeamerMode() {
		int nodeStackWidth;
		Font font;
		if(ComponentUtility.BEAMER_MODE) {
			font = BEAMER_WRITING_FONT;
			nodeSize = NODE_BEAMER_SIZE;
		}
		else {
			font = WRITING_FONT;
			nodeSize = NODE_SIZE;
		}
		
		nodeStackWidth =
			SPACE_SMALL + nodeSize + SPACE_BIG
			+ NODESTACKVIEW_NUMBER_SUCCESSORS *
			(nodeSize + SPACE_SMALL)+ 25;
		
		nodeViewLabel.setFont(font);
		startNodeLabel.setFont(font);
		successorLabel.setFont(font);
		randomLabel.setFont(font);
		successorChooser.setFont(font);
		chooserPane.setPreferredSize(new Dimension(nodeStackWidth,160));
	}
	
	/**
	 * ScrollArea has been resized. The viewport is set with coordinates that
	 * keeps the current content centered. The current viewport position is
	 * loaded from the {@linkplain GUIController}.
	 * @author Florian Dornbusch
	 * @author Anselm Schmidt
	 */
	public void componentResized(ComponentEvent arg0) {
		// change the position in a way, that the new content is at the
		// same place where the old was.
		Point p = gui.getGraphViewPosition();
		if(p==null) return;
		int x = p.x - scrollPane.getViewport().getWidth() / 2;
		int y = p.y - scrollPane.getViewport().getHeight() / 2;
		scrollPane.getViewport().setViewPosition(new Point(x,y));
	}
	
	/** Unused observer method. */
	public void componentHidden(ComponentEvent arg0) {}
	
	/** Unused observer method. */
	public void componentMoved(ComponentEvent arg0) {}
	
	/** Unused observer method. */
	public void componentShown(ComponentEvent arg0) {}
	
	
	/**
	 * This method is invoked, when the mouse is released from one of the
	 * scroll bars of the viewport indicating that resizing has finished.
	 * The new center of the viewport is calculated and saved in the
	 * {@linkplain GUIController}.
	 * @author Florian Dornbusch
	 */
	public void mouseReleased(MouseEvent e) {
		Point p = scrollPane.getViewport().getViewPosition();
		int x = p.x + scrollPane.getViewport().getWidth() / 2;
		int y = p.y + scrollPane.getViewport().getHeight() / 2;
		gui.setGraphViewPosition(new Point(x,y));
	}
	
	/** unused mouse events */
	public void mouseClicked(MouseEvent e) {}
	
	/** unused mouse events */
	public void mouseEntered(MouseEvent e) {}
	
	/** unused mouse events */
	public void mouseExited(MouseEvent e) {}
	
	/** unused mouse events */
	public void mousePressed(MouseEvent e) {}
	
	
	/**
	 * If a new node was added to the graph, it has to be
	 * inserted in the start node chooser.
	 * @author Florian Dornbusch
	 */
	public void onNodeAdded(int id, Point point) {
		List<Integer> list = new LinkedList<Integer>();
		list.add(id);
		startNodeChooser.addItem(createEntry(list));
		startNodeChooser.setEnabled(true);
	}
	/**
	 * If a node was removed from the graph it also has to be
	 * removed from the <code>startNodeChooser</code>.
	 * @author Florian Dornbusch
	 */
	@SuppressWarnings("unchecked")
	public void onNodeRemoved(int id) {
		for(int i=0; i<startNodeChooser.getItemCount();i++){
			Object o = startNodeChooser.getItemAt(i);
			if(o instanceof Object[]) {
				Object[] o2 = (Object[]) o;
				if(o2[1] instanceof List) {
					List<Integer>o3 = (List<Integer>)o2[1];
					if(o3.get(0) == id) {
						startNodeChooser.removeItem(o);
						if(this.startNodeChooserEmpty()) {
							startNodeChooser.setEnabled(false);
						}
					}
				}
			}
		}	
	}
	
	/**
	 * If the id of a node has changed, delete the old
	 * id and add the new one.
	 * @author Florian Dornbusch
	 */
	public void onNodeChanged(int oldNode, int newNode) {
		this.onNodeRemoved(oldNode);
		this.onNodeAdded(newNode, null);
	}
	
	/** unused observer method */
	public void onNodeMoved(int id, Point point) {}
	
	/** unused observer method */
	public void onEdgeAdded(int id1, int id2) {}
	
	/** unused observer method */
	public void onEdgeRemoved(int id1, int id2) {}
	
	/** unused observer method */
	public void onEdgeChanged(int oldStart, int oldEnd, int newStart,
			int newEnd) {}
	
	/**
	 * This method removes all entries in {@linkplain SuccessorChooser},
	 * saves the current successors and displays the standard permutation.
	 * @author Florian Dornbusch
	 */
	public void onNodesAdded(List<Integer> nodes) {
		if(nodes == null || nodes.isEmpty()) return;
		successorsAvailable = true;
		
		currentSuccessors = nodes;
		
		successorChooser.setEnabled(true);
		
		if(randomBox.isSelected()) {
			List<Integer> random = getRandomPermutation();
			successorChooser.fill(random);
			gui.setSuccessorOrder(random);
		}
		else {
			successorChooser.fill(nodes);
		}
	}
	
	/**
	 * Enables or disables the {@linkplain SuccessorChooser} depending on
	 * the changed {@linkplain NodeStatus}.
	 * @author Florian Dornbusch
	 */
	public void onStatusChanged(int node, NodeStatus status) {
		successorsAvailable = false;
		successorChooser.setEnabled(false);
		leftScrollArea.setEnabled(false);
		rightScrollArea.setEnabled(false);
	}
	
	/**
	 * Removes all items from {@linkplain SuccessorChooser} if the algorithm
	 * has finished.
	 * @author Florian Dornbusch
	 */
	public void onAllNodesFinished() {
		successorChooser.setEnabled(false);
		leftScrollArea.setEnabled(false);
		rightScrollArea.setEnabled(false);
	}
	
	/** Unused observer method. */
	public void onFirstQueueAdded(int owner) {}
	
	/** Unused observer method. */
	public void onQueueAdded(int owner) {}
	
	/** Unused observer method. */
	public void onTopQueueRemoved() {}
	
	/** Unused observer method. */
	public void onAllQueuesRemoved() {}
	
	/** Unused observer method. */
	public void onUntouchedReplaced(List<Integer> o, List<Integer> n) {}
	
	/** Unused observer method. */
	public void onCurrentNodeChanged(int i) {}
	
	/**
	 * If undo is enabled meaning the algorithm is running,
	 * the start node chooser has to be disabled.
	 * @author Florian Dornbusch
	 */
	public void onUndoEnabled() {
		startNodeChooser.setEnabled(false);
	}
	
	/**
	 * If undo is disabled meaning the algorithm is not running,
	 * the start node chooser has to be enabled.
	 * Also, the {@linkplain SuccessorChooser} has to be disabled.
	 * @author Florian Dornbusch
	 */
	public void onUndoDisabled() {
		startNodeChooser.setEnabled(true);
		successorChooser.setEnabled(false);
		leftScrollArea.setEnabled(false);
		rightScrollArea.setEnabled(false);
	}
	
	/** Unused observer method. */
	public void onRedoEnabled() {}
	
	/** Unused observer method. */
	public void onRedoDisabled() {}
	
	/** 
	 * Fills <code>chooserPane</code> with 4 rows and 2 columns. The utility
	 * class {@linkplain FormUtility} is used to create the
	 * {@linkplain GridBagLayout}.
	 * @author Florian Dornbusch
	 */	
	private void setupChooserPane() {
		chooserPane = new JPanel();
		chooserPane.setLayout(new GridBagLayout());
		int nodeStackWidth =
			SPACE_SMALL + NODE_SIZE + SPACE_BIG + SPACE_BIG + SPACE_BIG + 
			NODESTACKVIEW_NUMBER_SUCCESSORS * (NODE_SIZE+SPACE_SMALL)+
			SPACE_SMALL+20;
		chooserPane.setPreferredSize(new Dimension(nodeStackWidth,160));
		chooserPane.setBorder(BorderFactory.createEtchedBorder());
		
		FormUtility formUtility = new FormUtility();
		// The following comments use (row, column) to identify the cells.
		
		// (1,1) set the startNodeLabel and add it to chooserPane
		JPanel startLabelPanel = new JPanel();
		startLabelPanel.setLayout(new BorderLayout());
		startLabelPanel.setPreferredSize(new Dimension(120,25));
		startLabelPanel.add(startNodeLabel, BorderLayout.CENTER);
		formUtility.addLabel(startLabelPanel, chooserPane);
		
		// (1,2) set the randomPane and add it to chooserPane
		JPanel randomPane = new JPanel();
		randomPane.setLayout(new BoxLayout(randomPane, BoxLayout.LINE_AXIS));
		randomPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		randomPane.add(randomBox);
		randomPane.add(randomLabel);
		formUtility.addLastField(randomPane, chooserPane);
		
		// (2,1) set the startNodeChooser and add it to chooserPane
		JPanel startChooserPanel = new JPanel();
		startChooserPanel.setLayout(new BorderLayout());
		startChooserPanel.add(startNodeChooser, BorderLayout.WEST);
		startNodeChooser.setPreferredSize(new Dimension (65,50));
		formUtility.addLabel(startChooserPanel, chooserPane);
		
		// (2,2) add the buttons to handle the algorithm BFSTab and DFSTab
		JPanel buttonPane = new JPanel();
		buttonPane.setPreferredSize(new Dimension(50,25));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		cancelAction = gui.getCancelAction();
		buttonPane.add(ComponentUtility.createToolbarButton(cancelAction));
		playAction = gui.getPlayAction();
		buttonPane.add(ComponentUtility.createToolbarButton(playAction));
		stepForwardAction = gui.getStepForwardAction();
		buttonPane.add(ComponentUtility.
				createToolbarButton(stepForwardAction));
		stepBackAction = gui.getStepBackAction();
		buttonPane.add(ComponentUtility.createToolbarButton(stepBackAction));
		resetAction = gui.getResetAction();
		buttonPane.add(ComponentUtility.createToolbarButton(resetAction));
		formUtility.addLastField(buttonPane, chooserPane);
		
		// (3,1) set the successorLabel and add it to chooserPane
		JPanel succLabelPanel = new JPanel();
		succLabelPanel.setLayout(new BorderLayout());
		succLabelPanel.setPreferredSize(new Dimension(0,25));
		succLabelPanel.add(successorLabel, BorderLayout.CENTER);
		formUtility.addLastField(succLabelPanel, chooserPane);
		
		// (4,1) set the successorChooser and add it to chooserPane
		JPanel succChooserPanel = new JPanel();
		succChooserPanel.setLayout(new BorderLayout());
		succChooserPanel.add(successorPane, BorderLayout.CENTER);
		succChooserPanel.setPreferredSize(new Dimension(0,50));
		formUtility.addLastField(succChooserPanel, chooserPane);
	}
	
	/**
	 * This utility method adds {@linkplain ActionListener}s and
	 * {@linkplain MouseListener}s to all components that need it.
	 * @author Florian Dornbusch
	 */
	@SuppressWarnings("unchecked")
	private void handleEvents() {
		
		startNodeChooser.addActionListener(new ActionListener() {
			/**
			 * If a start node is selected by the user, the algorithm
			 * and the tree view have to know it.
			 * @author Florian Dornbusch
			 */
			public void actionPerformed(ActionEvent arg0) {
				Object o = startNodeChooser.getSelectedItem();
				if(o instanceof Object[]) {
					Object[] oa = (Object[])o;
					if(oa[1] instanceof List) {
						List<Integer> list = (List<Integer>)oa[1];
						gui.setStartNode(list.get(0));
						treeView.onCurrentNodeChanged(list.get(0));
					}
				}
			}
		});

		
		// add MouseListeners to the following components that update
		// the status bar according to their status text
		
		for(Component c:startNodeChooser.getComponents()) {
			c.addMouseListener(new StatusMouseAdapter(
			"AlgoTab.startNodeChooserStatus"));
		}
		
		startNodeChooser.addMouseListener(new StatusMouseAdapter(
				"AlgoTab.startNodeChooserStatus"));
		
		successorChooser.addMouseListener(new StatusMouseAdapter(
				"AlgoTab.successorChooserStatus"));
		
		randomBox.addMouseListener(new StatusMouseAdapter(
				"AlgoTab.randomBoxStatus"));
				
		nodeViewPane.addMouseListener(new StatusMouseAdapter(
				this.getClass().getSimpleName()+".nodeViewStatus"));
		
		nodeStackView.addMouseListener(new StatusMouseAdapter(
				this.getClass().getSimpleName()+".nodeViewStatus"));	
		
		chooserPane.addMouseListener(new StatusMouseAdapter(
				"AlgoTab.chooserPaneStatus"));
		
		scrollPane.addMouseListener(new StatusMouseAdapter(
		"AlgoTab.scrollPaneStatus"));
	}

	/**
	 * Creates the <code>scrollPane</code> and the <code>scrollPanel</code>
	 * including {@linkplain GraphView} and {@linkplain TreeView}
	 * and adds itself as {@linkplain ComponentListener} to itself. 
	 * @author Anselm Schmidt
	 */
	private void setupScrollArea() {
		// create components
		graphView = new GraphView(this.graph);
		gui.addGraphObserver(graphView);
		scrollPanel = new JPanel();
		scrollPane = new JScrollPane(scrollPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		// adds itself as ComponentListener to the scrollArea
		scrollPane.addComponentListener(this);
		scrollPane.getVerticalScrollBar().addMouseListener(this);
		scrollPane.getHorizontalScrollBar().addMouseListener(this);

		// set up scroll area
		scrollPanel.setLayout(null);
		scrollPanel.add(treeView);
		scrollPanel.add(graphView);
		
		// set sizes
		Dimension size = new Dimension(MAX_GRAPH_WIDTH, MAX_GRAPH_HEIGHT);
		scrollPanel.setSize(size);
		scrollPanel.setPreferredSize(size);
		graphView.setSize(size);
		graphView.setPreferredSize(size);
		treeView.setSize(size);
		treeView.setPreferredSize(size);
		
		// show the center of the scroll area
		int viewportWidth = scrollPane.getViewport().getWidth();
		int viewportHeight = scrollPane.getViewport().getHeight();
		int x = MAX_GRAPH_WIDTH / 2 - viewportWidth / 2;
		int y = MAX_GRAPH_HEIGHT / 2 - viewportHeight / 2;
		scrollPane.getViewport().setViewPosition(new Point(x, y));
		mouseReleased(null);
	}
	
	/**
	 * This method draws a horizontal line of node icons and creates an image
	 * of it. It is used to render the nodes in <code>startNodeChooser</code>.
	 * @param i the position of a node in the current line.
	 * 			i=0 is the first node after the owner
	 * @author Florian Dornbusch
	 */
	private BufferedImage createImageIcon(List<Integer> list) {
		Integer i=0;
		
		//initiate the image
		BufferedImage p = new BufferedImage(NODE_ICON_SIZE * list.size(),
				NODE_ICON_SIZE, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = p.createGraphics();
		
		// turn on anti-aliasing
		GraphDrawing.enableAntiAliasing(g);
		
		// turn background transparent
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, NODE_ICON_SIZE * list.size(), NODE_ICON_SIZE);
		g.setColor(Color.BLACK);
		for (Integer e : list) {
			ComponentUtility.drawNode(g, i * NODE_ICON_SIZE, 0, e,
					NodeStatus.UNTOUCHED, NODE_ICON_SIZE,
					NODE_ICON_BORDER_SIZE, NODE_ICON_FONT, ALPHA_100_PERCENT);
			i++;
		}
		return p;
	}
	
	/**
	 * Utility method to create entries for the start node chooser because each
	 * entry contains an {@linkplain ImageIcon} and the underlying integer
	 * list.
	 * @param list : The list from which the image icons are created.
	 * 				 It is read in the according action events.
	 * @return :	 an object array with two elements - the icon and the list
	 * @author Florian Dornbusch
	 */
	private Object[] createEntry(List<Integer> list) {
		Object[] o = new Object[2];
		o[0] = new ImageIcon(createImageIcon(list));
		o[1] = list;
		return o;
	}
}