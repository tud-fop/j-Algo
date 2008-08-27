package org.jalgo.module.hoare.view;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;

/**
 * Class to get a round cell
 * @author Tomas
 * 
 * also see: http://jsourcery.com/api/sourceforge/jgraph/5.9.2.1/com/jgraph/example/mycellview/
 */
public class JGraphRoundRectView  extends  VertexView {
	
	private static final long serialVersionUID = 1L;
	/**
	 * value which discripes the rounding of the cells (higher -> more round)
	 */
	private static int roundValue = 5;
	public static transient CellViewRenderer normalRenderer = new PanelVertexRenderer();
	public static transient CellViewRenderer roundRectRenderer = new ActivityRenderer();
    
	/**
	 * Constructor calls super()
	 */
    public JGraphRoundRectView() {
        super();
    }
   
  /**
	 * Constructor calls super() with the given cell
	 * @param cell the Object
	 */
    public JGraphRoundRectView(Object cell) {
        super(cell);
    }
    
	/**
	 * Returns the intersection of the bounding rectangle and the straight line
	 * between the source and the specified point p. The specified point is
	 * expected not to intersect the bounds. Note: You must override this method
	 * if you use a different renderer. This is because this method relies on
	 * the VertexRenderer interface, which can not be safely assumed for
	 * subclassers.
	 */
	public Point2D getPerimeterPoint(EdgeView edge, Point2D source, Point2D p) {
		if (getRenderer() instanceof PanelVertexRenderer)
			return ((PanelVertexRenderer) getRenderer()).getPerimeterPoint(this,
					source, p);
		return super.getPerimeterPoint(edge, source, p);
	}

    /**
     * gets the Size of the Arc added by the roundValue
     * @param width
     * 		Cell Width
     * @param height
     * 		Cell Height
     * @return
     * 		Arcsize integer
     */
    public static int getArcSize(int width, int height) {
        int arcSize;
        if (width <= height) {
            arcSize = height / 5;
            if (arcSize > (width / 2)) {
                arcSize = width / 2;
            }
        } else  {
            arcSize = width / 5;
            if (arcSize > (height / 2)) {
                arcSize = height / 2;
            }
        }
        return arcSize + roundValue;
    }
   
    /**
     * getter for CellViewRenderer
     * @return CellViewRenderer
     */
    public CellViewRenderer getRenderer() {
    	if (((MyCell)getCell()).isRounded()) {
    		return roundRectRenderer;
    	}
    	else {
    		return normalRenderer;
    	}
    }
    
    public static class ActivityRenderer  extends  PanelVertexRenderer {
    	
		private static final long serialVersionUID = -7089865114703122718L;

		/**
    	 * overwridden getPreferredSize method to get the round cells
    	 * @return Dimension
    	 */
       public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width += d.height / 5;
            return d;
        }
       
       /**
         * overwridden paint method to get the round cells
         * @param Graphics
         */
        public void paint(Graphics g) {
        	int b = borderWidth;
           	Graphics2D g2 = (Graphics2D)g;
            Dimension d = getSize();
            boolean tmp = selected;
            int roundRectArc = JGraphRoundRectView.getArcSize(d.width - b, d.height - b);
            if (super.isOpaque()) {
                g.setColor(super.getBackground());
                if (gradientColor != null && !preview) {
                    setOpaque(false);
                    g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), gradientColor, true));
                }
                g.fillRoundRect(b / 2, b / 2, d.width - (int)(b * 1.5), d.height - (int)(b * 1.5), roundRectArc, roundRectArc);
            }
            try {
                setBorder(null);
                setOpaque(false);
                selected = false;
                super.paint(g);
            } finally {
                selected = tmp;
            }
            if (bordercolor != null) {
                g.setColor(bordercolor);
                g2.setStroke(new BasicStroke(b));
                g.drawRoundRect(b / 2, b / 2, d.width - (int)(b * 1.5), d.height - (int)(b * 1.5), roundRectArc, roundRectArc);
            }
        }
    }
}