package org.jalgo.module.ebnf.renderer.wordalgorithm;

import org.jalgo.module.ebnf.renderer.elements.RenderBase;
import org.jalgo.module.ebnf.renderer.elements.RenderBranch;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;
import org.jalgo.module.ebnf.renderer.elements.RenderRepetition;
import org.jalgo.module.ebnf.renderer.elements.RenderTerminal;
import org.jalgo.module.ebnf.renderer.elements.RenderVariable;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * This Element is the graphical representation of the actual Position in a
 * <code>SyntaxDiagram</code> during the WordAlgorithm
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class Position extends RenderElement {

	// Size of the positionPoint
	private int positionSize;

	// Needed to correct positions
	private int corr;

	private int radius;

	// Represents some shape
	protected Rectangle2D.Double rect;

	/**
	 * Initializes the element
	 * 
	 * @param aRenderElem
	 *            The <code>RenderElement</code> that is the actual Position
	 *            in a <code>SyntaxDiagram</code> during the WordAlgorithm.
	 * @param alternativeMode
	 *            makes a second position of the Position possible. For example
	 *            the Position at the returnAdress of a Variable when the user
	 *            should jump to a Diagram.
	 * @param behindElement
	 *            if true, the position is drawn behind the Element which
	 *            represents the actual position.
	 * @param renderFont
	 *            The font which is used, to draw Terminals or Variables.
	 * @param changeDirection
	 *            specifies wether or not, the direction should be changed (used
	 *            in repetitions)
	 */
	public Position(RenderElement aRenderElem, boolean alternativeMode,
			boolean behindElement, Font renderFont, boolean changeDirection) {

		int fontSize = renderFont.getSize();
		
		FontMetrics fm = new JPanel().getFontMetrics(new Font("Courier",
				Font.PLAIN, fontSize));
		radius = (int) Math.round(0.7 * fm.getHeight());
		corr = (int) Math.round(radius / 1.5);

		// Height of the positionPoint is 60% of the Height of a Variable.
		this.positionSize = (int) Math.round(0.6 * fm.getHeight());

		// The ADRESS_SIZE part size of its Variable
		this.setSize(positionSize, positionSize);

		// Checkout, which Location is needed for the Position
		// It depends on the type of aRenderElem

		// If aRenderElem is a Terminal, the Position can be
		// behind or in front of the RenderElem. It depends on the depth
		// of repetitions.
		if (aRenderElem instanceof RenderTerminal) {
			this.setLocation(initTerminal(aRenderElem, alternativeMode,
					behindElement, changeDirection));
		}

		// If aRenderElem is a Variable, the Position can be behind the Variable
		// or at the jump to a Diagram
		if (aRenderElem instanceof RenderVariable) {
			this.setLocation(initVariable(aRenderElem, alternativeMode,
					behindElement, changeDirection));
		}

		// If aRenderElem is a RenderBase the position must be the begin of the
		// SyntaxDiagram.
		else if (aRenderElem instanceof RenderBase) {
			this.setLocation(initRenderBase(aRenderElem, alternativeMode,
					behindElement));
		}

		// If aRenderElem is a RenderRepetition the position must be the begin
		// of the
		// SyntaxDiagram.
		else if (aRenderElem instanceof RenderRepetition) {
			this.setLocation(initRepetition(aRenderElem, alternativeMode,
					behindElement, changeDirection));
		}

		// If aRenderElem is a RenderBranch the position must be the begin of
		// the
		// SyntaxDiagram.
		else if (aRenderElem instanceof RenderBranch) {
			this.setLocation(initBranch(aRenderElem, alternativeMode,
					behindElement, changeDirection));
		}

		int correctureX;

		if (this.getX() < 0)
			correctureX = 0;
		else
			correctureX = this.getX();
		this.setLocation(correctureX, this.getY());

		rect = new Rectangle2D.Double();

	}

	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		// Shadow
		g2d.setColor(Color.BLACK);
		rect.x += 3;
		rect.y += 3;
		g2d.setComposite(makeComposite(0.4F));
		g2d.fill(rect);

		// Point itself
		rect.x -= 3;
		rect.y -= 3;
		g2d.setComposite(makeComposite(1F));
		g2d.setColor(Color.RED);
		g2d.fill(rect);

		g2d.draw(rect);

	}

	public void update() {
		rect.setRect(0, 0, this.getWidth() - 4, this.getHeight() - 4);
	}

	/**
	 * Init method if aRenderElem is a Terminal
	 * 
	 * @param aRenderElem
	 *            The <code>RenderElement</code> that is the actual Position
	 *            in a <code>SyntaxDiagram</code> during the WordAlgorithm.
	 * @param alternativeMode
	 *            makes a second position of the Position possible. For example
	 *            the Position at the returnAdress of a Variable when the user
	 *            should jump to a Diagram.
	 * @param behindElement
	 *            if true, the position is drawn behind the Element which
	 *            represents the actual position.
	 * @return a Point which represents the Location where the Position should
	 *         be drawn.
	 * @param changeDirection
	 *            specifies wether or not, the direction should be changed
	 */
	private Point initTerminal(RenderElement aRenderElem,
			boolean alternativeMode, boolean behindElement,
			boolean changeDirection) {

		int posX, posY;

		// The location realative to the Element
		// Ceckout if position should be drawn in front of or behind
		if (behindElement ^ changeDirection) {
			posX = (int) Math.round(aRenderElem.getX() + aRenderElem.getWidth()
					+ corr * 0.2);
			posY = (int) Math.round(aRenderElem.getY() + radius
					- (positionSize / 2) + 1);
		} else {
			posX = (int) Math.round(aRenderElem.getX() - corr * 1.2);
			posY = (int) Math.round(aRenderElem.getY() + radius
					- (positionSize / 2) + 1);
		}

		return new Point(posX, posY);
	}

	/**
	 * Init method if aRenderElem is a Variable
	 * 
	 * @param aRenderElem
	 *            The <code>RenderElement</code> that is the actual Position
	 *            in a <code>SyntaxDiagram</code> during the WordAlgorithm.
	 * @param alternativeMode
	 *            makes a second position of the Position possible. For example
	 *            the Position at the returnAdress of a Variable when the user
	 *            should jump to a Diagram.
	 * @param behindElement
	 *            if true, the position is drawn behind the Element which
	 *            represents the actual position.
	 * @return a Point which represents the Location where the Position should
	 *         be drawn.
	 * @param changeDirection
	 *            specifies wether or not, the direction should be changed
	 */
	private Point initVariable(RenderElement aRenderElem,
			boolean alternativeMode, boolean behindElement,
			boolean changeDirection) {

		int posX, posY;

		// Checkout, if jump to Diagram or not
		// The location realative to the Element
		if (alternativeMode) {
			posX = (int) Math.round(aRenderElem.getX() + aRenderElem.getWidth()
					+ (aRenderElem.getHeight() * ReturnAdress.ADRESS_SIZE / 2)
					- this.getWidth());
			posY = (int) Math.round(aRenderElem.getY()
					- (aRenderElem.getHeight() * ReturnAdress.ADRESS_SIZE / 2));
		}
		// Ceckout if position should be drawn in front of or behind
		else if (behindElement ^ changeDirection) {
			posX = (int) Math.round(aRenderElem.getX() + aRenderElem.getWidth()
					+ corr * 0.2);
			posY = Math.round(aRenderElem.getY() + radius - (positionSize / 2)
					+ 1);
		} else {
			posX = (int) Math.round(aRenderElem.getX() - corr * 1.2);
			posY = Math.round(aRenderElem.getY() + radius - (positionSize / 2)
					+ 1);
		}

		return new Point(posX, posY);
	}

	/**
	 * Init method if aRenderElem is a Branch
	 * 
	 * @param aRenderElem
	 *            The <code>RenderElement</code> that is the actual Position
	 *            in a <code>SyntaxDiagram</code> during the WordAlgorithm.
	 * @param alternativeMode
	 *            makes a second position of the Position possible. For example
	 *            the Position at the returnAdress of a Variable when the user
	 *            should jump to a Diagram.
	 * @param behindElement
	 *            if true, the position is drawn behind the Element which
	 *            represents the actual position.
	 * @param changeDirection
	 *            specifies wether or not, the direction should be changed
	 * @return a Point which represents the Location where the Position should
	 *         be drawn.
	 */
	private Point initBranch(RenderElement aRenderElem,
			boolean alternativeMode, boolean behindElement,
			boolean changeDirection) {

		int posX, posY;

		// The location realative to the Element
		if (!changeDirection) {
			if (behindElement) {
				posX = (int) Math.round(aRenderElem.getX()
						+ aRenderElem.getWidth() + (corr * .4));
				posY = Math.round(aRenderElem.getY() + radius - positionSize
						/ 2);
			} else {
				posX = (int) Math.round(aRenderElem.getX() - (corr * 1.4));
				posY = Math.round(aRenderElem.getY() + radius - positionSize
						/ 2);
			}
		} else {
			if (behindElement) {
				posX = (int) Math.round(aRenderElem.getX()
						- (corr * 1.4));
				posY = Math.round(aRenderElem.getY() + radius - positionSize
						/ 2);
			} else {
				posX = (int) Math.round(aRenderElem.getX()
						+ aRenderElem.getWidth() + (corr * .4));
				posY = Math.round(aRenderElem.getY() + radius - positionSize
						/ 2);
			}
		}

		return new Point(posX, posY);
	}

	/**
	 * Init method if aRenderElem is a Repetition
	 * 
	 * @param aRenderElem
	 *            The <code>RenderElement</code> that is the actual Position
	 *            in a <code>SyntaxDiagram</code> during the WordAlgorithm.
	 * @param alternativeMode
	 *            makes a second position of the Position possible. For example
	 *            the Position at the returnAdress of a Variable when the user
	 *            should jump to a Diagram.
	 * @param behindElement
	 *            if true, the position is drawn behind the Element which
	 *            represents the actual position.
	 * @param changeDirection
	 *            specifies wether or not, the direction should be changed
	 * @return a Point which represents the Location where the Position should
	 *         be drawn.
	 */
	private Point initRepetition(RenderElement aRenderElem,
			boolean alternativeMode, boolean behindElement,
			boolean changeDirection) {

		int posX, posY;

		// The location realative to the Element
		if (!changeDirection) {
			if (behindElement) {
				posX = (int) Math.round(aRenderElem.getX()
						+ aRenderElem.getWidth());
				posY = Math.round(aRenderElem.getY() + radius - positionSize
						/ 2);
			} else {
				posX = (int) Math.round(aRenderElem.getX() + corr * 1.5);
				posY = Math.round(aRenderElem.getY() + radius - positionSize
						/ 2);
			}
		} else {
			if (behindElement) {
				posX = (int) Math.round(aRenderElem.getX());
				posY = Math.round(aRenderElem.getY() + radius - positionSize
						/ 2);
			} else {
				posX = (int) Math.round(aRenderElem.getX()
						+ aRenderElem.getWidth() - positionSize * 2);
				posY = Math.round(aRenderElem.getY() + radius - positionSize
						/ 2);
			}

		}

		return new Point(posX, posY);
	}

	/**
	 * Init method if aRenderElem is a RenderBase
	 * 
	 * @param aRenderElem
	 *            The <code>RenderElement</code> that is the actual Position
	 *            in a <code>SyntaxDiagram</code> during the WordAlgorithm.
	 * @param alternativeMode
	 *            makes a second position of the Position possible. For example
	 *            the Position at the returnAdress of a Variable when the user
	 *            should jump to a Diagram.
	 * @param behindElement
	 *            if true, the position is drawn behind the Element which
	 *            represents the actual position.
	 * @return a Point which represents the Location where the Position should
	 *         be drawn.
	 */
	private Point initRenderBase(RenderElement aRenderElem,
			boolean alternativeMode, boolean behindElement) {

		// The positionHeight must be correted. It depends on the position
		// height
		int correcture = 3;
		if (positionSize > 30)
			correcture = -1;
		else if (positionSize > 24)
			correcture = 0;
		else if (positionSize > 16)
			correcture = 1;
		else if (positionSize > 8)
			correcture = 2;

		int posX, posY;

		// The location realative to the Element
		if (behindElement) {
			posX = aRenderElem.getWidth() - rv.radius;
			posY = Math.round(aRenderElem.getHeight() * 2 / 3) + correcture;
		} else {
			posX = aRenderElem.getX() + 3;
			posY = Math.round(aRenderElem.getHeight() * 2 / 3) + correcture;
		}

		return new Point(posX, posY);
	}
}