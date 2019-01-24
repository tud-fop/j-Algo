package org.jalgo.module.kmp.gui.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.ComponentOrientation;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.module.kmp.gui.event.PhaseOneScreenListener;

/**
 * This is the input panel of the phase one.
 * 
 * @author Danilo Lisske
 */
public class PhaseOneInputPanel extends JPanel {
	private static final long serialVersionUID = 4627572670136569244L;
	
	private PhaseOneScreenListener listener;
	private JButton btset;
	private JButton btaddpattern;
	private JButton btgoon;
	private JTextField tfpattern;
	private JTextField tfaddpattern;
	private JCheckBox jbcycle;
	
	/**
	 * The constructor of the <code>PhaseOneInputPanel</code>.
	 * 
	 * @param l1 the <code>PhaseOneScreenListener</code>
	 */
	public PhaseOneInputPanel(PhaseOneScreenListener l1) {
		listener = l1;
		
		setFont(GUIConstants.SCREEN_FONT);
		setLayout(new BorderLayout());

		JPanel patternPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lbpattern = new JLabel(Messages.getString("kmp","InputPanel.Label_pattern") + ":");
		lbpattern.setFont(getFont());
		patternPane.add(lbpattern);
		
		tfpattern = new JTextField();
		tfpattern.setName("tfpattern");
		tfpattern.setFont(getFont());
		tfpattern.setToolTipText(Messages.getString("kmp","InputPanel.TextField_pattern_ttt"));
		tfpattern.setPreferredSize(new Dimension(120,tfpattern.getPreferredSize().height));
		tfpattern.addKeyListener(listener);
		tfpattern.addMouseListener(listener);
		patternPane.add(tfpattern);
		
		btset = new JButton(Messages.getString("kmp","InputPanel.Button_set"));
		btset.setActionCommand("setpattern");
		btset.addActionListener(listener);
		btset.addMouseListener(listener);
		btset.setToolTipText(Messages.getString("kmp","InputPanel.Button_set_ttt"));
		btset.setFont(getFont());
		patternPane.add(btset);
		
		JButton btrandom = new JButton(Messages.getString("kmp","InputPanel.Button_random"));
		btrandom.setActionCommand("random");
		btrandom.addActionListener(listener);
		btrandom.addMouseListener(listener);
		btrandom.setToolTipText(Messages.getString("kmp","InputPanel.Button_random_ttt"));
		btrandom.setFont(getFont());
		patternPane.add(btrandom);
		
		JPanel goonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		btgoon = new JButton(Messages.getString("kmp","InputPanel.Button_goon"));
		btgoon.setActionCommand("goon");
		btgoon.addActionListener(listener);
		btgoon.addMouseListener(listener);
		btgoon.setForeground(GUIConstants.MOVE_ON_TO_PHASE_TWO);
		btgoon.setToolTipText(Messages.getString("kmp","InputPanel.Button_goon_ttt"));
		btgoon.setFont(getFont());
		btgoon.setVisible(false);
		goonPane.add(btgoon);
		
		JPanel extpatternPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		jbcycle = new JCheckBox(Messages.getString("kmp","InputPanel.CheckBox_cycle") + ":",true);
		jbcycle.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		jbcycle.setActionCommand("cyclechange");
		jbcycle.addActionListener(listener);
		jbcycle.addMouseListener(listener);
		jbcycle.setToolTipText(Messages.getString("kmp","InputPanel.CheckBox_cycle_ttt"));
		jbcycle.setFont(getFont());
		extpatternPane.add(jbcycle);

		JLabel lbaddpattern = new JLabel(Messages.getString("kmp","InputPanel.Label_extension") + ":");
		extpatternPane.add(lbaddpattern);
		
		tfaddpattern = new JTextField();
		tfaddpattern.setName("tfaddpattern");
		tfaddpattern.setFont(getFont());
		tfaddpattern.setToolTipText(Messages.getString("kmp","InputPanel.TextField_addpattern_ttt"));
		tfaddpattern.setPreferredSize(new Dimension(20,tfaddpattern.getPreferredSize().height));
		tfaddpattern.addKeyListener(listener);
		tfaddpattern.addMouseListener(listener);
		extpatternPane.add(tfaddpattern);
		
		btaddpattern = new JButton(Messages.getString("kmp","InputPanel.Button_addpattern"));
		btaddpattern.setActionCommand("addpattern");
		btaddpattern.addActionListener(listener);
		btaddpattern.addMouseListener(listener);
		btaddpattern.setToolTipText(Messages.getString("kmp","InputPanel.Button_addpattern_ttt"));
		btaddpattern.setFont(getFont());
		extpatternPane.add(btaddpattern);
		
		add(patternPane,BorderLayout.WEST);
		add(goonPane,BorderLayout.CENTER);
		add(extpatternPane,BorderLayout.EAST);
	}
	
	/**
	 * Enables or disables the button to set the pattern. 
	 * 
	 * @param value the value
	 */
	public void setPatternEnabled(boolean value) {
		btset.setEnabled(value);
	}
	
	/**
	 * Returns the status of the button to set the pattern.
	 * 
	 * @return if the button is enabled
	 */
	public boolean isPatternEnabled() {
		return btset.isEnabled();
	}
	
	/**
	 * Returns the text in the pattern textfield.
	 * 
	 * @return the text in the pattern textfield
	 */
	public String getTfPatternText() {
		return tfpattern.getText();
	}
	
	/**
	 * Sets the content of the pattern textfield.
	 * 
	 * @param p the content
	 */
	public void setTfPatternText(String p) {
		tfpattern.setText(p);
	}
	
	/**
	 * Returns the length of the content in the pattern textfield.
	 * 
	 * @return the length
	 */
	public int getTfPatternLength() {
		return tfpattern.getText().length();
	}
	
	/**
	 * Enables or disables the pattern expand button.
	 * 
	 * @param value the value
	 */
	public void setAddPatternEnabled(boolean value) {
		btaddpattern.setEnabled(value);
	}
	
	/**
	 * Returns the status of the expand pattern button.
	 * 
	 * @return if the expand pattern button is enabled
	 */
	public boolean isAddPatternEnabled() {
		return btaddpattern.isEnabled();
	}
	
	/**
	 * Returns the content of the expand pattern textfield.
	 * 
	 * @return the text
	 */
	public String getTfAddPatternText() {
		return tfaddpattern.getText();
	}
	
	/**
	 * Sets the content of the expand pattern textfield.
	 * 
	 * @param s the text
	 */
	public void setTfAddPatternText(String s) {
		tfaddpattern.setText(s);
	}
	
	/**
	 * Returns the length of the content in the expand pattern textfield.
	 * 
	 * @return the length
	 */
	public int getTfAddPatternLength() {
		return tfaddpattern.getText().length();
	}
	
	/**
	 * Returns the status of the cycle checkbox.
	 * 
	 * @return if cycles is selected
	 */
	public boolean isCycleSelected() {
		return jbcycle.isSelected();
	}
	
	/**
	 * Sets the visibility of the go on button.
	 * 
	 * @param value the value
	 */
	public void setBtGoOnVisible(boolean value) {
		btgoon.setVisible(value);
	}
}