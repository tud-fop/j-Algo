/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and
 * platform independent. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jalgo.module.ebnf.gui.ebnf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import org.jalgo.main.util.Messages;

/**
 * 
 * @author Tom
 *
 */public class ChoicePanelActionListener implements ActionListener {
	
	private GuiController guiController;
	 
	public ChoicePanelActionListener(GuiController guiController) {
		this.guiController = guiController;
	}
	
	/**
	 * The actions for the three buttons on top are defined and proceeded here
	 * 
	 */public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("switchtotransgui")) {
			guiController.setEditMode(false);
			guiController.getEbnfController().switchToTransGUI();
		} else if (e.getActionCommand().equals("editdefinition")) {
			if (guiController.getStrictMode() && !guiController.getEbnfController().getDefinition().isStrict()) {
				int saveStrictMode = guiController.showSaveStrictModeDialog(true);
				if (saveStrictMode == -1) return;
				else if (saveStrictMode == 1) {
					guiController.getEbnfController().setDefinitionStrict();
				}
			}
			guiController.setStrictMode(false);
			guiController.setEditMode(true);
			guiController.getRuleViewPanel().removeAll();
			guiController.getRuleViewPanel().drawRules();
			guiController.showInputGUI();
		} else if (e.getActionCommand().equals("strictdefinition")) {
			boolean strictmode = guiController.getStrictMode();
			if (!((JToggleButton)e.getSource()).isSelected()) {
				((JToggleButton)e.getSource()).setText(Messages.getString("ebnf", "Ebnf.ChoicePanel_strictButton"));
			} else {
				((JToggleButton)e.getSource()).setText(Messages.getString("ebnf", "Ebnf.ChoicePanel_strictButton2"));
			}
			guiController.setStrictMode(((JToggleButton)e.getSource()).isSelected());
			guiController.getRuleViewPanel().removeAll();
			guiController.getRuleViewPanel().drawRules();
			guiController.getRuleViewPanel().repaint();
		} 

	}
 }
