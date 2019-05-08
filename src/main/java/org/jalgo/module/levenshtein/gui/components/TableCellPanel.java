package org.jalgo.module.levenshtein.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.gui.LatexRenderer;

public class TableCellPanel extends JPanel {
	private static final long serialVersionUID = 4571815172078498836L;

	public static final int TEXT = 1;
	public static final int ARROWRIGHT = 2;
	public static final int ARROWRIGHTDOWN = 3;
	public static final int ARROWDOWN = 4;
	public static final int NUMBER = 5;

	private final int BLACK = 0;
	private final int RED = 1;
	private final int GREEN = 2;
	private final int BLUE = 3;

	private int content;

	private boolean filled;
	private JLabel label;
	private String latex;
	private String latexFormatted;
	private int size;

	private boolean marked;

	private Color defaultBackground;

	private int color;

	private boolean gray;

	private Dimension preferredSize;

	public TableCellPanel(boolean gray) {
		label = new JLabel();
		add(label);
		latex = "";
		latexFormatted = "";
		marked = false;
		filled = false;
		defaultBackground = getBackground();
		size = 13;
		color = BLACK;
		content = TEXT;
		this.gray = gray;
	}

	/**
	 * sets the text of this cell latex formatted
	 * 
	 * @param text, the text that should show up
	 */
	public void setText(String text) {
		content = TEXT;
		latex = text;
		preferredSize = LatexRenderer.maxDimension(text, size);
		black();
	}

	/**
	 * sets an arrow latex formatted
	 * 
	 * @param arrowType, should be ARROWRIGHT, ARROWRIGHTDOWN or ARROWDOWN
	 */
	public void setArrow(int arrowType) {
		this.content = arrowType;
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
		preferredSize = LatexRenderer.maxDimension(latex, size);
		black();
	}

	/**
	 * styles the text of the field fat fat() needs to be called after red(),
	 * green(), blue() if wished
	 */
	public void fat() {
//		switch (arrowType) {
//		case TEXT:
//			latexFormatted = latex;
//			break;
//		case ARROWDOWN:
//			latexFormatted = "\\Downarrow";
//			break;
//		case ARROWRIGHT:
//			latexFormatted = "\\Rightarrow";
//			break;
//		case ARROWRIGHTDOWN:
//			latexFormatted = "\\Seaarrow";
//			break;
//		default:
//			latexFormatted = latex;
//		}
		latexFormatted = latex;
		switch (color) {
		case RED:
			latexFormatted = LatexRenderer.red(latexFormatted);
			break;
		case GREEN:
			latexFormatted = LatexRenderer.green(latexFormatted);
			break;
		case BLUE:
			latexFormatted = LatexRenderer.blue(latexFormatted);
			break;
		}
		latexFormatted = LatexRenderer.fat(latexFormatted);
		LatexRenderer.render(latexFormatted, label, size);
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException _) {
			return false;
		}
		return true;
	}

	/**
	 * returns the text to normal black
	 */
	public void black() {
		if (!gray) {
			label.setText(latex);
		} else {
			
			latexFormatted = LatexRenderer.gray(latex);
			

			LatexRenderer.render(latexFormatted, label, size);
			color = BLACK;
		}
	}

	/**
	 * colors the text red
	 */
	public void red() {
		latexFormatted = LatexRenderer.red(latex);
		LatexRenderer.render(latexFormatted, label, size);
		color = RED;
	}

	/**
	 * colors the text green
	 */
	public void green() {
		latexFormatted = LatexRenderer.green(latex);
		LatexRenderer.render(latexFormatted, label, size);
		color = GREEN;
	}

	/**
	 * colors the text blue
	 */
	public void blue() {
		latexFormatted = LatexRenderer.blue(latex);
		LatexRenderer.render(latexFormatted, label, size);
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
	 * 
	 * @param size
	 */
	public void resize(int size) {
		this.size = size;

		if (!gray) {
			Font font = label.getFont();
			Font newFont = font.deriveFont((float) size);
			label.setFont(newFont);
		} else {
			LatexRenderer.render(latexFormatted, label, size);
		}
		
		
	}

	@Override
	public Dimension getPreferredSize() {
		return LatexRenderer.maxDimension(latex, size);
	}
}
