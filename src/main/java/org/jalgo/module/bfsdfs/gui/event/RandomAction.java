package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.components.AlgoTab;

/**
 * This class represents an action to toggle the random check box.
 * 
 * @author Florian Dornbusch
 */
public class RandomAction
extends AbstractAction {
	private static final long serialVersionUID = 2372809553068441995L;
	
	private AlgoTab algoTab;
	
	private GUIController gui;
	
	public RandomAction(AlgoTab algoTab, GUIController gui) {
		this.algoTab = algoTab;
		this.gui = gui;
		putValue(SHORT_DESCRIPTION, Messages.getString(
				"bfsdfs", "AlgoTab.randomBoxStatus"));
	}
	
	/**
	 * If the check box is selected, the permutations must not be chosen
	 * to ensure the non-determinism of the algorithm.
	 * @author Florian Dornbusch
	 */
	public void actionPerformed(ActionEvent arg0) {

		boolean b = algoTab.getSuccessorsAvailable();
		algoTab.getSuccessorChooser().setEnabled(b);
		
		if(algoTab.getRandomBox().isSelected()) {
			List<Integer> random = algoTab.getRandomPermutation();
			algoTab.getSuccessorChooser().fill(random);
			gui.setSuccessorOrder(random);
		}
	}
}