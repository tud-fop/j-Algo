/**
 * 
 */
package org.jalgo.module.ebnf.renderer.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.jalgo.module.ebnf.renderer.RenderValues;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderSplit;

/**
 * @author Andre
 * 
 */
public class SplitListener implements MouseListener, MouseMotionListener {

	private RenderValues rv;

	private boolean top = false;

	private boolean in = true;

	/**
	 * Initializes the controller
	 * 
	 * @param rv
	 *            The <code>RenderValues</code> to calculate the border
	 *            between top and bottom
	 */
	public SplitListener(RenderValues rv) {

		this.rv = rv;

	}

	public void mouseMoved(MouseEvent e) {

		RenderSplit rb = (RenderSplit) e.getComponent();

		if (e.getY() < 2 * rv.radius) {

			if (!top || in) {
				rb.setTopHighlight(true, RenderElement.HIGHLIGHT_COLOR);
				rb.setBottomHighlight(false, RenderElement.STANDARD_COLOR);
				top = true;
			}

		} else {

			if (top || in) {
				rb.setTopHighlight(false, RenderElement.STANDARD_COLOR);
				rb.setBottomHighlight(true, RenderElement.HIGHLIGHT_COLOR);
				top = false;
			}
		}
		in = false;

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

		in = true;

	}

	public void mouseExited(MouseEvent e) {

		RenderSplit rb = (RenderSplit) e.getComponent();
		rb.setTopHighlight(false, RenderElement.STANDARD_COLOR);
		rb.setBottomHighlight(false, RenderElement.STANDARD_COLOR);

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseClicked(MouseEvent e) {

	}

}
