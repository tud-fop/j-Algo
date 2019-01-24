package org.jalgo.module.kmp.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JTextPane;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.kmp.gui.GUIController;
import org.jalgo.module.kmp.gui.component.PhaseOneScreen;
import org.jalgo.module.kmp.gui.component.RandomPatternDialog;;

/**
 * This is the <code>PhaseOneScreenListener</code> which is responsible for
 * actions happening in the phase one screen.
 * 
 * @author Danilo Lisske
 */
public class PhaseOneScreenListener implements ActionListener, MouseListener,
									KeyListener, WindowFocusListener, ChangeListener {
	private GUIController guicontroller;
	private PhaseOneScreen phaseonescreen;
	private RandomPatternDialog rpd;
	
	/**
	 * The constructor of the <code>PhaseOneScreenListener</code>.
	 * 
	 * @param g the <code>GUIController</code> instance
	 * @param w the <code>PhaseOneScreen</code> instance
	 */
	public PhaseOneScreenListener(GUIController g, PhaseOneScreen w) {
		guicontroller = g;
		phaseonescreen = w;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("setpattern")) guicontroller.setPattern();
		if(e.getActionCommand().equals("random")) {
			rpd = new RandomPatternDialog(this,null);
			rpd.setLocationRelativeTo(phaseonescreen);
			rpd.setVisible(true);
		}
		if(e.getActionCommand().equals("addpattern")) guicontroller.addPattern();
		if(e.getActionCommand().equals("setrandom")) {
			guicontroller.createRandomPattern(rpd.getAlphabetSize(),rpd.getPatternLength());
			rpd.dispose();
		}
		if(e.getActionCommand().equals("cancelrandom")) rpd.dispose();
		if(e.getActionCommand().equals("cyclechange")) 
			guicontroller.setCycles();
		if(e.getActionCommand().equals("goon")) guicontroller.installPhaseTwoScreen();
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
		if(e.getSource() instanceof JTextPane)
			JAlgoGUIConnector.getInstance().setStatusMessage(
				((JComponent)e.getSource()).getName());
		else JAlgoGUIConnector.getInstance().setStatusMessage(
				((JComponent)e.getSource()).getToolTipText());
	}

	/**
	 * Invoked when the mouse leaves a component.
	 */
	public void mouseExited(MouseEvent e) {
		JAlgoGUIConnector.getInstance().setStatusMessage(null);
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void keyPressed(KeyEvent e) {
		
	}
	
	public void keyReleased(KeyEvent e) {
		if(e.getComponent().getName().equals("tfpattern")) {
			guicontroller.keyOnTfPattern();
			if(e.getKeyCode() == KeyEvent.VK_ENTER && phaseonescreen.phaseoneinputPane.isPatternEnabled()) 
				guicontroller.setPattern(); 
		}
		if(e.getComponent().getName().equals("tfaddpattern")) {
			guicontroller.keyOnTfAddPattern();
			if(e.getKeyCode() == KeyEvent.VK_ENTER && phaseonescreen.phaseoneinputPane.isAddPatternEnabled()) 
				guicontroller.addPattern();
		}
	}
	
	public void windowLostFocus(WindowEvent e) {
		e.getComponent().requestFocus();
	}
	
	public void windowGainedFocus(WindowEvent e) {
		
	}
	
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		double newsize = source.getValue();
	    guicontroller.scaleScreen(newsize * 0.1);
	}
}
