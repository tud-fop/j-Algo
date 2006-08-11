/**
 * 
 */
package org.jalgo.module.ebnf.gui.trans.explanations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

import org.jalgo.main.util.Messages;


/**
 * This class represents a drawn terminal symbol of a syntax diagram
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class ExpAlt extends Explanation {

	private Font ebnffont;

	private Arc2D.Double arc_left_top;

	private Arc2D.Double arc_right_top;

	private Arc2D.Double arc_left_bottom;

	private Arc2D.Double arc_right_bottom;

	private Line2D.Double line_top;

	private Line2D.Double line_bottom;

	private Line2D.Double line_left;

	private Line2D.Double line_right;

	
	/**
	 * Inititalizes the element by getting the name of the symbol
	 * @param ebnfFont The EbnfFont
	 */
	public ExpAlt(Font ebnfFont) {

		this.ebnffont = ebnfFont;

		

	}

	public void paintComponent(Graphics g) {
		
		int radius = 15;
		int x = 190;
		int y = 40;
		int width = 180;
		int height = 50;

		arc_left_top = new Arc2D.Double(x, y, 2 * radius, 2 * radius,
				0, 90, Arc2D.OPEN);
		arc_left_bottom = new Arc2D.Double(x + 2 * radius, y + height - 2 * radius,
				2 * radius, 2 * radius, 180, 90, Arc2D.OPEN);
		arc_right_top = new Arc2D.Double(x + width - 2 * radius, y, 2 * radius,
				2 * radius, 90, 90, Arc2D.OPEN);
		arc_right_bottom = new Arc2D.Double(x + width - 4 * radius, y + height
				- 2 * radius, 2 * radius, 2 * radius, 270, 90, Arc2D.OPEN);

		line_top = new Line2D.Double(x, y, x + width, y);

		line_bottom = new Line2D.Double(x + 3 * radius, y + height, x + width
				- 3 * radius, y + height);

		line_left = new Line2D.Double(x + 2 * radius, y + radius, x + 2 * radius, y
				+ height - radius);
		line_right = new Line2D.Double(x + width - 2 * radius, y + radius, x
				+ width - 2 * radius, y + height - radius);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
		//lines
		g2d.setFont(ebnffont);
		g2d.setColor(Color.BLACK);
		g2d.draw(arc_left_top);
		g2d.draw(arc_left_bottom);
		g2d.draw(arc_right_top);
		g2d.draw(arc_right_bottom);

		g2d.draw(line_top);
		g2d.draw(line_bottom);
		g2d.draw(line_left);
		g2d.draw(line_right);

		g2d.setColor(Color.WHITE);
		// trans element
		
		g2d.fillRoundRect(x + 3 * radius, y -radius, width - 6 * radius, 30, 30, 30);
		g2d.fillRoundRect(x + 3 * radius, y + height - radius, width - 6 * radius, 30, 30, 30);
		
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(x + 3 * radius, y - radius, width - 6 * radius, 30, 30, 30);
		g2d.drawRoundRect(x + 3 * radius, y + height - radius, width - 6 * radius, 30, 30, 30);

		g2d.drawString("trans(\u03b11)", x + 3 * radius + 15, y + 5 );
		g2d.drawString("trans(\u03b12)", x + 3 * radius + 15, y + height + 5 );
		

		g2d.drawString(Messages.getString("ebnf", "Trans.Sei")+" \u03b11,\u03b12 \u2208 T(\u2211,V)", 10, 30);
		g2d.drawString("\u2022 trans(\u3011\u03b11\u300e\u03b12\u3012) =", 50, 70);

	}

}