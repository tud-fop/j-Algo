package org.jalgo.module.levenshtein.gui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	
	
	private String fillTarget0Row = "d(0,i) = i * $c_{ins}$";
	private String target0Constr = "\\forall\\ 0 \\le i \\le k";
	private String fillSource0Col = "d(j,0) = j * $c_{del}$";
//	private String fillSource0Col = "\\textbf{c}_{\\textbf{del}}";
	private String source0Constr = "\\forall\\ 0 \\le j \\le n";
	private String formInsert = "d(j,i-1) + $c_{ins}$";
	private String formDelete = "d(j-1,i) + $c_{del}$";
	private String formSubs = "d(j-1,i-1) + ";
	private String restConstr = "\\forall\\ 1 \\le j \\le n, \\\\" + "1 \\le i \\le k";
	private String costSubstitution = "c_{sub}" ;
	private String subsConstr = "\\text{" + GuiController.getString("gui.if") + "}, $w_j$ \\neq $v_i$ \\\\";
	private String costIdentity = "$c_{id}$";
	private String idConstr = "\\text{" + GuiController.getString("gui.otherwise") + "}";
	private String closingBracket = "\\end{array}\\right\\}";
	private String fillRest = "d(j,i) = \\min";
	private String openingBracket = "\\left\\{\\begin{array}{lc}";
	
	private JLabel lblD0i = new JLabel();
	private JLabel lbl0ik = new JLabel();
	private JLabel lblDj0 = new JLabel();
	private JLabel lbl0jn = new JLabel();
	private JLabel lblDji = new JLabel();
	private JLabel lbl1ik1jn = new JLabel();
	
	private Controller controller;
	
	private int width;
	private int height;
	
	private int size = 13;
	
	private int clickedJ = -1;
	private int clickedI = -1;
	
	private boolean wasAlreadyFilled = true;
	
	/**
	 * initializes the GerneralFormulaPanel by writing the formulas of the levenshtein
	 * distance into the Panel
	 */
	public void init(Controller controller) {
		this.controller = controller;
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		LatexRenderer.render(fillTarget0Row, lblD0i, size);
		LatexRenderer.render(target0Constr, lbl0ik, size);
		
		LatexRenderer.render(fillSource0Col, lblDj0, size);
		LatexRenderer.render(source0Constr, lbl0jn, size);
		
		String string = fillRest + openingBracket + formDelete + ", \\\\ " 
				+ formInsert + ", \\\\" 
				+ formSubs + openingBracket + costSubstitution + "&" + subsConstr
							+ costIdentity + "&" + idConstr
							+ closingBracket + closingBracket;
		LatexRenderer.render(string, lblDji, size);
		LatexRenderer.render(restConstr, lbl1ik1jn, size);
		
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
		
		width = super.getPreferredSize().width;
		height = super.getPreferredSize().height;
		
	}

	/**
	 * marks the interesting details of the formula with colors such that
	 * its easier to understand, what's going on
	 */
	public void cellClicked(int j, int i, boolean wasAlreadyFilled) {
		this.wasAlreadyFilled = wasAlreadyFilled;
		init(controller);
		clickedJ = j;
		clickedI = i;
		if (j == 0) {
			String string1 = LatexRenderer.fat(fillTarget0Row);
			LatexRenderer.render(string1, lblD0i, size);
			String string2 = LatexRenderer.fat(target0Constr);
			LatexRenderer.render(string2, lbl0ik, size);
		} else if (i == 0) {
			String string1 = LatexRenderer.fat(fillSource0Col);
			LatexRenderer.render(string1, lblDj0, size);
			String string2 = LatexRenderer.fat(source0Constr);
			LatexRenderer.render(string2, lbl0jn, size);
		} else if (i > 0 && j > 0 && wasAlreadyFilled){
			String string1 = fillRest + openingBracket + formDelete + ", \\\\ " 
					+ formInsert + ", \\\\" 
					+ formSubs + openingBracket + costSubstitution + "&" + subsConstr
								+ costIdentity + "&" + idConstr
								+ closingBracket + closingBracket;
			LatexRenderer.render(LatexRenderer.fat(string1), lblDji, size);
			
			String string2 = LatexRenderer.fat(restConstr);
			LatexRenderer.render(string2, lbl1ik1jn, size);
			
		} else if (i > 0 && j > 0 && !wasAlreadyFilled){
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
			
			LatexRenderer.render(string1, lblDji, size);
			
			String string2 = LatexRenderer.fat(restConstr);
			LatexRenderer.render(string2, lbl1ik1jn, size);
			
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
	
	private void fat() {
		LatexRenderer.render(LatexRenderer.fat(fillTarget0Row), lblD0i, size);
		LatexRenderer.render(LatexRenderer.fat(target0Constr), lbl0ik, size);
		
		LatexRenderer.render(LatexRenderer.fat(fillSource0Col), lblDj0, size);
		LatexRenderer.render(LatexRenderer.fat(source0Constr), lbl0jn, size);
		
		String string = fillRest + openingBracket + formDelete + ", \\\\ " 
				+ formInsert + ", \\\\" 
				+ formSubs + openingBracket + costSubstitution + "&" + subsConstr
							+ costIdentity + "&" + idConstr
							+ closingBracket + closingBracket;
		LatexRenderer.render(LatexRenderer.fat(string), lblDji, size);
		LatexRenderer.render(LatexRenderer.fat(restConstr), lbl1ik1jn, size);
	}
	
	/**
	 * calculates the new size of the latex formulas as the window size changes
	 * @param width, the allowed width of the GeneralFormulaPanel
	 * @param height, the allowed height of the GeneralFormulaPanel
	 */
	public void onResize(int width, int height) {
		this.width = width;
		this.height = height;
		
		// calculate the maximum size
		Dimension dim = super.getPreferredSize();
		double ratioWidth = (double) width / (double) dim.width;
		double ratioHeight = (double) height / (double) dim.height;
		double maxRatio = Math.min(ratioWidth, ratioHeight);
		int maxSize = (int) (size * maxRatio);
		
		do {
			size = maxSize;
			fat();
			repaint();
			revalidate();
			maxSize--;
			dim = super.getPreferredSize();
		} while(width < dim.width || height < dim.height);
		
		cellClicked(clickedJ, clickedI, wasAlreadyFilled);
		
		repaint();
		revalidate();
	}
}
