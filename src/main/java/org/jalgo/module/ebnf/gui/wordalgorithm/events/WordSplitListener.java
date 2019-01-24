package org.jalgo.module.ebnf.gui.wordalgorithm.events;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.jalgo.module.ebnf.gui.wordalgorithm.GuiController;
import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.renderer.elements.RenderSplit;
import org.jalgo.module.ebnf.renderer.RenderValues;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;

/**
 * Listens a <code>Branch</code> or <code>Repetition</code> and performs
 * Actions if Left or Right Concatenation is empty. Can be added as a
 * <code>MouseListener</code> or <code>MouseMotionListener</code>.
 * 
 * @author Claas Wilke
 * 
 */
public class WordSplitListener implements MouseListener, MouseMotionListener {

	private RenderValues rv;

	private SynDiaElem myElement;

	private boolean top = false;

	private boolean in = false;

	boolean over = false;

	private Color highlightColor = RenderElement.HIGHLIGHT_BLUE;

	private GuiController myGuiController;

	/**
	 * Initializes the controller
	 * 
	 * @param controller
	 *            the GuiController which Model should used to perform Clicks.
	 * @param rv
	 *            RenderValues needed to Highlight the RenderElem
	 * @param myElement
	 *            The <code>SynDiaElem</code> which should be listened.
	 */
	public WordSplitListener(GuiController controller, RenderValues rv,
			SynDiaElem myElement) {

		this.myGuiController = controller;

		this.rv = rv;

		this.myElement = myElement;

	}

	public void mouseMoved(MouseEvent e) {

		// Checkout if curser is in Element
		if (in) {
			RenderSplit rb = (RenderSplit) e.getComponent();

			// Checkout if Repetition or Branch
			Repetition aRepetition = null;
			Branch aBranch = null;
			Concatenation myConcat = null;

			boolean parentRepPos = false;

			if (myElement instanceof Repetition) {
				aRepetition = (Repetition) myElement;
				if (myGuiController.getWordAlgoController().getPosition() == aRepetition
						&& !myGuiController.getWordAlgoController()
								.isPositionBehindElem()) {
					parentRepPos = true;
				}
			} else if (myElement instanceof Branch) {
				aBranch = (Branch) myElement;
			}

			// Left Concatenation
			if (e.getY() < 2 * rv.radius) {

				// Concat is just highlighted, if Concat is empty
				if (aRepetition != null) {
					myConcat = aRepetition.getLeft();
				} else if (aBranch != null) {
					myConcat = aBranch.getLeft();
				}
				if (myConcat != null
						&& !over
						&& (myGuiController.getWordAlgoController()
								.getPosition() != myConcat) && !parentRepPos) {

					if ((!top || in)
							&& myConcat.getNumberOfElems() == 0
							&& myGuiController.getWordAlgoController()
									.isElementReachable(myConcat)) {
						myGuiController.getWordAlgoController()
								.overElementEntry(myConcat, false);
						rb.setTopHighlight(true, highlightColor);
						rb.setBottomHighlight(false,
								RenderElement.STANDARD_COLOR);
						top = true;
						over = true;
					} else
						over = false;

				}

			}
			// Right Concatenation
			else {

				// Concat is just highlighted, if Concat is empty
				if (aRepetition != null) {
					myConcat = aRepetition.getRight();
				} else if (aBranch != null) {
					myConcat = aBranch.getRight();
				}
				if (myConcat != null
						&& !over
						&& (myGuiController.getWordAlgoController()
								.getPosition() != myConcat) && !parentRepPos) {

					if ((top || in)
							&& myConcat.getNumberOfElems() == 0
							&& myGuiController.getWordAlgoController()
									.isElementReachable(myConcat)) {
						myGuiController.getWordAlgoController()
								.overElementEntry(myConcat, false);
						rb.setTopHighlight(false, RenderElement.STANDARD_COLOR);
						rb.setBottomHighlight(true, highlightColor);
						top = false;
						over = true;
					} else
						over = false;

				}
			}
			// in = false;
		}

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

		if (myGuiController.getWordAlgoController().isAlgorithmRunning()) {
			in = true;
		} else {
			in = false;
		}

	}

	public void mouseExited(MouseEvent e) {

		if (in) {

			in = false;
			over = false;
			myGuiController.getWordAlgoController().overElementExit();

			RenderSplit rb = (RenderSplit) e.getComponent();
			rb.setTopHighlight(false, RenderElement.STANDARD_COLOR);
			rb.setBottomHighlight(false, RenderElement.STANDARD_COLOR);
		}

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

		if (in) {
			Repetition aRepetition = null;
			Branch aBranch = null;
			Concatenation myConcat = null;

			boolean parentRepPos = false;

			if (myElement instanceof Repetition) {
				aRepetition = (Repetition) myElement;
				if (myGuiController.getWordAlgoController().getPosition() == aRepetition
						&& !myGuiController.getWordAlgoController()
								.isPositionBehindElem()) {
					parentRepPos = true;
				}
			} else if (myElement instanceof Branch) {
				aBranch = (Branch) myElement;
			}

			// Left Concatenation
			if (e.getY() < 2 * rv.radius) {

				// Concat is just highlighted, if Concat is empty
				if (aRepetition != null) {
					myConcat = aRepetition.getLeft();
				} else if (aBranch != null) {
					myConcat = aBranch.getLeft();
				}
				if (myConcat != null
						&& (myGuiController.getWordAlgoController()
								.getPosition() != myConcat) && !parentRepPos) {

					if ((!top || in)
							&& myConcat.getNumberOfElems() == 0
							&& myGuiController.getWordAlgoController()
									.isElementReachable(myConcat)) {
						myGuiController.getWordAlgoController().gotoSplit(
								myConcat);
						in = false;
						over = false;

						RenderSplit rb = (RenderSplit) e.getComponent();
						rb.setTopHighlight(false, RenderElement.STANDARD_COLOR);
						rb.setBottomHighlight(false,
								RenderElement.STANDARD_COLOR);
					}

				}

			}
			// Right Concatenation
			else {

				// Concat is just highlighted, if Concat is empty
				if (aRepetition != null) {
					myConcat = aRepetition.getRight();
				} else if (aBranch != null) {
					myConcat = aBranch.getRight();
				}
				if (myConcat != null
						&& (myGuiController.getWordAlgoController()
								.getPosition() != myConcat) && !parentRepPos) {

					if ((top || in) && myConcat.getNumberOfElems() == 0) {
						myGuiController.getWordAlgoController().gotoSplit(
								myConcat);
						in = false;
						over = false;

						RenderSplit rb = (RenderSplit) e.getComponent();
						rb.setTopHighlight(false, RenderElement.STANDARD_COLOR);
						rb.setBottomHighlight(false,
								RenderElement.STANDARD_COLOR);
					}

				}
			}
		}
	}

	public void mouseClicked(MouseEvent e) {

	}

}
