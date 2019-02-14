package org.jalgo.module.levenshtein.gui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.LatexRenderer;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.CellClickedObserver;

public class CalculationPanel 
extends JPanel 
implements CellClickedObserver {
	private static final long serialVersionUID = -2564836710083529767L;

	private Controller controller;
	
	private JLabel label;
	
	private int height;
	private int width;
	
	String latex;
	int size;
	
	public CalculationPanel(Controller controller) {
		this.controller = controller;
		
		// init the CalculationPanel by adding the Label to itself
		label = new JLabel();
//		label.setBorder(BorderFactory.createLineBorder(Color.black));
		
		// uses GridBagLayout such that the Label is centered
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		add(label, c);
		
		// standard latex and standard size, if the window did not change its size
		latex = "";
		size = 13;
	}
	
	/**
	 * sets a latex styled formula to the label for the calculation of
	 * of the cell (j,i) in the levenshtein example given by controller
	 * @param j, the index in the source word
	 * @param i, the index in the target word
	 */
	public void setFormula(int j, int i) {
		
		// decide in which case we are
		if (j == 0 && i == 0) {
			latex = "d(0,0) = 0";
		} else if (j == 0) {
			latex = "d(0," + i + ") = " + i;
		} else if (i == 0) {
			latex = "d(" + j + ",0) = " + j;
		} else {
			// build the complex formula for the general case
			int subsCosts = 
					controller.sameCharAt(j, i) ? controller.getIdentity() : controller.getSubstitution();
			latex = "d(" + j + "," + i + ") = \\min\\left\\{\\begin{array}{l}" 
					+ controller.getCell(j-1, i).getValue() + " + " + controller.getDeletion() + ", \\\\"
					+ controller.getCell(j, i-1).getValue() + " + " + controller.getInsertion() + ", \\\\"
					+ controller.getCell(j-1, i-1).getValue() + " + " + subsCosts + "\\end{array}\\right\\}";
		}
		
		// render the formula and write it to the label
		LatexRenderer.render(latex, label, size);
	}

	/**
	 * show the calculations for the cell (j,i)
	 */
	public void cellClicked(int j, int i) {
		if (j < 0 || i < 0) {
			label.setIcon(null);
		} else {
			setFormula(j, i);
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	/**
	 * sets a new size for the formula such that it shows up nicely
	 * @param width, the allowed width of the CalculationPanel
	 * @param height, the allowed height of the CalculationPanel
	 */
	public void onResize(int width, int height) {
		this.width = width;
		this.height = height;
		
		// calculate the maximal size of the formula
		int maxWidthSize = (int) (width / 7.5);
		int maxHeightSize = height / 9;
		int maxSize = Math.min(maxWidthSize, maxHeightSize);
		size = maxSize;
		label.setText("");
		
		repaint();
		revalidate();
	}
}
