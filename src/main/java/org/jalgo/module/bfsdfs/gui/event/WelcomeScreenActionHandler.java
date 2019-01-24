package org.jalgo.module.bfsdfs.gui.event;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.components.WelcomeButton;
import org.jalgo.module.bfsdfs.gui.components.WelcomeScreen;

/**
 * This class handles the actions of the three welcome buttons to create a new
 * graph, load an existing graph and load the last opened graph.
 * 
 * @author Florian Dornbusch
 */
public class WelcomeScreenActionHandler
implements ActionListener, MouseListener {

	private GUIController gui;
	private WelcomeScreen screen;


	public WelcomeScreenActionHandler(WelcomeScreen screen,
			GUIController gui) {
		this.gui = gui;
		this.screen = screen;
	}
	
	/**
	 * 	Execute the actions according to the three welcome buttons.
	 *  @author Florian Dornbusch
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("new"))
			gui.installStandardLayout();
		else if (e.getActionCommand().equals("load")) {
			String filename =
				JAlgoGUIConnector.getInstance().showOpenDialog(false,false);
			if (filename != null) {
				try {
					gui.loadGraph(filename);
				} catch (IOException io) {
					JAlgoGUIConnector.getInstance().showErrorMessage(
							Messages.getString("bfsdfs",
									"WelcomeScreen.ioError"));
				}
			}
		}
		else if (e.getActionCommand().equals("last")) { 
			try {
				gui.loadLastGraph();
			} catch (IOException io) {
				JAlgoGUIConnector.getInstance().showErrorMessage(
						Messages.getString("bfsdfs",
								"WelcomeScreen.ioError"));
			}
		}

		((WelcomeButton)e.getSource()).setSelected(false);
		screen.setDescription(null);
	}

	/**
	 * 	Change to the highlighted version of the button and display
	 *	its description.
	 *  @author Florian Dornbusch
	 */
	public void mouseEntered(MouseEvent e) {
		WelcomeButton source = (WelcomeButton)e.getSource();
		if (!source.isEnabled()) return;
		source.setSelected(true);
		screen.setDescription(source.getDescription());
	}
	
	/**
	 * 	Change to the normal version of the button and display no
	 *  description.
	 *  @author Florian Dornbusch
	 */
	public void mouseExited(MouseEvent e) {
		((WelcomeButton)e.getSource()).setSelected(false);
		screen.setDescription(null);
	}

	/** unused observer method */
	public void mouseClicked(MouseEvent e) {}
	
	/** unused observer method */
	public void mousePressed(MouseEvent e) {}
	
	/** unused observer method */
	public void mouseReleased(MouseEvent e) {}
}
