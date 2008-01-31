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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.jalgo.module.ebnf.gui.EbnfFont;
import org.jalgo.module.ebnf.gui.FontNotInitializedException;
import org.jalgo.module.ebnf.gui.GUIConstants;
import org.jalgo.module.ebnf.gui.ebnf.TermPanel.Type;
import org.jalgo.module.ebnf.model.ebnf.Rule;

/**
 * 
 * @author Tom
 *
 */public class RuleViewPanel extends JPanel implements Observer, MouseWheelListener {
    
	private static final long serialVersionUID = -7757770574867810075L;
	private GuiController guiController;
	private List<TermPanel> rulePanels;
	private int margin_x;
	private int margin_y;
	private int ruleColumnWidth;
	private int rowHeight;
	private int stringHeight;
	private double factor;
	private String displayName;
	
	
	
	
	
	/** Creates new form RuleViewPanel */
    public RuleViewPanel(GuiController guicontroller) {
    	this.guiController = guicontroller;
		try {
			this.setFont(EbnfFont.getFont());
		} catch (FontNotInitializedException e) {
			// if the right font could not be found use the "sans" font
			this.setFont(new Font("Sans", Font.PLAIN, 18));
			e.printStackTrace();
		}
    	this.setLayout(null);
    	this.addComponentListener(guicontroller.viewPanelSizeListener);
    	this.setAutoscrolls(true);
    	this.addMouseWheelListener(this);
    	stringHeight = getFont().getSize();
    	margin_y = 5;
    	margin_x = 40;
    	
    	factor = 0.22;
    	displayName = "editor";
    }
    
    /**
     * Draws the rules on the RuleViewPanel. 
     */
    public void drawRules() {
    	String term;
    	String variable;
    	this.removeAll();
    	ruleColumnWidth = 0;
    	rowHeight = stringHeight + 10;
    	List<Rule> rules = new ArrayList<Rule>();
    	List<Integer> distances = new ArrayList<Integer>();
    	rulePanels = new ArrayList<TermPanel>();
    	
    	for(Rule rule : guiController.getEbnfController().getDefinition().getRules()) {
    		rules.add(rule);
    		
    		variable = EbnfRenderer.toRenderString(rule.getName(), false, "");
    		term = EbnfRenderer.toRenderString(rule.getTerm(), guiController.getStrictMode(), " ");
    		
    		if (ruleColumnWidth < getStringWidth(variable)) ruleColumnWidth = getStringWidth(variable);

    		TermPanel tPanel = new TermPanel(this, variable+"  ::=  "+term, rule.getName(),
    				getStringWidth(variable+"  ::=  "+term), stringHeight);
    		this.rulePanels.add(tPanel);
    		
    		distances.add(getStringWidth(variable));
    		
    		tPanel.addMouseListener(guiController.editDefinitionActionListener);
    		tPanel.type = Type.RULE;
    	}
    	
    	for(int i=0; i<rules.size(); i++) {
    		this.rulePanels.get(i).setLocation(margin_x + ruleColumnWidth - distances.get(i), margin_y + i * rowHeight);
    		this.add(this.rulePanels.get(i));
    	}
    	

    }
    
    private int getStringWidth(String string, Font f) {
    	FontMetrics metrics = getFontMetrics(f);
    	return metrics.stringWidth(string);
    }
    private int getStringWidth(String string) {
    	FontMetrics metrics = getFontMetrics(this.getFont());
    	return metrics.stringWidth(string);
    }
    
    @Override
	protected void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
    	
    	g2.clearRect(0,0, getWidth(), getHeight());
    	g2.setColor(GUIConstants.STANDARD_COLOR_BACKGROUND);
    	g2.fillRect(0,0, getWidth(), getHeight());
    	
    	super.paintComponents(g);
    	
    	
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
		    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		
		
    	int fontSize = (int) Math.round(factor * this.getParent().getWidth());
    	
    	Font f = new Font("Courier", Font.BOLD, fontSize);

		g2.setFont(f);
		g2.setColor(GUIConstants.STANDARD_COLOR_BACKGROUND_TEXT);
		int y = this.getParent().getHeight() + (int) Math.round(0.2 * fontSize);
		g2.rotate(-Math.PI * 7 / 180);
		g2.drawString(displayName, (int) (this.getParent().getWidth()
				- getStringWidth(displayName, f)
				- factor * 0.2 * this.getParent().getWidth()), y);
		g2.rotate(Math.PI * 7 / 180);
    	    	
    	
    	
    	
		
		g2.setColor(Color.BLACK);
		g2.setFont(getFont());
		
		g2.drawString("R:", 5, margin_y+stringHeight);
		
    }

	public void update(Observable o, Object arg) {
		drawRules();
		manageScrollBars();
		this.repaint();
	}
	
	public void editRule(TermPanel tPanel) {
		
	}
	
	public void manageScrollBars() {
		int ruleSize = 0;
		int ruleHeight = margin_y;
		int i = 0;
		int scrollBarHeight = Integer.parseInt(UIManager.get("ScrollBar.width").toString());
		
		for (TermPanel tPanel : rulePanels) {
			i = tPanel.getLocation().x + tPanel.getWidth() + margin_x + scrollBarHeight;
			if (i > ruleSize)
				ruleSize = i;
			ruleHeight =+ tPanel.getLocation().y + tPanel.getHeight();
		}
		this.setPreferredSize(new Dimension(ruleSize, ruleHeight));
		this.revalidate();
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		javax.swing.JScrollBar scrollbar = ((JScrollPane)this.getParent().getParent()).getVerticalScrollBar();
		
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
	          int totalScrollAmount =
	              e.getUnitsToScroll() * 4;
	          scrollbar.setValue(scrollbar.getValue() + totalScrollAmount);
	      }
	}
}
