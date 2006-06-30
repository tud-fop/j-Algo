package org.jalgo.module.kmp.gui.component;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import org.jalgo.module.kmp.gui.event.PhaseOneScreenListener;
import org.jalgo.module.kmp.gui.event.PhaseTwoScreenListener;
import org.jalgo.module.kmp.Constants;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.main.util.Messages;

/**
 * This is the dialog for generating the random pattern.
 * 
 * @author Danilo Lisske
 */
public class RandomPatternDialog extends JDialog {
	private static final long serialVersionUID = 7323667361401103501L;
	
	private boolean isphaseone;
	private PhaseOneScreenListener listener1;
	private PhaseTwoScreenListener listener2;
	private JComboBox cbsizealphabet;
	private JComboBox cbsizepattern;
	
	/**
	 * The constructor of the random pattern dialog.
	 * 
	 * @param l1 the <code>PhaseOneScreenListener</code>
	 * @param l2 the <code>PhaseTwoScreenListener</code>
	 */
	public RandomPatternDialog(PhaseOneScreenListener l1, PhaseTwoScreenListener l2) {
		listener1 = l1;
		listener2 = l2;
		if(listener1 == null) isphaseone = false;
		else isphaseone = true;
		setTitle(Messages.getString("kmp","RPD.Title"));
		setFont(GUIConstants.SCREEN_FONT);
		setResizable(false);
		setLayout(new GridBagLayout());
		setSize(220,120);
		if(isphaseone) addWindowFocusListener(listener1);
		else addWindowFocusListener(listener2);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		JLabel lbsizealphabet = new JLabel(Messages.getString("kmp","RPD.Label_alphabet") + ":");
		lbsizealphabet.setFont(getFont());
		add(lbsizealphabet,c);
		c.gridx = 1;
		cbsizealphabet = new JComboBox();
		cbsizealphabet.setEditable(false);
		cbsizealphabet.setFont(getFont());
		for(int i = 1; i <= Constants.ALPHABET_SIZE; i++) {
			cbsizealphabet.addItem(String.valueOf(i));
		}
		add(cbsizealphabet,c);
		c.gridx = 0;
		c.gridy = 1;
		JLabel lbsizepattern = new JLabel(Messages.getString("kmp","RPD.Label_pattern") + ":");
		lbsizepattern.setFont(getFont());
		add(lbsizepattern,c);
		c.gridx = 1;
		cbsizepattern = new JComboBox();
		cbsizepattern.setEditable(false);
		cbsizepattern.setFont(getFont());
		for(int i = 1; i <= Constants.MAX_PAT_LENGTH; i++) {
			cbsizepattern.addItem(String.valueOf(i));
		}
		add(cbsizepattern,c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(4,4,4,4); 
		JPanel buttonPane = new JPanel(new BorderLayout());
		JButton btapply = new JButton(Messages.getString("kmp","RPD.Button_apply"));
		btapply.setActionCommand("setrandom");
		btapply.setFont(getFont());
		if(isphaseone) btapply.addActionListener(listener1);
		else btapply.addActionListener(listener2);
		buttonPane.add(btapply,BorderLayout.CENTER);
		JButton btcancel = new JButton(Messages.getString("kmp","RPD.Button_cancel"));
		btcancel.setActionCommand("cancelrandom");
		btcancel.setFont(getFont());
		if(isphaseone) btcancel.addActionListener(listener1);
		else btcancel.addActionListener(listener2);
		buttonPane.add(btcancel,BorderLayout.EAST);
		add(buttonPane,c);
	}
	
	/**
	 * Returns the size of the alphabet.
	 * 
	 * @return the size of the alphabet
	 */
	public int getAlphabetSize() {
		return cbsizealphabet.getSelectedIndex() + 1;
	}
	
	/**
	 * Returns the length of the pattern.
	 * 
	 * @return the length of the pattern
	 */
	public int getPatternLength() {
		return cbsizepattern.getSelectedIndex() + 1;
	}
}