package org.jalgo.module.app.view.run;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jalgo.module.app.controller.*;
import org.jalgo.module.app.core.graph.*;

/**
 * Displays the adjacency matrix during the Graph Editing mode.
 * 
 */
public class MatrixPreviewComponent extends JPanel implements GraphObserver {

	private static final long serialVersionUID = -8497615664297725862L;
	private GraphController controller;
	
	private MatrixDisplay display;
	private boolean beamerMode;

	public MatrixPreviewComponent(GraphController controller) {
		display = new MatrixDisplay(false);

		this.controller = controller;
		controller.addGraphObserver(this);
		
		display.addComponentListener(new MatrixComponentListener());
		
		display.hideMatrixName();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(Box.createRigidArea(new Dimension(10,10)));
		this.add(display);
		this.add(Box.createRigidArea(new Dimension(10,10)));
	}
	
	public void nodeAdded(Graph graph, Node node) {
	}

	public void nodeAltered(Graph graph, Node node) {
	}

	public void nodeRemoved(Graph graph, Node node) {
	}

	public void edgeAdded(Graph graph, Edge edge) {
	}

	public void edgeAltered(Graph graph, Edge edge) {
	}

	public void edgeRemoved(Graph graph, Edge edge) {
	}

	public void graphUpdated() {
		display.updateMatrix(controller.getInitialMatrix());
		display.repaint();
		display.revalidate();
	}
	
	public void updateBeamerMode(boolean beamerMode) {
		this.beamerMode = beamerMode;
		display.updateBeamerMode(beamerMode);
		repaint();
		revalidate();
	}
	
	public void graphSelectionChanged() {
	}
	
	private class MatrixComponentListener implements ComponentListener {

		public void componentHidden(ComponentEvent arg0) {
			componentResized(arg0);	
		}

		public void componentMoved(ComponentEvent arg0) {
			componentResized(arg0);			
		}

		public void componentResized(ComponentEvent event) {
			if (display.getHeight() != display.getWidth()) {
				display.scaleTo(getWidth(), getWidth());
				graphUpdated();
			}
		}

		public void componentShown(ComponentEvent arg0) {
			componentResized(arg0);			
		}
	}
}
