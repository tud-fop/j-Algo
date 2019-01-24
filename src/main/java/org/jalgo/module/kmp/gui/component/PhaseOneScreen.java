package org.jalgo.module.kmp.gui.component;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.module.kmp.gui.GUIController;
import org.jalgo.module.kmp.gui.event.PhaseOneScreenListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * This is the main screen of the phase one.
 * 
 * @author Danilo Lisske
 */
public class PhaseOneScreen extends JPanel implements GUIConstants {
	private static final long serialVersionUID = -902635070485942488L;
	
	private PhaseOneScreenListener listener;
	/** the input panel of phase one */
	public PhaseOneInputPanel phaseoneinputPane;
	/** the show panel of phase one */
	public PhaseOneShowPanel phaseoneshowPane;
	/** the info panel of phase one */
	public InfoTabbedPanel infotabbedPane;
	private JSplitPane phaseonesplitPane;
	private JSlider sizeslider;
	
	/**
	 * The constructor of the phase one screen.
	 * 
	 * @param gc the <code>GUIController</code>
	 */
	public PhaseOneScreen(GUIController	gc) {
		listener = new PhaseOneScreenListener(gc, this);
		phaseoneinputPane = new PhaseOneInputPanel(listener);
		phaseoneshowPane = new PhaseOneShowPanel(listener);
		infotabbedPane = new InfoTabbedPanel(listener,null);
		
		setFont(new Font("SansSerif", Font.PLAIN, 14));
		setLayout(new BorderLayout());
		
		JPanel showPane = new JPanel(new BorderLayout());
		
		sizeslider = new JSlider(JSlider.VERTICAL,10,20,10);
		sizeslider.setMinorTickSpacing(1);
		sizeslider.setBackground(Color.WHITE);
		sizeslider.setToolTipText(Messages.getString("kmp","Screen.Slider_ttt"));
		sizeslider.addChangeListener(listener);
		sizeslider.addMouseListener(listener);
		sizeslider.setPaintTicks(true);
		
		showPane.add(phaseoneshowPane,BorderLayout.CENTER);
		showPane.add(sizeslider,BorderLayout.EAST);

		phaseoneinputPane.setPreferredSize(new Dimension(phaseoneinputPane.getPreferredSize().width,40));
		infotabbedPane.setPreferredSize(new Dimension(infotabbedPane.getPreferredSize().width,211));
		
		phaseonesplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
			showPane, infotabbedPane);
		phaseonesplitPane.setOneTouchExpandable(true);
		phaseonesplitPane.setResizeWeight(1);
		
		add(phaseoneinputPane,BorderLayout.NORTH);
		add(phaseonesplitPane,BorderLayout.CENTER);
	}
	
	/**
	 * Returns the scalefactor of the size slider.
	 * 
	 * @return the scalefactor
	 */
	public int getScaleFactor() {
		return sizeslider.getValue();
	}
	
	/**
	 * Returns the divider location.
	 * 
	 * @return the divider location
	 */
	public int getDividerLocation() {
		return phaseonesplitPane.getDividerLocation();
	}
	
	/**
	 * Sets the divider location.
	 * 
	 * @param size the position
	 */
	public void setDividerLocation(int size) {
		phaseonesplitPane.setDividerLocation(size);
	}
	
	/**
	 * Sets the slider position.
	 * 
	 * @param value the value
	 */
	public void setSliderPosition(int value) {
		sizeslider.setValue(value);
	}
}