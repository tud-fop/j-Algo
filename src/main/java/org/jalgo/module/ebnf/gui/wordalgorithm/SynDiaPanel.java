package org.jalgo.module.ebnf.gui.wordalgorithm;

import java.awt.Color;
import javax.swing.JPanel;

/**
 * This Panel represents the area in which <i>ONE</i>
 * <code>SyntaxDiagrams</code> should be drawn on.
 * 
 * @author Claas Wilke
 * 
 */
@SuppressWarnings("serial")
public class SynDiaPanel extends JPanel {

	/**
	 * Constructor to create a new <code>SynDiaPanel</code>.
	 * 
	 * @param bgcolor
	 *            The Backgroundcolor of this Panel
	 */
	public SynDiaPanel(Color bgcolor) {

		this.setLayout(null);
		this.setBackground(bgcolor);

		// Must be transparent because of the background
		this.setOpaque(false);

	}

}
