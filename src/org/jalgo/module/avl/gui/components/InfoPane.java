/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/* Created on 26.04.2005 */
package org.jalgo.module.avl.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>InfoPane</code> represents a panel, where informations
 * about the current instance of the <code>SearchTree</code> are displayed.
 * This informations are:<ul>
 * <li>the height of the tree</li>
 * <li>the weight of the tree</li>
 * <li>the average search depth of the tree</li>
 * 
 * @author Alexander Claus
 */
public class InfoPane
extends JPanel {

	private static final long serialVersionUID = -3131790632990225572L;

	private SearchTree tree;

	private JLabel weight;
	private JLabel height;
	private JLabel averageDepth;

	/**
	 * Constructs an <code>InfoPane</code> object, which is backed by the
	 * given <code>SearchTree</code>.
	 * 
	 * @param tree the <code>SearchTree</code> for which informations should
	 *            be provided
	 */
	public InfoPane(SearchTree tree) {
		this.tree = tree;

		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		setBorder(BorderFactory.createTitledBorder(Messages.getString(
			"avl", "InfoPane.Info"))); //$NON-NLS-1$ //$NON-NLS-2$

		JLabel weightLabel = new JLabel(Messages.getString(
			"avl", "InfoPane.Node_count")); //$NON-NLS-1$ //$NON-NLS-2$
		GUIController.setGBC(weightLabel, gbl, 0, 0, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(weightLabel);
		weight = new JLabel();
		GUIController.setGBC(weight, gbl, 1, 0, 1, 1,
			GridBagConstraints.EAST, GridBagConstraints.NONE);
		add(weight);

		JLabel heightLabel = new JLabel(Messages.getString(
			"avl", "InfoPane.Height")); //$NON-NLS-1$ //$NON-NLS-2$
		GUIController.setGBC(heightLabel, gbl, 0, 1, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(heightLabel);
		height = new JLabel();
		GUIController.setGBC(height, gbl, 1, 1, 1, 1,
			GridBagConstraints.EAST, GridBagConstraints.NONE);
		add(height);

		JLabel averageDepthLabel = new JLabel(Messages.getString(
			"avl", "InfoPane.Average_depth")); //$NON-NLS-1$ //$NON-NLS-2$
		GUIController.setGBC(averageDepthLabel, gbl, 0, 2, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(averageDepthLabel);
		averageDepth = new JLabel();
		GUIController.setGBC(averageDepth, gbl, 1, 2, 1, 1,
			GridBagConstraints.EAST, GridBagConstraints.NONE);
		add(averageDepth);

		// the status line updater
		addMouseListener(new MouseAdapter() {

			public void mouseExited(MouseEvent e) {
				JAlgoGUIConnector.getInstance().setStatusMessage(null);
			}

			public void mouseEntered(MouseEvent e) {
				JAlgoGUIConnector.getInstance().setStatusMessage(
					Messages.getString("avl", "InfoPane.Status_message")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});

		update();
	}

	/**
	 * Updates the informations by calculating or getting from the tree and
	 * displays them.
	 */
	public void update() {
		weight.setText("" + tree.getWeight()); //$NON-NLS-1$
		height.setText("" + tree.getHeight()); //$NON-NLS-1$
		// calculate average search depth
		List<Node> nodes = tree.exportInOrder();
		double depthSum = 0;
		for (Node node : nodes)
			depthSum += tree.getLevelFor(node);
		double avgDepth = (int)(depthSum / nodes.size() * 10) / 10.0;
		averageDepth.setText(Double.toString(avgDepth));
		doLayout();
	}
}