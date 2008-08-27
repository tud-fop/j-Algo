package org.jalgo.module.hoare.view.util;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

/**
 * Button that changes its preferred and minimum size when the font size is changed.
 * @author Antje
 *
 */
public class VariableSizeButton extends JButton {
	
	private static final long serialVersionUID = -6440409413153586489L;
	
	/**
	 * Relative distance between the text and the border in y direction: height = font size * (1+2*<code>borderWidthFactor</code>)
	 */
	private double borderWidthFactor = 0.2;
	
	/**
	 * Creates a new button that changes its preferred and minimum size when the font size is changed.
	 * @param borderWidthFactor relative distance between the text and the border in y direction: height = font size * (1+2*<code>borderWidthFactor</code>)
	 */
	public VariableSizeButton(final double borderWidthFactor) {
		this.borderWidthFactor = borderWidthFactor;
	}

	/**
	 * Sets the font for this button.
	 * @param font the desired Font for this button
	 */
	public void setFont(Font f) {
		super.setFont(f);
		Dimension d = new Dimension(getWidth(), (int)Math.round(f.getSize()*(1+borderWidthFactor*2)));
		this.setPreferredSize(d);
		this.setMinimumSize(d);
	}
	
}
