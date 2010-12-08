package org.jalgo.module.bfsdfs.gui.components;

import java.awt.BorderLayout;

import org.jalgo.module.bfsdfs.algorithms.Algo;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.graphview.GraphView;

/**
 * <code>DFSTab</code> manages the depth first search algorithm. The user can
 * click through it step by step (forward and backward), run it, and reset it.
 * <br>
 * The resulting graph is directly shown in the left end of the window, the
 * {@linkplain GraphView}.<br>
 * Furthermore the user can choose the start node and the successor order if
 * multiple successors are available.<br>
 * The stack that is used by the algorithm is visualized by the use of
 * {@linkplain NodeStackView}.
 * 
 * @author Florian Dornbusch
 */

public class DFSTab
extends AlgoTab {
	private static final long serialVersionUID = -4710478217401676189L;

	public DFSTab(GUIController gui, GraphController graph, Algo algo) {
		super(gui, graph, algo);
	}
	
	@Override
	protected void adjustLayout() {
		// add observers
		gui.addDFSUndoableObserver(this);
		gui.addDFSStackObserver(this);
		gui.addDFSStackObserver(nodeStackView);
		
		//adjust components
		nodeStackView.setSuccessorsVisible(true);
		controlPane.setLayout(new BorderLayout());
		controlPane.add(nodeViewPane, BorderLayout.CENTER);
		controlPane.add(chooserPane, BorderLayout.SOUTH);
		
		// add main components to the content pane
		add(scrollPane, BorderLayout.CENTER);
		add(controlPane, BorderLayout.EAST);		
	}
}