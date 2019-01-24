package org.jalgo.module.app.view.run;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.jalgo.main.util.Messages;
import org.jalgo.module.app.controller.AlgorithmController;

/**
 * Displays the toolbar on the lower end of the algorithm panel.
 */
public class ControlToolbar extends JPanel {
	private static final long serialVersionUID = 24047077533400298L;

	private AlgorithmController algorithmController;

	private JButton fastBackwardButton, backwardButton, forwardButton,
			fastForwardButton;
	private JToggleButton animateButton;
	private JPanel animateSpacer;
	
	public enum ControlButtons {
		FastBackwardButton, BackwardButton, ForwardButton, FastForwardButton, AnimateButton
	}

	/**
	 * Instantiates the toolbar on the lower end of the algorithm panel.
	 */
	public ControlToolbar(AlgorithmController algoCtrl) {
		algorithmController = algoCtrl;

		fastBackwardButton = new JButton();
		backwardButton = new JButton();
		forwardButton = new JButton();
		fastForwardButton = new JButton();
		animateButton = new JToggleButton();

		// Setup Buttons
		fastBackwardButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_GroupStepBackward"))); //$NON-NLS-1$
		backwardButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$ 
				"icon_StepBackward"))); //$NON-NLS-1$
		forwardButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_StepForward"))); //$NON-NLS-1$
		fastForwardButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_GroupStepForward"))); //$NON-NLS-1$
		animateButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
				"icon_animateStart"))); //$NON-NLS-1$
		
		fastBackwardButton.setToolTipText(Messages.getString("app", "ControlToolbar.FastBackward")); //$NON-NLS-1$ //$NON-NLS-2$
		backwardButton.setToolTipText(Messages.getString("app", "ControlToolbar.Backward")); //$NON-NLS-1$ //$NON-NLS-2$
		forwardButton.setToolTipText(Messages.getString("app", "ControlToolbar.Forward")); //$NON-NLS-1$ //$NON-NLS-2$
		fastForwardButton.setToolTipText(Messages.getString("app", "ControlToolbar.FastForward")); //$NON-NLS-1$ //$NON-NLS-2$
		animateButton.setToolTipText(Messages.getString("app", "ControlToolbar.Animate")); //$NON-NLS-1$ //$NON-NLS-2$

		fastBackwardButton.addActionListener(new fastBackwardListener());
		backwardButton.addActionListener(new backwardListener());
		forwardButton.addActionListener(new forwardListener());
		fastForwardButton.addActionListener(new fastForwardListener());
		animateButton.addActionListener(new animateListener());
		
		// Setup Tool Bar
		animateSpacer = new JPanel();
		animateSpacer.setSize(60, 0);

		this.add(fastBackwardButton);
		this.add(backwardButton);
		this.add(forwardButton);
		this.add(fastForwardButton);
		this.add(animateSpacer);
		this.add(animateButton);
	}

	/**
	 * Enables/Disables all buttons based on the parameter <code>state</code>.
	 * 
	 * @param state
	 *            <code>true</code> to enable all Buttons in this toolbar.
	 */
	public void setEnabled(boolean state) {
		fastBackwardButton.setEnabled(state);
		backwardButton.setEnabled(state);
		forwardButton.setEnabled(state);
		fastForwardButton.setEnabled(state);
		animateButton.setEnabled(state);
	}

	/**
	 * Toggles the state of the animation button between animate(play) and pause
	 * 
	 * @param animate
	 *            if <code>true</code>, set the animation button to play,
	 *            otherwise set it to pause
	 */
	public void toggleAnimationState(boolean animate) {
		if (animate) {
			animateButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
					"icon_animateStart"))); //$NON-NLS-1$
			animateButton.setSelected(false);
		} else {
			animateButton.setIcon(new ImageIcon(Messages.getResourceURL("app", //$NON-NLS-1$
					"icon_animatePause"))); //$NON-NLS-1$
			animateButton.setSelected(true);
		}
	}

	/**
	 * Enables/disables the control toolbar buttons according to <code>state</code>.
	 * @param button the button to be changed.
	 * @param state if <code>true</code>, enable button, otherwise disable it (gray it out).
	 */
	public void setButtonState(ControlButtons button, boolean state) {
		switch (button) {
		case FastBackwardButton:
			fastBackwardButton.setEnabled(state);
			break;

		case BackwardButton:
			backwardButton.setEnabled(state);
			break;

		case ForwardButton:
			forwardButton.setEnabled(state);
			break;

		case FastForwardButton:
			fastForwardButton.setEnabled(state);
			break;

		case AnimateButton:
			animateButton.setEnabled(state);
		}
	}

	class fastBackwardListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			algorithmController.previousGroupStep();
		}
	}

	class backwardListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			algorithmController.previousStep();
		}
	}

	class forwardListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			algorithmController.nextStep();
		}
	}

	class fastForwardListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			algorithmController.nextGroupStep();
		}
	}

	class animateListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (animateButton.isSelected()) {
				algorithmController.startAnimation();
			} else {
				algorithmController.pauseAnimation();
			}
		}
	}
}
