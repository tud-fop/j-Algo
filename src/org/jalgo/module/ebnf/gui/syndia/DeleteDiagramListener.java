package org.jalgo.module.ebnf.gui.syndia;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import org.jalgo.module.ebnf.gui.syndia.display.SynDiaPanel;
import org.jalgo.module.ebnf.renderer.elements.RenderElement;

/**
 * MouseListener that paints a border around syntax diagrams in delete mode.
 * 
 * @author Michael Thiele
 * 
 */
public class DeleteDiagramListener extends MouseInputAdapter {

	private GuiController guiController;

	/**
	 * 
	 * @param guiController
	 *            the guiController this Listener belongs to
	 */
	public DeleteDiagramListener(GuiController guiController) {
		this.guiController = guiController;
	}

	/**
	 * If mouse enters, paint a red border around syntax diagram.
	 */
	public void mouseEntered(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof SynDiaPanel) {
			((SynDiaPanel) e.getComponent()).setBorder(BorderFactory
					.createLineBorder(RenderElement.HIGHLIGHT_COLOR, 3));
		} else {
			((SynDiaPanel) e.getComponent().getParent())
					.setBorder(BorderFactory.createLineBorder(
							RenderElement.HIGHLIGHT_COLOR, 3));
		}
	}

	/**
	 * If mouse exits, set border back to standard (no border).
	 */
	public void mouseExited(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof SynDiaPanel) {
			((SynDiaPanel) e.getComponent()).setBorder(BorderFactory
					.createEmptyBorder());
		} else {
			((SynDiaPanel) e.getComponent().getParent())
					.setBorder(BorderFactory.createEmptyBorder());
		}
	}

	/**
	 * If mouse is clicked, remove the diagram.
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			Component c = e.getComponent();
			if (c instanceof SynDiaPanel) {
				try {
					guiController.getSynDiaController().removeSyntaxDiagram(
							guiController.getRenderMap().get(
									((JPanel) c).getComponent(2))
									.getMySyntaxDiagram());
				} catch (IllegalArgumentException ex) {
					guiController.ShowDoNotDeleteStartDiagram();
				} catch (Exception ex2) {
					ex2.printStackTrace();
				}
			} else {
				try {
					guiController.getSynDiaController().removeSyntaxDiagram(
							guiController.getRenderMap().get(
									c.getParent().getComponent(2))
									.getMySyntaxDiagram());
				} catch (IllegalArgumentException ex) {
					guiController.ShowDoNotDeleteStartDiagram();
				} catch (Exception ex2) {
					ex2.printStackTrace();
				}
			}
		}
	}
}
