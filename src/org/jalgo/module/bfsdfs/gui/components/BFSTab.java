package org.jalgo.module.bfsdfs.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;

import org.jalgo.module.bfsdfs.algorithms.Algo;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.graphview.GraphView;

/**
 * <code>BFSTab</code> manages the breadth first search algorithm. The user can
 * click through it step by step (forward and backward), reset, run and cancel
 * it.<br>
 * The resulting graph is directly shown in the top end of the window, the
 * {@linkplain GraphView}.<br>
 * Furthermore the user can choose the start node and the successor order if
 * multiple successors are available.<br>
 * The queue that is used by the algorithm is visualized by the use of
 * {@linkplain NodeStackView}.
 * 
 * @author Florian Dornbusch
 */

public class BFSTab
extends AlgoTab {
	private static final long serialVersionUID = -218770648365873881L;

	public BFSTab(GUIController gui, GraphController graph, Algo algo) {
		super(gui, graph, algo);
	}	
	
	@Override
	protected void adjustLayout() {
		// add observers
		gui.addBFSUndoableObserver(this);
		gui.addBFSStackObserver(this);
		gui.addBFSStackObserver(nodeStackView);
			
		// adjust components
		nodeStackView.setSuccessorsVisible(false);
		stackScrollPane.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		stackScrollPane.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		JPanel blubb = new JPanel();
		blubb.setLayout(new BoxLayout(blubb, BoxLayout.PAGE_AXIS));
		blubb.add(Box.createVerticalGlue());
		blubb.add(stackScrollPane);
		blubb.add(Box.createVerticalGlue());
		nodeViewPane.add(blubb, BorderLayout.CENTER);
		
		controlPane.setLayout(new BorderLayout());
		controlPane.add(nodeViewPane, BorderLayout.CENTER);
		controlPane.add(chooserPane, BorderLayout.EAST);
		
		// add main components to the content pane
		add(scrollPane, BorderLayout.CENTER);
		add(controlPane, BorderLayout.SOUTH);
	}
	
	public void toggleBeamerMode() {
		super.toggleBeamerMode();
		stackScrollPane.setPreferredSize(new Dimension(0,nodeSize));
	}
}