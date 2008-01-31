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
package org.jalgo.module.pulsemem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.module.pulsemem.core.IOSimulation;
import org.jalgo.module.pulsemem.gui.GUIConstants;
import org.jalgo.module.pulsemem.gui.GUIController;
import org.jalgo.module.pulsemem.gui.components.InlineBreakpoint;

public class ModuleConnector extends AbstractModuleConnector implements IOSimulation {
    private Controller controller;

    private GUIController gui;

    private LinkedList<Integer> savedInput = new LinkedList<Integer>();

    static {
    	File f = new File("out.log");
    	
    	try {
			f.createNewFile();
			System.setOut(new PrintStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		f = new File("err.log");
    	
    	try {
			f.createNewFile();
			System.setErr(new PrintStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void init() {    	
        controller = new Controller(this);
        gui = controller.getGUIController();
    }

    @Override
    public void run() {
        gui.showPulseMem();
    }

    @Override
    public void setDataFromFile(ByteArrayInputStream data) {
    	try {
			ObjectInputStream in = new ObjectInputStream(data);
			InlineBreakpoint BPObj = (InlineBreakpoint)in.readObject();
			String CodeObj = (String)in.readObject();

			InlineBreakpoint inBP = gui.getInlineBreakpoints();
			for (int thisBP : (HashSet<Integer>)BPObj.getLineOfBreakpoints()) {
				inBP.addLine(thisBP);
			}
			gui.setSourceCode(CodeObj);

			//this Boolean is saved as true, when the User Saved in
			//parse-Mode
			if (in.readBoolean()) {
				this.savedInput.addAll((List<Integer>)in.readObject());//read saved Input
				gui.switchParseStopEnabled(GUIConstants.PARSE_ENABLED);
				controller.interpretAndBeautifyWithInternalIO();
				gui.getMemPanel().setVisibleLines(in.readInt());//read Visible Lines
				gui.getMemPanel().updateTable();
			}

		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}

    @Override
    public ByteArrayOutputStream getDataForFile() {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(this.gui.getInlineBreakpoints());

			//when in PulseMem-Mode, save the input the User made and currently shown line
			if (gui.getGUIMode()==GUIConstants.PARSE_ENABLED) {
				objOut.writeObject(this.controller.getCodeGenerator().getOutput());
				objOut.writeBoolean(true);
				objOut.writeObject(gui.getInput());
				objOut.writeInt(gui.getMemPanel().getPulseMemTable().getNumberOfVisibleRows());
			}
			else {
				objOut.writeObject(gui.getSourceCode());
				objOut.writeBoolean(false);
			}


			objOut.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return out;
    }

    @Override
    public void print() {
        // no action performed here, JAlgo still doesn't support printing :-)
    }

    /**
     * Is called by the interpreter when interpreting with InternalIO
     * whenever there is data to ouput. (used for loading jAlgo-files)
     * This is exactly the same as in the GUI.output().
     */
    public void output(String output, int line) {
        gui.getLinedTextarea().setCaretToLine(line);
        Document doc = gui.getConsole().getDocument();
        try {
        	String outString = Admin
            .getLanguageString("GUIController.printf_Start")
            + line
            + Admin.getLanguageString("GUIController.printf_End") //$NON-NLS-1$
            + output + "\n";
            doc.insertString(doc.getEndPosition().getOffset() - 1, outString, new SimpleAttributeSet()); //$NON-NLS-1$
        } catch (BadLocationException e) {
            // ignore the error
        }

    }

    /**
     * Is called whenever the interpreter needs input while interpreting with InternalIO
     * (used for loading jAlgo-files)
     *
     * @return the inputs the user made before saving, one by one in the order they were entered
     */
    public int input(int line) {
    	try {
	        gui.getInput().add(savedInput.getFirst());
	        return savedInput.removeFirst();
    	}

    	//this exception should never be thrown, as the number of saved Inputs
    	//always equals the number of inputs the programm requested (and therefore will request on load)
    	//(this is also the reason why the error is not translated :-) )
    	catch (NoSuchElementException e) {
    		gui.showErrorMessage("Critical error while loading JAlgoFile!");
    		return 0;
    	}
    }
}