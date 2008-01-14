package org.jalgo.module.hoare.gui;

/**
 * The Welcome Screen
 * 
 * @author Markus
 *
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;
import org.jalgo.module.hoare.gui.actions.WelcomeScreenListener;

/**
 * Class <code>WelcomeScreen</code> represents a screen with three buttons,
 * where the user can choose what to do. The buttons are rollover enabled and a
 * description of the selected task is displayed.
 * 
 * @author Markus
 */
public class WelcomeScreen extends JPanel implements GUIConstants {
	private static final long serialVersionUID = 1152679735426768226L;

	private WelcomeScreenListener listener;

	private WelcomeScreenButton loadC0;

	private WelcomeScreenButton newC0;

	private WelcomeScreenButton loadTree;

	private JLabel descriptionLabel;

	/**
	 * Constructs a <code>WelcomeScreen</code> object with the given
	 * reference.
	 * 
	 * @param gui
	 *            the <code>GUIController</code> instance
	 */
	public WelcomeScreen(final GuiControl gui) {
		listener = new WelcomeScreenListener(gui, this);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		String lang = Settings.getString("main", "Language");
		if (!lang.equals("de"))
			lang = "en";
		loadC0 = new WelcomeScreenButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.openC0Welcome0")), new ImageIcon(Messages
				.getResourceURL("hoare", "icon.openC0Welcome1")),
				new ImageIcon(Messages.getResourceURL("hoare",
						"icon.openC0WelcomeDesc_" + lang)), "openC0", listener);

		newC0 = new WelcomeScreenButton(new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.newWelcome0")), new ImageIcon(Messages
				.getResourceURL("hoare", "icon.newWelcome1")),
				new ImageIcon(Messages.getResourceURL("hoare",
						"icon.newWelcomeDesc_" + lang)), "newC0", listener);

		loadTree = new WelcomeScreenButton(new ImageIcon(Messages
				.getResourceURL("hoare", "icon.openWelcome0")), new ImageIcon(
				Messages.getResourceURL("hoare", "icon.openWelcome1")),
				new ImageIcon(Messages.getResourceURL("hoare",
						"icon.openWelcomeDesc_" + lang)), "openTree", listener);

		descriptionLabel = new JLabel();
		descriptionLabel.setText("    ");
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(WELCOME_BACKGROUND);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(loadC0);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(newC0);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(loadTree);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));

		add(Box.createVerticalStrut(150));
		add(buttonPane);
		add(Box.createVerticalStrut(50));
		add(descriptionLabel);
	}

	/**
	 * Displays the given string description on the screen.
	 * 
	 * @param desc
	 *            the task description string
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
	 * @param b
	 *            <code>true</code>, if the buttons should be enabled,
	 *            <code>false</code> otherwise
	 */
	public void setButtonsEnabled(boolean b) {
		loadC0.setEnabled(b);
		newC0.setEnabled(b);
		if (!b)
			setDescription(null);
	}

	/**
	 * Draws the background with a beautyful color ;o). Normally there would be
	 * the background color set with <code>setBackground(..)</code>, but,
	 * under linux (GTK) this has no effect. So this is a workaround...
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(WELCOME_BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
