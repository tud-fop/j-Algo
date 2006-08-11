/**
 * 
 */
package org.jalgo.module.ebnf.gui.trans.explanations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import org.jalgo.main.util.Messages;


/**
 * This class represents a drawn terminal symbol of a syntax diagram
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class ExpConc extends Explanation {

	private Font ebnffont;

	

	private Line2D.Double line_left;
	private Line2D.Double line_right;
	
	/**
	 * Inititalizes the element by getting the name of the symbol
	 * @param ebnfFont the EbnfFont
	 */
	public ExpConc(Font ebnfFont) {

		this.ebnffont = ebnfFont;

		

	}

	public void paintComponent(Graphics g) {
		
		int radius = 15;
		int x = 170;
		int y = 65;
		int width = 120;
				
		line_left = new Line2D.Double(x, y, x + width, y);
		line_right = new Line2D.Double(x + width + 2 * radius, y, x + 2*(width + radius), y);
		
				
		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
		//lines
		g2d.setFont(ebnffont);
		g2d.setColor(Color.BLACK);
		
		g2d.draw(line_left);
		g2d.draw(line_right);
		
		g2d.setColor(Color.WHITE);
		// trans element
		
		g2d.fillRoundRect(x + radius, y -radius, width - 2 * radius, 30, 30, 30);
		g2d.fillRoundRect(x + width + 3 * radius, y + - radius, width - 2 * radius, 30, 30, 30);
		
		
		
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(x + radius, y - radius, width - 2 * radius, 30, 30, 30);
		g2d.drawRoundRect(x + width + 3 * radius, y - radius, width - 2 * radius, 30, 30, 30);
		
		
		g2d.drawString("trans(\u03b11)", x + 2 * radius, y + 5 );
		g2d.drawString("...", x + width + 8, y+2);
		g2d.drawString("trans(\u03b1n)", x + width + 4 * radius, y + 5 );
		

		g2d.drawString(Messages.getString("ebnf", "Trans.Sei")+" \u03b11...\u03b1n \u2208 T(\u2211,V)", 10, 30);
		g2d.drawString("\u2022 trans(\u03b11...\u03b1n) =", 50, 70);

	}

}