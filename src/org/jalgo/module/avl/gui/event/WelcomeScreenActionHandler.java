/* Created on 15.04.2005 */
package org.jalgo.module.avl.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.components.WelcomeButton;
import org.jalgo.module.avl.gui.components.WelcomeScreen;

/**
 * The class <code>WelcomeScreenActionHandler</code> is an event handler for the
 * <code>WelcomeScreen</code> class. It handles button clicks and mouse events for
 * rollover mechanism.
 * 
 * @author Alexander Claus
 */
public class WelcomeScreenActionHandler
implements ActionListener, MouseListener {
	
	private GUIController gui;
	private WelcomeScreen screen;
	
	/**
	 * Constructs a <code>WelcomeScreenActionHandler</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance
	 * @param screen the <code>WelcomeScreen</code> instance, for which this
	 * 		  event handler is used
	 */
	public WelcomeScreenActionHandler(GUIController gui, WelcomeScreen screen){
		this.gui = gui;
		this.screen = screen;
	}

	/**
	 * Handles button clicks.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("load")) gui.showOpenDialog();
		else if (e.getActionCommand().equals("createManually")) {
			gui.setAVLMode(true, true);
			gui.installStandardLayout();
		}
		else if (e.getActionCommand().equals("createRandomly")) {
			screen.setButtonsEnabled(false);
			gui.showRandomGenerationDialog();
			screen.setButtonsEnabled(true);
		}

		((WelcomeButton)e.getSource()).setSelected(false);
		screen.setDescription(null);
	}

	/**
	 * Causes the event source button to highlight and to display its description
	 * string on screen.
	 */
	public void mouseEntered(MouseEvent e) {
		WelcomeButton source = (WelcomeButton)e.getSource();
		if (!source.isEnabled()) return;
		source.setSelected(true);
		screen.setDescription(source.getDescription());
	}

	/**
	 * Causes the event source button to be displayed normally and to remove the
	 * description string from the screen.
	 */
	public void mouseExited(MouseEvent e) {
		((WelcomeButton)e.getSource()).setSelected(false);
		screen.setDescription(null);
	}

	/**
	 * This method has no effect.
	 */
	public void mouseClicked(MouseEvent e) {}

	/**
	 * This method has no effect.
	 */
	public void mousePressed(MouseEvent e) {}

	/**
	 * This method has no effect.
	 */
	public void mouseReleased(MouseEvent e) {}
}