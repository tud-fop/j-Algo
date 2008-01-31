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

/**
 * 
 * @author Tom
 *
 */public class EditRuleActionListener implements ActionListener {
	 
	 private GuiController guiController;

	public EditRuleActionListener(GuiController controller) {
		this.guiController = controller;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ebnfParentLeft")) guiController.getInputPanel().addSpecialChar("\u3011");
		else if (e.getActionCommand().equals("ebnfParentRight")) guiController.getInputPanel().addSpecialChar("\u3012");
		else if (e.getActionCommand().equals("ebnfBar")) guiController.getInputPanel().addSpecialChar("\u300E");
		else if (e.getActionCommand().equals("ebnfBraceRight")) guiController.getInputPanel().addSpecialChar("\u3010");
		else if (e.getActionCommand().equals("ebnfBraceLeft")) guiController.getInputPanel().addSpecialChar("\u300F");
		else if (e.getActionCommand().equals("ebnfBracketLeft")) guiController.getInputPanel().addSpecialChar("\u300C");
		else if (e.getActionCommand().equals("ebnfBracketRight")) guiController.getInputPanel().addSpecialChar("\u300D");
		else if (e.getActionCommand().equals("ebnfLess")) guiController.getInputPanel().addSpecialChar("\u3008");
		else if (e.getActionCommand().equals("ebnfMore")) guiController.getInputPanel().addSpecialChar("\u3009");
		
		else if (e.getActionCommand().equals("inputfinished")) guiController.showChoiceGUI(guiController.getEbnfController().isTransActive());
		else if (e.getActionCommand().equals("checkdefinition")) guiController.showWarningsDialog(true, true);
	}
	
 }
