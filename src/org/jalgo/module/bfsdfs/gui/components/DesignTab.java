package org.jalgo.module.bfsdfs.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.ComponentUtility;
import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.graphview.EditMode;
import org.jalgo.module.bfsdfs.gui.graphview.InteractiveGraphView;

/**
 * <code>DesignTab</code> represents a design mode to configure the graph. It
 * consists of a <code>scrollPane</code>, where the Graph is drawn, and 6
 * Buttons to handle it:
 * <ul>
 * <li>Undo</li>
 * <li>Redo</li>
 * <li>Node</li>
 * <li>Edge</li>
 * <li>DoubleEdge</li>
 * <li>Eraser</li>
 * </ul>
 * 
 * @author Florian Dornbusch
 * @author Anselm Schmidt
 */
public class DesignTab 
extends JPanel 
implements ComponentListener, MouseListener, GUIConstants {
	private static final long serialVersionUID = 2662482559820040502L;
	
	// general references
	private GUIController gui;
	private GraphController graphController = null;
	private InteractiveGraphView graphView;
	
	// main components
	private JPanel scrollArea;
	private JPanel buttonPane;
	private JScrollPane scrollPane;
	
	// buttons
	private JButton undoButton;
	private JButton redoButton;
	private JToggleButton nodeButton;
	private JToggleButton edgeButton;
	private JToggleButton doubleEdgeButton;
	private JToggleButton eraserButton;
	
	// actions
	private AbstractAction undoAction;
	private AbstractAction redoAction;
	private AbstractAction nodeAction;
	private AbstractAction edgeAction;
	private AbstractAction doubleEdgeAction;
	private AbstractAction eraserAction;

	/**
	 * Constructor.
	 * @author Florian Dornbusch
	 */
	public DesignTab(GUIController guiController,
			GraphController graphController) {

		this.graphController = graphController;
		this.gui = guiController;
		
		setLayout(new BorderLayout());
		
		setupButtons();
		setupScrollArea();
		
		JPanel south = new JPanel();
		south.setPreferredSize(new Dimension(0,35));
		south.add(buttonPane);
		
		// add components to pane
		add(scrollPane, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
	}
	
	/**
	 * Toggles the buttons so that the button according to the given
	 * {@linkplain EditMode} is the only selected button.
	 * @author Florian Dornbusch
	 */
	public void switchTo(EditMode mode) {
		nodeButton.setSelected(mode == EditMode.PUT_NODE);
		edgeButton.setSelected(mode == EditMode.START_EDGE);
		doubleEdgeButton.setSelected(mode == EditMode.START_DOUBLE_EDGE);
		eraserButton.setSelected(mode == EditMode.ERASE);
	}

	/**
	 * Returns the {@linkplain InteractiveGraphView}.
	 * @author Florian Dornbusch
	 */
	public InteractiveGraphView getGraphView() {
		return graphView;
	}
	
	/**
	 * Saves the view port position of the scroll pane.
	 * @author Florian Dornbusch
	 */
	public void saveViewportPosition() {
		mouseReleased(null);
	}
	
	/**
	 * Loads the view port position of the scroll pane.
	 * @author Florian Dornbusch
	 */
	public void loadViewportPosition() {
		componentResized(null);
	}
	
	/** Unused observer method. */
	public void componentHidden(ComponentEvent arg0) {}
	
	/** Unused observer method. */
	public void componentMoved(ComponentEvent arg0) {}
	
	/** Unused observer method. */
	public void componentShown(ComponentEvent arg0) {}
	
	/**
	 * ScrollArea has been resized. The view port is set with coordinates that
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
	
	/** Unused observer method. */
	public void mouseClicked(MouseEvent e) {}
	
	/** Unused observer method. */
	public void mouseEntered(MouseEvent e) {}
	
	/** Unused observer method. */
	public void mouseExited(MouseEvent e) {}
	
	/** Unused observer method. */
	public void mousePressed(MouseEvent e) {}
	
	/**
	 * Creates the design buttons and sets them up with the same action as the
	 * design buttons in the toolbar.
	 * @author Florian Dornbusch
	 */
	private void setupButtons() {
		buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		
		undoAction = gui.getUndoAction();
		undoButton = ComponentUtility.createButton(undoAction);
		buttonPane.add(undoButton);
		
		redoAction = gui.getRedoAction();
		redoButton = ComponentUtility.createButton(redoAction);
		buttonPane.add(redoButton);
		
		buttonPane.add(Box.createRigidArea(new Dimension(50, 0)));
		
		nodeAction = gui.getNodeAction();
		nodeButton = ComponentUtility.createToolbarToggleButton(nodeAction);
		nodeButton.setSelected(true);
		
		buttonPane.add(nodeButton);
		edgeAction = gui.getEdgeAction();
		edgeButton = ComponentUtility.createToolbarToggleButton(edgeAction);
		buttonPane.add(edgeButton);
		
		doubleEdgeAction = gui.getDoubleEdgeAction();
		doubleEdgeButton = ComponentUtility.
			createToolbarToggleButton(doubleEdgeAction);
		buttonPane.add(doubleEdgeButton);
		
		eraserAction = gui.getEraserAction();
		eraserButton = ComponentUtility.
			createToolbarToggleButton(eraserAction);
		buttonPane.add(eraserButton);
	}
	
	/**
	 * Creates the scroll pane and the scroll area including the
	 * {@linkplain InteractiveGraphView} and adds itself as
	 * {@linkplain ComponentListener} to the scroll area.
	 * 
	 * @author Anselm Schmidt
	 */
	private void setupScrollArea() {
		// create components
		graphView = new InteractiveGraphView(this.graphController);
		graphController.addGraphObserver(graphView);
		scrollArea = new JPanel();
		scrollPane = new JScrollPane(scrollArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.addComponentListener(this);
		scrollPane.getVerticalScrollBar().addMouseListener(this);
		scrollPane.getHorizontalScrollBar().addMouseListener(this);
		
		// set up scroll area
		scrollArea.setLayout(new BorderLayout());
		scrollArea.add(graphView, BorderLayout.CENTER);
		
		// set sizes
		graphView.setPreferredSize(new Dimension(
				MAX_GRAPH_WIDTH, MAX_GRAPH_HEIGHT));
		
		// show the center of the scroll area
		int viewportWidth = scrollPane.getViewport().getWidth();
		int viewportHeight = scrollPane.getViewport().getHeight();
		int x = MAX_GRAPH_WIDTH / 2 - viewportWidth / 2;
		int y = MAX_GRAPH_HEIGHT / 2 - viewportHeight / 2;
		scrollPane.getViewport().setViewPosition(new Point(x, y));
		gui.setGraphViewPosition(new Point(x,y));
		mouseReleased(null);
	}
}