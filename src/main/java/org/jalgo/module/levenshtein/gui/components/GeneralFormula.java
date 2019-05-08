package org.jalgo.module.levenshtein.gui.components;

import javax.swing.JPanel;

import org.jalgo.module.levenshtein.model.Controller;

public abstract class GeneralFormula extends JPanel {

	private static final long serialVersionUID = -5537745531677039827L;

	
	public abstract void cellClicked(int j, int i, boolean wasAlreadyFilled);
	
	public abstract void onResize(int width, int heigth);
	
	public abstract void setController(Controller controller);
	
	
	
}
