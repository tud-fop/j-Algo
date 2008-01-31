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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jalgo.module.ebnf.controller.ebnf.EbnfController;
import org.jalgo.module.ebnf.model.ebnf.EVariable;

/**
 * 
 * @author Tom
 *
 */public class ModifyDefinitionActionListener implements ActionListener, MouseListener {
	 
	 private GuiController guiController;
	 private EbnfController ebnfController;
	 
	 public ModifyDefinitionActionListener(GuiController guiController) {
		 this.guiController = guiController;
		 this.ebnfController = guiController.getEbnfController();
	 }
	 

	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("addVariable")) {
			
			try {
				ebnfController.addVariable(guiController.getInputPanel().addVariableTextField.getText());
				guiController.getInputPanel().hideInfo();
				guiController.getInputPanel().addVariableTextField.setText("");
			}catch (Exception ex)
			{
				guiController.getInputPanel().showInfo(ex.getMessage(), true);
			}

			guiController.getInputPanel().addVariableTextField.grabFocus();
			
		} else if (e.getActionCommand().equals("addTerminal")) {
			try {
				ebnfController.addTerminal(guiController.getInputPanel().addTerminalTextField.getText());
				guiController.getInputPanel().hideInfo();
				guiController.getInputPanel().addTerminalTextField.setText("");
			} catch (Exception ex)
			{
				guiController.getInputPanel().showInfo(ex.getMessage(), true);
			}

			guiController.getInputPanel().addTerminalTextField.grabFocus();
			
		} else if (e.getActionCommand().equals("addRule")) {
			try {
				ebnfController.addRule((EVariable) guiController.getInputPanel().addRuleLeftSideComboBox.getSelectedItem(), guiController.getInputPanel().addRuleRightSideTextbox.getText());
				guiController.getInputPanel().hideInfo();
				guiController.getInputPanel().addRuleRightSideTextbox.setText("");
			} catch (Exception ex)
			{
				guiController.getInputPanel().showInfo(ex.getMessage(), true);
			}
			guiController.getInputPanel().addRuleRightSideTextbox.grabFocus();
		} else if (e.getActionCommand().equals("cancelRule")) {
			guiController.cancelRule();
		} else if (e.getActionCommand().equals("changeRule")) {
			guiController.changeRule();
		} else if (e.getActionCommand().equals("setStartVariable")) {
			try {
				ebnfController.setStartVariable((EVariable)guiController.getInputPanel().startVariableComboBox.getSelectedItem());
			} catch (Exception ex) {
				guiController.getInputPanel().showInfo(ex.getMessage(), true);
			}
		} else if (e.getActionCommand().equals("cancelTerminal")) {
			guiController.cancelTerminal();
		} else if (e.getActionCommand().equals("changeTerminal")) {
			guiController.changeTerminal();
		} else if (e.getActionCommand().equals("cancelVariable")) {
			guiController.cancelVariable();
		} else if (e.getActionCommand().equals("changeVariable")) {
			guiController.changeVariable();
		}
		
//		System.out.println("Action: " + e.getActionCommand());
//		System.out.println(ebnfController.getDefinition().toString());
//		System.out.println("----");
	}


	public void mouseClicked(MouseEvent arg0) {
		// only necessary to implement interface
	}


	public void mousePressed(MouseEvent arg0) {
//		 only necessary to implement interface
	}


	public void mouseReleased(MouseEvent e) {
		guiController.getInputPanel().hideInfo();
		
	}


	public void mouseEntered(MouseEvent arg0) {
//		 only necessary to implement interface
		
	}


	public void mouseExited(MouseEvent arg0) {
//		 only necessary to implement interface
		
	}
 }
