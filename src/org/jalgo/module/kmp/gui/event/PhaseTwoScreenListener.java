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
import org.jalgo.module.kmp.gui.component.PhaseTwoScreen;
import org.jalgo.module.kmp.gui.component.RandomPatternDialog;
import org.jalgo.module.kmp.gui.component.SearchTextLoadDialog;

/**
 * This is the <code>PhaseTwoScreenListener</code> which is responsible for
 * actions happening in the phase two screen.
 * 
 * @author Danilo Lisske
 */
public class PhaseTwoScreenListener implements ActionListener, MouseListener,
									KeyListener, WindowFocusListener, ChangeListener {
	private GUIController guicontroller;
	private PhaseTwoScreen phasetwoscreen;
	private RandomPatternDialog rpd;
	private SearchTextLoadDialog stld;
	
	/**
	 * The constructor of the <code>PhaseOneScreenListener</code>.
	 * 
	 * @param g the <code>GUIController</code> instance
	 * @param w the <code>PhaseTwoScreen</code> instance
	 */
	public PhaseTwoScreenListener(GUIController g, PhaseTwoScreen w) {
		guicontroller = g;
		phasetwoscreen = w;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("setpattern")) guicontroller.setPattern();
		if(e.getActionCommand().equals("random")) {
			rpd = new RandomPatternDialog(null,this);
			rpd.setLocationRelativeTo(phasetwoscreen);
			rpd.setVisible(true);
		}
		if(e.getActionCommand().equals("loadst")) {
			stld = new SearchTextLoadDialog(this);
			stld.setTaSearchText(guicontroller.getSearchText());
			stld.setLocationRelativeTo(phasetwoscreen);
			stld.setVisible(true);
		}
		if(e.getActionCommand().equals("generst")) guicontroller.createRandomSearchText();
		if(e.getActionCommand().equals("stfilechooser")) {
			String text = guicontroller.openSearchTextFileChooser();
			if (text != null && text != "") stld.setTaSearchText(text);
		}
		if(e.getActionCommand().equals("setrandom")) {
			guicontroller.createRandomPattern(rpd.getAlphabetSize(),rpd.getPatternLength());
			rpd.dispose();
		}
		if(e.getActionCommand().equals("cancelrandom")) rpd.dispose();
		if(e.getActionCommand().equals("setsearchtext")) {
			guicontroller.setSearchText(stld.getTaSearchText()); 
			stld.dispose();
		}
		if(e.getActionCommand().equals("cancelsearchtext")) stld.dispose();
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
			if(e.getKeyCode() == KeyEvent.VK_ENTER && phasetwoscreen.phasetwoinputPane.isPatternEnabled()) 
				guicontroller.setPattern();
		}
	}
	
	public void windowLostFocus(WindowEvent e) {
		e.getComponent().requestFocus();
	}
	
	public void windowGainedFocus(WindowEvent e) {
		
	}
	
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
	    guicontroller.scaleScreen(source.getValue() * 0.1);
	}
}
