/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and
 * platform independent. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jalgo.module.ebnf.gui.ebnf;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import org.jalgo.module.ebnf.gui.GUIConstants;

/**The class <code>EbndTextField</code> extends an JTextField to add some RenderingHints and
 * makes it possible to highlight our EBNF-Symbols and their counterpart
 * @author Tom Kazimiers
 *
 */
public class EbnfTextField extends JTextField {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int movecount = 0;
	private int nextpos = 0;
	private int margin_left;
	private int margin_top;
	private int caretPos = 0;
	private EbnfTextField me = null;

	String strOpening = String.valueOf(RenderConstants.LBRACE)+String.valueOf(RenderConstants.LBRACKET)+String.valueOf(RenderConstants.LPARENTHESES);
	String strClosing = String.valueOf(RenderConstants.RBRACE)+String.valueOf(RenderConstants.RBRACKET)+String.valueOf(RenderConstants.RPARENTHESES);

	public EbnfTextField() {
		me = this;
        initializeTimer();
	}
	
	 
    public EbnfTextField(String text)
    {
        super(text);
    }
    
    /**This function initializes the timer t which is responsible for repainting the textfield.
     * At the moment t does this every 200ms with a check if the caret has moved for more then one
     * field.
     * 
     *
     */private void initializeTimer() {
    	javax.swing.Timer t = new javax.swing.Timer( 150, new ActionListener() {
    		  public void actionPerformed( ActionEvent e ) {
    	    		if ((movecount != caretPos) || (movecount - 1 != caretPos) || (movecount + 1 != caretPos)) {
    	    			me.repaint();
		      		}
//    			  SwingUtilities.invokeLater( new Runnable() { 
//    				    public void run() {
//    				guiController.cancelRule();
//    				}} );
    		  }
    		});
    		t.start();
    }
 
    @Override
	protected void paintComponent(Graphics g)
    {
    	// Unicode-Mappings: "\u300C" = [, "\u300D" = ], "\u300E" = |",
    	// "\u300F" = (, " \u3010" = ), " \u3011" = {, "\u3012" = }
    	// 3014 und 3015 sind nochmal () für die generierten Klammern
    	
    	Graphics2D g2 = (Graphics2D)g;
    	caretPos = this.getCaretPosition();
    	
    	String text = " ";
    	
    	
    	if (caretPos>0) {
    		try {
				text = this.getText(caretPos - 1, 1);
			} catch (BadLocationException e) {
				// should not occur because of the if-clause
			}
		}

        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
        super.paintComponent(g);
        
        if (strClosing.contains(text)) {	 //Bracket is closing
        	margin_left = getInsets().left;
        	margin_top = getInsets().top;
        	nextpos = findNextChaaracterPos(caretPos, text, false);
			if (nextpos >= 0) {
				g2.setColor(GUIConstants.START_SCREEN_BACKGROUND);
				g2.draw(findCorrectCharacterPos(nextpos));
			} else {
				g2.setColor(GUIConstants.ERROR_COLOR);
			}
				
			g2.draw(findCorrectCharacterPos(caretPos));
    		movecount = caretPos;
    		
        } else if (strOpening.contains(text)) {			// Bracket is opening
				margin_left = getInsets().left;
				margin_top = getInsets().top;
				nextpos = findNextChaaracterPos(caretPos, text, true);
				if (nextpos >= 0) {
					g2.setColor(GUIConstants.START_SCREEN_BACKGROUND);
					g2.draw(findCorrectCharacterPos(nextpos));
				} else {
					g2.setColor(GUIConstants.ERROR_COLOR);
				}
					
				g2.draw(findCorrectCharacterPos(caretPos));
				movecount = caretPos;
		}
    		
        
    	
    }
    
    /**The function <code>findCorrectCharacterPos</code> generates a fitting rectangle for
     * out brackets, parenth. and braces.
     * 
     * @param pos is the character-position in the TextField
     * @return the correct rectangel is returned
     */private Rectangle2D findCorrectCharacterPos(int pos) {
    	Font font = this.getFont();
    	FontMetrics metrics = getFontMetrics( font );
    	Rectangle2D rect;
    	int position = 0;
    	int width = 0;
    	
    	try {
    		
    		position = margin_left + metrics.stringWidth(this.getText(0, pos-1)) - 1;
    		width = metrics.stringWidth(this.getText(pos-1, 1));
		} catch (BadLocationException e) {
			// This appears to be no serious error, so it can be ignored
		}
		
		rect = new Rectangle2D.Float(position, margin_top + 1, width + 3, 26);
    	
    	return rect;
    }
    
    /**This function finds the correct closing or opening bracket/parenth./brace for an correspondending
     * closing/opening one.
     * 
     * @param pos is the start position to search from
     * @param searchtext is the String of the bracket/parenth./brace to look for
     * @param fromlefttoright should be set to true if the clicket bracket/.../... is opening, otherwise falses
     * @return the position of the found matching bracket/.../... is given back
     */private int findNextChaaracterPos(int pos, String searchtext, boolean fromlefttoright) {
    	String nextbracket = "";
    	String text = this.getText();
    	int counter = 0;
    	if (searchtext.equals(String.valueOf(RenderConstants.LBRACKET))) nextbracket = String.valueOf(RenderConstants.RBRACKET);
    	else if (searchtext.equals(String.valueOf(RenderConstants.LBRACE))) nextbracket = String.valueOf(RenderConstants.RBRACE);
    	else if (searchtext.equals(String.valueOf(RenderConstants.LPARENTHESES))) nextbracket = String.valueOf(RenderConstants.RPARENTHESES);
    	else if (searchtext.equals(String.valueOf(RenderConstants.RBRACKET))) nextbracket = String.valueOf(RenderConstants.LBRACKET);
    	else if (searchtext.equals(String.valueOf(RenderConstants.RBRACE))) nextbracket = String.valueOf(RenderConstants.LBRACE);
    	else if (searchtext.equals(String.valueOf(RenderConstants.RPARENTHESES))) nextbracket = String.valueOf(RenderConstants.LPARENTHESES);

    	if (fromlefttoright) {
	    	for(int i=pos;i<text.length(); i++) {
				if ((text.substring(i,i+1).equals(nextbracket)) && (counter == 0)) {
					return i+1;
				} else if (text.substring(i,i+1).equals(searchtext)) {
					counter++;
				} else if ((text.substring(i,i+1).equals(nextbracket)) && (counter != 0)) {
					counter--;
				}
	    	}
    	} else {
    		for(int i=pos-2;i>=0; i--) {
				if ((text.substring(i,i+1).equals(nextbracket)) && (counter == 0)) {
					return i+1;
				} else if (text.substring(i,i+1).equals(searchtext)) {
					counter++;
				} else if ((text.substring(i,i+1).equals(nextbracket)) && (counter != 0)) {
					counter--;
				}
	    	}
    	}
    	
    	// if no counterpart is found -1 is returned
    	return -1;
    	
    }

}
