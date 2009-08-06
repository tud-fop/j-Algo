package org.jalgo.module.lambda.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.JLabel;

import org.jalgo.module.lambda.model.Format;

/**
 * Represents the particular rendered Termparts in the renderLabel. 
 * Instances are created by the Renderer. 
 */
public class RenderElement extends JLabel implements MouseListener {

	private static final long serialVersionUID = 1322379355306466942L;
	
	private String text;
	private String termPosition;
	private boolean showBoundVar;
	private boolean showFreeVar;
	private boolean show_alpha_possible;
	private boolean show_beta_possible;
	private boolean isSelected;
	private boolean isHighlighted;
	private boolean hideBrackets;
	private GUIController parent;
	
	public static final Color BOUND_VAR_COLOR = new Color(255, 0, 0);
	public static final Color FREE_VAR_COLOR = new Color(0, 255, 0);
	
	public static final Color HIGHLIGHT_COLOR = new Color(255, 215, 0);
	public static final Color NORMAL_COLOR = Color.black;
	
	public static final Color BRACKET_COLOR = new Color(220, 220, 220);
	
	public static final Color SELECTED_COLOR = new Color(255, 160, 122);
	public static final Color NONSELECTED_COLOR = Color.white;
	
	
	public RenderElement(Set<Format> format, String text, String termPos, GUIController parent) {
		this.text = text;
		this.setText(this.text);
		this.termPosition = termPos;
		this.parent = parent;
		
		this.setFont(GUIController.TERM_FONT);
				
		this.setOpaque(true);
		this.setVisible(true);
		
		this.addMouseListener(this);
		
		this.validate();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//set default values
		if((this.text == "(" || this.text == ")") && hideBrackets) {
			this.setForeground(BRACKET_COLOR);
		} 
		else {
			this.setForeground(NORMAL_COLOR);
		}
		this.setBackground(NONSELECTED_COLOR);
		
		//overwrite with format values - order gives priority of the formats
		if(this.showFreeVar) {
			this.setForeground(FREE_VAR_COLOR);
		}
		
		if(this.showBoundVar) {
			this.setForeground(BOUND_VAR_COLOR);	
		}
		
		if(this.show_alpha_possible) {
			
		}
		
		if(this.show_beta_possible) {
			
		}
		
		if(this.isSelected) {
			this.setBackground(SELECTED_COLOR);
		}
		
		if(this.isHighlighted) {
			this.setForeground(HIGHLIGHT_COLOR);
		}
	}
	
	public String getTermPosition() {
		return this.termPosition;
	}

	public void mouseClicked(MouseEvent arg0) {
		
		parent.selectPosition(this.termPosition);
		
		this.repaint();
	}

	public void mouseEntered(MouseEvent arg0) {
		
		parent.highlightPosition(this.termPosition);
		
		this.repaint();
	}

	public void mouseExited(MouseEvent arg0) {
		
		parent.unhighlight();
		
		this.repaint();
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void setSelected(boolean s) {
		this.isSelected = s;
	}
	
	public void setHighlighted(boolean h) {
		this.isHighlighted = h;
	}
	
	public boolean getSelected() {
		return this.isSelected;
	}
	
	public boolean getHighlighted() {
		return this.isHighlighted;
	}
	
	public void markAsFreeVar(boolean s) {
		this.showFreeVar = s;
	}
	
	public void markAsBoundVar(boolean s) {
		this.showBoundVar = s;
	}
	
	public void showAlphaPossible(boolean s) {
		this.show_alpha_possible = s;
	}
	
	public void showBetaPossible(boolean s) {
		this.show_beta_possible = s;
	}
	
	public void hideBrackets(boolean h) {
		hideBrackets = h;
	}
}
