/**
 * 
 */
package org.jalgo.module.ebnf.gui.syndia;

import javax.swing.JScrollPane;

import org.jalgo.module.ebnf.gui.syndia.display.AbstractControlPanel;
import org.jalgo.module.ebnf.gui.syndia.display.DrawPanel;

/**
 * @author Andre
 * 
 */
public class AbstractSDGuiController {

	protected DrawPanel drawPanel;

	protected AbstractControlPanel controlPanel;

	protected JScrollPane drawScrollPane;

	/**
	 * Updates the DrawPanel (where the SyntaxDiagrams are located).
	 * 
	 * @param size
	 *            The size of the Font associated to the SyntaxDiagram.
	 */
	public void resizeDrawPanel(int size) {

		drawPanel.resizeSystem(size);
		this.drawScrollPane.validate();

	}

	/**
	 * Sets zoom to autosize.
	 * 
	 * @param autosize
	 *            <code>true</code> if you want to autosize.
	 */
	public void setAutoSize(boolean autosize) {
		drawPanel.setAutoSize(autosize);
		if (autosize)
			drawPanel.update(null, null);
		this.drawScrollPane.validate();
	}

	/**
	 * Sets the Zoomers value
	 * 
	 * @param size
	 *            new size
	 */
	public void setZoomerValue(int size) {
		controlPanel.setZoomerValue(size);
	}

	/**
	 * Returns the drawPanel.
	 * 
	 * @return the drawPanel
	 */
	public DrawPanel getDrawPanel() {
		return drawPanel;
	}

}
