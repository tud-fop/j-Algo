package org.jalgo.module.c0h0.views;

import javax.swing.*;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.controller.ViewManager;

/**
 * view which provides the menu promt for the C0 code parameters
 * @author Peter Schwede
 * 
 */
public class HelpView extends View {
	private static final long serialVersionUID = -6347171988776084694L;
	private JComboBox boxM = new JComboBox(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
	private JComboBox boxK = new JComboBox(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
	private JComboBox boxI = new JComboBox(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
	private JButton okButton = new JButton("Code erzeugen");
	private JButton transButton = new JButton("Transformieren");
	private JPanel pane = new JPanel();

	/**
	 * @param controller
	 * @param viewManager 
	 */
	@SuppressWarnings("serial")
	public HelpView(final Controller controller, ViewManager viewManager) {
		Color foreground = Color.black;
		
		AbstractAction generateAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Integer m = ((Integer) boxM.getSelectedItem()).intValue();
				Integer k = ((Integer) boxK.getSelectedItem()).intValue();
				Integer i = ((Integer) boxI.getSelectedItem()).intValue();
				
				if(m>=k && m>=i)
					controller.readMKI(m, k, i);
			}
		};
		
		AbstractAction transformAction = new AbstractAction() {
			
			public void actionPerformed(ActionEvent arg0) {
				controller.runTransformation();
			}
		};
		
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		setLayout(new BorderLayout());
		pane.setLayout(gridbag);

		JLabel headline = new JLabel("C0/H0 Transformation");
		headline.setForeground(foreground);
		headline.setAlignmentX(CENTER_ALIGNMENT);
		headline.setFont(new Font(headline.getFont().getFontName(), headline.getFont().getStyle(), 20));
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(headline, c);
		pane.add(headline);

		// 
		JLabel l = new JLabel(
				"<HTML> <br> Bitte w&auml;hle die Gr&ouml;&szlig;e der Parameter, <br> um ein neues Programm zu schreiben! <br> &nbsp;</HTML>");
		l.setForeground(foreground);
		l.setFont(new Font(l.getFont().getFontName(), l.getFont().getStyle(), 16));
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(l, c);
		pane.add(l);

		l = new JLabel("Anzahl der Variablen (m):");
		l.setForeground(foreground);
		l.setFont(new Font(l.getFont().getFontName(), l.getFont().getStyle(), 16));
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0 ,0, 10, 0);
		gridbag.setConstraints(l, c);
		pane.add(l);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(boxM, c);
		pane.add(boxM);

		l = new JLabel("Anzahl der Eingaben (k):");
		l.setForeground(foreground);
		l.setFont(new Font(l.getFont().getFontName(), l.getFont().getStyle(), 16));
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(l, c);
		pane.add(l);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(boxK, c);
		pane.add(boxK);

		l = new JLabel("Auszugebende Variable (i):");
		l.setForeground(foreground);
		l.setFont(new Font(l.getFont().getFontName(), l.getFont().getStyle(), 16));
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(l, c);
		pane.add(l);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(boxI, c);
		pane.add(boxI);

		c.gridwidth = GridBagConstraints.REMAINDER;
		
		JPanel buttonPain = new JPanel();
		buttonPain.setLayout(new BoxLayout(buttonPain,BoxLayout.X_AXIS));
		buttonPain.add(transButton);
		buttonPain.add(okButton);
		
		gridbag.setConstraints(buttonPain, c);
		
		
		okButton.setAction(generateAction);
		
		ActionListener actionlistener = new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				Integer m = ((Integer) boxM.getSelectedItem()).intValue();
				Integer k = ((Integer) boxK.getSelectedItem()).intValue();
				Integer i = ((Integer) boxI.getSelectedItem()).intValue();
				if(m>=k && m>=i)
					okButton.setEnabled(true);
				else okButton.setEnabled(false);
			}
			
		};
		
		boxM.addActionListener(actionlistener);
		boxK.addActionListener(actionlistener);
		boxI.addActionListener(actionlistener);
	
		transButton.setAction(transformAction);
		
		okButton.setText("Code erzeugen");
		transButton.setText("Transformieren");
		pane.add(buttonPain);
		// pane.setBackground(background);
		add(new JScrollPane(pane), BorderLayout.CENTER);
		doLayout();
	}

	/**
	 * Renders the view.
	 * 
	 * @return success
	 */
	public boolean render() {
		return true;
	}
}
