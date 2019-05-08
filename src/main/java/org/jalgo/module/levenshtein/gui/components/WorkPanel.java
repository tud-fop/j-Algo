package org.jalgo.module.levenshtein.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.events.ResizeListener;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.ChangeGeneralFormulaSizeObserver;
import org.jalgo.module.levenshtein.pattern.ToolbarObserver;

public class WorkPanel extends JPanel implements ResizeComponent, ChangeGeneralFormulaSizeObserver {
	private static final long serialVersionUID = -281061652091384919L;

	private String source;
	private String target;
	
	private Controller controller;
	
	private GeneralFormulaPanel generalForm;
	private CalculationPanel calcPane;
	private TablePanel tablePanel;
	private AlignmentPanel alignmentPanel;
	
	private GFP gfp;
	
	private JPanel leftSide;
	private JPanel rightSide;
	
	private double weightx = 0.7;
	private double weighty = 0.7;
	
	
	public void init(Controller controller, String source, String target) {
		this.source = source;
		this.target = target;
		this.controller = controller;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
//		c.fill = GridBagConstraints.BOTH;
		

//		generalForm = new GeneralFormulaPanel();
//		generalForm.init(controller);
//		generalForm.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		
		gfp = new GFP();
		gfp.setController(controller);
		gfp.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		
		
		calcPane = new CalculationPanel(controller);
		//controller.init(source, target, 1, 1, 1, 0);
		calcPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
		
		tablePanel = new TablePanel();
		tablePanel.init(source, target, controller);
		tablePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		
		
		alignmentPanel = new AlignmentPanel(source, target, controller);
		
		c.gridx = 0;
		c.gridy = 0;
//		c.fill = GridBagConstraints.BOTH;
//		c.weightx = weightx;
//		c.weighty = weighty;
		add(tablePanel, c);
		
		c.gridx = 0;
		c.gridy = 1;
//		c.fill = GridBagConstraints.BOTH;
//		c.weightx = weightx;
//		c.weighty = 1- weighty;
//		add(generalForm, c);
		add(gfp, c);
		
		c.gridx = 1;
		c.gridy = 1;
//		c.fill = GridBagConstraints.BOTH;
//		c.weightx = 1-weightx;
//		c.weighty = 1-weighty;
		add(calcPane, c);
		
		c.gridx = 1;
		c.gridy = 0;
//		c.fill = GridBagConstraints.BOTH;
//		c.weightx = 1-weightx;
//		c.weighty = weighty;
		add(alignmentPanel, c);
		
//		c.gridx = 0;
//		c.gridy = 0;
//		add(leftSide, c);
//		
//		c.gridx = 1;
//		c.gridy = 0;
//		add(rightSide, c);
		
		
		tablePanel.registerObserver(calcPane);
//		tablePanel.registerObserver(generalForm);
		tablePanel.registerObserver(gfp);
		tablePanel.registerObserver(alignmentPanel);
		
		alignmentPanel.registerAlignmentObserver(tablePanel);
		
		addComponentListener(new ResizeListener(this));
		
//		generalForm.registerObserver(this);
		gfp.registerObserver(this);
	}

	public void onResized() {
		Dimension dim = getSize();
		int padding = 1;
		int smallSize = (int) Math.max(50, dim.height * (1-weighty));
		
		tablePanel.onResize((int) (dim.width * weightx) - padding, 
				(int) (dim.height - smallSize) - padding);

//		generalForm.onResize((int) (dim.width * weightx) - padding, 
//				smallSize - padding);
		gfp.onResize((int) (dim.width * weightx), 
				smallSize);
		
		
		calcPane.onResize((int) (dim.width * (1-weightx)) - padding, 
				smallSize - padding);
		alignmentPanel.onResize((int) (dim.width * (1-weightx)) - padding,
				(int) (dim.height - smallSize) - padding);
	}
	
	public ToolbarObserver getToolBarObserver() {
		return tablePanel;
	}



	public void changeToSmallFormula(boolean smallFormula) {
		if (smallFormula) {
			weighty = 0.95;
			calcPane.setSmallFormula(true);
			gfp.setSmallFormula(true);
		} else {
			weighty = 0.7;
			calcPane.setSmallFormula(false);
			gfp.setSmallFormula(false);
		}
		
		onResized();
	}
	
}
