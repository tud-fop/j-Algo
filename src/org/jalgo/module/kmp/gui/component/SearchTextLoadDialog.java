package org.jalgo.module.kmp.gui.component;

import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

import org.jalgo.module.kmp.gui.event.PhaseTwoScreenListener;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.main.util.Messages;

/**
 * This is the dialog for loading a searchtext.
 * 
 * @author Danilo Lisske
 */
public class SearchTextLoadDialog extends JDialog {
	private static final long serialVersionUID = 5735872302716708982L;
	
	private PhaseTwoScreenListener listener;
	private JTextPane tasearchtext;
	
	/**
	 * The constructor of the searchtext loading dialog.
	 * 
	 * @param l2 the <code>PhaseTwoScreenListener</code>
	 */
	public SearchTextLoadDialog(PhaseTwoScreenListener l2) {
		listener = l2;
		setTitle(Messages.getString("kmp","STLD.Title"));
		setSize(240,190);
		setFont(GUIConstants.SCREEN_FONT);
		setLayout(new GridBagLayout());
		setResizable(false);
		addWindowFocusListener(listener);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		JLabel lbsearchtext = new JLabel(Messages.getString("kmp","STLD.Label_searchtext") + ":");
		lbsearchtext.setFont(getFont());
		add(lbsearchtext,c);
		c.gridx = 1;
		c.insets = new Insets(4,4,4,4); 
		c.anchor = GridBagConstraints.EAST;
		JButton btopenfc = new JButton(Messages.getString("kmp","STLD.Button_load"));
		btopenfc.setActionCommand("stfilechooser");
		btopenfc.addActionListener(listener);
		btopenfc.setFont(getFont());
		add(btopenfc,c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(0,0,0,0); 
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		tasearchtext = new JTextPane();
		tasearchtext.setPreferredSize(new Dimension(190,60));
		tasearchtext.setFont(getFont());
		JScrollPane scrollPane = new JScrollPane(tasearchtext,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		add(scrollPane,c);
		c.gridy = 2;
		c.insets = new Insets(4,4,4,4); 
		JPanel buttonPane = new JPanel(new BorderLayout());
		JButton btapply = new JButton(Messages.getString("kmp","STLD.Button_apply"));
		btapply.setActionCommand("setsearchtext");
		btapply.addActionListener(listener);
		btapply.setFont(getFont());
		buttonPane.add(btapply,BorderLayout.CENTER);
		JButton btcancel = new JButton(Messages.getString("kmp","STLD.Button_cancel"));
		btcancel.setActionCommand("cancelsearchtext");
		btcancel.addActionListener(listener);
		btcancel.setFont(getFont());
		buttonPane.add(btcancel,BorderLayout.EAST);
		add(buttonPane,c);
	}
	
	/**
	 * Returns the searchtext.
	 * 
	 * @return the searchtext
	 */
	public String getTaSearchText() {
		return tasearchtext.getText();
	}
	
	/**
	 * Sets the searchtext to the input area of this dialog.
	 * 
	 * @param s the searchtext
	 */
	public void setTaSearchText(String s) {
		tasearchtext.setText(s);
	}
}