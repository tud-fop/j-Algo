/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/* Created on 07.05.2007 */
package org.jalgo.module.pulsemem.gui.events;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import java.io.*;

import org.jalgo.main.util.Messages;
import org.jalgo.module.pulsemem.Admin;
import org.jalgo.module.pulsemem.Controller;
import org.jalgo.module.pulsemem.gui.GUIController;

/**
 * NextBreakpointAction.java
 * <p>
 * Will import a C-File.
 * <p>
 */
public class ExportCFileAction extends AbstractAction {

    private GUIController gui;

    private Controller controller;

    /**
     * @param gui
     * @param controller
     */
    public ExportCFileAction(GUIController gui, Controller controller) {

        this.gui = gui;
        this.controller = controller;
                putValue(NAME, Messages.getString(
                "pulsemem", "ExportCFileAction.exportCShort")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, Messages.getString(
                "pulsemem", "ExportCFileAction.exportCLong")); //$NON-NLS-1$
        putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("pulsemem",
                "ExportCFile")));
    }

    /*
     * creates JFileChooser, checks whether a file has to be overwritten (asks User in that case)
     * then writes into choosen file
     */
    public void actionPerformed(ActionEvent e) {

    	final JFileChooser fc = new JFileChooser();
    	fc.setCurrentDirectory(new File("./examples/pulsemem"));
    	fc.setFileFilter(new FileFilter() { //shows C-Files only

    		public boolean accept(File f) {
    			if (f.isDirectory()) return true;
    			String ext = f.getName().substring(f.getName().length()-2,f.getName().length());
    			return (ext.equals(".c"));
    		}

    		public String getDescription() {
    			return Admin.getLanguageString("gui.FileFilterDescription");
    		}
    	});

    	//creates SaveDialogs until a legal operation was performed
    	while (true){
    		int returnVal = fc.showSaveDialog(this.gui.getMemPanel());
    		File file = fc.getSelectedFile();
    		if (returnVal == JFileChooser.APPROVE_OPTION) {
    			if (file.exists()) {
    				if (JOptionPane.showConfirmDialog(
    						gui.getMemPanel(),
    						Admin.getLanguageString("gui.ExportCFileDialogText"),
    						Admin.getLanguageString("gui.ExportCFileDialogTitle"),
    						JOptionPane.YES_NO_OPTION) == 1 )
    					continue; //new SaveDialog, if user chooses not to overwrite
    			}

    			String fileStr= file.getPath();
    			if (!(fileStr.substring(fileStr.length()-2,fileStr.length()).equals(".c")))
    				fileStr+=".c";

    			try {
    				BufferedWriter out = new BufferedWriter(new FileWriter(fileStr));
    				out.write(controller.getGUIController().getSourceCode());
    				out.close();
    				break; //break if file was written successfully
    			}
    			catch (FileNotFoundException ex)  {
    				ex.printStackTrace();
    			}
    			catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		} else break; //break if user chooses "Cancel" in SaveDialog
    	}
    }

}
