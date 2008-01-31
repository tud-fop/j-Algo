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

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import org.jalgo.module.ebnf.gui.GUIConstants;
import org.jalgo.module.ebnf.model.ebnf.ESymbol;

 /**This class represents a JComponent in which EBNF-Elements got drawn.
  * 
  * @author Tom
  *
  */public class TermPanel extends JComponent {
	  
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	  private int strheight;
	  private ESymbol symbol;
	  public enum Type { RULE, VARIABLE, TERMINALSYMBOL }
	  public Type type;
	  private boolean highlighted;
	  private boolean mouseover;
	  
	  
	  
	  public TermPanel(JComponent parent, String renderString, ESymbol symbol, int strwidth, int strheight) {
		  this.symbol = symbol;
		  this.name = renderString;
		  this.strheight = strheight;
		  this.setSize(strwidth+2, strheight+8);
		  setFont(parent.getFont());
		  setLayout(null);
		  highlighted = false;
		  mouseover = false;
	  }
	  
	  
		private int getStringWidth(String string) {
			FontMetrics metrics = getFontMetrics( this.getFont() );
			return metrics.stringWidth(string);
		}
	  
	  @Override
	protected void paintComponent(Graphics g) {
		  
		  Graphics2D g2 = (Graphics2D)g;
		  
		  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                  RenderingHints.VALUE_ANTIALIAS_ON);
		  g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				  RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		  
		  if (highlighted) g2.setColor(GUIConstants.HIGHLIGHT_COLOR);
		  else if (mouseover)g2.setColor(GUIConstants.MOUSEOVER_COLOR); 
		  else g2.setColor(GUIConstants.STANDARD_COLOR_BACKGROUND);
		  g2.fillRoundRect(0,0,getWidth(), getHeight(), 10, 10);  
		  g2.setFont(getFont());
		  
		  for(int i=0;i<name.length();i++) {
			  String c = name.substring(i,i+1);
			  if (c.equals(String.valueOf(RenderConstants.LPARENTHESES_M)) 
					  || c.equals(String.valueOf(RenderConstants.RPARENTHESES_M)))
				  g2.setColor(RenderConstants.HIGHCOLOR);
			  else
				  g2.setColor(RenderConstants.TEXTCOLOR);
			
			  g2.drawString(c,getStringWidth(name.substring(0,i)), strheight + 1);
		  }
		  
		  
	  }


	/**
	 * @return Returns the symbol.
	 */
	public ESymbol getSymbol() {
		return symbol;
	}
	
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
		this.repaint();
	}
	
	public void setMouseOver(boolean highlighted) {
		this.mouseover = highlighted;
		this.repaint();
	}
	
}
