package org.jalgo.module.ebnf.gui.wordalgorithm.events;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.jalgo.module.ebnf.gui.wordalgorithm.GuiController;
import org.jalgo.module.ebnf.gui.wordalgorithm.SynDiaPanel;
import org.jalgo.module.ebnf.renderer.wordalgorithm.ReturnAdress;
import org.jalgo.module.ebnf.model.syndia.SyntaxDiagram;
import org.jalgo.module.ebnf.model.syndia.SynDiaElem;
import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderTerminal;
import org.jalgo.module.ebnf.renderer.elements.RenderVariable;

/**
 * This Listener performs the Click onto a Variable or a TerminalSymbol.
 * 
 * @author Claas Wilke
 * 
 */
public class WordSynDiaListener implements MouseListener {

	// Neede to Highlight Element during MouseOver
	private Color highlightColor = RenderElement.HIGHLIGHT_BLUE;
	private static final Color DEHIGHLIGHTED_COLOR = Color.WHITE;
	private static final Color RETURNADRESS_DEHIGHLIGHTED_COLOR = Color.BLACK;

	private GuiController myGuiController;

	private Object myCaller;
	
	private boolean over = false;

	/**
	 * Initializes the controller
	 * 
	 * @param controller
	 *            the GuiController which Model should used to perform Clicks.
	 * @param aSynDiaElem
	 *            The <code>SynDiaElem</code> which clicks should be performed
	 *            by this Controller.
	 */
	public WordSynDiaListener(GuiController controller, SynDiaElem aSynDiaElem) {

		this.myGuiController = controller;

		this.myCaller = aSynDiaElem;
		
	}

	/**
	 * Initializes the controller
	 * 
	 * @param controller
	 *            the GuiController which Model should used to perform Clicks.
	 * @param aSyntaxDiagram The
	 *            <code>SyntaxDiagram</code> which clicks should be performed
	 *            by this Controller.
	 */
	public WordSynDiaListener(GuiController controller, SyntaxDiagram aSyntaxDiagram) {

		this.myGuiController = controller;

		this.myCaller = aSyntaxDiagram;
		
	}

	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Handles a MousOver event.
	 * 
	 * Changes the Explanation.
	 * 
	 */
	public void mouseEntered(MouseEvent e) {
		// If Element was not entered before, enter.
		if (!this.over) {
			// Checkout which type of SynDiaElem was clicked
			Object caller = e.getSource();
			if ((caller instanceof RenderVariable
					|| caller instanceof RenderTerminal) && myGuiController.getWordAlgoController().isAlgorithmRunning()) {

				this.over = true;

				SynDiaElem aSynDiaElem = (SynDiaElem) myCaller;
				boolean isReacheable = myGuiController.getWordAlgoController().overElementEntry(
						aSynDiaElem, false);

				// Color of Element is highlighted
				RenderElement aRenderElem = (RenderElement) caller;
				if (isReacheable) {
					aRenderElem.setColor(this.highlightColor);
				}
			}

			if (caller instanceof ReturnAdress && myGuiController.getWordAlgoController().isAlgorithmRunning() && myGuiController.getWordAlgoController().isOnlyEndReachable()) {

				this.over = true;

				SynDiaElem aSynDiaElem = (SynDiaElem) myCaller;
				boolean isReacheable = myGuiController.getWordAlgoController().overElementEntry(
						aSynDiaElem, true);
				

				// Color of Element is highlighted
				RenderElement aRenderElem = (RenderElement) caller;
				if (isReacheable) {
					aRenderElem.setColor(this.highlightColor);
				}
			}

			// If the algorithm is during a Jump to a Diagram
			if (caller instanceof SynDiaPanel) {
				if (myGuiController.getWordAlgoController().isJumpToDiagram()) {
					
					SynDiaPanel myCallerPanel = (SynDiaPanel) caller;
					myCallerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(highlightColor, 3));

					this.over = true;
				}
			}

		}
	}

	/**
	 * Handles a MousOver event.
	 * 
	 * Changes the Explanation.
	 * 
	 */
	public void mouseExited(MouseEvent e) {
		// If Element was enteredbefore, leave.
		if (this.over) {
			// Checkout which type of SynDiaElem was clicked
			Object caller = e.getSource();
			if (caller instanceof RenderVariable
					|| caller instanceof RenderTerminal
					|| caller instanceof ReturnAdress) {
				this.over = false;

				// Color of Element is dehighlighted
				RenderElement aRenderElem = (RenderElement) caller;
				if (aRenderElem instanceof ReturnAdress) {
					aRenderElem.setColor(RETURNADRESS_DEHIGHLIGHTED_COLOR);					
				}
				else {
				aRenderElem.setColor(DEHIGHLIGHTED_COLOR);
				}

				// Change Explanation
				myGuiController.getWordAlgoController().overElementExit();
			}
			
			if (caller instanceof SynDiaPanel) {

				// Change Explanation
				myGuiController.getWordAlgoController().overElementExit();

				SynDiaPanel myCallerPanel = (SynDiaPanel) caller;
					myCallerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				this.over = false;
			}

		}
	}

	/**
	 * Performs a Click on a <code>SynDiaElem</code> or
	 * <code>SyntaxDiagram</code>.
	 * 
	 */
	public void mouseReleased(MouseEvent e) {
		if (myGuiController.getWordAlgoController().isAlgorithmRunning()) {
		// Checkout which type of SynDiaElem was clicked
		Object caller = e.getSource();
		// If it was a Variable, perform a Click onto a Variable.
		if (caller instanceof RenderVariable) {
			Variable callerVar = (Variable) myCaller;
			myGuiController.getWordAlgoController().gotoVariable(callerVar);
			RenderElement aRenderElem = (RenderElement) caller;
			if (!(myGuiController.getWordAlgoController().isElementReachable(callerVar) && myGuiController.getWordAlgoController().isAlgorithmRunning())) {
			aRenderElem.setColor(DEHIGHLIGHTED_COLOR);
			this.over = false;
			}
		}
		// If it was a ReturnAdress of a Variable, perform a return to a
		// Variable.
		if (caller instanceof ReturnAdress && myGuiController.getWordAlgoController().isOnlyEndReachable()) {
			Variable callerVar = (Variable) myCaller;
			boolean wasSuccessfull = myGuiController.getWordAlgoController().returnToDiagram(callerVar);
			RenderElement aRenderElem = (RenderElement) caller;
			if (!(myGuiController.getWordAlgoController().isEndReached() && myGuiController.getWordAlgoController().isAlgorithmRunning())) {
				aRenderElem.setColor(RETURNADRESS_DEHIGHLIGHTED_COLOR);
				this.over = false;
			}
			if (!wasSuccessfull) {
				animationReturn(aRenderElem);
			}
		}
		// If it was a Termnial, perform a Click onto a Terminal.
		if (caller instanceof RenderTerminal) {
			TerminalSymbol callerTerminal = (TerminalSymbol) myCaller;
			myGuiController.getWordAlgoController()
					.gotoTerminal(callerTerminal);
			RenderElement aRenderElem = (RenderElement) caller;
			if (!(myGuiController.getWordAlgoController().isElementReachable(callerTerminal) && myGuiController.getWordAlgoController().isAlgorithmRunning())) {
			aRenderElem.setColor(DEHIGHLIGHTED_COLOR);
			this.over = false;
			}
		}
		// If it was a SyntaxDiagram, perform a Click onto a SyntaxDiagram.
		if (caller instanceof SynDiaPanel) {
			SyntaxDiagram callerDiagram = (SyntaxDiagram) myCaller;
			if (myGuiController.getWordAlgoController().isJumpToDiagram()) {
			boolean wasSuccessfull = myGuiController.getWordAlgoController()
					.jumpToDiagram(callerDiagram);
			SynDiaPanel myCallerPanel = (SynDiaPanel) caller;
			if (wasSuccessfull) {
				myCallerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
				this.over = false;
			}
			else {
				animationJump(myCallerPanel);
			}
			}
		}
		}

	}

	public void mousePressed(MouseEvent e) {
	}
	
	private void animationJump(SynDiaPanel aCallerPanel) {
		aCallerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED, 3));
	}
	
	private void animationReturn(RenderElement aRenderElem) {
		aRenderElem.setColor(Color.RED);
	}

}
