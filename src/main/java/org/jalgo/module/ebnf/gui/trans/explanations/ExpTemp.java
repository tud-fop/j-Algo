package org.jalgo.module.ebnf.gui.trans.explanations;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.jalgo.main.util.Messages;


/**
 * This class represents a drawn terminal symbol of a syntax diagram
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class ExpTemp extends Explanation {

	private Font ebnffont;

	/**
	 * Inititalizes the element by getting the name of the symbol
	 * @param ebnfFont the EbnfFont
	 */
	public ExpTemp(Font ebnfFont) {

		this.ebnffont = ebnfFont;
				
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON));
		
		g2d.setFont(ebnffont);
		g2d.drawString("\u2022 "+Messages.getString("ebnf", "Trans.FirstAd"), 10, 30);
		g2d.drawString("\u2022 "+Messages.getString("ebnf", "Trans.SecondAd"), 10, 55);
		
	}

	

	

}