/*
 * Created on May 29, 2004
 */
 
package org.jalgo.main.gui.widgets;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.gui.ScrollZoomCanvas;

/**
 * @author Cornelius Hald
 * @author Christopher Friedrich
 */
public class GraphViewForm extends CustomViewForm {

	private ScrollZoomCanvas canvas;

	public GraphViewForm(Composite parent, int style) {

		super(parent, style);

		// Init the canvas
		canvas = new ScrollZoomCanvas(this);
		canvas.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
		canvas.setScrollBarVisibility(1);
		canvas.setLayoutManager(new StackLayout());

		// Sets the content of this GraphViewForm
		setContent(canvas);
		canvas.layout();
		canvas.validate();
		canvas.revalidate();
		canvas.redraw();
		
	}

	public void zoomIn() {
		canvas.setZoom(canvas.getZoom() * 1.2f);
	}

	public void zoomToFit() {
		canvas.setZoom(1.0f);
	}

	public void zoomOut() {
		canvas.setZoom(canvas.getZoom() * 0.8f);
	}

	public IFigure getPanel() {
		return canvas.getContents();
	}

	public void setPanel(IFigure f) {
		canvas.setContents(f);
	}
}
