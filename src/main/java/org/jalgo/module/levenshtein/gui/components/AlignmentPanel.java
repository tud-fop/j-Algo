package org.jalgo.module.levenshtein.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.module.levenshtein.model.Controller;

public class AlignmentPanel extends JPanel {

	private static final long serialVersionUID = 1670629543439950694L;
	
	private AlignmentScrollPanel scrollPanel;
	private JLabel label;
	private int height;
	private int width;

	public AlignmentPanel() {
		label = new JLabel("Alignments");
		add(label, BorderLayout.PAGE_START);
		height = 50;
		width = 30;
	}
	
	public void setScrollPanel(AlignmentScrollPanel scrollPanel) {
		if (this.scrollPanel != null)
			remove(this.scrollPanel);
		this.scrollPanel = scrollPanel;
		add(scrollPanel);
	}
	
	public void onResize(int width, int height) {
		this.width = width;
		this.height = height;
		Dimension labelDim = label.getPreferredSize();
		double ratio = (double) width / (double) labelDim.width;
		Font font = label.getFont();
		Font newFont = font.deriveFont((float) (font.getSize() * ratio * 0.25));
		label.setFont(newFont);
		labelDim = label.getPreferredSize();
		
		scrollPanel.onResize(width, height - labelDim.height);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
}
