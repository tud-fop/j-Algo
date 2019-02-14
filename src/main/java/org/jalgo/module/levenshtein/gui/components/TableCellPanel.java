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
	
	private final int BLACK = 0;
	private final int RED = 1;
	private final int GREEN = 2;
	private final int BLUE = 3;
	
	
	private boolean filled;
	private JLabel label;
	private String latex;
	private int size;
	
	private boolean marked;
	
	private Color defaultBackground;
	
	private int color;
	
	public TableCellPanel() {
		label = new JLabel();
		add(label);
		latex = "";
		marked = false;
		filled = false;
		defaultBackground = getBackground();
		size = 13;
		color = BLACK;
	}
	
	/**
	 * sets the text of this cell latex formatted
	 * @param text, the text that should show up
	 */
	public void setText(String text) {
		latex = text;
		black();
	}
	
	/**
	 * sets an arrow latex formatted
	 * @param arrowType, should be ARROWRIGHT, ARROWRIGHTDOWN or ARROWDOWN
	 */
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
	
	/**
	 * styles the text of the field fat
	 */
	public void fat() {
		String string = latex;
		switch (color) {
		case RED:
			string = LatexRenderer.red(latex);
			break;
		case GREEN:
			string = LatexRenderer.green(latex);
			break;
		case BLUE:
			string = LatexRenderer.blue(latex);
			break;
		}
		String fat = LatexRenderer.fat(string);
		LatexRenderer.render(fat, label, size);
	}
	
	/**
	 * returns the text to normal black
	 */
	public void black() {
		LatexRenderer.render(latex, label, size);
		color = BLACK;
	}
	
	/**
	 * colors the text red
	 */
	public void red() {
		String red = LatexRenderer.red(latex);
		LatexRenderer.render(red, label, size);
		color = RED;
	}
	
	/**
	 * colors the text green
	 */
	public void green() {
		String green = LatexRenderer.green(latex);
		LatexRenderer.render(green, label, size);
		color = GREEN;
	}
	
	/**
	 * colors the text blue
	 */
	public void blue() {
		String blue = LatexRenderer.blue(latex);
		LatexRenderer.render(blue, label, size);
		color = BLUE;
	}
	
	/**
	 * marks the cell
	 */
	public void mark() {
		setBackground(Color.lightGray);
		marked = true;
	}
	
	/**
	 * 
	 * @return if the cell is marked
	 */
	public boolean isMarked() {
		return marked;
	}
	 /**
	  * unmarks the cell
	  */
	public void unmark() {
		setBackground(defaultBackground);
		marked = false;
	}
	
	/**
	 * the panel stays visible but the label so the text gets (in-)visible
	 */
	public void setVisible(boolean visible) {
		label.setVisible(visible);
		filled = visible;
	}
	
	/**
	 * 
	 * @return if the label is visible
	 */
	public boolean isFilled() {
		return filled;
	}
	
	/**
	 * resizes the text of the cell
	 * @param size
	 */
	public void resize(int size) {
		this.size = size;
		black();
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension dim = label.getPreferredSize();
		return new Dimension(Math.max((int) (size * 1.5), dim.width), 
				Math.max((int) (size * 1.5), dim.height));
	}
}
