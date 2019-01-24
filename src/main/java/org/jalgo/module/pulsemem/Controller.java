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
/* Created on 05.05.2007 */
package org.jalgo.module.pulsemem;

import java.util.ArrayList;
import java.util.List;
import o3b.antlr.runtime.RecognitionException;
import org.jalgo.module.pulsemem.core.CodeGenerator;
import org.jalgo.module.pulsemem.core.PulsMemLine;
import org.jalgo.module.pulsemem.core.myInterpreter;
import org.jalgo.module.pulsemem.gui.GUIConstants;
import org.jalgo.module.pulsemem.gui.GUIController;
import org.jalgo.module.pulsemem.gui.components.InlineBreakpoint;
import c00.AST;
import c00.parser.ParseException;

/**
 * Controller.java
 * <p>
 * The class <code>Controller</code> is an important connection between the
 * GUI and the working model.
 * <p>
 *
 * @version $Revision: 1.21 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 *
 */
public class Controller {
	private final ModuleConnector connector;
	private final GUIController gui;
	private AST.Program program;
	private boolean validSourceCode = false;
	private myInterpreter interpreter;
	private CodeGenerator currentCodeGen;

	/**
	 * The constructor of the type.
	 *
	 * @param c
	 *            An instance of the ModuleConnector, used in the current
	 *            environment.
	 */
	public Controller(ModuleConnector c) {
		currentCodeGen = new CodeGenerator();
		connector = c;
		gui = new GUIController(connector, this);
	}

	/**
	 * Returns the instance of the ModuleConnector, used in the current
	 * environment.
	 *
	 * @return The instance of the ModuleConnector, used in the current
	 *         environment.
	 */
	public ModuleConnector getConnector() {
		return connector;
	}

	public CodeGenerator getCodeGenerator() {
		return currentCodeGen;
	}

	/**
	 * Returns the GUIController used in the current environment.
	 *
	 * @return The GUIController used in the current environment.
	 */
	public GUIController getGUIController() {
		return gui;
	}

	/**
	 * Specifies if the source code is valid.
	 *
	 * @return true, if the source code is valid.
	 */
	public boolean isSourceCodeValid() {
		return validSourceCode;
	}

	/**
	 * clears the PM (deletes all PM-Lines)
	 *
	 */
	public void clearPM() {
		if (!(this.interpreter == null))
			this.interpreter.clearPm();
	}

	/**
	 * parses the given source code.
	 *
	 * @param text
	 *            the source code to parse.
	 * @throws Exception
	 *             the exception thrown.
	 */
	private void parse(String source) {
		try {
			program = (AST.Program) AST.parseString(source, "Program", null, false);
		} catch (RecognitionException e) {
			throw new ParseException(e.getMessage());
		}
	}

	/**
	 * Beautifies the given source.
	 *
	 * @param source
	 *            The source code that should be formatted.
	 * @return The formatted source code or the input string if an error
	 *         occured.
	 */
	public String beautify(String source) {
		String result;
		try {
			AST.Program program = (AST.Program) AST.parseString(source, "Program");
			CodeGenerator gen = new CodeGenerator();
			gen.SwitchProgram(program);
			result = gen.getOutput();
		} catch (Exception e) {
			result = source;
		}
		return result;
	}
	
	/**
	 * Interprets and beautifies the source code within the source textbox. Is
	 * either called with the standart method interpretAndBeautify() oder with
	 * interpretAndBeautifyWithInternalIO, which is used for loading from Jalgo
	 * Files
	 *
	 * @return true, if the source code was successfully parsed and interpreted.
	 */
	private boolean interpretAndBeautifyCore() {
		gui.clearInput();
		String errorMsg = null;
		String originalSource = gui.getSourceCode();
		try {
			// parsing tier 1
			parse(gui.getSourceCode());
			// beautifier tier 1
			currentCodeGen = new CodeGenerator();
			currentCodeGen.SwitchProgram(program);
			// parsing tier 2
			gui.setSourceCode(currentCodeGen.getOutputWithRM());
			parse(currentCodeGen.getOutput());
			currentCodeGen = new CodeGenerator();
			currentCodeGen.SwitchProgram(program);
			try {
				interpreter.runInterpreter(program);
			} catch (ParseException e) {
				errorMsg = Admin.getLanguageString("gui.ParsingError");
				throw e;
			} catch (Exception e) {
				errorMsg = Admin.getLanguageString("gui.InterpretationError");
				throw e;
			}
		} catch (Throwable e) {
			interpreter = null;
			System.gc();
			if (errorMsg == null)
				errorMsg = Admin.getLanguageString("gui.UnknownInterpretationError");
			errorMsg += ": " + e.getMessage();
			gui.switchParseStopEnabled(GUIConstants.PARSE_DISABLED);
		} finally {
			if (!(validSourceCode = (errorMsg == null))) {
				gui.setSourceCode(originalSource);
				gui.showErrorMessage(errorMsg);
			}
			else {
				// add Breakpoints where a Label is 'hardcoded'
				InlineBreakpoint inBP = gui.getInlineBreakpoints();
				for (PulsMemLine thisLine : (List<PulsMemLine>) interpreter.getPm()) {
					if (thisLine.isLabel())
						inBP.addLine(thisLine.getCodeLine());
				}
				gui.updateTableBreakpoints();
				gui.setTableData(interpreter.getPm());
			}
		}
		return validSourceCode;
	}

	/**
	 * standard-method for interpreting, see interpretAndBeautifyCore()
	 *
	 * @return true, if the source code was successfully parsed and
	 *         interpreted.@return
	 */
	public boolean interpretAndBeautify() {
		interpreter = new myInterpreter();
		interpreter.setIOSimulation(gui);
		return interpretAndBeautifyCore();
	}

	/**
	 * method for interpreting with internal IO, used for Loading see
	 * interpretAndBeautifyCore()
	 *
	 * @return true, if the source code was successfully parsed and
	 *         interpreted.@return
	 */
	public boolean interpretAndBeautifyWithInternalIO() {
		interpreter = new myInterpreter();
		interpreter.setIOSimulation(connector);
		return interpretAndBeautifyCore();
	}

	/**
	 * Restores the formatted input Source without return marks.
	 *
	 */
	public void restoreEditorModeSourceCode() {
		if (currentCodeGen == null)
			return;
		gui.setSourceCode(currentCodeGen.getOutput());
	}
}
