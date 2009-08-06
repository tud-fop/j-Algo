package org.jalgo.module.lambda.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.html.HTMLEditorKit;

import org.jalgo.main.util.Messages;
import org.jalgo.module.lambda.ShortcutHandler;

public class WorkScreen  extends JPanel{

	/**
	 * builds the GUI for working 
	 */
	private static final long serialVersionUID = 7414195714028071904L;
	private ExchangeTextField inputTextField;
	
	private GridBagLayout gbl;
	private GridBagConstraints c;

	private JButton clearButton;
	private JButton doneButton;
	private JButton toListButton;
	
	private JScrollPane outputAreaScrollPane;
	private JEditorPane outputTextArea; 
	
	private JLabel scTitle1;
	private JList shortcutListField;
	private DefaultListModel model;
	private JScrollPane listScrollPane;
	private JButton insertButton;
	private JButton deleteButton;
	private JButton newSCButton;
	private JLabel scTitle2;
	private JTextField inputSCName;
	private JButton inputSCOKButton;
	private JButton inputSCCancelButton;

	private RenderLabel renderLabel;
	private JScrollPane renderScrollPane;
	
	private JLabel commentLine;
	
	private boolean scListVisible = true;

	public WorkScreen() {
		
		gbl = new GridBagLayout();
		setLayout(gbl);
		
		c = new GridBagConstraints();
		c.insets = new Insets(3,3,3,3);
		
		//special ComboBox for typing the LAMBDA
		inputTextField = new ExchangeTextField();
		inputTextField.setFont(GUIController.INPUT_FONT);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;		
		gbl.setConstraints(inputTextField, c);
		add(inputTextField);

		//buttons relating to inputField
		//clicked when inputterm has been typed
		doneButton = new JButton(Messages.getString("lambda","btxt.done"));	
		doneButton.setToolTipText(Messages.getString("lambda","bts.done"));
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		gbl.setConstraints(doneButton, c);
		add(doneButton);
				
		//clears the inputBox
		clearButton = new JButton(new ImageIcon(Messages.getResourceURL("lambda", "Icon.Clear")));	
		clearButton.setToolTipText(Messages.getString("lambda","bts.clear"));
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		gbl.setConstraints(clearButton, c);
		add(clearButton);

		//make the outputTextArea scrollable
		outputAreaScrollPane = new JScrollPane();
		outputTextArea = new JEditorPane();
		outputTextArea.setEditorKit(new HTMLEditorKit());
		outputTextArea.setEditable(false);
		outputTextArea.setFont(GUIController.TERM_FONT);
		outputAreaScrollPane.setViewportView(outputTextArea);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 3;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(outputAreaScrollPane, c);
		add(outputAreaScrollPane);

		//create title1
		scTitle1 = new JLabel(Messages.getString("lambda", "scDlg.ListLabel"));
		scTitle1.setVisible(scListVisible);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(scTitle1, c);
		add(scTitle1);

		//create the shortcutListField		
		model = new DefaultListModel();
		refreshListModel();
		shortcutListField = new JList(model);
		shortcutListField.setFont(GUIController.INPUT_FONT);
		listScrollPane = new JScrollPane();
		listScrollPane.setViewportView(shortcutListField);
		listScrollPane.setVisible(scListVisible);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(listScrollPane, c);
		add(listScrollPane);
		
		//create buttons
		insertButton = new JButton(Messages.getString("lambda", "scDlg.insBtn"));
		insertButton.setToolTipText(Messages.getString("lambda","bts.insertSC"));
		insertButton.setVisible(scListVisible);
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		gbl.setConstraints(insertButton, c);
		add(insertButton);
		
		deleteButton = new JButton(Messages.getString("lambda", "scDlg.delBtn"));
		deleteButton.setToolTipText(Messages.getString("lambda","bts.deleteSC"));
		deleteButton.setVisible(scListVisible);
		deleteButton.setEnabled(false);
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		gbl.setConstraints(deleteButton, c);
		add(deleteButton);
		
		newSCButton = new JButton(Messages.getString("lambda", "scDlg.newBtn"));
		newSCButton.setToolTipText(Messages.getString("lambda","bts.newSC"));
		newSCButton.setVisible(scListVisible);
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		gbl.setConstraints(newSCButton, c);
		add(newSCButton);

		//create title2
		scTitle2 = new JLabel(Messages.getString("lambda", "scDlg.ListLabel2"));
		scTitle2.setVisible(!scListVisible);
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(scTitle2, c);
		add(scTitle2);

		//inputField for names of shortcuts
		inputSCName = new JTextField();
		inputSCName.setEditable(true);
		inputSCName.setFont(GUIController.INPUT_FONT);
		inputSCName.setVisible(!scListVisible);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;		
		gbl.setConstraints(inputSCName, c);
		add(inputSCName);

		//button for confirm shortcut naming
		inputSCOKButton = new JButton("OK");
		inputSCOKButton.setVisible(!scListVisible);
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(inputSCOKButton, c);
		add(inputSCOKButton);
		
		//button for cancel shortcut naming
		inputSCCancelButton = new JButton("X");
		inputSCCancelButton.setVisible(!scListVisible);
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(inputSCCancelButton, c);
		add(inputSCCancelButton);

		//setup the renderLabel		
		renderLabel = new RenderLabel();
		renderLabel.setOpaque(true);
		renderLabel.setMinimumSize(new Dimension(0, 60));
		renderLabel.setBackground(Color.white);
		renderLabel.setForeground(Color.white);
		renderLabel.setVisible(true);		
		renderScrollPane = new JScrollPane();
		renderScrollPane.setViewportView(renderLabel);
		renderScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		renderScrollPane.setMinimumSize(new Dimension(0, 60));
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(renderScrollPane, c);
		add(renderScrollPane);

		//create a comment line for messages and states		
		commentLine = new JLabel(" ");
		commentLine.setOpaque(true);
		commentLine.setBackground(Color.white);
		commentLine.setFont(GUIController.INPUT_FONT);
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		gbl.setConstraints(commentLine, c);
		add(commentLine);
		
		validate();
	}
	
	/**
	 * updates the Model of the shortcutList
	 */
	public void refreshListModel() {
		model.clear();
		//add listentries from ShortcutHandler
		Set<String> shortcutSet = ShortcutHandler.getInstance().getShortcutList();
		for(String s : shortcutSet) {
			if (s.length() > 0) model.addElement(s);
		}
	}
	
	/**
	 * switch the shortcutPanel to creation-view
	 * and back to list-view
	 */
	public void changeVisibility() {
		scListVisible = !scListVisible;

		scTitle1.setVisible(scListVisible);
		listScrollPane.setVisible(scListVisible);
		insertButton.setVisible(scListVisible);
		deleteButton.setVisible(scListVisible);
		newSCButton.setVisible(scListVisible);

		scTitle2.setVisible(!scListVisible);
		inputSCName.setVisible(!scListVisible);
		inputSCOKButton.setVisible(!scListVisible);
		inputSCCancelButton.setVisible(!scListVisible);
	}

	public JTextField getInputSCName() {
		return inputSCName;
	}

	public JButton getInputSCOKButton() {
		return inputSCOKButton;
	}
	
	public JButton getInputSCCancelButton() {
		return inputSCCancelButton;
	}
	public ExchangeTextField getInputTextField() {
		return inputTextField;
	}

	public RenderLabel getRenderLabel() {
		return renderLabel;
	}

	public JEditorPane getOutputTextArea() {
		return outputTextArea;
	}

	public JLabel getCommentLine() {
		return commentLine;
	}

	public JList getShortcutListField() {
		return shortcutListField;
	}

	public JButton getClearButton() {
		return clearButton;
	}

	public JButton getDoneButton() {
		return doneButton;
	}

	public JButton getToListButton() {
		return toListButton;
	}

	public JButton getInsertButton() {
		return insertButton;
	}

	public JButton getDeleteButton() {
		return deleteButton;
	}

	public JButton getNewSCButton() {
		return newSCButton;
	}		
}
