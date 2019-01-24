package org.jalgo.module.em.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.module.em.control.Controller;
import org.jalgo.module.em.control.IOController;
import org.jalgo.module.em.data.StartParameters;

/**
 * Class <code>StartScreen</code> represents a screen with two buttons, where
 * the user can choose which action he/she wants to perform. The buttons have a
 * roll-over effect and additionally a description of the selected action is
 * displayed.
 * 
 * @author Tobias Nett
 */
public class StartScreen extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9085830968372916356L;

	private static final Color START_SCREEN_BACKGROUND = new Color(0x00, 0x86,
			0x8b);

	private StartScreenActionHandler actionhandler;
	private Controller controller;
	private IOController ioController;

	// components
	private StartButton loadButton;
	private StartButton startButton;
	private JLabel descriptionLabel;
	private StartParameters startParameters;

	/**
	 * Creates a <code>StartScreen</code> object with the specified controllers.
	 * 
	 * @param controller
	 *            the module's main controller
	 * @param ioController
	 *            the controller for load/save actions
	 * @param startParameters
	 *            {@code StartParameters} object for the experiment
	 * 
	 */
	public StartScreen(Controller controller, final IOController ioController,
			StartParameters startParameters) {
		this.startParameters = startParameters;
		this.controller = controller;
		this.ioController = ioController;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		actionhandler = new StartScreenActionHandler();

		loadButton = new StartButton(new ImageIcon(Messages.getResourceURL(
				"em", "StartScreen.load")), new ImageIcon(
				Messages.getResourceURL("em", "StartScreen.load.rollover")),
				Messages.getString("em", "StartScreen.load.description"),
				"load", actionhandler);

		startButton = new StartButton(new ImageIcon(Messages.getResourceURL(
				"em", "StartScreen.start")), new ImageIcon(
				Messages.getResourceURL("em", "StartScreen.start.rollover")),
				Messages.getString("em", "StartScreen.start.description"),
				"start", actionhandler);

		descriptionLabel = new JLabel();
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(START_SCREEN_BACKGROUND);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(startButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(loadButton);

		add(Box.createVerticalStrut(150));
		add(buttonPane);
		add(Box.createVerticalStrut(50));
		add(descriptionLabel);
	}

	/**
	 * Sets the description label's text to the given string and updates the UI.
	 * 
	 * @param desc
	 *            the action's textual description
	 */
	public void setDescription(String desc) {
		descriptionLabel.setText(desc);
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
		loadButton.setEnabled(b);
		startButton.setEnabled(b);
		if (!b)
			setDescription(null);
	}

	/**
	 * Draws the background of this panel.
	 * <p>
	 * This method is a workaround for Linux (GTK), because
	 * <code>setBackground(..)</code> does not work correctly.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(START_SCREEN_BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * The {@code StartScreenActionHandler} listens for mouse and action events
	 * caused by components of the {@code StartScreen} class.
	 * 
	 * @author Tobias Nett
	 * 
	 */
	class StartScreenActionHandler implements ActionListener, MouseListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("load")) {
				startParameters = ioController.read();
				if (startParameters != null) {
					controller.setStartParameters(startParameters);
					controller.input();
				}
			} else if (actionCommand.equals("start")) { //$NON-NLS-1$
				controller.input();
			}
			// deselect the button and remove the description
			((StartButton) e.getSource()).setSelected(false);
			setDescription(null);
		}

		/**
		 * Causes the event source button to highlight and to display its
		 * description string on screen.
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			StartButton source = (StartButton) e.getSource();
			if (!source.isEnabled())
				return;
			source.setSelected(true);
			setDescription(source.getDescription());
		}

		/**
		 * Causes the event source button to be displayed normally and to remove
		 * the description string from the screen.
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			((StartButton) e.getSource()).setSelected(false);
			setDescription(null);
		}

		/**
		 * This method has no effect.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			// This method has no effect
		}

		/**
		 * This method has no effect.
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			// This method has no effect
		}

		/**
		 * This method has no effect.
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			// This method has no effect
		}
	}
}
