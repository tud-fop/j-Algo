package org.jalgo.module.levenshtein.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.events.ResizeListener;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.ToolbarObserver;

public class WorkPanel extends JPanel implements ResizeComponent {
	private static final long serialVersionUID = -281061652091384919L;

	private String source;
	private String target;
	
	private Controller controller;
	
	private GeneralFormulaPanel generalForm;
	private CalculationPanel calcPane;
	private TablePanel tablePanel;
	private JLabel label;
	
	private final double weightx = 0.7;
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
		
		label = new JLabel();
		
		
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
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1-weightx;
		c.weighty = weighty;
		add(label, c);
		
		addComponentListener(new ResizeListener(this));
		
	}

	public void onResized() {
		Dimension dim = getSize();
		tablePanel.onResize((int) (dim.width * weightx), 
				(int) (dim.height * weighty));
		generalForm.onResize((int) (dim.width * weightx), 
				(int) (dim.height * (1-weighty)));
		calcPane.onResize((int) (dim.width * (1-weightx)), 
				(int) (dim.height * (1-weighty)));
	}
	
	public ToolbarObserver getToolBarObserver() {
		return tablePanel;
	}
}
