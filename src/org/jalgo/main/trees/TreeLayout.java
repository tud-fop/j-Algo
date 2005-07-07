/*
 * Created on 11.06.2005
 *
 */
package org.jalgo.main.trees;

import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Michael Pradel
 *
 */
public class TreeLayout extends AbstractLayout {

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure, int, int)
	 */
	protected Dimension calculatePreferredSize(IFigure tree, int wHint, int hHint) {
		// TODO: copied from draw2d example - verify that correct here
		tree.validate();
		List children = tree.getChildren();
		Rectangle result =
			new Rectangle().setLocation(tree.getClientArea().getLocation());
		for (int i = 0; i < children.size(); i++)
			result.union(((IFigure)children.get(i)).getBounds());
		result.resize(tree.getInsets().getWidth(), tree.getInsets().getHeight());
		return result.getSize();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout(IFigure treeFigure) {
		
	}

}
