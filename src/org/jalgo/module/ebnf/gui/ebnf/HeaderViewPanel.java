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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jalgo.module.ebnf.gui.EbnfFont;
import org.jalgo.module.ebnf.gui.FontNotInitializedException;
import org.jalgo.module.ebnf.gui.GUIConstants;
import org.jalgo.module.ebnf.gui.ebnf.TermPanel.Type;
import org.jalgo.module.ebnf.model.ebnf.Definition;
import org.jalgo.module.ebnf.model.ebnf.ESymbol;
import org.jalgo.module.ebnf.model.ebnf.ETerminalSymbol;
import org.jalgo.module.ebnf.model.ebnf.EVariable;

/**
 * This is the upper panel of the definition view, it shows the header of the
 * definition: the tupel and the variable and terminal symbol sets
 * 
 * @author Tom, Johannes
 * 
 */
public class HeaderViewPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

	// the GuiController which is needed to get the Definition
	private GuiController guiController;

	// This is panel which displays the start variable
	private TermPanel startVarTermPanel;

	// these are the panels that display the variables
	private List<TermPanel> variables;

	// these are the panels that display the terminal symbols
	private List<TermPanel> terminals;

	// the height of the EBNF font
	private int stringHeight;

	// the width of the list of variables
	private int variableSetWidth;

	// the width of the list of terminal symbols
	private int terminalSetWidth;

	// the horizontal position of the variable set
	private int variableSetX;

	/** Creates new form HeaderViewPanel */
	public HeaderViewPanel(GuiController guicontroller) {

		// save the guiController reference
		this.guiController = guicontroller;
		
		// initialize the font
		try {
			this.setFont(EbnfFont.getFont());
		} catch (FontNotInitializedException e) {
			// if the right font could not be found use the "sans" font
			this.setFont(new Font("Sans", Font.PLAIN, 18));
			e.printStackTrace();
		}
		
		stringHeight = getFont().getSize();
		
		// initialize the actionListener
		this.addComponentListener(guicontroller.viewPanelSizeListener);
		
		variableSetX = getStringWidth("V = {") + 12;
		
		// set a null layout
		this.setLayout(null);
		
		// draw the header; this is the central function
		drawHeader();
	}

	/**
	 * @return the String height of the Ebnf font
	 */
	public int getStringHeight() {
		return stringHeight;
	}

	/**
	 * @param string the string to be measured
	 * @return the width of the string
	 */
	private int getStringWidth(String string) {
		FontMetrics metrics = getFontMetrics(this.getFont());
		return metrics.stringWidth(string);
	}
	
	/**
	 *  this function draws the panels of the definition header. it is called 
	 *  whenever the definition changes. note that it does not draw the rest
	 *  of the panel; this is done in <code>paintComponent()</code>
	 */
	public void drawHeader() {

		// temporarily store the definition
		Definition definition = guiController.getEbnfController().getDefinition();
		
		// clear the panel
		this.removeAll();
		
		// clear the variable and terminal symbol lists
		this.variables = new ArrayList<TermPanel>();
		this.terminals = new ArrayList<TermPanel>();

// -----------------------------------------------------------------------------		
// draw the start variable
// -----------------------------------------------------------------------------
		// create a termpanel with the size of the start variable and add it to
		// the header view panel and place it
		ESymbol startVar = definition.getStartVariable();
		String tempString = EbnfRenderer.toRenderString(startVar, false, "");
		startVarTermPanel = new TermPanel(this, tempString, startVar,
				getStringWidth(tempString), stringHeight);
		startVarTermPanel.type = Type.VARIABLE;
		this.add(startVarTermPanel);
		startVarTermPanel.setLocation(getStringWidth("E = (V, \u03a3,   "), 4);

// -----------------------------------------------------------------------------		
// draw the variables
// -----------------------------------------------------------------------------
		
		List<EVariable> variables = definition.getVariables();
		tempString = "";
		
		// initialize the width with 0
		variableSetWidth = 0;

		for (int i = 0; i < variables.size(); i++) {
			this.variables.add(new TermPanel(this, variables.get(i).getName(),
					variables.get(i),
					getStringWidth(variables.get(i).getName()), stringHeight));
			this.variables.get(i).setLocation(variableSetX + variableSetWidth,
					stringHeight + 15);
			this.variables.get(i).type = Type.VARIABLE;
			this.variables.get(i).addMouseListener(
					guiController.editDefinitionActionListener);
			this.add(this.variables.get(i));
			variableSetWidth = variableSetWidth
					+ this.variables.get(i).getWidth() + getStringWidth(", ");

		}

// -----------------------------------------------------------------------------		
// draw the terminal symbols
// -----------------------------------------------------------------------------

		List<ETerminalSymbol> terminals = definition.getTerminals();
		terminalSetWidth = 0;

		for (int i = 0; i < terminals.size(); i++) {
			this.terminals.add(new TermPanel(this, terminals.get(i).getName(),
					terminals.get(i),
					getStringWidth(terminals.get(i).getName()), stringHeight));
			this.terminals.get(i).setLocation(variableSetX + terminalSetWidth,
					2 * stringHeight + 25);
			this.terminals.get(i).type = Type.TERMINALSYMBOL;
			this.terminals.get(i).addMouseListener(
					guiController.editDefinitionActionListener);
			this.add(this.terminals.get(i));
			terminalSetWidth = terminalSetWidth
					+ this.terminals.get(i).getWidth() + getStringWidth(", ");

		}

		// finally, validate and repaint to show the changes
		this.validate();
		this.repaint();
	}



	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.clearRect(0, 0, getWidth(), getHeight());
		g2.setColor(GUIConstants.STANDARD_COLOR_BACKGROUND);
		g2.fillRect(0, 0, getWidth(), getHeight());

		super.paintComponents(g);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		g2.setColor(Color.BLACK);
		g2.setFont(getFont());

		// draw the first header-line with the startsymbol
		if (startVarTermPanel != null) {
			g2.drawString("E = (V, \u03a3, ", 10, stringHeight + 5);
			g2.drawString(", R)", getStringWidth("E = (V, \u03a3,   ")
					+ startVarTermPanel.getWidth(), stringHeight + 5);
		}

		// draw the second line with the definitions variables
		g2.drawString("V = {", 10, 2 * stringHeight + 15);

		for (int i = 0; i < variables.size() - 1; i++) {
			g2.drawString(",", variables.get(i).getX()
					+ variables.get(i).getWidth(), 2 * stringHeight + 15);
		}
		g2.drawString("}", variableSetX + variableSetWidth,
				2 * stringHeight + 15);

		// draw the third line with terminalsymbols
		g2.drawString("\u03a3 = {", 10, 3 * stringHeight + 25);

		for (int i = 0; i < terminals.size() - 1; i++) {
			g2.drawString(",", terminals.get(i).getX()
					+ terminals.get(i).getWidth(), 3 * stringHeight + 25);
		}
		g2.drawString("}", variableSetX + terminalSetWidth,
				3 * stringHeight + 25);
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		drawHeader();
		manageScrollBars();
	}

	/**
	 * This function manages the scrollbars, i.e. it shows them exactly when
	 * necessary
	 */
	public void manageScrollBars() {
		int terminalSize = 0;
		int variableSize = 0;
		int startTermSize = 0;
		int scrollBarHeight = Integer.parseInt(UIManager.get("ScrollBar.width")
				.toString());

		if (terminals.size() > 0)
			terminalSize = 20
					+ terminals.get(terminals.size() - 1).getLocation().x
					+ terminals.get(terminals.size() - 1).getWidth();

		if (variables.size() > 0)
			variableSize = 20
					+ variables.get(variables.size() - 1).getLocation().x
					+ variables.get(variables.size() - 1).getWidth();

		if (startVarTermPanel != null)
			startTermSize = 30 + startVarTermPanel.getLocation().x
					+ startVarTermPanel.getWidth();		
		if (terminalSize >= this.getWidth()) {
			this.setPreferredSize(new Dimension(terminalSize,
					95 + scrollBarHeight));
			guiController.repositionDivider(95 + scrollBarHeight);
			this.revalidate();
		} else if (variableSize >= this.getWidth()) {
			this.setPreferredSize(new Dimension(variableSize,
					95 + scrollBarHeight));
			guiController.repositionDivider(95 + scrollBarHeight);
			this.revalidate();
		} else if (startTermSize >= this.getWidth()) {
			this.setPreferredSize(new Dimension(startTermSize,
					95 + scrollBarHeight));
			guiController.repositionDivider(95 + scrollBarHeight);
			this.revalidate();
		} else {
			int i = 0;
			int b = this.getParent().getWidth();
			if (terminalSize > variableSize) {
				if (terminalSize > startTermSize)
					i = terminalSize;
				else
					i = startTermSize;
			} else {
				if (variableSize > startTermSize)
					i = variableSize;
				else
					i = startTermSize;
			}
			if (i<b) {
				this.setPreferredSize(new Dimension(this.getParent().getWidth() - 1,
						95));
				guiController.repositionDivider(95);
				return;
			} else {
				this.setPreferredSize(new Dimension(i,
						95 + scrollBarHeight));
				guiController.repositionDivider(95 + scrollBarHeight);
			}
			this.revalidate();
		}
	}

}
