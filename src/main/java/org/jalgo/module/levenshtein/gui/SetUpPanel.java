package org.jalgo.module.levenshtein.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.jalgo.main.gui.JAlgoWindow;
import org.jalgo.main.util.Messages;
import org.jalgo.module.levenshtein.Consts;

public class SetUpPanel extends JPanel {

	
	private JAlgoWindow appWin;
	
	private JLabel lblSource;
	private JTextField inputSource;
	private JLabel lblTarget;
	private JTextField inputTarget;
	
	private JLabel lblCostFunction;
	private JLabel lblDeletion;
	private JTextField inputDeletion;
	private JLabel lblInsertion;
	private JTextField inputInsertion;
	private JLabel lblSubstitution;
	private JTextField inputSubstitution;
	private JLabel lblIdentity;
	private JTextField inputIdentity;
	
	private JButton btnAbort;
	private JButton btnStart;

	private int hgap = 0;
	private int vgap = 0;
	
	private int columnsWord = 20;
	private int columnsCostFunction = 5;
	
	public void init() {
		
		setLayout(new BorderLayout());
		
		JPanel contentPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// init the Components of the View
		lblSource = new JLabel(getString("input.source"));
		lblTarget = new JLabel(getString("input.target"));
		lblCostFunction = new JLabel(getString("input.costfunction"));
		lblDeletion = new JLabel(getString("input.deletion"));
		lblInsertion = new JLabel(getString("input.insertion"));
		lblSubstitution = new JLabel(getString("input.substitution"));
		lblIdentity = new JLabel(getString("input.identity"));
		
		inputSource = new JTextField(columnsWord);
		inputTarget = new JTextField(columnsWord);
		inputDeletion = new JTextField(
				Integer.toString(Consts.deletionCosts), columnsCostFunction);
		inputInsertion = new JTextField(
				Integer.toString(Consts.insertionCosts), columnsCostFunction);
		inputSubstitution = new JTextField(
				Integer.toString(Consts.substitutionCosts), columnsCostFunction);
		inputIdentity = new JTextField(
				Integer.toString(Consts.identityCosts), columnsCostFunction);
		
		btnAbort = new JButton(getString("button.abort"));
		btnStart = new JButton(getString("button.start"));
		
		// create a component for the source word label and input field
		JPanel sourcePanel = new JPanel(new BorderLayout(hgap,vgap));
		sourcePanel.add(lblSource, BorderLayout.WEST);
		sourcePanel.add(inputSource, BorderLayout.EAST);
		
		// add the component to the GridBagLayout as first component
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		contentPanel.add(sourcePanel, c);
		
		// create a component for the target word label and input field
		JPanel targetPanel = new JPanel(new BorderLayout(hgap,vgap));
		targetPanel.add(lblTarget, BorderLayout.WEST);
		targetPanel.add(inputTarget, BorderLayout.EAST);
		
		// add the component to the GridBagLayout as second component
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		contentPanel.add(targetPanel, c);
		
		// add the cost function label as third component to the GridBagLayout
		// here some padding to the top is added
		// TODO: create another panel for the label and move the label to the bottom
		Insets paddInsets1 = new Insets(20, 0, 0, 0);
		Insets origInsets1 = c.insets;
		c.insets = paddInsets1;
		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		contentPanel.add(lblCostFunction, c);
		c.insets = origInsets1;
		
		// create a new panel for the cost function input fields
		JPanel costInputs = new JPanel(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		
		// create a component for the deletion label and input field
//		JPanel deletionInput = new JPanel(new BorderLayout(hgap,vgap));
//		deletionInput.add(lblDeletion, BorderLayout.WEST);
//		deletionInput.add(inputDeletion, BorderLayout.EAST);
		
		// add the deletion label
		c1.gridx = 0;
		c1.gridy = 0;
		costInputs.add(lblDeletion, c1);
		
		// create a component for the insertion label and input field
//		JPanel insertionInput = new JPanel(new BorderLayout(hgap,vgap));
//		insertionInput.add(lblInsertion, BorderLayout.WEST);
//		insertionInput.add(inputInsertion, BorderLayout.EAST);
		
		// add the component as top right component to the function input fields
		Insets paddInsets = new Insets(0, 0, 0, 20);
		Insets origInsets = c1.insets;
		c1.insets = paddInsets;
		c1.gridx = 1;
		c1.gridy = 0;
		costInputs.add(inputDeletion, c1);
		c1.insets = origInsets;
		
		c1.gridx = 2;
		c1.gridy = 0;
		costInputs.add(lblInsertion, c1);
		
		c1.gridx = 3;
		c1.gridy = 0;
		costInputs.add(inputInsertion, c1);
		
		
		c1.gridx = 0;
		c1.gridy = 1;
		costInputs.add(lblSubstitution, c1);
		
		c1.insets = paddInsets;
		c1.gridx = 1;
		c1.gridy = 1;
		costInputs.add(inputSubstitution, c1);
		c1.insets = origInsets;
		
		c1.gridx = 2;
		c1.gridy = 1;
		costInputs.add(lblIdentity, c1);
		
		c1.gridx = 3;
		c1.gridy = 1;
		costInputs.add(inputIdentity, c1);
		
		
		// create a component for the substitution label and input field
//		JPanel substitutionInput = new JPanel(new BorderLayout(hgap,vgap));
//		substitutionInput.add(lblSubstitution, BorderLayout.WEST);
//		substitutionInput.add(inputSubstitution, BorderLayout.EAST);
//		
//		// add the component in the bottom left corner
//		c1.gridx = 0;
//		c1.gridy = 1;
//		c1.weightx = 1;
//		c1.ipadx = 20;
//		c1.fill = GridBagConstraints.HORIZONTAL;
//		costInputs.add(substitutionInput, c1);
//		
//		// create a component for the identity label and input field
//		JPanel identityInput = new JPanel(new BorderLayout(hgap,vgap));
//		identityInput.add(lblIdentity, BorderLayout.WEST);
//		identityInput.add(inputIdentity, BorderLayout.EAST);
//		
//		// add the component in the bottom right corner
//		c1.gridx = 1;
//		c1.gridy = 1;
//		c1.weightx = 1;
//		c1.ipadx = 20;
//		c1.fill = GridBagConstraints.HORIZONTAL;
//		costInputs.add(identityInput, c1);
//		
		// add all the cost function input fields as fourth component to the
		// content GridBagLayout
		c.gridx = 0;
		c.gridy = 3;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		contentPanel.add(costInputs, c);
		
		// create a component for the buttons
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(btnAbort, BorderLayout.WEST);
		buttonPanel.add(btnStart, BorderLayout.EAST);
		
		// add the button panel to fifth component
		c.gridx = 0;
		c.gridy = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		contentPanel.add(buttonPanel, c);
		
		
		
		// add the GridBagLayout to the contentPane centered.
		add(contentPanel, BorderLayout.CENTER);

		
	}
	
	private static String getString(String key) {
		return Messages.getString("levenshtein", key);
	}
}
