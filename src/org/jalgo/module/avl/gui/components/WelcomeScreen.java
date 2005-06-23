/* Created on 25.05.2005 */
package org.jalgo.module.avl.gui.components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.event.WelcomeScreenActionHandler;

/**
 * Class <code>WelcomeScreen</code> represents a screen with three buttons, where
 * the user can choose what to do. The buttons are rollover enabled and a
 * description of the selected task is displayed.
 * 
 * @author Alexander Claus
 */
public class WelcomeScreen
extends JPanel
implements GUIConstants {

	private WelcomeScreenActionHandler action;

	//components
	private WelcomeButton loadButton;
	private WelcomeButton manualButton;
	private WelcomeButton randomButton;
	private JLabel descriptionLabel;

	/**
	 * Constructs a <code>WelcomeScreen</code> object with the given reference.
	 * 
	 * @param gui the <code>GUIController</code> instance
	 */
	public WelcomeScreen(GUIController gui) {
		action = new WelcomeScreenActionHandler(gui, this);
		
		setBackground(WELCOME_SCREEN_BACKGROUND);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        loadButton = new WelcomeButton(
        	new ImageIcon("pix/avl/welcome_load.gif"),
        	new ImageIcon("pix/avl/welcome_load_rollover.gif"),
        	new ImageIcon("pix/avl/welcome_load_desc.gif"),
			"load", action);
		manualButton = new WelcomeButton(
            new ImageIcon("pix/avl/welcome_manual.gif"),
        	new ImageIcon("pix/avl/welcome_manual_rollover.gif"),
        	new ImageIcon("pix/avl/welcome_manual_desc.gif"),
			"createManually", action);
		randomButton = new WelcomeButton(
           	new ImageIcon("pix/avl/welcome_random.gif"),
        	new ImageIcon("pix/avl/welcome_random_rollover.gif"),
        	new ImageIcon("pix/avl/welcome_random_desc.gif"),
			"createRandomly", action);
//		TODO: enable this, when switching to plugin structure
/*        loadButton = new WelcomeButton(
	        	new ImageIcon(
					getClass().getResource("/pix/avl/welcome_load.gif")),
	        	new ImageIcon(
					getClass().getResource("/pix/avl/welcome_load_rollover.gif")),
	        	new ImageIcon(
					getClass().getResource("/pix/avl/welcome_load_desc.gif")),
				"load", action);
			manualButton = new WelcomeButton(
	            new ImageIcon(
					getClass().getResource("/pix/avl/welcome_manual.gif")),
	        	new ImageIcon(
					getClass().getResource("/pix/avl/welcome_manual_rollover.gif")),
	        	new ImageIcon(
					getClass().getResource("/pix/avl/welcome_manual_desc.gif")),
				"createManually", action);
			randomButton = new WelcomeButton(
	           	new ImageIcon(
					getClass().getResource("/pix/avl/welcome_random.gif")),
	        	new ImageIcon(
					getClass().getResource("/pix/avl/welcome_random_rollover.gif")),
	        	new ImageIcon(
					getClass().getResource("/pix/avl/welcome_random_desc.gif")),
				"createRandomly", action);
*/
		descriptionLabel = new JLabel();
		descriptionLabel.setMaximumSize(new Dimension(800, 30));
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setBackground(WELCOME_SCREEN_BACKGROUND);
        descriptionLabel.setFont(GUIConstants.WELCOME_SCREEN_FONT);
        descriptionLabel.setForeground(WELCOME_SCREEN_FOREGROUND);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(WELCOME_SCREEN_BACKGROUND);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(loadButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20,0)));
		buttonPane.add(manualButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20,0)));
		buttonPane.add(randomButton);

		add(Box.createVerticalStrut(150));
		add(buttonPane);
		add(Box.createVerticalStrut(50));
		add(descriptionLabel);
	}

	/**
	 * Displays the given string description on the screen.
	 * 
	 * @param desc the task description string
	 */
	public void setDescription(Icon desc) {
		descriptionLabel.setIcon(desc);
		descriptionLabel.updateUI();
	}

	/**
	 * Sets the enabled status of the buttons. If the given value is
	 * <code>false</code>, also the description string is removed from the screen.
	 * 
	 * @param b <code>true</code>, if the buttons should be enabled,
	 * 			<code>false</code> otherwise
	 */
	public void setButtonsEnabled(boolean b) {
		loadButton.setEnabled(b);
		manualButton.setEnabled(b);
		randomButton.setEnabled(b);
		if (!b) setDescription(null);
	}
}