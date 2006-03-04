/*
 * Created on 11.06.2005
 *
 */
package org.jalgo.tests.trees;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jalgo.main.trees.Leaf;
import org.jalgo.main.trees.Tree;

/**
 * @author Michael Pradel
 *
 */
public class GraphicalTree4 {

	public static void main(String[] args) {
		Display d = new Display();
		final Shell shell = new Shell(d);
		shell.setSize(400, 400);
		shell.setText("Tree test");
		LightweightSystem lws = new LightweightSystem(shell);
		Figure contents = new Figure();
		contents.setLayoutManager(new FlowLayout());

		Tree t1 = new Tree("11");
		Tree t2 = new Tree("22");
		Leaf l1 = new Leaf();
		Tree t3 = new Tree("33");
		t1.addChild(t2);
		t1.addChild(l1);
		t2.addChild(t3);
		Figure f = t1.layout();
		
		contents.add(f);
		//contents.add(t2.getNodeFigure());
		//contents.add(l1.getFigure());
		
		
		
		lws.setContents(contents);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}
}
