package org.jalgo.module.ebnf.gui.syndia;

import java.awt.Component;

import javax.swing.JPanel;

import org.jalgo.module.ebnf.gui.syndia.display.DrawPanel;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.renderer.SynDiaRenderer;

/**
 * DrawPanel for the syntax diagram editor. Sets synDiaMouseListener to
 * synDiaPanels.
 * 
 * @author Michael Thiele
 * 
 */
@SuppressWarnings("serial")
public class EditorDrawPanel extends DrawPanel {

	private SynDiaMouseListener synDiaMouseListener;

	/**
	 * Initializes the EditorDrawPanel.
	 * 
	 * @param synDiaSystem
	 *            the actual syntax diagram system
	 * @param dc
	 *            the guiController the EditorDrawPanel belongs to
	 * @param synDiaRenderer
	 *            the syntax diagram renderer for this EditorDrawPanel
	 */
	public EditorDrawPanel(SynDiaSystem synDiaSystem,
			AbstractSDGuiController dc, SynDiaRenderer synDiaRenderer) {
		super(synDiaSystem, dc, synDiaRenderer);
		autoSize = false;
		displayName = "editor";
		factor = 0.22;
	}

	/**
	 * Sets the <code>SynDiaMouseListener</code> for all
	 * <code>SynDiaPanel</code>s.
	 * 
	 * @param synDiaMouseListener
	 *            the synDiaMouseListener
	 */
	public void setMouseListener(SynDiaMouseListener synDiaMouseListener) {
		this.synDiaMouseListener = synDiaMouseListener;
	}

	/**
	 * Fills the <code>synDiaPanelList</code> with elements. Adds the
	 * synDiaMouseListener.
	 */
	public void drawSynDiaSystem(SynDiaSystem sds) {
		super.drawSynDiaSystem(sds);
		for (JPanel sdp : synDiaPanelList) {
			sdp.setSize(sdp.getWidth(), sdp.getHeight()
					+ renderer.getRenderValues().radius + 2);
			for (Component c : sdp.getComponents()) {
				c.addMouseListener(synDiaMouseListener);
			}
			sdp.addMouseListener(synDiaMouseListener);
		}
		((GuiController) controller).setRenderMap(renderMap);
		((GuiController) controller).hasBeenDrawn();
	}

}
