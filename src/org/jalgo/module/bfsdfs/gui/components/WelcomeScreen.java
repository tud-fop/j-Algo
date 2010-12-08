package org.jalgo.module.bfsdfs.gui.components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;
import org.jalgo.module.bfsdfs.ModuleConnector;
import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.event.WelcomeScreenActionHandler;

/**
 * This class represents the welcome screen of this module. It contains three
 * buttons to create a new graph, load a graph from an existing file and load
 * the last opened graph. Furthermore it contains a description label that
 * indicated the use of every button.
 * 
 * @author Florian Dornbusch
 */
public class WelcomeScreen
extends JPanel
implements GUIConstants {
	private static final long serialVersionUID = 8620144727183351679L;

	
	private WelcomeScreenActionHandler action;

	// components
	private WelcomeButton newButton;
	private WelcomeButton loadButton;
	private WelcomeButton lastButton;
	private JLabel descriptionLabel;
	private JPanel buttonPane;
	
	public WelcomeScreen(GUIController guiController) {
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(WELCOME_SCREEN_BACKGROUND);
		
		action = new WelcomeScreenActionHandler(this, guiController);
		
		// create buttons
		String lang = Settings.getString("main", "Language");
		if (!lang.equals("de")) lang = "en";
		newButton = new WelcomeButton(
				new ImageIcon(Messages.getResourceURL("bfsdfs",
						"WelcomeScreen.newButton")),
				new ImageIcon(Messages.getResourceURL("bfsdfs",
						"WelcomeScreen.newButton_over")),
				new ImageIcon(Messages.getResourceURL("bfsdfs",
						"WelcomeScreen.newButton_descr_"+lang)),
				"new", action);
		loadButton = new WelcomeButton(
				new ImageIcon(Messages.getResourceURL("bfsdfs",
						"WelcomeScreen.loadButton")),
				new ImageIcon(Messages.getResourceURL("bfsdfs",
						"WelcomeScreen.loadButton_over")),
				new ImageIcon(Messages.getResourceURL("bfsdfs",
						"WelcomeScreen.loadButton_descr_"+lang)),
				"load", action);
		lastButton = new WelcomeButton(
				new ImageIcon(Messages.getResourceURL("bfsdfs",
						"WelcomeScreen.lastButton")),
				new ImageIcon(Messages.getResourceURL("bfsdfs",
						"WelcomeScreen.lastButton_over")),
				new ImageIcon(Messages.getResourceURL("bfsdfs",
						"WelcomeScreen.lastButton_descr_"+lang)),
				"last", action);
		
		// set up the button pane
		buttonPane = new JPanel();
		buttonPane.setBackground(WELCOME_SCREEN_BACKGROUND);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(newButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(loadButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(lastButton);
		
		// hide "lastButton" if it is not needed
		lastButton.setVisible(ModuleConnector.islastOpenedFileKnown());
		
		// set up the description label
		descriptionLabel = new JLabel();
		descriptionLabel.setPreferredSize(new Dimension(0,25));
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// add the main components to the welcome screen
		add(Box.createVerticalGlue());
		add(buttonPane);
		add(Box.createVerticalStrut(50));
		add(descriptionLabel);
		add(Box.createVerticalGlue());
	}
	
	/**
	 * Changes the description of each button dynamically.
	 * @author Florian Dornbusch
	 */
	public void setDescription(Icon desc) {
		descriptionLabel.setIcon(desc);
	}	
}