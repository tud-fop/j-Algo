package org.jalgo.module.kmp.gui.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;
import org.jalgo.module.kmp.gui.component.WelcomeButton;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.module.kmp.gui.GUIController;
import org.jalgo.module.kmp.gui.event.WelcomeScreenListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class <code>WelcomeScreen</code> represents a screen with four buttons,
 * where the user can choose what to do. The buttons are rollover enabled and a
 * description of the selected task is displayed.
 * 
 * @author Danilo Lisske
 */
public class WelcomeScreen extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1152679735426768226L;

	private WelcomeScreenListener listener;
	
	private WelcomeButton phaseoneButton;
	private WelcomeButton phasetwoButton;
	private WelcomeButton openButton;
	private WelcomeButton exampleButton;
	private JLabel descriptionLabel;

	/**
	 * Constructs a <code>WelcomeScreen</code> object with the given
	 * reference.
	 * 
	 * @param gui the <code>GUIController</code> instance
	 */
	public WelcomeScreen(GUIController gui) {
		listener = new WelcomeScreenListener(gui, this);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		String lang = Settings.getString("main", "Language");
		if (!lang.equals("de")) lang = "en";
		phaseoneButton = new WelcomeButton(
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_phaseone")),
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_phaseone_rollover")),
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_phaseone_description_"+lang)),
			"startPhaseOne", listener);
		phasetwoButton = new WelcomeButton(
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_phasetwo")),
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_phasetwo_rollover")),
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_phasetwo_description_"+lang)),
			"startPhaseTwo", listener);
		openButton = new WelcomeButton(
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_open")),
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_open_rollover")),
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_open_description_"+lang)),
			"openFile", listener);
		exampleButton = new WelcomeButton(
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_example")),
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_example_rollover")),
			new ImageIcon(
				Messages.getResourceURL("kmp", "Welcome_example_description_"+lang)),
			"loadExample", listener);

		descriptionLabel = new JLabel();
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(WELCOME_SCREEN_BACKGROUND);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(phaseoneButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(phasetwoButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(openButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(exampleButton);

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
	 * <code>false</code>, also the description string is removed from the
	 * screen.
	 * 
	 * @param b <code>true</code>, if the buttons should be enabled,
	 *            <code>false</code> otherwise
	 */
	public void setButtonsEnabled(boolean b) {
		phaseoneButton.setEnabled(b);
		phasetwoButton.setEnabled(b);
		openButton.setEnabled(b);
		exampleButton.setEnabled(b);
		if (!b) setDescription(null);
	}

	/**
	 * Draws the background with a beautyful color ;o). Normally there would be
	 * the background color set with <code>setBackground(..)</code>, but, under
	 * linux (GTK) this has no effect. So this is a workaround...
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(WELCOME_SCREEN_BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
