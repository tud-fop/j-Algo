package org.jalgo.module.levenshtein.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.jalgo.module.levenshtein.Consts;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class LatexRenderer {

	/**
	 * render some latex formula and display it in the given JLabel
	 * @param latex, the latex formula
	 * @param jLabel, the label to display the formula in
	 */
	public static void render(String latex, JLabel jLabel, int size) {
		// following the example from https://github.com/jeffheaton/jlatexmath-example/blob/master/src/com/jeffheaton/latex/LatexExample.java
		TeXFormula formula = new TeXFormula(latex);
		TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
		
		BufferedImage image = 
				new BufferedImage(icon.getIconWidth(), 
						icon.getIconHeight(), 
						BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		
		JLabel label = new JLabel();
		icon.paintIcon(label, g2, 0, 0);
		
		jLabel.setIcon(new ImageIcon(image));
	}
	
	public static Dimension maxDimension(String latex, int size) {
		// following the example from https://github.com/jeffheaton/jlatexmath-example/blob/master/src/com/jeffheaton/latex/LatexExample.java
		latex = fat(latex);
		TeXFormula formula = new TeXFormula(latex);
		TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
		
		BufferedImage image = 
				new BufferedImage(icon.getIconWidth(), 
						icon.getIconHeight(), 
						BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		
		JLabel label = new JLabel();
		icon.paintIcon(label, g2, 0, 0);
		
		ImageIcon i = new ImageIcon(image);
		
		return new Dimension((int) (i.getIconWidth() * 1.5), (int) (i.getIconHeight() * 1.5));
	}
	
	public static String gray(String latex) {
		return "\\textcolor{gray}{" + latex + "}";
	}
	
	/**
	 * adds the latex code for making the string red
	 * @param latex, the latex formula that should be red
	 * @return the latex formula in red
	 */
	public static String colorSubstitution(String latex) {
		return "\\textcolor{" + Consts.colorSubstitution + "}{" + latex + "}";
	}
	
	/**
	 * adds the latex code for making the string green
	 * @param latex, the latex formula that should be green
	 * @return the latex formula in green
	 */
	public static String colorDeletion(String latex) {
		return "\\textcolor{" + Consts.colorDeletion + "}{" + latex + "}";
	}
	
	/**
	 * adds the latex code for making the string blue
	 * @param latex, the latex formula that should be blue
	 * @return the latex formula in blue
	 */
	public static String colorInsertion(String latex) {
		return "\\textcolor{" + Consts.colorInsertion + "}{" + latex + "}";
	}
	
	/**
	 * adds the latex code for making the string fat
	 * @param latex, the latex formula that should be fat
	 * @return the latex formula in fat
	 */
	public static String fat(String latex) {
		return "\\textbf{" + latex + "}";
	}
}
