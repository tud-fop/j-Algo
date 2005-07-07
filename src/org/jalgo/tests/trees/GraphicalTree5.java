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
public class GraphicalTree5 {

	public static void main(String[] args) {
		Display d = new Display();
		final Shell shell = new Shell(d);
		shell.setSize(400, 400);
		shell.setText("Tree test");
		LightweightSystem lws = new LightweightSystem(shell);
		Figure contents = new Figure();
		contents.setLayoutManager(new FlowLayout());

		Tree t1 = new Tree("1");
		Tree t2 = new Tree("2");
		Leaf l1 = new Leaf();
		Leaf l2 = new Leaf("NILPFERD");
		Leaf l3 = new Leaf();
		t1.addChild(t2);
		t1.addChild(l1);
		t2.addChild(l2);
		t2.addChild(l3);
		
		Figure all = new Figure();
		FlowLayout allLayout = new FlowLayout(false);
		allLayout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
		allLayout.setMinorSpacing(20);
		all.setLayoutManager(allLayout);
		
		all.add(t1.getNodeFigure());
		Figure children1 = new Figure();
		FlowLayout children1Layout = new FlowLayout(true);
		children1Layout.setMinorSpacing(20);
		children1.setLayoutManager(children1Layout);
		
		Figure subTree = new Figure();
		FlowLayout subTreeLayout = new FlowLayout(false);
		subTreeLayout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
		subTreeLayout.setMinorSpacing(20);
		subTree.setLayoutManager(subTreeLayout);
		
		subTree.add(t2.getNodeFigure());
		Figure children2 = new Figure();
		FlowLayout children2Layout = new FlowLayout(true);
		children2Layout.setMinorSpacing(20);
		children2.setLayoutManager(children2Layout);
		
		children2.add(l2.getFigure());
		children2.add(l3.getFigure());
		subTree.add(t2.getNodeFigure());
		subTree.add(children2);
		children1.add(subTree);
		children1.add(l1.getFigure());
		all.add(t1.getNodeFigure());
		all.add(children1);
		
		contents.add(all); 
		
		
		lws.setContents(contents);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}
}
