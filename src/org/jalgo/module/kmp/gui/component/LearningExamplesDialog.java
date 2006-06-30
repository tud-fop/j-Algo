package org.jalgo.module.kmp.gui.component;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ButtonModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import org.jalgo.module.kmp.gui.event.WelcomeScreenListener;
import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIConstants;

/**
 * This is the dialog where the user can choose one of ten examples.
 * 
 * @author Danilo Lisske
 */
public class LearningExamplesDialog extends JDialog {
	private static final long serialVersionUID = 2338105141588581154L;
	
	private WelcomeScreenListener listener;
	private JTextPane tpdescription;
	private ButtonGroup bgexamples;
	
	/**
	 * The constructor of the <code>LearningExamplesDialog</code>
	 * 
	 * @param l the <code>WelcomeScreenListener</code>
	 */
	public LearningExamplesDialog(WelcomeScreenListener l) {
		listener = l;
		setTitle(Messages.getString("kmp","LED.Title"));
		setSize(500,500);
		setFont(GUIConstants.SCREEN_FONT);
		setResizable(false);
		addWindowFocusListener(listener);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 20;
		c.anchor = GridBagConstraints.WEST;
		tpdescription = new JTextPane();
		tpdescription.setEditable(false);
		tpdescription.setFont(getFont());
		tpdescription.setPreferredSize(new Dimension(120,400));
		tpdescription.setBackground(Color.LIGHT_GRAY);
		JScrollPane scrollPane = new JScrollPane(tpdescription,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane,c);
		bgexamples = new ButtonGroup();
		JRadioButton rb0 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_0"),true);
		JLabel lb0 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_0"));
		JRadioButton rb1 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_1"),true);
		JLabel lb1 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_1"));
		JRadioButton rb2 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_2"),true);
		JLabel lb2 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_2"));
		JRadioButton rb3 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_3"),true);
		JLabel lb3 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_3"));
		JRadioButton rb4 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_4"),true);
		JLabel lb4 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_4"));
		JRadioButton rb5 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_5"),true);
		JLabel lb5 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_5"));
		JRadioButton rb6 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_6"),true);
		JLabel lb6 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_6"));
		JRadioButton rb7 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_7"),true);
		JLabel lb7 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_7"));
		JRadioButton rb8 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_8"),true);
		JLabel lb8 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_8"));
		JRadioButton rb9 = new JRadioButton(Messages.getString("kmp","LED.RB_Title") 
			+ ": " + Messages.getString("kmp","LED.Example_pattern_9"),true);
		JLabel lb9 = new JLabel(Messages.getString("kmp","LED.LB_Title") + ": " 
			+ Messages.getString("kmp","LED.Example_searchtext_9"));
		bgexamples.add(rb0);
		bgexamples.add(rb1);
		bgexamples.add(rb2);
		bgexamples.add(rb3);
		bgexamples.add(rb4);
		bgexamples.add(rb5);
		bgexamples.add(rb6);
		bgexamples.add(rb7);
		bgexamples.add(rb8);
		bgexamples.add(rb9);
		rb0.addMouseListener(listener);
		rb0.setActionCommand("0");
		rb0.setName("0");
		rb0.setFont(getFont());
		lb0.addMouseListener(listener);
		lb0.setName("0");
		lb0.setFont(getFont());
		rb1.addMouseListener(listener);
		rb1.setActionCommand("1");
		rb1.setName("1");
		rb1.setFont(getFont());
		lb1.addMouseListener(listener);
		lb1.setName("1");
		lb1.setFont(getFont());
		rb2.addMouseListener(listener);
		rb2.setActionCommand("2");
		rb2.setName("2");
		rb2.setFont(getFont());
		lb2.addMouseListener(listener);
		lb2.setName("2");
		lb2.setFont(getFont());
		rb3.addMouseListener(listener);
		rb3.setActionCommand("3");
		rb3.setName("3");
		rb3.setFont(getFont());
		lb3.addMouseListener(listener);
		lb3.setName("3");
		lb3.setFont(getFont());
		rb4.addMouseListener(listener);
		rb4.setActionCommand("4");
		rb4.setName("4");
		rb4.setFont(getFont());
		lb4.addMouseListener(listener);
		lb4.setName("4");
		lb4.setFont(getFont());
		rb5.addMouseListener(listener);
		rb5.setActionCommand("5");
		rb5.setName("5");
		rb5.setFont(getFont());
		lb5.addMouseListener(listener);
		lb5.setName("5");
		lb5.setFont(getFont());
		rb6.addMouseListener(listener);
		rb6.setActionCommand("6");
		rb6.setName("6");
		rb6.setFont(getFont());
		lb6.addMouseListener(listener);
		lb6.setName("6");
		lb6.setFont(getFont());
		rb7.addMouseListener(listener);
		rb7.setActionCommand("7");
		rb7.setName("7");
		rb7.setFont(getFont());
		lb7.addMouseListener(listener);
		lb7.setName("7");
		lb7.setFont(getFont());
		rb8.addMouseListener(listener);
		rb8.setActionCommand("8");
		rb8.setName("8");
		rb8.setFont(getFont());
		lb8.addMouseListener(listener);
		lb8.setName("8");
		lb8.setFont(getFont());
		rb9.addMouseListener(listener);
		rb9.setActionCommand("9");
		rb9.setName("9");
		rb9.setFont(getFont());
		lb9.addMouseListener(listener);
		lb9.setName("9");
		lb9.setFont(getFont());
		c.gridx = 1;
		c.gridheight = 1;
		add(rb0,c);
		c.gridy = 1;
		c.insets = new Insets(0,20,0,0);
		add(lb0,c);
		c.gridy = 2;
		c.insets = new Insets(0,0,0,0);
		add(rb1,c);
		c.gridy = 3;
		c.insets = new Insets(0,20,0,0);
		add(lb1,c);
		c.gridy = 4;
		c.insets = new Insets(0,0,0,0);
		add(rb2,c);
		c.gridy = 5;
		c.insets = new Insets(0,20,0,0);
		add(lb2,c);
		c.gridy = 6;
		c.insets = new Insets(0,0,0,0);
		add(rb3,c);
		c.gridy = 7;
		c.insets = new Insets(0,20,0,0);
		add(lb3,c);
		c.gridy = 8;
		c.insets = new Insets(0,0,0,0);
		add(rb4,c);
		c.gridy = 9;
		c.insets = new Insets(0,20,0,0);
		add(lb4,c);
		c.gridy = 10;
		c.insets = new Insets(0,0,0,0);
		add(rb5,c);
		c.gridy = 11;
		c.insets = new Insets(0,20,0,0);
		add(lb5,c);
		c.gridy = 12;
		c.insets = new Insets(0,0,0,0);
		add(rb6,c);
		c.gridy = 13;
		c.insets = new Insets(0,20,0,0);
		add(lb6,c);
		c.gridy = 14;
		c.insets = new Insets(0,0,0,0);
		add(rb7,c);
		c.gridy = 15;
		c.insets = new Insets(0,20,0,0);
		add(lb7,c);
		c.gridy = 16;
		c.insets = new Insets(0,0,0,0);
		add(rb8,c);
		c.gridy = 17;
		c.insets = new Insets(0,20,0,0);
		add(lb8,c);
		c.gridy = 18;
		c.insets = new Insets(0,0,0,0);
		add(rb9,c);
		c.gridy = 19;
		c.insets = new Insets(0,20,0,0);
		add(lb9,c);
		c.gridx = 0;
		c.gridy = 20;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(2,2,2,2);
		JButton btapply = new JButton(Messages.getString("kmp","LED.Button_apply"));
		btapply.setActionCommand("setlearningexample");
		btapply.addActionListener(listener);
		btapply.setFont(getFont());
		add(btapply,c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.WEST;
		JButton btcancel = new JButton(Messages.getString("kmp","LED.Button_cancel"));
		btcancel.setActionCommand("cancellearningexample");
		btcancel.addActionListener(listener);
		btcancel.setFont(getFont());
		add(btcancel,c);
	}
	
	/**
	 * Sets the content of the description text to the selected number.
	 * 
	 * @param number the number which has to be set
	 */
	public void setTfDescriptionText(String number) {
		if(number.equals("mouseoff")) 
			tpdescription.setText(Messages.getString("kmp","LED.Example_description_"+getSelectedIndex()));
		else tpdescription.setText(Messages.getString("kmp","LED.Example_description_"+number));
	}
	
	/**
	 * Returns the selected learning example.
	 * 
	 * @return the selected index
	 */
	public String getSelectedIndex() {
		return ((ButtonModel)bgexamples.getSelection()).getActionCommand();
	}
}