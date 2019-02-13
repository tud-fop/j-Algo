package org.jalgo.module.levenshtein.gui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.GuiController;
import org.jalgo.module.levenshtein.gui.LatexRenderer;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.CellClickedObserver;

public class GeneralFormulaPanel 
extends JPanel 
implements CellClickedObserver {
	private static final long serialVersionUID = -3969386447472499860L;
	
	
	private String fillTarget0Row = "d(0,i) = i";
	private String target0Constr = "\\forall\\ 0 \\le i \\le k";
	private String fillSource0Col = "d(j,0) = j";
	private String source0Constr = "\\forall\\ 0 \\le j \\le n";
	private String formInsert = "d(j,i-1) + c_{ins}";
	private String formDelete = "d(j-1,i) + c_{del}";
	private String formSubs = "d(j-1,i-1) + ";
	private String restConstr = "\\forall\\ 1 \\le j \\le n, \\\\" + "1 \\le i \\le k";
	private String costSubstitution = "c_{sub}" ;
	private String subsConstr = "\\text{" + GuiController.getString("gui.if") + "}, w_j \\neq v_i \\\\";
	private String costIdentity = "c_{id}";
	private String idConstr = "\\text{" + GuiController.getString("gui.otherwise") + "}";
	private String closingBracket = "\\end{array}\\right\\}";
	private String fillRest = "d(j,i) = \\min";
	private String openingBracket = "\\left\\{\\begin{array}{lc}";
	
	private JLabel lblD0i = new JLabel();
	private JLabel lbl0ik = new JLabel();
	private JLabel lblDj0 = new JLabel();
	private JLabel lbl0jn = new JLabel();
	private JLabel lblDji = new JLabel();
	private JLabel lbl1ik1jn = new JLabel();
	
	private Controller controller;
	
	private int width;
	private int height;
	
	private int size = 13;
	
	
	/**
	 * initializes the GerneralFormulaPanel by writing the formulas of the levenshtein
	 * distance into the Panel
	 */
	public void init(Controller controller ) {
		this.controller = controller;
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		LatexRenderer.render(fillTarget0Row, lblD0i, size);
		LatexRenderer.render(target0Constr, lbl0ik, size);
		
		LatexRenderer.render(fillSource0Col, lblDj0, size);
		LatexRenderer.render(source0Constr, lbl0jn, size);
		
		String string = fillRest + openingBracket + formDelete + ", \\\\ " 
				+ formInsert + ", \\\\" 
				+ formSubs + openingBracket + costSubstitution + "&" + subsConstr
							+ costIdentity + "&" + idConstr
							+ closingBracket + closingBracket;
		LatexRenderer.render(string, lblDji, size);
		LatexRenderer.render(restConstr, lbl1ik1jn, size);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		add(lblD0i, c);
		
		c.gridx = 1;
		c.gridy = 0;
		add(lbl0ik, c);
		
		c.gridx = 0;
		c.gridy = 1;
		add(lblDj0, c);
		
		c.gridx = 1;
		c.gridy = 1;
		add(lbl0jn, c);
		
		c.gridx = 0;
		c.gridy = 2;
		add(lblDji, c);
		
		c.gridx = 1;
		c.gridy = 2;
		add(lbl1ik1jn, c);
		
		width = getPreferredSize().width;
		height = getPreferredSize().height;
	}

	/**
	 * marks the interesting details of the formula with colors such that
	 * its easier to understand, what's going on
	 */
	public void cellClicked(int j, int i) {
		init(controller);
		if (j == 0) {
			String string1 = LatexRenderer.fat(fillTarget0Row);
			LatexRenderer.render(string1, lblD0i, size);
			String string2 = LatexRenderer.fat(target0Constr);
			LatexRenderer.render(string2, lbl0ik, size);
		} else if (i == 0) {
			String string1 = LatexRenderer.fat(fillSource0Col);
			LatexRenderer.render(string1, lblDj0, size);
			String string2 = LatexRenderer.fat(source0Constr);
			LatexRenderer.render(string2, lbl0jn, size);
		} else if (i > 0 && j > 0){
			String string1 = LatexRenderer.fat(fillRest) + openingBracket;
			string1 += LatexRenderer.green(formDelete )+ ", \\\\ ";
			string1 += LatexRenderer.blue(formInsert )+ ", \\\\";
			string1 += LatexRenderer.red(formSubs) + openingBracket;
			if (!controller.sameCharAt(j, i)) {
				string1 += LatexRenderer.red(costSubstitution) + "&" + subsConstr;
				string1 += costIdentity + "&" + idConstr;
			} else {
				string1 += costSubstitution + "&" + subsConstr;
				string1 += LatexRenderer.red(costIdentity) + "&" + idConstr;
			}
			string1 += closingBracket + closingBracket;
			
			LatexRenderer.render(string1, lblDji, size);
			
			String string2 = LatexRenderer.fat(restConstr);
			LatexRenderer.render(string2, lbl1ik1jn, size);
			
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	public void onResize(int width, int height) {
		this.width = width;
		this.height = height;
		
		int maxWidthSize = width / 25;
		int maxHeightSize = height / 9;
		int maxSize = Math.min(maxWidthSize, maxHeightSize);
		size = maxSize;
		init(controller);
		
		repaint();
		revalidate();
	}
	

}
