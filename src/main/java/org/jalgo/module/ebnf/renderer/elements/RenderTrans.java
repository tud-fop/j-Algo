/**
 * 
 */
package org.jalgo.module.ebnf.renderer.elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import org.jalgo.module.ebnf.gui.EbnfFont;
import org.jalgo.module.ebnf.gui.FontNotInitializedException;

/**
 * This class represents a transformable element of a syntax diagram used in the
 * trans() algorithm
 * 
 * @author Andre
 */
@SuppressWarnings("serial")
public class RenderTrans extends RenderElement {

	private static Color TRANS_HIGHLIGHT_STD = new Color(90, 110, 80);

	/**
	 * Represents some shape
	 */
	private RoundRectangle2D.Double kreis;

	/**
	 * Represents the "trans(..)"-String
	 */
	private String label;

	private List<String> stringList;

	private boolean showSeperated = false;

	/**
	 * Initializes the elements
	 * 
	 * @param label
	 *            the trans(..)-String
	 * @param stringList
	 *            all trans(..)-Strings separated by first layer
	 */
	public RenderTrans(String label, List<String> stringList) {

		this.setEnabled(true);
		this.label = label;
		this.stringList = stringList;
		kreis = new RoundRectangle2D.Double();

		color = new Color(170, 190, 160);
	}

	public void paintComponent(Graphics g) {

		// font
		Font ebnfFont = null;
		try {
			ebnfFont = EbnfFont.getFont().deriveFont(Font.ITALIC,
					rv.font.getSize());
		} catch (FontNotInitializedException e) {

			e.printStackTrace();
		}

		FontMetrics fm = getFontMetrics(ebnfFont);

		// rendering hints
		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));

		// rounded rectangle
		g2d.setColor(color);
		g2d.fill(kreis);

		// String
		g2d.setFont(ebnfFont); // (new Font(rv.font.getName(), Font.ITALIC,
								// rv.font.getSize()));
		g2d.setColor(new Color(0, 0, 0));

		if (showSeperated) {
			int x = (int) Math.round(0.6 * rv.radius);

			// "trans(" begin String
			g2d.setColor(TRANS_HIGHLIGHT_STD);
			g2d.drawString("trans(", x, (int) Math.floor(1.3 * rv.radius));
			x += fm.stringWidth("trans(");

			// Term in different colors
			for (int i = 0; i < stringList.size(); i++) {

				String s = stringList.get(i);

				if (i % 2 == 0)
					g2d.setColor(new Color(250, 250, 250));
				else
					g2d.setColor(new Color(0, 0, 0));

				g2d.drawString(s, x, (int) Math.floor(1.3 * rv.radius));

				x += fm.stringWidth(s);

			}
			// ")" closing bracket
			g2d.setColor(TRANS_HIGHLIGHT_STD);
			g2d.drawString(")", x, (int) Math.floor(1.3 * rv.radius));
		} else {

			g2d.drawString(label, (int) Math.round(0.6 * rv.radius), (int) Math
					.floor(1.3 * rv.radius));
		}
		g2d.setColor(Color.BLACK);
		g2d.draw(kreis);

	}

	/**
	 * Tells the element if it should display the trans(..)-String in separated
	 * colors or not
	 * 
	 * @param showSep true, if should be separated (alterning black - white)
	 */
	public void showSeperated(boolean showSep) {

		this.showSeperated = showSep;
		this.repaint();
	}

	public void update() {

		kreis.setRoundRect(1, 1, getWidth() - 2, getHeight() - 2,
				getHeight() - 2, getHeight() - 2);

	}

}
