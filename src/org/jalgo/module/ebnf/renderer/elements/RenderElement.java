/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import javax.swing.JComponent;

import org.jalgo.module.ebnf.renderer.RenderValues;

/**
 * @author Andre Viergutz
 */
public abstract class RenderElement extends JComponent {

	/**
	 * a fat stroke, used on mouse over in the syntax diagram editor
	 */
	public static final Stroke HIGHLIGHT_STROKE = new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
	/**
	 * a warning red, used to show elements to delete
	 */
	public static final Color HIGHLIGHT_COLOR = new Color(255, 50, 50);
	/**
	 * the standard stroke
	 */
	public static final Stroke STANDARD_STROKE = new BasicStroke();
	/**
	 * the standard color of basic strokes in diagram
	 */
	public static final Color STANDARD_COLOR = new Color(0, 0, 0);
	/**
	 * the standard fill color of elements
	 */
	public static final Color STANDARD_FILL_COLOR = new Color(255, 255, 255);
	/**
	 * a light green
	 */
	public static final Color HIGHLIGHT_COLOR_2 = new Color(220, 255, 220);
	/**
	 * a light yellow
	 */
	public static final Color HIGHLIGHT_YELLOW = new Color(240, 240, 200);
	/**
	 * a light blue
	 */
	public static final Color HIGHLIGHT_BLUE = new Color(200, 200, 240);
	
	protected Color color = STANDARD_FILL_COLOR;

	protected RenderValues rv = new RenderValues();

	protected AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	/**
	 * Must be implemented, nothing happens (paintComponent is called anyway)
	 */
	public abstract void update();
	
	/** Sets the given Color
	 * @param c a Color object
	 */
	public void setColor(Color c) {
		this.color = c;
		this.repaint();
	}	
	/** Changes each coloor channel by 20 values.
	 * @param brighter true, if it should be brighter or false, if darker
	 */
	public void changeColor(boolean brighter) {

		if (brighter) {
			int r = color.getRed() + 20;
			int g = color.getGreen() + 20;
			int b = color.getBlue() + 20;
			
			if (r > 255) r = 255;
			if (g > 255) g = 255;
			if (b > 255) b = 255;
			
			color = new Color(r, g, b);
		} else {
			
			int r = color.getRed() - 20;
			int g = color.getGreen() - 20;
			int b = color.getBlue() - 20;
			
			if (r > 255) r = 255;
			if (g > 255) g = 255;
			if (b > 255) b = 255;
			
			color = new Color(r, g, b);
			
		}	
		update();
		this.getParent().repaint();

	}

	/** Sets the given <code>RenderValues</code>
	 * @param rv
	 */
	public void setRenderValues(RenderValues rv) {

		this.rv = rv;
	}
		

}
