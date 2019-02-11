package org.jalgo.module.levenshtein.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.LatexRenderer;
import org.jalgo.module.levenshtein.model.Controller;

public class CalculationPanel extends JPanel {
	private static final long serialVersionUID = -2564836710083529767L;

	Controller controller;
	
	JLabel label;
	
	public CalculationPanel(Controller controller) {
		this.controller = controller;
		
		label = new JLabel();
		
		setLayout(new BorderLayout());
		
		add(label, BorderLayout.CENTER);
	}
	
	public void setFormula(int j, int i) {
		String latex;
		if (j == 0 && i == 0) {
			latex = "d(0,0) = 0";
		} else if (j == 0) {
			latex = "d(0," + i + ") = " + i;
		} else if (i == 0) {
			latex = "d(" + j + ",0) = " + j;
		} else {
			int subsCosts = 
					controller.sameCharAt(j, i) ? controller.getIdentity() : controller.getSubstitution();
			latex = "d(" + j + "," + i + ") = \\min\\left\\{\\begin{array}{l}" 
					+ controller.getCell(j-1, i).getValue() + " + " + controller.getDeletion() + ", \\\\"
					+ controller.getCell(j, i-1).getValue() + " + " + controller.getInsertion() + ", \\\\"
					+ controller.getCell(j-1, i-1).getValue() + " + " + subsCosts + "\\end{array}\\right\\}";
		}
		
		LatexRenderer.render(latex, label);
	}
	

}
