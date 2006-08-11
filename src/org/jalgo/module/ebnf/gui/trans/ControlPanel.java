package org.jalgo.module.ebnf.gui.trans;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.syndia.display.AbstractControlPanel;
import org.jalgo.module.ebnf.gui.syndia.display.DiagramSize;
import org.jalgo.module.ebnf.model.trans.TransMap;

/**
 * @author Andre Viergutz
 */
@SuppressWarnings("serial")
public class ControlPanel extends AbstractControlPanel implements Observer {

	private JToggleButton noStairs = new JToggleButton(new ImageIcon(Messages
			.getResourceURL("ebnf", "Icon.Stairs")));
	private JButton toEbnf = new JButton(Messages.getString("ebnf",
			"SynDia.Control_ToEbnf"));
	private JButton toSynDia = new JButton(Messages.getString("ebnf",
			"SynDia.Control_ToSynDiaDisplay"));
	private GUIController guicontroller;

	/**
	 * Initializes the components
	 * 
	 * @param guicontroller the GuiController which aggregates this class
	 */
	public ControlPanel(GUIController guicontroller) {

		super();
		this.guicontroller = guicontroller;

		init();
		initListener();
	}

	/**
	 * Initializes the ControlPanel components
	 */
	public void init() {

		noStairs.setBorder(new EmptyBorder(3, 3, 3, 3));
		noStairs.setFocusPainted(false);
		noStairs.setSelectedIcon(new ImageIcon(Messages.getResourceURL("ebnf",
				"Icon.NoStairs")));
		noStairs.setToolTipText(Messages.getString("ebnf", "SynDia.Control_Stairs"));
		toSynDia.setEnabled(false);

		// left.add(stairs);
		left.add(noStairs);
		right.setBackground(Color.WHITE);
		right.add(toEbnf);
		right.add(toSynDia);

	}

	/**
	 * Initializes all Listeners
	 */
	private void initListener() {

		fitToSize.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					// zoomer.setEnabled(false);
					guicontroller.setAutoSize(true);
					guicontroller.updateDrawPanel(20);

				} else {

					// zoomer.setEnabled(true);
					guicontroller.setAutoSize(false);

				}

			}

		});
		noStairs.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					guicontroller.getDrawPanel().setStairs(false);

				} else {

					guicontroller.getDrawPanel().setStairs(true);

				}

				guicontroller.getDrawPanel().update(null, null);

			}

		});

		zoomer.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				fitToSize.setSelected(false);
			}
		});
		zoomer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				guicontroller.updateDrawPanel(zoomer.getValue());

			}

		});

		toSynDia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DiagramSize.setFontSize(guicontroller.getDrawPanel()
						.getFontSize());
				guicontroller.switchToSynDiaDisplay();
			}
		});

		toEbnf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guicontroller.setEbnfInputMode();
			}
		});

	}

	public void update(Observable o, Object arg) {

		if (o instanceof TransMap) {

			toSynDia.setEnabled(((TransMap) o).isTransformed());

		}

	}

}
