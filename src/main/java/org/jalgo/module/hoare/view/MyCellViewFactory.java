package org.jalgo.module.hoare.view;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.VertexView;

/**
 * Cell View Factory changed to return our JGraphRoundRectView to draw the cells
 * 
 * @author Tomas
 */
public class MyCellViewFactory  extends  DefaultCellViewFactory {

    private static final long serialVersionUID = 1L;
/**
 * overridden method to return our JGraphRoundRectView
 * 
 * @param Object (Cell)
 * 		DefaultGraphCell which should be painted
 * @return VertexView
 * 		VertexView (JGraphRoundRectView) for Jgraph to use
 */
	protected VertexView createVertexView(Object v) {

    	VertexView view = new JGraphRoundRectView(v);
    	view.setCell(v);
        return view;

  }
    
    
}