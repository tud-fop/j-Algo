/*
 * Created on 03.06.2005
 *
 */
package org.jalgo.tests.trees;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.main.trees.LeafFigure;

/**
 * @author Michael Pradel
 *  
 */
public class GraphicalTree1 {

	public static void main(String[] args) {
		Display d = new Display();
		final Shell shell = new Shell(d);
		shell.setSize(400, 400);
		shell.setText("Tree test");
		LightweightSystem lws = new LightweightSystem(shell);
		Figure contents = new Figure();

		LeafFigure l1 = new LeafFigure("NIASasssssssssssssssasSSS");		
		contents.add(l1);
		
		lws.setContents(contents);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}
}
