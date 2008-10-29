package org.jalgo.module.app.controller;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.jalgo.module.app.core.SemiRing;
import org.jalgo.module.app.view.SemiringComponent;

/**
 * Controls the panel, which is on the left side of the user interface. The
 * SemiringController initializes the one component, which is involved in these
 * Panel.
 */
public class SemiringController {

	// Connection to the MainController to communicate with the Core and the
	// InterfaceController.
	private MainController mainCtrl;

	private JPanel semiringPanel;

	SemiringComponent semiringComponent;

	/**
	 * Creates a new controller for the semiring.
	 * 
	 * @param mainController The link back to the main controller.
	 * @param panel The panel which holds the semiring components.  
	 */
	public SemiringController(MainController mainController, JPanel panel) {
		mainCtrl = mainController;
		semiringPanel = panel;

		semiringComponent = new SemiringComponent(this);
		semiringPanel.setLayout(new BorderLayout());
		semiringPanel.add(semiringComponent, BorderLayout.CENTER);
	}

	/**
	 * Gets the semiring from the calculation. 
	 * 
	 * @return The semiring.
	 */
	public SemiRing getSemiRing() {
		return mainCtrl.getCalculation().getSemiring();
	}

	/**
	 * Sets the Semiring, which is given by the User (either by choosing it from
	 * the drop-down menu or from a saved file).
	 */
	public void setSemiRing(SemiRing semiring) {
		mainCtrl.getCalculation().setSemiring(semiring);

		updateDisplay(null);
		mainCtrl.newGraph(semiring);
	}

	/**
	 * Forwards the beamer mode to the semiring components.
	 * 
	 * @param beamerMode A boolean which should be true to enable the beamer mode.
	 */
	public void setBeamerMode(boolean beamerMode){
		semiringComponent.updateBeamerMode(beamerMode);
	}
	
	
	/**
	 * Redraws the semiring component with the given interface mode.
	 * 
	 * @param interfaceMode The interface mode. 
	 */
	public void updateDisplay(InterfaceMode interfaceMode) {
		semiringComponent.updateDisplay(getSemiRing());
	}

}