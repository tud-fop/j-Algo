package org.jalgo.module.levenshtein.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class LatexRenderer {

	
	public static void render(String latex, JLabel jLabel) {
		TeXFormula formula = new TeXFormula(latex);
		TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 14);
		
		BufferedImage image = 
				new BufferedImage(icon.getIconWidth(), 
						icon.getIconHeight(), 
						BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		
		JLabel label = new JLabel();
		icon.paintIcon(label, g2, 0, 0);
		
		jLabel.setIcon(new ImageIcon(image));
	}
	
	public static String red(String latex) {
		return "\\textcolor{red}{" + latex + "}";
	}
	
	public static String green(String latex) {
		return "\\textcolor{green}{" + latex + "}";
	}
	
	public static String blue(String latex) {
		return "\\textcolor{blue}{" + latex + "}";
	}
	
	public static String fat(String latex) {
		return "\\bm{" + latex + "}";
	}
}
