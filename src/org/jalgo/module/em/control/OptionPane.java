package org.jalgo.module.em.control;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.gui.input.InputStringParser;

/**
 * An option window to change the settings
 * to limit precalculations.
 * 
 * @author Christian Hendel
 */
public class OptionPane extends JFrame implements FocusListener {
	
	private static final String DEFAULT = "1,5";
	
	private static final long serialVersionUID = 5698661714725081879L;
	private double limit = -1;
	private JLabel label1 = new JLabel(Messages.getString("em", "Options.limitlabel"));
	private JTextField inpLimit = new JTextField(DEFAULT);
	private GridBagLayout layout = new GridBagLayout();
	private GridBagConstraints c= new GridBagConstraints();
	private JButton ok = new JButton("OK");
	private JButton cancel = new JButton(Messages.getString("em", "Options.c"));
	private ActionListener lok,lc;
	private Controller cont;
	private JLabel stepLabel;
	
	/**
	 * this Constructor just initilizes the BoxbagLayout and one actionlistener
	 * 
	 * @param cont Controller for the module	
	 */
	public OptionPane(Controller cont, JLabel stepLabel){
		this.cont=cont;
		this.stepLabel = stepLabel;
		lc = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zu();
			}
		};
		lok = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ok();
			}
		};
		inpLimit.addFocusListener(this);
		inpLimit.addActionListener(lok);
		
		ok.addActionListener(lok);
		cancel.addActionListener(lc);
		this.setLayout(layout);
		c.gridx=0;
		c.gridy=0;
		c.weightx=0.75;
		this.add(label1,c);
		c.gridx=2;
		c.gridy=0;
		c.weightx=0.25;
		this.add(inpLimit,c);
		inpLimit.setPreferredSize(new Dimension(120, inpLimit.getPreferredSize().height));
		inpLimit.setMinimumSize(inpLimit.getPreferredSize());
		c.gridy=1;
		c.gridx=2;
		this.add(ok,c);
		c.gridy=1;
		c.gridx=0;
		this.add(cancel,c);
		
	}
	/**
	 * this method is used when the OK button is pressed.
	 * It converts inputed text to a usable float
	 * and catches errors that occur when
	 * input is not usable
	 */
	public void ok(){
		try{
			limit=(InputStringParser.parseInputString(inpLimit.getText(), 0, 100)) / 100;
			cont.setLimit(limit);
			cont.getLogState().calcUntilLimit(limit);
			cont.getMC().updateViews();
			stepLabel.setText(cont.getLogState().getStepCount()+"."+cont.getLogState().getSingleStepCount());
			close();
		}
		catch (IllegalArgumentException e){
			JOptionPane.showMessageDialog(null,Messages.getString("em", "Options.ex1"),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void close(){
		if (!(limit < 0)){
			this.setVisible(false);
		}

	}
	
	public void focusGained(FocusEvent foc){
		inpLimit.selectAll();
	}
	
	public void focusLost(FocusEvent foc) {
	}
	
	public void zu(){this.setVisible(false);}
}
