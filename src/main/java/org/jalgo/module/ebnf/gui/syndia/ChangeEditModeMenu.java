package org.jalgo.module.ebnf.gui.syndia;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;

/**
 * A popup menu to change the edit mode in the syntax diagram editor. It is
 * ActionListener for all menu items.
 * 
 * @author Michael Thiele
 * 
 */
public class ChangeEditModeMenu extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 3596209418605843774L;

	private GuiController guiController;
	
	private MouseListener myStatusUpdater;

	/**
	 * Initalizes the popup menu.
	 * 
	 * @param guiController
	 */
	public ChangeEditModeMenu(GuiController guiController) {
		this.guiController = guiController;

		this.setBackground(Color.WHITE);

		JMenuItem modeEdit = new JMenuItem(new ImageIcon(Messages
				.getResourceURL("ebnf", "ModeEdit_Image")));
		modeEdit.addActionListener(this);
		add(modeEdit);

		JMenuItem modeDelete = new JMenuItem(new ImageIcon(Messages
				.getResourceURL("ebnf", "ModeDelete_Image")));
		modeDelete.addActionListener(this);
		add(modeDelete);

		this.addSeparator();

		JMenuItem modeAddTerminal = new JMenuItem(new ImageIcon(Messages
				.getResourceURL("ebnf", "ModeAddTerminal_Image")));
		modeAddTerminal.addActionListener(this);
		add(modeAddTerminal);

		JMenuItem modeAddVariable = new JMenuItem(new ImageIcon(Messages
				.getResourceURL("ebnf", "ModeAddVariable_Image")));
		modeAddVariable.addActionListener(this);
		add(modeAddVariable);

		JMenuItem modeBranch = new JMenuItem(new ImageIcon(Messages
				.getResourceURL("ebnf", "ModeAddBranch_Image")));
		modeBranch.addActionListener(this);
		add(modeBranch);

		JMenuItem modeRepetition = new JMenuItem(new ImageIcon(Messages
				.getResourceURL("ebnf", "ModeAddRepetition_Image")));
		modeRepetition.addActionListener(this);
		add(modeRepetition);

		this.myStatusUpdater = StatusLineUpdater.getInstance();
		
		modeEdit.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_Edit"));
		modeEdit.addMouseListener(myStatusUpdater);
		modeDelete.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_Delete"));
		modeDelete.addMouseListener(myStatusUpdater);
		modeAddTerminal.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_AddTerminal"));
		modeAddTerminal.addMouseListener(myStatusUpdater);
		modeAddVariable.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_AddVariable"));
		modeAddVariable.addMouseListener(myStatusUpdater);
		modeBranch.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_AddBranch"));
		modeBranch.addMouseListener(myStatusUpdater);
		modeRepetition.setToolTipText(Messages.getString("ebnf",
				"SynDiaEditor.ToolTip_AddRepetition"));
		modeRepetition.addMouseListener(myStatusUpdater);

	}

	/**
	 * Changes the edit mode if a menu button was pressed.
	 */
	public void actionPerformed(ActionEvent e) {
		String event = e.getSource().toString();
		// a bad trick to know which element was clicked: getSource contains the
		// path of the image -> is also in Messages.getResourceURL(...)
		if (event.contains(Messages.getResourceURL("ebnf", "ModeEdit_Image")
				.toString())) {
			guiController.setModeEdit();
		} else if (event.contains(Messages.getResourceURL("ebnf",
				"ModeAddTerminal_Image").toString())) {
			guiController.setModeAddTerminal();
		} else if (event.contains(Messages.getResourceURL("ebnf",
				"ModeAddVariable_Image").toString())) {
			guiController.setModeAddVariable();
		} else if (event.contains(Messages.getResourceURL("ebnf",
				"ModeAddBranch_Image").toString())) {
			guiController.setModeAddBranch();
		} else if (event.contains(Messages.getResourceURL("ebnf",
				"ModeAddRepetition_Image").toString())) {
			guiController.setModeAddRepetition();
		} else if (event.contains(Messages.getResourceURL("ebnf",
				"ModeDelete_Image").toString())) {
			guiController.setModeDelete();
		}
	}
}
