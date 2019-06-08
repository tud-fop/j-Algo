package org.jalgo.module.levenshtein.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.GuiController;
import org.jalgo.module.levenshtein.gui.LatexRenderer;
import org.jalgo.module.levenshtein.model.Controller;

public class ExpandedGeneralFormula extends GeneralFormula {

	private String fillTarget0Row = "d(0,i) = i \\cdot $c_{ins}$";
	private String target0Constr = "\\forall\\ 0 \\le i \\le k";
	private String fillSource0Col = "d(j,0) = j \\cdot $c_{del}$";
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
	
	private JPanel contentPanel = new JPanel();
	
	private Controller controller;
	
	private int size = 13;
	
	private boolean wasAlreadyFilled = false;
	
	private int clickedJ = 0;
	private int clickedI = 0;
	
	private int width;
	private int height;
	
	public ExpandedGeneralFormula() {
		setLayout(new BorderLayout());
		
		add(contentPanel, BorderLayout.CENTER);
		
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		contentPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		contentPanel.setLayout(new GridBagLayout());
		
		addLabelsToLayout();
		
		fillLabels();
	}
	
	private void addLabelsToLayout() {
		GridBagConstraints cRight = new GridBagConstraints();
		cRight.fill = GridBagConstraints.BOTH;
		
		cRight.gridx = 0;
		cRight.gridy = 0;
		cRight.anchor = GridBagConstraints.WEST;
		contentPanel.add(lblD0i, cRight);
		lblD0i.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		cRight.gridx = 1;
		cRight.gridy = 0;
		cRight.anchor = GridBagConstraints.EAST;
		contentPanel.add(lbl0ik, cRight);
		lbl0ik.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		cRight.gridx = 0;
		cRight.gridy = 1;
		cRight.anchor = GridBagConstraints.WEST;
		contentPanel.add(lblDj0, cRight);
		lblDj0.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		cRight.gridx = 1;
		cRight.gridy = 1;
		cRight.anchor = GridBagConstraints.EAST;
		contentPanel.add(lbl0jn, cRight);
		lbl0jn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		cRight.gridx = 0;
		cRight.gridy = 2;
		cRight.anchor = GridBagConstraints.WEST;
		contentPanel.add(lblDji, cRight);
		lblDji.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
		
		cRight.gridx = 1;
		cRight.gridy = 2;
		cRight.anchor = GridBagConstraints.EAST;
		contentPanel.add(lbl1ik1jn, cRight);
		lbl1ik1jn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	private void fillLabels() {
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
	}
	
	private Dimension getSuperPreferredSize() {
		return super.getPreferredSize();
	}
	
	/**
	 * marks the interesting details of the formula with colors such that
	 * its easier to understand, what's going on
	 */
	public void cellClicked(int j, int i, boolean wasAlreadyFilled) {
		this.wasAlreadyFilled = wasAlreadyFilled;
		fillLabels();
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
			string1 += LatexRenderer.colorDeletion(formDelete )+ ", \\\\ ";
			string1 += LatexRenderer.colorInsertion(formInsert )+ ", \\\\";
			string1 += LatexRenderer.colorSubstitution(formSubs) + openingBracket;
			if (!controller.sameCharAt(j, i)) {
				string1 += LatexRenderer.colorSubstitution(costSubstitution) + "&" + subsConstr;
				string1 += costIdentity + "&" + idConstr;
			} else {
				string1 += costSubstitution + "&" + subsConstr;
				string1 += LatexRenderer.colorSubstitution(costIdentity) + "&" + idConstr;
			}
			string1 += closingBracket + closingBracket;
			
			LatexRenderer.render(string1, lblDji, size);
			
			String string2 = LatexRenderer.fat(restConstr);
			LatexRenderer.render(string2, lbl1ik1jn, size);
			
		}
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
	
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	public void onResize(int width, int height) {
		this.width = width;
		this.height = height;
		
		lblD0i.setText(width + "");
		lblDj0.setText(height + "");
		
		double padding = 0;
		
		Dimension currentDimension = getSuperPreferredSize();
		
//		lblD0i.setPreferredSize(null);
//		lbl0ik.setPreferredSize(null);
//		lblDj0.setPreferredSize(null);
//		lbl0jn.setPreferredSize(null);
//		lblDji.setPreferredSize(null);
//		lbl1ik1jn.setPreferredSize(null);
		
		double widthRatio = (double) width / (double) (currentDimension.getWidth() + padding);
		double heightRatio = (double) height / (double) (currentDimension.getHeight() + padding);
		double ratio = Math.min(widthRatio, heightRatio);
		
		int maxSize = (int) (ratio * size);
		
		do {
			size = maxSize;
			fat();
			maxSize--;
			repaint();
			revalidate();
			currentDimension = getSuperPreferredSize();
		} while (width < currentDimension.getWidth() 
				|| height < currentDimension.getHeight());
		
//		lblD0i.setPreferredSize(lblD0i.getPreferredSize());
//		lbl0ik.setPreferredSize(lbl0ik.getPreferredSize());
//		lblDj0.setPreferredSize(lblDj0.getPreferredSize());
//		lbl0jn.setPreferredSize(lbl0jn.getPreferredSize());
//		lblDji.setPreferredSize(lblDji.getPreferredSize());
//		lbl1ik1jn.setPreferredSize(lbl1ik1jn.getPreferredSize());
		
		fillLabels();
		cellClicked(clickedJ, clickedI, wasAlreadyFilled);
		
//		
//		double padding = 0;
//		
//		Dimension currentSize = getSuperPreferredSize();
//		
//		double widthRatio = (double) width / (double) (currentSize.getWidth() + padding);
//		double heightRatio = (double) height / (double) (currentSize.getHeight() + padding);
//		double ratio = Math.min(widthRatio, heightRatio);
//		
//		int maxSize = (int) (size * ratio);
//		
//		do {
//			size = maxSize;
//			fat();
//			maxSize--;
//			revalidate();
//			repaint();
//			currentSize = getSuperPreferredSize();
//		} while (width < currentSize.getWidth() || height < currentSize.getHeight());
//		
//		fillLabels();
//		cellClicked(clickedJ, clickedI, wasAlreadyFilled);
	}
	
}
