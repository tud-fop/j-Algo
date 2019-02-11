package org.jalgo.module.levenshtein.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.GuiController;
import org.jalgo.module.levenshtein.gui.LatexRenderer;

public class GeneralFormulaPanel extends JPanel {
	private static final long serialVersionUID = -3969386447472499860L;
	
	
	String fillTarget0Row = "d(0,i) = i";
//	String target0Constr = "\\text{" + GuiController.getString("gui.forall") + "} 0 \\le i \\le k";
	String target0Constr = "\\forall\\ 0 \\le i \\le k";
	String fillSource0Col = "d(j,0) = j";
//	String source0Constr = "\\text{" + GuiController.getString("gui.forall") + "} 0 \\le j \\le n";
	String source0Constr = "\\forall\\ 0 \\le j \\le n";
	String formInsert = "d(j,i-1) + c_{ins}";
	String formDelete = "d(j-1,i) + c_{del}";
	String formSubs = "d(j-1,i-1) + \\left\\{\\begin{array}{lc}";
//	String restConstr = "\\text{" + GuiController.getString("gui.forall") + "} 1 \\le j \\le n \\\\" 
//						+ "\\text{" + GuiController.getString("gui.andall") + "} 1 \\le i \\le k";
	String restConstr = "\\forall\\ 1 \\le j \\le n, \\\\" + "1 \\le i \\le k";
	String costSubstitution = "c_{sub} & " + "\\text{" + GuiController.getString("gui.if") + "}, w_j \\neq v_i \\\\";
	String costIdentity = "c_{id} & " + "\\text{" + GuiController.getString("gui.otherwise") + "}";
	String closingBracket = "\\end{array}\\right\\}";
	String fillRest = "d(j,i) = \\min\\left\\{\\begin{array}{lr}";
	
	JLabel lblD0i = new JLabel();
	JLabel lbl0ik = new JLabel();
	JLabel lblDj0 = new JLabel();
	JLabel lbl0jn = new JLabel();
	JLabel lblDji = new JLabel();
	JLabel lbl1ik1jn = new JLabel();
	
	
	public void init() {
		setLayout(new GridBagLayout());
		
//		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		LatexRenderer.render(fillTarget0Row, lblD0i);
		LatexRenderer.render(target0Constr, lbl0ik);
		
		LatexRenderer.render(fillSource0Col, lblDj0);
		LatexRenderer.render(source0Constr, lbl0jn);
		
		String string = fillRest + formDelete + ", \\\\ " 
				+ formInsert + ", \\\\" 
				+ formSubs + costSubstitution
							+ costIdentity + closingBracket + closingBracket;
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
		
//		add(contentPanel, BorderLayout.CENTER);
		
		
	}
	
}
