package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.avl.algorithm.UpdateBalance;
import org.jalgo.module.hoare.gui.GuiControl;
import org.jalgo.module.hoare.gui.WelcomeScreen;
import org.jalgo.module.hoare.gui.WelcomeScreenButton;

/**
 * This is the <code>WelcomeScreenListener</code> which is responsible for
 * actions happening in the welcome screen.
 * 
 * @author Markus
 */
public class WelcomeScreenListener implements ActionListener, MouseListener,
		WindowFocusListener {
	private GuiControl gui;

	private WelcomeScreen welcomescreen;

	/**
	 * The constructor of the <code>PhaseOneScreenListener</code>.
	 * 
	 * @param gc
	 *            the <code>GUIController</code> instance
	 * @param ws
	 *            the <code>WelcomeScreen</code> instance
	 */
	public WelcomeScreenListener(GuiControl gui, WelcomeScreen ws) {
		this.gui = gui;
		welcomescreen = ws;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("openC0")) {
			JFileChooser fc = new JFileChooser(System.getProperty("user.dir")
					+ System.getProperty("file.separator") + "examples"
					+ System.getProperty("file.separator") + "hoare");
			fc.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					if (f.getName().endsWith("c"))
						return true;
					if (f.isDirectory())
						return true;
					return false;
				}

				@Override
				public String getDescription() {
					return "*.c";
				}

			});
			if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				if (gui.init(fc.getSelectedFile())) // parsing succeeded
					gui.installWorkScreen();
			}
		} else if (e.getActionCommand().equals("newC0")) {
			gui.installWorkScreen();
			gui.init();
		} else if (e.getActionCommand().equals("openTree")) {
			JAlgoGUIConnector.getInstance().showOpenDialog(true, true);
		}
	}

	public void mouseClicked(MouseEvent e) {

	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof WelcomeScreenButton) {
			WelcomeScreenButton source = (WelcomeScreenButton) e.getSource();
			if (!source.isEnabled())
				return;
			source.setSelected(true);
			welcomescreen.setDescription(source.getDescription());
		}
	}

	/**
	 * Causes the event source button to be displayed normally and to remove the
	 * description string from the screen.
	 */
	public void mouseExited(MouseEvent e) {
		if (e.getSource() instanceof WelcomeScreenButton) {
			((WelcomeScreenButton) e.getSource()).setSelected(false);
			welcomescreen.setDescription(null);
		}
	}

	public void windowLostFocus(WindowEvent e) {
		e.getComponent().requestFocus();
	}

	public void windowGainedFocus(WindowEvent e) {

	}
}
