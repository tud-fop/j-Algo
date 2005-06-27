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
 * Created on 10.06.2005
 *
 */
package org.jalgo.tests.trees;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.main.trees.EdgeFigure;
import org.jalgo.main.trees.ITreeConstants;
import org.jalgo.main.trees.LeafFigure;

/**
 * @author Michael Pradel
 *
 */
public class GraphicalTree2 {

	public static void main(String[] args) {
		Display d = new Display();
		final Shell shell = new Shell(d);
		shell.setSize(400, 400);
		shell.setText("Tree test");
		LightweightSystem lws = new LightweightSystem(shell);
		Figure contents = new Figure();
		contents.setLayoutManager(new XYLayout());

		LeafFigure l1 = new LeafFigure();
		LeafFigure l2 = new LeafFigure();
		l2.setLocation(new Point(250,150));
			
		EdgeFigure e = new EdgeFigure(ITreeConstants.RIGHT_ROT);
		ChopboxAnchor a1 = new ChopboxAnchor(l1);
		ChopboxAnchor a2 = new ChopboxAnchor(l2);
		e.setSourceAnchor(a1);
		e.setTargetAnchor(a2);
		
		e.setText("Rechtsrotation!");
		e.setTextColor(ColorConstants.yellow);
		
		contents.add(l1);
		contents.add(l2);
		contents.add(e);
		
		lws.setContents(contents);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}
}
