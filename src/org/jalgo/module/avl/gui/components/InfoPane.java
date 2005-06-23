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

import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>InfoPane</code> represents a panel, where informations about the
 * current instance of the <code>SearchTree</code> are displayed.
 * This informations are:<br>
 *     - the height of the tree<br>
 *     - the weight of the tree<br>
 *     - the average search depth of the tree
 * 
 * @author Alexander Claus
 */
public class InfoPane
extends JPanel {

	private SearchTree tree;
	
	private JLabel weight;
	private JLabel height;
	private JLabel averageDepth;
	
	/**
	 * Constructs an <code>InfoPane</code> object, which is backed by the given
	 * <code>SearchTree</code>.
	 * 
	 * @param tree the <code>SearchTree</code> for which informations should be
	 * 				provided
	 */
	public InfoPane(final GUIController gui, SearchTree tree) {
		this.tree = tree;

		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		setBorder(BorderFactory.createTitledBorder("Info"));

		JLabel weightLabel = new JLabel("Anzahl Knoten: ");
		GUIController.setGBC(weightLabel, gbl, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(weightLabel);
		weight = new JLabel();
		GUIController.setGBC(weight, gbl, 1, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		add(weight);
		
		JLabel heightLabel = new JLabel("Baumhöhe: ");
		GUIController.setGBC(heightLabel, gbl, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(heightLabel);
		height = new JLabel();
		GUIController.setGBC(height, gbl, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		add(height);
		
		JLabel averageDepthLabel = new JLabel("Durchschnittliche Suchtiefe:     ");
		GUIController.setGBC(averageDepthLabel, gbl, 0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(averageDepthLabel);
		averageDepth = new JLabel();
		GUIController.setGBC(averageDepth, gbl, 1, 2, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
		add(averageDepth);

		//the status line updater
		addMouseListener(new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				gui.setStatusMessage(null);
			}
			public void mouseEntered(MouseEvent e) {
				gui.setStatusMessage("Zeigt Informationen über den Baum an");
			}
		});

		update();
	}
	
	/**
	 * Updates the informations by calculating or getting from the tree and
	 * displays them.
	 */
	public void update() {
		weight.setText(""+tree.getWeight());
		height.setText(""+tree.getHeight());
		//calculate average search depth
		List<Node> nodes = tree.exportInOrder();
		double depthSum = 0;
		for (Node node : nodes) depthSum += tree.getLevelFor(node);
		double avgDepth = (int)(depthSum/nodes.size()*10)/10.0;
		averageDepth.setText(Double.toString(avgDepth));
		doLayout();
	}
}