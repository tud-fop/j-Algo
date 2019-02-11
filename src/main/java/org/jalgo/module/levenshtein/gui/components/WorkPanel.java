package org.jalgo.module.levenshtein.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ResourceBundle.Control;

import javax.swing.JPanel;

import org.jalgo.module.levenshtein.model.Controller;

public class WorkPanel extends JPanel {
	private static final long serialVersionUID = -281061652091384919L;

	private String source;
	private String target;
	
	private Controller controller;
	
	private GeneralFormulaPanel generalForm;
	private CalculationPanel calcPane;
	
	public void init(Controller controller, String source, String target) {
		this.source = source;
		this.target = target;
		this.controller = controller;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		generalForm = new GeneralFormulaPanel();
		generalForm.init();
		
		calcPane = new CalculationPanel(controller);
		controller.init(source, target, 1, 1, 1, 0);
		calcPane.setFormula(5, 5);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.75;
		add(generalForm, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.25;
		add(calcPane, c);
	}
}
