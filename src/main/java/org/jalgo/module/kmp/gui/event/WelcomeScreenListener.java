package org.jalgo.module.kmp.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JRadioButton;
import javax.swing.JLabel;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.component.WelcomeButton;
import org.jalgo.module.kmp.gui.GUIController;
import org.jalgo.module.kmp.gui.component.WelcomeScreen;
import org.jalgo.module.kmp.gui.component.LearningExamplesDialog;

/**
 * This is the <code>WelcomeScreenListener</code> which is responsible for
 * actions happening in the welcome screen.
 * 
 * @author Danilo Lisske
 */
public class WelcomeScreenListener implements ActionListener, MouseListener, WindowFocusListener {
	private GUIController gui;
	private WelcomeScreen welcomescreen;
	private LearningExamplesDialog led;
	
	/**
	 * The constructor of the <code>PhaseOneScreenListener</code>.
	 * 
	 * @param gc the <code>GUIController</code> instance
	 * @param ws the <code>WelcomeScreen</code> instance
	 */
	public WelcomeScreenListener(GUIController gc, WelcomeScreen ws) {
		gui = gc;
		welcomescreen = ws;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("startPhaseOne"))
			gui.installPhaseOneScreen();
		else if(e.getActionCommand().equals("startPhaseTwo"))
			gui.installPhaseTwoScreen();
		else if(e.getActionCommand().equals("openFile"))
			JAlgoGUIConnector.getInstance().showOpenDialog(true, true);
		else if(e.getActionCommand().equals("loadExample")) {
			led = new LearningExamplesDialog(this);
			led.setLocationRelativeTo(welcomescreen);
			led.setVisible(true);
		}
		else if(e.getActionCommand().equals("setlearningexample")) {
			gui.loadLearningExample(Messages.getString("kmp","LED.Example_pattern_"+led.getSelectedIndex()),
					Messages.getString("kmp","LED.Example_searchtext_"+led.getSelectedIndex()));
			gui.installPhaseOneScreen();
			led.dispose();
		}
		else if(e.getActionCommand().equals("cancellearningexample")) led.dispose();
		else {
			((WelcomeButton)e.getSource()).setSelected(false);
			welcomescreen.setDescription(null);
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
		if(e.getSource() instanceof WelcomeButton) {
			WelcomeButton source = (WelcomeButton)e.getSource();
			if (!source.isEnabled()) return;
			source.setSelected(true);
			welcomescreen.setDescription(source.getDescription());
		}
		if(e.getSource() instanceof JRadioButton) 
			led.setTfDescriptionText(((JRadioButton)e.getSource()).getName());
		if(e.getSource() instanceof JLabel) 
			led.setTfDescriptionText(((JLabel)e.getSource()).getName());
	}

	/**
	 * Causes the event source button to be displayed normally and to remove the
	 * description string from the screen.
	 */
	public void mouseExited(MouseEvent e) {
		if(e.getSource() instanceof WelcomeButton) {
			((WelcomeButton)e.getSource()).setSelected(false);
			welcomescreen.setDescription(null);
		}
		if(e.getSource() instanceof JRadioButton || e.getSource() instanceof JLabel) 
			led.setTfDescriptionText("mouseoff");
	}
	
	public void windowLostFocus(WindowEvent e) {
		e.getComponent().requestFocus();
	}
	
	public void windowGainedFocus(WindowEvent e) {
		
	}
}
