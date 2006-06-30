package org.jalgo.module.kmp.gui.component;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIConstants;
import org.jalgo.module.kmp.gui.GUIController;
import org.jalgo.module.kmp.gui.event.PhaseTwoScreenListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * This is the main screen of the phase two.
 * 
 * @author Danilo Lisske
 */
public class PhaseTwoScreen extends JPanel implements GUIConstants {
	private static final long serialVersionUID = -24048124349086693L;
	
	private PhaseTwoScreenListener listener;
	/** the input panel of phase two */
	public PhaseTwoInputPanel phasetwoinputPane;
	/** the show panel of phase two */
	public PhaseTwoShowPanel phasetwoshowPane;
	/** the info panel of phase two */
	public InfoTabbedPanel infotabbedPane;
	private JSplitPane phasetwosplitPane;
	private JSlider sizeslider;
	
	/**
	 * The constructor of the phase two screen.
	 * 
	 * @param gc the <code>GUIController</code>
	 */
	public PhaseTwoScreen(GUIController gc) {
		listener = new PhaseTwoScreenListener(gc,this);
		phasetwoinputPane = new PhaseTwoInputPanel(listener);
		phasetwoshowPane = new PhaseTwoShowPanel(listener);
		infotabbedPane = new InfoTabbedPanel(null,listener);
		
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
		
		showPane.add(phasetwoshowPane,BorderLayout.CENTER);
		showPane.add(sizeslider,BorderLayout.EAST);
		
		infotabbedPane.setPreferredSize(new Dimension(infotabbedPane.getPreferredSize().width,211));
		phasetwoinputPane.setPreferredSize(new Dimension(phasetwoinputPane.getPreferredSize().width,40));
		
		phasetwosplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
			showPane, infotabbedPane);
		phasetwosplitPane.setOneTouchExpandable(true);
		phasetwosplitPane.setResizeWeight(1);
		
		add(phasetwoinputPane,BorderLayout.NORTH);
		add(phasetwosplitPane,BorderLayout.CENTER);
	}
	
	/**
	 * Sets the scalefactor of the size slider.
	 * 
	 * @param value the scalefactor
	 */
	public void setScaleFactor(int value) {
		sizeslider.setValue(value);
	}
	
	/**
	 * Sets the divider location.
	 * 
	 * @param size the position
	 */
	public void setDividerLocation(int size) {
		phasetwosplitPane.setDividerLocation(size);
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