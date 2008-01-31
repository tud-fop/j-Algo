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

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.ebnf.TermPanel.Type;
import org.jalgo.module.ebnf.model.ebnf.Definition;
import org.jalgo.module.ebnf.model.ebnf.ESymbol;
import org.jalgo.module.ebnf.model.ebnf.ETerminalSymbol;
import org.jalgo.module.ebnf.model.ebnf.EVariable;

/**
 * 
 * @author Tom
 *
 */public class EditDefinitionActionListener implements ActionListener, MouseListener, PopupMenuListener{

	 private ESymbol symbol;
	 private GuiController guiController;
	 private boolean menuOpen;
	 
	 private TermPanel mouseoverPanel;
	 
	 private JPopupMenu pMenu;
	private JMenuItem edititem = new JMenuItem(Messages.getString("ebnf", "Ebnf.PopupMenu_Edit"));
	private JMenuItem deleteitem = new JMenuItem(Messages.getString("ebnf", "Ebnf.PopupMenu_Delete"));
	private JMenuItem startitem = new JMenuItem(Messages.getString("ebnf", "Ebnf.PopupMenu_SetAsStartRule"));
	private JMenuItem startvar = new JMenuItem(Messages.getString("ebnf", "Ebnf.PopupMenu_SetAsStartVar"));
	 
	public EditDefinitionActionListener(GuiController guiController) {
		this.guiController = guiController;
		
		edititem.addActionListener(this);
		deleteitem.addActionListener(this);
		startitem.addActionListener(this);
		startvar.addActionListener(this);

		pMenu = new JPopupMenu();
		
		pMenu.addPopupMenuListener(this);
		
		
		
		pMenu.add(edititem);
		pMenu.add(deleteitem);
		pMenu.add(startitem);
	}
	
	public void actionPerformed(ActionEvent e) {
		Definition definition = guiController.getEbnfController().getDefinition();
		try {
			if (mouseoverPanel.type == Type.VARIABLE) { 				// our currently clicked Panel is a variable
				if(e.getSource().equals(deleteitem)) {					// delete variable action
					guiController.getEbnfController().deleteVariable((EVariable)symbol);
				} else if(e.getSource().equals(edititem)) {			// edit variable action
					guiController.editVariable((EVariable)symbol, mouseoverPanel);
				} else if(e.getSource().equals(startvar)) {			// change startVar
					guiController.getEbnfController().setStartVariable((EVariable)symbol);
				}
			} else if (mouseoverPanel.type == Type.TERMINALSYMBOL){		// our currently clicked Panel is a terminalsymbol
				if(e.getSource().equals(deleteitem)) {					// delete terminal action
					guiController.getEbnfController().deleteTerminal((ETerminalSymbol)symbol);
				} else if(e.getSource().equals(edititem)) {			// edit terminal action
					guiController.editTerminal((ETerminalSymbol)symbol, mouseoverPanel);
				}
			} else if (mouseoverPanel.type == Type.RULE) {				// our currently clicked Panel is a rule
				if(e.getSource().equals(deleteitem)) {					// delete rule action
					guiController.getEbnfController().deleteRule(definition.getRule((EVariable)symbol));
				} else if(e.getSource().equals(edititem)) {			// edit rule action
					guiController.editRule(definition.getRule((EVariable)symbol), mouseoverPanel);
				} else if(e.getSource().equals(startitem)) {			// change startVar
					guiController.getEbnfController().setStartVariable((EVariable)symbol);
				}
			}
		} catch (Exception ex) {
			guiController.getInputPanel().showInfo(Messages.getString("ebnf", "Ebnf.Error.Undefined"), true);
			ex.printStackTrace();
		}
//		System.out.println("Action: " + e.getSource());
//		System.out.println(guiController.getEbnfController().getDefinition().toString());
//		System.out.println("----");
	}

	public void mouseClicked(MouseEvent e) {
		// This mehthod is not used here, but has to exist due to the interface
	}

	public void mousePressed(MouseEvent e) {
		// This mehthod is not used here, but has to exist due to the interface
	}

	public void mouseEntered(MouseEvent e) {
		if (guiController.isEditMode()) {
			if(!menuOpen) {
				mouseoverPanel = ((TermPanel)e.getComponent());
				mouseoverPanel.setMouseOver(true);
			}
		}
		
	}

	public void mouseExited(MouseEvent e) {
		if (guiController.isEditMode()) {
			if (!menuOpen) {
				mouseoverPanel.setMouseOver(false);
			} 
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		
		if (guiController.isEditMode()) {
				mouseEntered(e);
				//remove StartVariable-related menu entries
				pMenu.remove(startitem);
				pMenu.remove(startvar);
				symbol = mouseoverPanel.getSymbol();
				
				//and check if (other) StartVariable related menu entries should be added
				if (mouseoverPanel.type == Type.VARIABLE) {		// VARIABLE
					//check if the variable is allowed to be deleted
					if (guiController.getEbnfController().occursInRightSide(symbol)
							|| (guiController.getEbnfController().getDefinition().getRule((EVariable)symbol) != null))
						deleteitem.setEnabled(false);
					else
						deleteitem.setEnabled(true);

					edititem.setText(Messages.getString("ebnf", //$NON-NLS-1$
					"Ebnf.PopupMenu_Rename"));
					if (guiController.getCurrentlyEditedVariable() == null){
						edititem.setEnabled(true);
						
					} else {
						deleteitem.setEnabled(false);
						edititem.setEnabled(false);
					}
					
					pMenu.add(startvar);
				} else if (mouseoverPanel.type == Type.RULE) {		// RULES
					
					
					edititem.setText(Messages.getString("ebnf", //$NON-NLS-1$
					"Ebnf.PopupMenu_Edit"));
					if (guiController.getCurrentlyEditedRule() == null){
						deleteitem.setEnabled(true);
						edititem.setEnabled(true);
					} else {
						deleteitem.setEnabled(false);
						edititem.setEnabled(false);
					}
					
					pMenu.add(startitem);
				} else if (mouseoverPanel.type == Type.TERMINALSYMBOL) {		// TERMINALSYMBOL
					if (guiController.getEbnfController().occursInRightSide(symbol))
						deleteitem.setEnabled(false);
					else
						deleteitem.setEnabled(true);
					
					edititem.setText(Messages.getString("ebnf", //$NON-NLS-1$
					"Ebnf.PopupMenu_Rename"));
					if (guiController.getCurrentlyEditedTerminal() == null) {
						edititem.setEnabled(true);
					} else {
						deleteitem.setEnabled(false);
						edititem.setEnabled(false);
					}
				}
				
				//show menu
				pMenu.show(e.getComponent(), e.getX(), e.getY());
		}	
	}



	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		menuOpen = true;
		
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		menuOpen = false;
		mouseoverPanel.setMouseOver(false);
	}

	public void popupMenuCanceled(PopupMenuEvent e) {
		// This mehthod is not used here, but has to exist due to the interface
	}
 }
