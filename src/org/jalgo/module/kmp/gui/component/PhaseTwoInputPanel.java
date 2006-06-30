package org.jalgo.module.kmp.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.module.kmp.gui.event.PhaseTwoScreenListener;

/**
 * This is the input panel of the phase two.
 * 
 * @author Danilo Lisske
 */
public class PhaseTwoInputPanel extends JPanel {
	private static final long serialVersionUID = -3852311834926117075L;
	
	private PhaseTwoScreenListener listener;
	private JButton btset;
	private JButton btgenerst;
	private JTextField tfpattern;
	private JLabel lbresult;
	
	/**
	 * The constructor of the <code>PhaseTwoInputPanel</code>.
	 * 
	 * @param l2 the <code>PhaseTwoScreenListener</code>
	 */
	public PhaseTwoInputPanel(PhaseTwoScreenListener l2) {
		listener = l2;
		
		setFont(GUIConstants.SCREEN_FONT);
		setLayout(new BorderLayout());

		JPanel patternPane = new JPanel();
		patternPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		
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
		
		JPanel resultPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		lbresult = new JLabel();
		lbresult.addMouseListener(listener);
		lbresult.setToolTipText(Messages.getString("kmp","InputPanel.Label_result_ttt"));
		lbresult.setFont(GUIConstants.RESULT_FONT);
		lbresult.setVisible(false);
		resultPane.add(lbresult);
		
		JPanel searchtextPane = new JPanel();
		searchtextPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel lbsearchtext = new JLabel(Messages.getString("kmp","InputPanel.Label_searchtext") + ":");
		lbsearchtext.setFont(getFont());
		searchtextPane.add(lbsearchtext);
		
		JButton btloadst = new JButton(Messages.getString("kmp","InputPanel.Button_loadst"));
		btloadst.setActionCommand("loadst");
		btloadst.addActionListener(listener);
		btloadst.addMouseListener(listener);
		btloadst.setToolTipText(Messages.getString("kmp","InputPanel.Button_loadst_ttt"));
		btloadst.setFont(getFont());
		searchtextPane.add(btloadst);
		
		btgenerst = new JButton(Messages.getString("kmp","InputPanel.Button_generst"));
		btgenerst.setActionCommand("generst");
		btgenerst.addActionListener(listener);
		btgenerst.addMouseListener(listener);
		btgenerst.setToolTipText(Messages.getString("kmp","InputPanel.Button_generst_ttt"));
		btgenerst.setFont(getFont());
		searchtextPane.add(btgenerst);
		
		add(patternPane,BorderLayout.WEST);
		add(resultPane,BorderLayout.CENTER);
		add(searchtextPane,BorderLayout.EAST);
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
	 * Enables or disables the button to generate a random searchtext.
	 * 
	 * @param value the value
	 */
	public void setGenerateSearchTextEnabled(boolean value) {
		btgenerst.setEnabled(value);
	}
	
	/**
	 * Sets the visibility of the label which displays the result of the search.
	 * 
	 * @param value the value
	 * @param isfound is the end reached
	 */
	public void setLabelResultVisible(boolean value, boolean isfound) {
		if(value) {
			if(isfound) {
				lbresult.setForeground(Color.GREEN.darker());
				lbresult.setText(Messages.getString("kmp","InputPanel.Label_result_true"));
			}
			else {
				lbresult.setForeground(Color.RED);
				lbresult.setText(Messages.getString("kmp","InputPanel.Label_result_false"));
			}
		}
		lbresult.setVisible(value);
	}
}