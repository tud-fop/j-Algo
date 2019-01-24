package org.jalgo.module.ebnf.gui.wordalgorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.wordalgorithm.StackAdress;
import org.jalgo.module.ebnf.renderer.wordalgorithm.StackBase;
import org.jalgo.module.ebnf.gui.syndia.display.IDrawPanel;
import org.jalgo.module.ebnf.gui.wordalgorithm.SynDiaPanel;

/**
 * This Panel contains the Graphical part of the Stack during the WordAlgorithm.
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class StackDrawPanel extends JPanel implements IDrawPanel {

	// Formats of Adresses on Stack
	private static final int ADRESS_WIDTH = 40;

	private static final int ADRESS_HEIGHT = 30;

	private static final int BASE_HEIGHT = 3;

	// Space between bottom and lowest Adress
	private static final int BOTTOM_HEIGHT = 10;

	// Vars on stack since last update
	private List varsOnStack;

	// Used to paint the Stack really
	private JPanel myAdressPanel;

	/**
	 * Constructor to create a new <code>StackDrawPanel</code>.
	 * 
	 * @param varsOnStack
	 *            The initilization values on the stack.
	 */
	public StackDrawPanel(List varsOnStack) {

		this.setLayout(null);
		this.setBackground(BACKGROUND_COLOR);

		this.varsOnStack = varsOnStack;

		// Needed because Stack should be scrollable
		this.myAdressPanel = new SynDiaPanel(BACKGROUND_COLOR);
		this.myAdressPanel.setVisible(true);
		this.add(myAdressPanel);

		// the Stack is painted with allAdresses in the List.
		paintStack(varsOnStack, false);

	}

	/**
	 * Method called, if the <code>StackDrawPanel</code> should be painted.
	 * 
	 * @param varsOnStack
	 *            The new Adresses on the stack (first element lowest adress).
	 * @param highestAdressHighlighted
	 *            If true, the highest Adress on the stack is be highlighted.
	 */
	public void paintStack(List varsOnStack, boolean highestAdressHighlighted) {

		this.varsOnStack = varsOnStack;

		paintStack(varsOnStack, highestAdressHighlighted,
				RenderElement.HIGHLIGHT_BLUE);

	}

	/**
	 * Method called, if the <code>StackDrawPanel</code> should be painted.
	 * 
	 * @param varsOnStack
	 *            The new Adresses on the stack (first element lowest adress).
	 * @param highestAdressHighlighted
	 *            If true, the highest Adress on the stack is be highlighted.
	 * @param hColor
	 *            The Color, the hightes Adress should be highlighted in
	 */
	public void paintStack(List varsOnStack, boolean highestAdressHighlighted,
			Color hColor) {

		this.varsOnStack = varsOnStack;

		// The panel is resetted
		this.myAdressPanel.removeAll();

		// The minimum Size of the AdressPannel is updated because
		// every Adress on the Stack schould be viewable.
		updateSize();

		// StartPositions
		// Coordinates to draw the adresses
		int posXCenter = (int) Math.round((this.myAdressPanel.getWidth() / 2));
		int posX = (int) Math.round(posXCenter - (ADRESS_WIDTH / 2));
		int posY = (int) Math.round(this.myAdressPanel.getHeight());

		// Base Line is drawn.
		int baseWidth = this.myAdressPanel.getWidth();
		if (baseWidth == 0)
			baseWidth = ADRESS_WIDTH;
		StackBase myBase = new StackBase(baseWidth);

		posY = posY - myBase.getHeight() - BOTTOM_HEIGHT;
		myBase.setLocation((int) Math.round(posXCenter
				- (myBase.getWidth() / 2)), posY);
		myBase.update();
		myBase.setVisible(true);
		myBase.repaint();
		this.myAdressPanel.add(myBase);

		// Eachs adress is drawn
		Iterator adrListIterator = varsOnStack.iterator();
		int i = 0;

		while (adrListIterator.hasNext()) {
			i++;
			Integer anAdress = (Integer) adrListIterator.next();

			// The Y-position is decremented before print the adress.
			posY = posY - ADRESS_HEIGHT - BOTTOM_HEIGHT;

			StackAdress aRenderAdress = new StackAdress(anAdress.toString());
			// If its the last Adress and this should be highlighted
			if (!adrListIterator.hasNext() && highestAdressHighlighted) {
				aRenderAdress.setColor(hColor);
			}
			aRenderAdress.setSize(ADRESS_WIDTH, ADRESS_HEIGHT);
			aRenderAdress.setLocation(posX, posY);
			aRenderAdress.update();
			this.myAdressPanel.add(aRenderAdress);
			aRenderAdress.setVisible(true);
			aRenderAdress.repaint();
		}

		// The graphical view is updated:
		this.myAdressPanel.repaint();
	}

	// Size must be updated because the visible size could have been changed
	// (if the size of the jAlgo Window has changed).
	public void repaint() {

		updateSize();

		super.repaint();
	}

	/**
	 * Sets the adressPanel to its minimum size. Could be the visible part or if
	 * the number of Adresses on the Stack is bigger, it could be higher.
	 * 
	 */
	public void updateSize() {
		if (varsOnStack != null) {
			// New panel Size if StackList can be higher or lower than the
			// Panel's
			// height.
			int minHeight = (int) (varsOnStack.size()
					* (ADRESS_HEIGHT + BOTTOM_HEIGHT) + BASE_HEIGHT + BOTTOM_HEIGHT * 3);

			// So it's updated.
			Rectangle myVisiblePart = this.getVisibleRect();
			int minWidth = myVisiblePart.width;
			if (ADRESS_WIDTH > myVisiblePart.width)
				minWidth = ADRESS_WIDTH;

			myAdressPanel.setSize(new Dimension(minWidth, minHeight));
			myAdressPanel.setPreferredSize(new Dimension(minWidth, minHeight));

			int difference = myVisiblePart.height - minHeight;
			if (difference < 0)
				difference = 0;

			myAdressPanel.setLocation(0, difference);
			myAdressPanel.repaint();

			if (minHeight < myVisiblePart.height)
				minHeight = myVisiblePart.height;

			this.setSize(new Dimension(minWidth, minHeight));
			this.setPreferredSize(new Dimension(minWidth, minHeight));
		}
	}

}
