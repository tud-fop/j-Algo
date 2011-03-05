package org.jalgo.module.c0h0.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * PanelComponent that splits window into 3 parts : left, right and bottom
 * 
 * @author Hendrik Sollich
 */
public class ModuleContainer extends JPanel {
	private static final long serialVersionUID = -3544118578261478226L;
	public JPanel leftPane;
	public JPanel rightPane;
	public JPanel bottomPane;

	private JSplitPane leftright;
	private JSplitPane trinity;

	public ModuleContainer() {
		initTrinity();
	}

	/**
	 * Initiates display with only one component
	 * */
	public void display(JComponent component) {
		// adopt division
		removeAll();
		add(component);
		updateUI();
	}

	/**
	 * Initiates display with three components (trinity)
	 * */
	public void display() {
		// adopt division
		removeAll();
		add(trinity);
		updateUI();
	}

	/**
	 * initiates trinity components
	 */
	private void initTrinity() {

		leftPane = new JPanel();
		rightPane = new JPanel();
		bottomPane = new JPanel();

		leftright = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPane, rightPane);
		trinity = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, leftright, bottomPane);

		leftPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
		rightPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));

		bottomPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.Y_AXIS));
		bottomPane.setPreferredSize(new Dimension(0, 150));
		bottomPane.setMaximumSize(new Dimension(0, 150));
		bottomPane.setMinimumSize(new Dimension(0, 10));

		// divison
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		trinity.setOneTouchExpandable(false);
		leftright.setResizeWeight(0.5);
		trinity.setResizeWeight(1);

	}

	public void toggleBottomPane(boolean on) {
		bottomPane.setVisible(on);
		if (on) {
			trinity.setDividerLocation(trinity.getSize().height - trinity.getInsets().bottom - trinity.getDividerSize()
					- bottomPane.getMaximumSize().height);
		}
	}

	/**
	 * returns the container split pane
	 * 
	 * @return split pane
	 */
	public JSplitPane getLeftright() {
		return leftright;
	}

}
