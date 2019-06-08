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

public class CalculationPanel extends JPanel implements CellClickedObserver {
	private static final long serialVersionUID = -2564836710083529767L;

	private Controller controller;

	private JLabel label;

	private int height;
	private int width;

	String latex;

	private int clickedJ = -1;
	private int clickedI = -1;
	private boolean wasAlreadyFilled;
	private boolean smallFormula;

	private String exampleSmallFormula = "d(99,99) = \\min\\left\\{999 + 99\\text{,  }999 + 99\\text{,  }999 + 99\\right\\}";
	private String ecampleBigFormula = "d(99,99) = \\min\\left\\{\\begin{array}{l}" + "999+99" + ", \\\\" + "999+99"
			+ ", \\\\" + "999+99" + "\\end{array}\\right\\}";
	
	private int size = 10;

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
	}

	/**
	 * sets a latex styled formula to the label for the calculation of of the cell
	 * (j,i) in the levenshtein example given by controller
	 * 
	 * @param j, the index in the source word
	 * @param i, the index in the target word
	 */
	public void setFormula(int j, int i, boolean color) {

		// decide in which case we are
		if (j == 0 && i == 0) {
			latex = "d(0,0) = 0";
		} else if (j == 0) {
			latex = "d(0," + i + ") = " + i;
		} else if (i == 0) {
			latex = "d(" + j + ",0) = " + j;
		} else {
			// build the complex formula for the general case
			int subsCosts = controller.sameCharAt(j, i) ? controller.getIdentity() : controller.getSubstitution();
			String del = controller.getCell(j - 1, i).getValue() + " + " + controller.getDeletion();
			String ins = controller.getCell(j, i - 1).getValue() + " + " + controller.getInsertion();
			String subs = controller.getCell(j - 1, i - 1).getValue() + " + " + subsCosts;
			if (color) {
				del = LatexRenderer.colorDeletion(del);
				ins = LatexRenderer.colorInsertion(ins);
				subs = LatexRenderer.colorSubstitution(subs);
			}
			if (smallFormula) {
				latex = "d(" + j + "," + i + ") = \\min\\left\\{" + del + "\\text{,  }" + ins + "\\text{,  }" + subs
						+ "\\right\\}";
			} else {
				latex = "d(" + j + "," + i + ") = \\min\\left\\{\\begin{array}{l}" + del + ", \\\\" + ins + ", \\\\"
						+ subs + "\\end{array}\\right\\}";
			}

		}

		// render the formula and write it to the label

		LatexRenderer.render(latex, label, size);

	}

	/**
	 * show the calculations for the cell (j,i)
	 */
	public void cellClicked(int j, int i, boolean wasAlreadyFilled) {
		clickedJ = j;
		clickedI = i;
		this.wasAlreadyFilled = wasAlreadyFilled;

		if (j < 0 || i < 0) {
			label.setIcon(null);
		} else {
			setFormula(j, i, !wasAlreadyFilled);
		}
	}

	/**
	 * sets a new size for the formula such that it shows up nicely
	 * 
	 * @param width, the allowed width of the CalculationPanel
	 * @param height, the allowed height of the CalculationPanel
	 */
	public void onResize(int width, int height) {
		int padding = 5;
		this.width = width - padding;
		this.height = height - padding;
		

		if (smallFormula)
			size = Math.max(1, GFP.RightSmallPanel.size);
		else
			size = Math.max(1, GFP.RightBigPanel.size);

		cellClicked(clickedJ, clickedI, wasAlreadyFilled);

		setPreferredSize(null);

		if (clickedI > 0 && clickedJ > 0) {
			Dimension currentDimension = getPreferredSize();

			int maxSize = size;

			do {
				size = maxSize;
				maxSize--;
				cellClicked(clickedJ, clickedI, wasAlreadyFilled);
				currentDimension = getPreferredSize();
			} while (width < currentDimension.getWidth() + padding 
					|| height < currentDimension.getHeight() + padding);
		}
		setPreferredSize(new Dimension(width, height));
	}

	public void setSmallFormula(boolean small) {
		smallFormula = small;
		onResize(width, height);
	}
}
