/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

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
