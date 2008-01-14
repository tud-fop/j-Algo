package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.gui.GuiControl;

/**
 * The class <code>BeamerModeAction</code> defines a checkbox menuitem for
 * switching between beamer mode and pc mode.<br>
 * Here the Singleton design pattern is implemented, in order that this setting
 * takes global effect for all open instances of the KMP module.<br>
 * 
 * @author Markus
 */
public class BeamerMode extends JCheckBoxMenuItem implements ActionListener {
	private static final long serialVersionUID = 5523818288828234644L;

	private static BeamerMode instance;

	private static GuiControl gui;

	/**
	 * Constructs the singleton instance of <code>BeamerModeAction</code>.
	 */
	private BeamerMode() {
		super(Messages.getString("hoare", "Beamer_mode"), new ImageIcon(
				Messages.getResourceURL("main", "Icon.Beamer_mode")), false);
		this.setState(false);
		addActionListener(this);
	}

	/**
	 * Retrieves and, if necessary, initializes the singleton instance of
	 * <code>BeamerModeAction</code>.
	 * 
	 * @param gc
	 *            the <code>GUIController</code>
	 * 
	 * @return the singleton instance
	 */
	public static BeamerMode getInstance(GuiControl gc) {
		gui = gc;
		if (instance == null)
			instance = new BeamerMode();
		return instance;
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		gui.setBeamer(isSelected());
	}
}