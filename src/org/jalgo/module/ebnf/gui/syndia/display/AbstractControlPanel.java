package org.jalgo.module.ebnf.gui.syndia.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;

	/**
	 * @author Andre Viergutz
	 */
public abstract class AbstractControlPanel extends JPanel {
		
		protected JPanel left;
		protected JPanel right;
		protected JSlider zoomer = new JSlider(JSlider.HORIZONTAL);
		protected JToggleButton fitToSize = new JToggleButton(new ImageIcon(Messages.getResourceURL("ebnf",
		"Icon.FitToSize")));
		
		
		/**
		 * Initializes the components
		 */
		public AbstractControlPanel() {
			this.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, Messages.getString("ebnf", "Border_Control"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION,
					new java.awt.Font("Tahoma", 0, 12)));
			this.setBackground(Color.WHITE);
		
			this.left = new JPanel();
			left.setBackground(Color.WHITE);
			this.right = new JPanel();
			right.setBackground(Color.WHITE);
			
			// settings
			fitToSize.setMnemonic(KeyEvent.VK_Z);
			fitToSize.setToolTipText(Messages.getString("ebnf", "SynDia.Control_FitToSize"));
			fitToSize.addMouseListener(StatusLineUpdater.getInstance());
			fitToSize.setBorder(new EmptyBorder(3,3,3,3));
			fitToSize.setFocusPainted(false);
					
			zoomer.setMinimum(10);
			zoomer.setMaximum(50);
			zoomer.setValue(DiagramSize.getFontSize());
			zoomer.setPreferredSize(new Dimension(70, 20));
			zoomer.setBackground(Color.WHITE);
									
			// layout
			this.setLayout(new BorderLayout(0,0));
			left.add(zoomer);
			left.add(fitToSize);
			this.add(left, BorderLayout.WEST);
			this.add(right, BorderLayout.EAST);
									
		}
		
		
		/**
		 * Sets the zoomer to the given value
		 * 
		 * @param value An absolute int
		 */
		public void setZoomerValue(int value) {
			if (value > zoomer.getMaximum())
				zoomer.setValue(zoomer.getMaximum());
			else if (value < zoomer.getMinimum())
				zoomer.setValue(zoomer.getMinimum());
			else
				zoomer.setValue(value);

		}
		
		

	}
