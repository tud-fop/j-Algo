package org.jalgo.module.levenshtein.gui.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ResourceBundle.Control;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.model.Controller;

public class WorkPanel extends JPanel {
	private static final long serialVersionUID = -281061652091384919L;

	private String source;
	private String target;
	
	private Controller controller;
	
	private GeneralFormulaPanel generalForm;
	private CalculationPanel calcPane;
	private TablePanel tablePanel;
	
	private final double weightx = 0.5;
	private final double weighty = 0.7;
	
	public void init(Controller controller, String source, String target) {
		this.source = source;
		this.target = target;
		this.controller = controller;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		generalForm = new GeneralFormulaPanel();
		generalForm.init(controller);
		generalForm.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.LIGHT_GRAY));
		
		calcPane = new CalculationPanel(controller);
		controller.init(source, target, 1, 1, 1, 0);
		calcPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		
		tablePanel = new TablePanel();
		tablePanel.init(source, target, controller);
		tablePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = weightx;
		c.weighty = weighty;
		add(tablePanel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = weightx;
		c.weighty = 1- weighty;
		add(generalForm, c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1-weightx;
		c.weighty = 1-weighty;
		add(calcPane, c);
		
		tablePanel.registerObserver(calcPane);
		tablePanel.registerObserver(generalForm);
	}
}
