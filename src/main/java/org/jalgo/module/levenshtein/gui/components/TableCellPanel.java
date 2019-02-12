package org.jalgo.module.levenshtein.gui.components;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.LatexRenderer;

public class TableCellPanel extends JPanel {
	private static final long serialVersionUID = 
			4571815172078498836L;
	
	public static final int TEXT = 1;
	public static final int ARROWRIGHT = 2;
	public static final int ARROWRIGHTDOWN = 3;
	public static final int ARROWDOWN = 4;
	
	private boolean filled;
	private JLabel label;
	private String latex;
	
	private boolean marked;
	
	private Color defaultBackground;
	
	public TableCellPanel() {
		label = new JLabel();
		add(label);
		latex = "";
		marked = false;
		filled = false;
		defaultBackground = getBackground();
	}
	
	public void setText(String text) {
		latex = text;
		black();
	}
	
	public void setArrow(int arrowType) {
		switch (arrowType) {
		case ARROWRIGHT:
			latex = "\\rightarrow";
			break;
		case ARROWRIGHTDOWN:
			latex = "\\searrow";
			break;
		case ARROWDOWN:
			latex = "\\downarrow";
			break;
		}
		black();
	}
	
	public void fat() {
		String fat = LatexRenderer.fat(latex);
		LatexRenderer.render(fat, label);
	}
	
	public void black() {
		LatexRenderer.render(latex, label);
	}
	
	public void red() {
		String red = LatexRenderer.red(latex);
		LatexRenderer.render(red, label);
	}
	
	public void green() {
		String green = LatexRenderer.green(latex);
		LatexRenderer.render(green, label);
	}
	
	public void blue() {
		String blue = LatexRenderer.blue(latex);
		LatexRenderer.render(blue, label);
	}
	
	public void mark() {
		setBackground(Color.lightGray);
		marked = true;
	}
	
	public boolean isMarked() {
		return marked;
	}
	
	public void unmark() {
		setBackground(defaultBackground);
		marked = false;
	}
	
	
	public void setVisible(boolean visible) {
		label.setVisible(visible);
		filled = visible;
	}
	
	public boolean isFilled() {
		return filled;
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension dim = label.getPreferredSize();
		return new Dimension(Math.max(20, dim.width), 
				Math.max(20, dim.height));
	}
}
