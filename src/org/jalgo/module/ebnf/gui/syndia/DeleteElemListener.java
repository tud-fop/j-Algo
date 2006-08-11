package org.jalgo.module.ebnf.gui.syndia;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.event.MouseInputListener;

import org.jalgo.module.ebnf.gui.trans.SynDiaElemNotFoundException;
import org.jalgo.module.ebnf.model.syndia.Branch;
import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.Repetition;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.renderer.elements.RenderBranch;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderNoStairBranch;
import org.jalgo.module.ebnf.renderer.elements.RenderRepetition;
import org.jalgo.module.ebnf.renderer.elements.RenderSplit;
import org.jalgo.module.ebnf.renderer.elements.RenderTerminal;
import org.jalgo.module.ebnf.renderer.elements.RenderVariable;

/**
 * Mouse- and MouseMotionListener that highlights branches and repetitions in
 * delete mode. If mouse is clicked, it deletes the highlighted elements.
 * 
 * @author Michael Thiele
 * 
 */
public class DeleteElemListener implements MouseInputListener {

	private GuiController guiController;

	private boolean top = false;

	private boolean in = true;

	/**
	 * Initalizes the listener.
	 * 
	 * @param guiController
	 *            the guiController, the listener belongs to
	 */
	public DeleteElemListener(GuiController guiController) {
		this.guiController = guiController;
	}

	private SynDiaElem getElem(Component component) {
		Map<RenderElement, SynDiaElem> renderMap = guiController.getRenderMap();
		if (renderMap.containsKey(component)) {
			return renderMap.get(component);
		}
		return null;
	}

	/**
	 * If the mosue is moved inside the component, this method checks if it is
	 * in the top part of the branch or repetition or the lower part of it. It
	 * highlights the actual path and all children of it.
	 */
	public void mouseMoved(MouseEvent e) {

		if (e.getComponent() instanceof RenderSplit) {
			RenderSplit rb = (RenderSplit) e.getComponent();

			if (e.getY() < 2 * guiController.getRenderRadius()) {

				if (!top || in) {
					rb.setTopHighlight(true, RenderElement.HIGHLIGHT_COLOR);
					rb.setBottomHighlight(false, RenderElement.STANDARD_COLOR);
					// search for elements on that line, that have to be
					// highlighted too
					Concatenation concat;
					// old highlighted elems back to "normal"
					if (rb instanceof RenderBranch
							|| rb instanceof RenderNoStairBranch) {
						concat = ((Branch) getElem(rb)).getRight();
					} else {
						concat = ((Repetition) getElem(rb)).getRight();
					}
					changeHighlightMode(concat, false);
					// highlight actual elems
					if (rb instanceof RenderBranch
							|| rb instanceof RenderNoStairBranch) {
						concat = ((Branch) getElem(rb)).getLeft();
					} else {
						concat = ((Repetition) getElem(rb)).getLeft();
					}
					changeHighlightMode(concat, true);
					top = true;
				}

			} else {

				if (top || in) {
					rb.setTopHighlight(false, RenderElement.STANDARD_COLOR);
					rb.setBottomHighlight(true, RenderElement.HIGHLIGHT_COLOR);
					// search for elements on that line, that have to be
					// highlighted too
					Concatenation concat;
					// old highlighted elems back to "normal"
					if (rb instanceof RenderBranch
							|| rb instanceof RenderNoStairBranch) {
						concat = ((Branch) getElem(rb)).getLeft();
					} else {
						concat = ((Repetition) getElem(rb)).getLeft();
					}
					changeHighlightMode(concat, false);
					// highlight actual elems
					if (rb instanceof RenderBranch
							|| rb instanceof RenderNoStairBranch) {
						concat = ((Branch) getElem(rb)).getRight();
					} else {
						concat = ((Repetition) getElem(rb)).getRight();
					}
					changeHighlightMode(concat, true);
					top = false;
				}
			}
			in = false;
		}
	}

	public void mouseDragged(MouseEvent e) {

	}

	/**
	 * If mouse enters this element, change color for terminal symbols and
	 * variables and initialize branch and repetition highlighting.
	 */
	public void mouseEntered(MouseEvent e) {

		RenderElement re = (RenderElement) e.getComponent();
		if (re instanceof RenderSplit) {
			in = true;
		} else if (re instanceof RenderTerminal || re instanceof RenderVariable) {
			re.setColor(RenderElement.HIGHLIGHT_COLOR);
		}
	}

	/**
	 * If mouse exits the element, change colors back to standard.
	 */
	public void mouseExited(MouseEvent e) {

		RenderElement re = (RenderElement) e.getComponent();
		if (re instanceof RenderSplit) {
			RenderSplit rb = (RenderSplit) re;
			rb.setTopHighlight(false, RenderElement.STANDARD_COLOR);
			rb.setBottomHighlight(false, RenderElement.STANDARD_COLOR);
			// search for elements on that line, that have to be
			// highlighted too
			Concatenation concat;
			if (top) {
				if (rb instanceof RenderBranch
						|| rb instanceof RenderNoStairBranch) {
					concat = ((Branch) getElem(rb)).getLeft();
				} else {
					concat = ((Repetition) getElem(rb)).getLeft();
				}
			} else {
				if (rb instanceof RenderBranch
						|| rb instanceof RenderNoStairBranch) {
					concat = ((Branch) getElem(rb)).getRight();
				} else {
					concat = ((Repetition) getElem(rb)).getRight();
				}
			}
			changeHighlightMode(concat, false);
		} else if (re instanceof RenderTerminal || re instanceof RenderVariable) {
			re.setColor(RenderElement.STANDARD_FILL_COLOR);
		}

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * If mouse is clicked, delete all highlighted elements.
	 */
	public void mouseClicked(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1) {
			Component currentComponent = e.getComponent();
			if (currentComponent instanceof RenderBranch) {
				if (top) {
					guiController.getSynDiaController().removeBranch(
							(Branch) getElem(currentComponent), false, true);
				} else {
					guiController.getSynDiaController().removeBranch(
							(Branch) getElem(currentComponent), true, false);
				}
			} else if (currentComponent instanceof RenderRepetition) {
				if (top) {
					guiController.getSynDiaController()
							.removeRepetition(
									(Repetition) getElem(currentComponent),
									false, true);
				} else {
					guiController.getSynDiaController()
							.removeRepetition(
									(Repetition) getElem(currentComponent),
									true, false);
				}
			}
		}
	}

	/**
	 * @param sde
	 *            a SynDiaElem finding itself in the RenderMap
	 * @return the render element representing the syntax diagram element in the
	 *         model
	 * @throws SynDiaElemNotFoundException
	 */
	private RenderElement getReFromSde(SynDiaElem sde)
			throws SynDiaElemNotFoundException {
		Map<RenderElement, SynDiaElem> renderMap = guiController.getRenderMap();
		if (renderMap.containsValue(sde)) {
			for (RenderElement re : renderMap.keySet()) {
				if (sde == renderMap.get(re))
					return re;
			}
		}
		throw new SynDiaElemNotFoundException(
				"RenderElement existiert nicht in der Map");
	}

	private void changeHighlightMode(Concatenation concat, boolean highlight) {
		// no NullElems
		for (int i = 1; i < concat.getNumberOfElems(); i = i + 2) {
			SynDiaElem sde = concat.getSynDiaElem(i);
			if (sde instanceof TerminalSymbol || sde instanceof Variable) {
				try {
					if (highlight) {
						getReFromSde(sde).setColor(
								RenderElement.HIGHLIGHT_COLOR);
					} else {
						getReFromSde(sde).setColor(
								RenderElement.STANDARD_FILL_COLOR);
					}
				} catch (SynDiaElemNotFoundException e1) {
					e1.printStackTrace();
				}
			} else if (sde instanceof Branch) {
				try {
					if (highlight) {
						((RenderSplit) getReFromSde(sde)).setTopHighlight(
								highlight, RenderElement.HIGHLIGHT_COLOR);
						((RenderSplit) getReFromSde(sde)).setBottomHighlight(
								highlight, RenderElement.HIGHLIGHT_COLOR);
					} else {
						((RenderSplit) getReFromSde(sde)).setTopHighlight(
								highlight, RenderElement.STANDARD_COLOR);
						((RenderSplit) getReFromSde(sde)).setBottomHighlight(
								highlight, RenderElement.STANDARD_COLOR);
					}
					// recursively call changeHighlightMode for children of a
					// branch
					changeHighlightMode(((Branch) sde).getLeft(), highlight);
					changeHighlightMode(((Branch) sde).getRight(), highlight);
				} catch (SynDiaElemNotFoundException e2) {
					e2.printStackTrace();
				}
			} else if (sde instanceof Repetition) {
				try {
					if (highlight) {
						((RenderSplit) getReFromSde(sde)).setTopHighlight(
								highlight, RenderElement.HIGHLIGHT_COLOR);
						((RenderSplit) getReFromSde(sde)).setBottomHighlight(
								highlight, RenderElement.HIGHLIGHT_COLOR);
					} else {
						((RenderSplit) getReFromSde(sde)).setTopHighlight(
								highlight, RenderElement.STANDARD_COLOR);
						((RenderSplit) getReFromSde(sde)).setBottomHighlight(
								highlight, RenderElement.STANDARD_COLOR);
					}
					// recursively call changeHighlightMode for children of a
					// repetition
					changeHighlightMode(((Repetition) sde).getLeft(), highlight);
					changeHighlightMode(((Repetition) sde).getRight(),
							highlight);
				} catch (SynDiaElemNotFoundException e2) {
					e2.printStackTrace();
				}
			}
		}
	}

}
