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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.util.Messages;
import org.jalgo.module.pulsemem.Admin;
import org.jalgo.module.pulsemem.Controller;
import org.jalgo.module.pulsemem.core.CodeGenerator;
import org.jalgo.module.pulsemem.gui.GUIConstants;
import org.jalgo.module.pulsemem.gui.GUIController;
import c00.AST;

/**
 * ImportCFileAction.java
 * <p>
 * Imports a C00-File.
 * <p>
 */
public class ImportCFileAction extends AbstractAction {

    private GUIController gui;

    private Controller controller;

    /**
     * @param gui
     * @param controller
     */
    public ImportCFileAction(GUIController gui, Controller controller) {
        this.gui = gui;
        this.controller = controller;
        putValue(NAME, Messages.getString(
                "pulsemem", "ImportCFileAction.importCShort")); //$NON-NLS-1$
        putValue(SHORT_DESCRIPTION, Messages.getString(
                "pulsemem", "ImportCFileAction.importCLong")); //$NON-NLS-1$
        putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("pulsemem",
                "ImportCFile")));
    }

    /*
     * creates JFileChooser, opens File, reads it as a string, and sets SourceCodeField
     */
    private String parseAndBeautify(String fileString)
	{
		try
		{
			AST.Program program = (AST.Program)AST.parseString(fileString, "Program");
			CodeGenerator gen = new CodeGenerator();
			gen.SwitchProgram(program);
			return gen.getOutput();
		}
		catch (Exception e)
		{
			gui.showErrorMessage(Admin.getLanguageString("gui.ImportError"));
			return fileString;
		}

	}

    public void actionPerformed(ActionEvent e) {

    	final JFileChooser fc = new JFileChooser();
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
    	fc.setCurrentDirectory(new File("./examples/pulsemem"));
        int returnVal = fc.showOpenDialog(this.gui.getMemPanel());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            StringBuffer fileData = new StringBuffer(1000);
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                char[] buf = new char[1024];
                int numRead=0;
                while((numRead=reader.read(buf)) != -1){
                    String readData = String.valueOf(buf, 0, numRead);
                    fileData.append(readData);
                    buf = new char[1024];
                }
                reader.close();
            }
            catch (FileNotFoundException ex)  {
            	ex.printStackTrace();
            }
            catch (IOException ex) {
            	ex.printStackTrace();
            }

            //clear saved In- and Output
            gui.clearInput();
        	//delete InlineBreakpoints the user set
        	gui.getInlineBreakpoints().clear();
        	//delete PMLines and updateTable
        	controller.clearPM();
        	gui.getMemPanel().updateTable();
        	//switch to Edit-Mode (switchParseStopEnabled)
        	gui.switchParseStopEnabled(GUIConstants.PARSE_DISABLED);
        	//change SaveStatus
        	controller.getConnector().setSaveStatus(SaveStatus.CHANGES_TO_SAVE);

        	//format importet Code
            String formattedCode = parseAndBeautify(fileData.toString());
            gui.setSourceCode(formattedCode);
        }
    }
}
