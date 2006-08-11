/**
 * 
 */
package org.jalgo.module.ebnf.gui.syndia.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jalgo.main.JAlgoMain;
import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.ModuleConnector;

/** This is the control area for the syntax diagram display.
 * 
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class ControlPanel extends AbstractControlPanel {

	private JToggleButton noStairs = new JToggleButton(new ImageIcon(Messages
			.getResourceURL("ebnf", "Icon.Stairs")));
	private JButton toWordAlgo = new JButton(Messages.getString("ebnf",
			"SynDia.Control_ToWordAlgo"));
	private JButton toSynDia = new JButton(Messages.getString("ebnf",
			"SynDia.Control_ToSynDiaInput"));
	private DisplayController controller;
	private ModuleConnector connector;

	/** This is the constructor. 
	 * 
	 * @param dc The GuiController which handles the actions between the Panels
	 * @param connector The ModuleConnector for saving actions
	 */
	public ControlPanel(DisplayController dc, ModuleConnector connector) {

		super();
		this.controller = dc;
		this.connector = connector;
		init();
		initListener();

	}

	/**
	 * Initializes the components
	 */
	public void init() {

		noStairs.setBorder(new EmptyBorder(3, 3, 3, 3));
		noStairs.setFocusPainted(false);
		noStairs.setSelectedIcon(new ImageIcon(Messages.getResourceURL("ebnf",
				"Icon.NoStairs")));
		noStairs.setToolTipText(Messages.getString("ebnf", "SynDia.Control_Stairs"));
		noStairs.addMouseListener(StatusLineUpdater.getInstance());
		
		left.add(noStairs);
		JPanel right = new JPanel();
		right.setBackground(Color.WHITE);
		right.add(toSynDia);
		right.add(toWordAlgo);
		this.add(right, BorderLayout.EAST);

	}

	/**
	 *  Initializes all listeners on the Panel
	 */
	public void initListener() {

		fitToSize.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					controller.setAutoSize(true);

				} else {

					controller.setAutoSize(false);
				}

			}

		});

		zoomer.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				fitToSize.setSelected(false);
			}
		});
		zoomer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				controller.resizeDrawPanel(zoomer.getValue());

			}

		});
		
		noStairs.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					controller.getDrawPanel().setStairs(false);

				} else {

					controller.getDrawPanel().setStairs(true);

				}

				controller.getDrawPanel().update(null, null);

			}

		});
		
		toSynDia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DiagramSize
						.setFontSize(controller.getDrawPanel().getFontSize());
				controller.switchToSynDiaInput();
			}
		});

		toWordAlgo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					DiagramSize.setFontSize(controller.getDrawPanel().getFontSize());
					controller.setWordAlgoMode();					
				}
				
				
		});

	}

}
