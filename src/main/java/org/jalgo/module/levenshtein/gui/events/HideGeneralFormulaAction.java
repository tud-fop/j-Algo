package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jalgo.module.levenshtein.pattern.ChangeGeneralFormulaSizeObserver;

public class HideGeneralFormulaAction implements MouseListener {

	private ChangeGeneralFormulaSizeObserver obs;
	
	public HideGeneralFormulaAction(ChangeGeneralFormulaSizeObserver obs) {
		this.obs = obs;
	}
	
	public void mouseClicked(MouseEvent e) {
		obs.changeToSmallFormula(true);
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
