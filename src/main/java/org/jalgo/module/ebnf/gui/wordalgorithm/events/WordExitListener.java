package org.jalgo.module.ebnf.gui.wordalgorithm.events;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.jalgo.module.ebnf.gui.wordalgorithm.GuiController;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.renderer.RenderValues;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.wordalgorithm.RenderEnd;

/**
 * Listens a <code>RenderEnd</code> and performs Actions if end is clicked or
 * MouseOvered.
 * 
 * @author Claas Wilke
 * 
 */
public class WordExitListener implements MouseListener, MouseMotionListener {

	private RenderValues rv;

	private boolean in = false;

	private Color highlightColor = RenderElement.HIGHLIGHT_BLUE;

	private GuiController myGuiController;

	private SyntaxDiagram myDiagram;

	/**
	 * Initializes the controller
	 * 
	 * @param controller
	 *            the GuiController which Model should used to perform Clicks.
	 * @param rv
	 *            RenderValues needed to Highlight the RenderElem
	 * @param myDiagram
	 *            The <code>SyntaxDiagram</code>, the Exit belongs to.
	 */
	public WordExitListener(GuiController controller, RenderValues rv,
			SyntaxDiagram myDiagram) {

		this.myGuiController = controller;

		this.rv = rv;

		this.myDiagram = myDiagram;

	}

	public void mouseMoved(MouseEvent e) {

		if (myGuiController.getWordAlgoController().isAlgorithmRunning()) {
			RenderEnd rb = (RenderEnd) e.getComponent();

			// Check if curser is over the exit line
			if (e.getY() > 2.8 * rv.radius && e.getY() < 3.2 * rv.radius) {

				if (!in) {
					// Checkout if end could be reached, an is not reached
					// allready.
					in = myGuiController.getWordAlgoController()
							.overSynDiaExitEntry(myDiagram);

					if (in) {
						rb.setHighlighted(true, highlightColor);
					}
				}
			}
			// else check if exit from exit line
			else {
				if (in) {
					myGuiController.getWordAlgoController()
							.overSynDiaExitExit();
					in = false;

					rb.setHighlighted(false, RenderElement.STANDARD_COLOR);
				}
			}
		}
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

		// If position was over exit line, rechange Explanation
		// and highlight color
		if (in) {
			myGuiController.getWordAlgoController().overSynDiaExitExit();
			in = false;

			RenderEnd rb = (RenderEnd) e.getComponent();
			rb.setHighlighted(false, RenderElement.STANDARD_COLOR);
		}

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

		// Perform click, if position is over exit line
		if (e.getY() > 2.8 * rv.radius && e.getY() < 3.2 * rv.radius) {

			if (in) {
				myGuiController.getWordAlgoController().leaveDiagram();
				in = false;
				RenderEnd rb = (RenderEnd) e.getComponent();
				// rehighlight
				rb.setHighlighted(false, RenderElement.STANDARD_COLOR);
			}

		}

	}

	public void mouseClicked(MouseEvent e) {

	}

}
