package org.jalgo.module.c0h0.views;

import javax.swing.*;

import org.jalgo.main.util.Messages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jalgo.module.c0h0.controller.ButtonCommand;
import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.controller.ViewManager;

/**
 * Presents J-Algo-typical 3 Button welcomming Screen
 * 
 * Codedatei Laden - Beispiel Auswählen - Neu schreiben
 * 
 * @author hendrik
 * 
 * 
 */
public class WelcomeView extends View implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private static final Color foreground = Color.white;
	private static final Color background = new Color(115, 0, 13);

	private Controller controller;
	public JLabel tipLine;

	private JToggleButton loadButton;
	private ImageIcon loadButtonIcon;
	private JToggleButton exampleButton;
	private ImageIcon exampleButtonIcon;
	private JToggleButton newButton;
	private ImageIcon newButtonIcon;

	/**
	 * @param con the controller
	 * @param viewManager
	 */
	public WelcomeView(Controller con, ViewManager viewManager) {
		controller = con;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(background);

		JPanel pane = new JPanel();
		tipLine = new JLabel();

		pane.setBackground(background);

		String newButtonTipp = "Neuen C0-Code schreiben";
		newButtonIcon = new ImageIcon(Messages.getResourceURL("c0h0", "newButtonIcon"));
		newButton = new WelcomeViewButton(newButtonIcon, newButtonTipp, ButtonCommand.newCode, newButtonTipp, this);

		String loadButtonTipp = "eigene .c0 Datei öffnen";
		loadButtonIcon = new ImageIcon(Messages.getResourceURL("c0h0", "loadButtonIcon"));
		loadButton = new WelcomeViewButton(loadButtonIcon, loadButtonTipp, ButtonCommand.loadCode, loadButtonTipp, this);

		String exampleButtonTipp = "Vorgefertigte .c0 Datei öffnen";
		exampleButtonIcon = new ImageIcon(Messages.getResourceURL("c0h0", "exampleButtonIcon"));
		exampleButton = new WelcomeViewButton(exampleButtonIcon, exampleButtonTipp, ButtonCommand.exampleCode,
				exampleButtonTipp, this);

		pane.add(newButton);
		pane.add(Box.createRigidArea(new Dimension(20, 0)));
		pane.add(loadButton);
		pane.add(Box.createRigidArea(new Dimension(20, 0)));
		pane.add(exampleButton);

		add(Box.createVerticalStrut(150));
		add(pane);
		tipLine.setAlignmentX(CENTER_ALIGNMENT);
		tipLine.setForeground(foreground);
		add(tipLine);
		add(Box.createVerticalStrut(150));

		doLayout();
	}

	/**
	 * Compares aa ActionCommand to BtnCmd-enum
	 * 
	 * @param e
	 * @param b
	 * @return
	 */
	private boolean isCmd(ActionEvent e, ButtonCommand b) {
		return (b.toString().equals(e.getActionCommand()));
	}

	public void actionPerformed(ActionEvent e) {
		if (isCmd(e, ButtonCommand.loadCode)) {
			loadButtonAction();
		} else if (isCmd(e, ButtonCommand.exampleCode)) {
			exampleButtonAction();
		} else if (isCmd(e, ButtonCommand.newCode)) {
			newButtonAction();
		}
	}

	/**
	 * Action for newButton
	 */
	private void newButtonAction() {
		controller.newCode();
	}

	/**
	 * Action for loadButton
	 */
	private void loadButtonAction() {
		controller.openFile();
	}

	/**
	 * Action for exampleButton
	 */
	private void exampleButtonAction() {
		controller.openExample();
	}

	/**
	 * Identifies which WelcomeButton was hovered
	 * 
	 * @param e
	 * @return
	 */
	private WelcomeViewButton caller(MouseEvent e) {
		WelcomeViewButton caller = (WelcomeViewButton) e.getSource();
		return caller;
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
		tipLine.setText(caller(arg0).getTip());
	}

	public void mouseExited(MouseEvent arg0) {
		tipLine.setText("");
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
}
