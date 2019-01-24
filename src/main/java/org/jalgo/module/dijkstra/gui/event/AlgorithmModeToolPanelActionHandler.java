package org.jalgo.module.dijkstra.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.GotoMacroStepAction;
import org.jalgo.module.dijkstra.actions.GotoMicroStepAction;
import org.jalgo.module.dijkstra.actions.GotoStepAction;
import org.jalgo.module.dijkstra.actions.SetStartNodeAction;
import org.jalgo.module.dijkstra.actions.StartAnimationAction;
import org.jalgo.module.dijkstra.actions.StopAnimationAction;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.components.AlgorithmModeToolPanel;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * This class handles all events around the tool panel in algorithm mode.
 * 
 * @author Alexander Claus
 */
public class AlgorithmModeToolPanelActionHandler
implements ActionListener, KeyListener {

	private final Controller controller;
	private final AlgorithmModeToolPanel toolPane;

	public AlgorithmModeToolPanelActionHandler(Controller controller,
		AlgorithmModeToolPanel toolPane) {
		this.controller = controller;
		this.toolPane = toolPane;
	}

	/**
	 * Handles button clicks.
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals("perform")) {
				if ((Boolean)
					((JComponent)e.getSource()).getClientProperty("macroStep"))
					new GotoMacroStepAction(controller, (Boolean)
						((JComponent)e.getSource()).getClientProperty("forward"));
				else new GotoMicroStepAction(controller, (Boolean)
					((JComponent)e.getSource()).getClientProperty("forward"));
			}
			else if (e.getActionCommand().equals("startNode"))
				new SetStartNodeAction(controller,
					((JComboBox)e.getSource()).getSelectedIndex()+1);
			else if (e.getActionCommand().equals("goto"))
				new GotoStepAction(controller, toolPane.getSelectedStep(), true);
			else if (e.getActionCommand().equals("startAnim")) {
				if (((JToggleButton)e.getSource()).isSelected())
					new StartAnimationAction(controller, 750);
				else new StopAnimationAction(controller);
			}
		}
		catch (ActionException ex) {
			new DefaultExceptionHandler(ex);
		}
	}

	/**
	 * Assert valid inputs in textfield.
	 */
	public void keyTyped(KeyEvent e) {
		// check that only numbers are accepted
		if (e.getKeyChar() < 48 || e.getKeyChar() > 57) e.consume();

	}

	public void keyPressed(KeyEvent e) {
		// this method has no effect
	}

	/**
	 * Updates the enabled state of the goto-button.
	 */
	public void keyReleased(KeyEvent e) {
		toolPane.setGotoButtonEnabled(
			((JTextField)e.getSource()).getText().length()>0);
	}
}