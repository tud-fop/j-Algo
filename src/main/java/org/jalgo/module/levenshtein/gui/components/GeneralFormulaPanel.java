package org.jalgo.module.levenshtein.gui.components;

import java.awt.Dimension;
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
	
	
	String fillTarget0Row = "d(0,i) = i";
	String target0Constr = "\\forall\\ 0 \\le i \\le k";
	String fillSource0Col = "d(j,0) = j";
	String source0Constr = "\\forall\\ 0 \\le j \\le n";
	String formInsert = "d(j,i-1) + c_{ins}";
	String formDelete = "d(j-1,i) + c_{del}";
	String formSubs = "d(j-1,i-1) + ";
	String restConstr = "\\forall\\ 1 \\le j \\le n, \\\\" + "1 \\le i \\le k";
	String costSubstitution = "c_{sub}" ;
	String subsConstr = "\\text{" + GuiController.getString("gui.if") + "}, w_j \\neq v_i \\\\";
	String costIdentity = "c_{id}";
	String idConstr = "\\text{" + GuiController.getString("gui.otherwise") + "}";
	String closingBracket = "\\end{array}\\right\\}";
	String fillRest = "d(j,i) = \\min";
	String openingBracket = "\\left\\{\\begin{array}{lc}";
	
	JLabel lblD0i = new JLabel();
	JLabel lbl0ik = new JLabel();
	JLabel lblDj0 = new JLabel();
	JLabel lbl0jn = new JLabel();
	JLabel lblDji = new JLabel();
	JLabel lbl1ik1jn = new JLabel();
	
	Controller controller;
	
	/**
	 * initializes the GerneralFormulaPanel by writing the formulas of the levenshtein
	 * distance into the Panel
	 */
	public void init(Controller controller ) {
		this.controller = controller;
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		LatexRenderer.render(fillTarget0Row, lblD0i);
		LatexRenderer.render(target0Constr, lbl0ik);
		
		LatexRenderer.render(fillSource0Col, lblDj0);
		LatexRenderer.render(source0Constr, lbl0jn);
		
		String string = fillRest + openingBracket + formDelete + ", \\\\ " 
				+ formInsert + ", \\\\" 
				+ formSubs + openingBracket + costSubstitution + "&" + subsConstr
							+ costIdentity + "&" + idConstr
							+ closingBracket + closingBracket;
		LatexRenderer.render(string, lblDji);
		LatexRenderer.render(restConstr, lbl1ik1jn);
		
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
		
	}

	/**
	 * marks the interesting details of the formula with colors such that
	 * its easier to understand, what's going on
	 */
	public void cellClicked(int j, int i) {
		init(controller);
		if (j == 0) {
			String string1 = LatexRenderer.fat(fillTarget0Row);
			LatexRenderer.render(string1, lblD0i);
			String string2 = LatexRenderer.fat(target0Constr);
			LatexRenderer.render(string2, lbl0ik);
		} else if (i == 0) {
			String string1 = LatexRenderer.fat(fillSource0Col);
			LatexRenderer.render(string1, lblDj0);
			String string2 = LatexRenderer.fat(source0Constr);
			LatexRenderer.render(string2, lbl0jn);
		} else {
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
			
			LatexRenderer.render(string1, lblDji);
			
			String string2 = LatexRenderer.fat(restConstr);
			LatexRenderer.render(string2, lbl1ik1jn);
			
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(Math.max(super.getPreferredSize().width,550), 
				super.getPreferredSize().height);
	}
}
