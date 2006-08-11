package org.jalgo.module.ebnf.gui.wordalgorithm.events;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.wordalgorithm.GuiController;

/**
 * ToolbarButtonAction represents a Button in the Toolbar. Could be an Undo, a
 * Redo, an UndoAll or a RedoAsMuchAsPossible.
 * 
 * @author Claas Wilke
 */
@SuppressWarnings("serial")
public class ToolbarButtonAction extends AbstractAction {

	private GuiController myGuiController;

	// Specifies the Button Type
	// 2 is Undo
	// 3 is Redo
	// (default is 2)
	private int myType = 2;

	/**
	 * Constructs a <code>ToolbarButtonAction</code> object with the given
	 * references.
	 * 
	 * @param myGuiController
	 *            the <code>GuiController</code> instance usted to undo or
	 *            redo Actions.
	 */
	public ToolbarButtonAction(GuiController myGuiController, int type) {
		super();
		this.myGuiController = myGuiController;

		initType(type);
	}

	/**
	 * Initiliazes the Type of Button
	 * 
	 * @param type
	 *            The type the Button should be. 2 is Undo 3 is Redo.
	 */
	private void initType(int type) {
		this.myType = type;
		switch (type) {
		case 3: {
			putValue(NAME, Messages.getString("ebnf", "Trans.Perform"));
			putValue(SHORT_DESCRIPTION, Messages.getString("ebnf",
					"WordAlgo.ToolBar_Redo"));
			putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("main",
					"Icon.Redo")));
			break;
		}
		default: {
			putValue(NAME, Messages.getString("ebnf", "Trans.Undo"));
			putValue(SHORT_DESCRIPTION, Messages
					.getString("ebnf", "WordAlgo.ToolBar_Undo"));
			putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("main",
					"Icon.Undo")));
		}
		}
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		switch (myType) {
		case 3: {
			// Redo
			try {
				myGuiController.getWordAlgoController().redo();
			} catch (Exception anException) {
				anException.printStackTrace();
			}
			break;
		}
		default: {
			// Undo
			try {
				myGuiController.getWordAlgoController().undo();
			} catch (Exception anException) {
				anException.printStackTrace();
			}
		}
		}
	}
}