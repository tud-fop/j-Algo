package org.jalgo.module.ebnf.gui.wordalgorithm;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.syndia.display.IDrawPanel;

/**
 * This Panel represents the area in which <i>ALL</i>
 * <code>SyntaxDiagrams</code> should be drawn on.
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class SynDiaSystemPanel extends JPanel implements IDrawPanel {

	// needed to print the Background
	private String displayName = Messages.getString("ebnf", "WordAlgo.BgName");

	private double factor = .2;

	/**
	 * Constructor to create a new <code>SynDiaSystemPanel</code>.
	 * 
	 */
	public SynDiaSystemPanel() {

		this.setLayout(null);
		this.setBackground(BACKGROUND_COLOR);

	}

	/**
	 * Calculates the width of a given String object in pixel
	 * 
	 * @param s
	 *            The String object to calculate
	 * @param f
	 *            The Font which size is to calculate
	 * @return the width of the String object in pixel
	 */
	public int getWidthFromString(String s, Font f) {

		FontMetrics fontmetrics = this.getFontMetrics(f);

		return fontmetrics.stringWidth(s);

	}

	/**
	 * Paint the <code>SynDiaSystemPanel</code> and its background.
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		int fontSize = (int) Math.round(factor * this.getParent().getWidth());
		Font f = new Font("Courier", Font.BOLD, fontSize);

		g2d.setFont(f);
		g2d.setColor(FONT_COLOR);
		int y = this.getParent().getHeight() + (int) Math.round(0.2 * fontSize);
		g2d.rotate(-Math.PI * 7 / 180);
		g2d.drawString(displayName, (int) (this.getParent().getWidth()
				- getWidthFromString(displayName, f) - factor * 0.2
				* this.getParent().getWidth()), y);

		g2d.rotate(Math.PI * 7 / 180);
	}

}
