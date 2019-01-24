/**
 * AM1 Simulator - simulating am1 code in an abstract machine based on the
 * definitions of the lectures 'Programmierung' at TU Dresden.
 * Copyright (C) 2010 Max Leuthäuser
 * Contact: s7060241@mail.zih.tu-dresden.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jalgo.module.am1simulator.model.am1.machine;

import java.util.LinkedList;


/**
 * Configuration for an abstract machine (AM1) as introduced in the lectures.
 * Holds the following elements:
 * 
 * <p>
 * <li>{@link ProgramCounter}</li>
 * <li>{@link Stack}</li>
 * <li>{@link RuntimeStack}</li>
 * <li>{@link Ref}</li>
 * <li>{@link InputStream}</li>
 * <li>{@link OutputStream}</li>
 * </p>
 * <br>
 * 
 * <p>
 * It is possible to do a deep copy. Use the copyconstructor:
 * {@link MachineConfiguration#MachineConfiguration(MachineConfiguration)}
 * </p>
 * <br>
 * 
 * @author Max Leuth&auml;user
 */
public class MachineConfiguration {
	private ProgramCounter programCounter;
	private Stack stack;
	private RuntimeStack runtimeStack;
	private Ref ref;
	private InputStream inputStream;
	private OutputStream outputStream;

	/**
	 * Creates an empty new machine configuration.
	 */
	public MachineConfiguration() {
		programCounter = new ProgramCounter();
		stack = new Stack();
		runtimeStack = new RuntimeStack();
		ref = new Ref();
		inputStream = new InputStream();
		outputStream = new OutputStream();
	}

	/**
	 * Copyconstructor
	 * 
	 * @param m
	 *            {@link MachineConfiguration} which should be copied.
	 */
	public MachineConfiguration(MachineConfiguration m) {
		if (m != null) {
			programCounter = new ProgramCounter(m.getProgramCounter());
			stack = new Stack(m.getStack());
			runtimeStack = new RuntimeStack(m.getRuntimeStack());
			ref = new Ref(m.getRef());
			inputStream = new InputStream(m.getInputStream());
			outputStream = new OutputStream(m.getOutputStream());
		} else {
			throw new IllegalArgumentException(
					"Null argument is not allowed here!");
		}
	}

	/**
	 * Create a new machine configuration with filled values. Use a parameter
	 * with <b>null</b> will create a new and empty element in the abstract
	 * machine.
	 * 
	 * @param programCounter
	 *            Use 0 here to set it to initial value.
	 * @param stack
	 * @param runtimeStack
	 * @param ref
	 * @param inputStream
	 * @param outputStream
	 */
	public MachineConfiguration(int programCounter, LinkedList<Integer> stack,
			LinkedList<RuntimeStackEntry> runtimeStack, int ref,
			LinkedList<Integer> inputStream, LinkedList<Integer> outputStream) {
		if (programCounter != 0) {
			this.programCounter = new ProgramCounter(programCounter);
		} else {
			this.programCounter = new ProgramCounter();
		}

		if (stack != null) {
			this.stack = new Stack(stack);
		} else {
			this.stack = new Stack();
		}

		if (runtimeStack != null) {
			this.runtimeStack = new RuntimeStack(runtimeStack);
		} else {
			this.runtimeStack = new RuntimeStack();
		}

		if (ref != 1) {
			this.ref = new Ref(ref);
		} else {
			this.ref = new Ref();
		}

		if (inputStream != null) {
			this.inputStream = new InputStream(inputStream);
		} else {
			this.inputStream = new InputStream();
		}

		if (outputStream != null) {
			this.outputStream = new OutputStream(outputStream);
		} else {
			this.outputStream = new OutputStream();
		}
	}

	public ProgramCounter getProgramCounter() {
		return programCounter;
	}

	public Stack getStack() {
		return stack;
	}

	public RuntimeStack getRuntimeStack() {
		return runtimeStack;
	}

	public Ref getRef() {
		return ref;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Returns a HTML formatted string containing all components with their
	 * contents.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "( <font bgcolor=\"yellow\">" + programCounter.toString()
				+ "</font>" + ",&nbsp;&nbsp;" + "<font bgcolor=\"yellow\">"
				+ stack.toString() + "</font>" + ",&nbsp;&nbsp;"
				+ "<font bgcolor=\"yellow\">" + runtimeStack.toString()
				+ "</font>" + ",&nbsp;&nbsp;" + "<font bgcolor=\"yellow\">"
				+ ref.toString() + "</font>" + ",&nbsp;&nbsp;"
				+ "<font bgcolor=\"yellow\">" + inputStream.toString()
				+ "</font>" + ",&nbsp;&nbsp;" + "<font bgcolor=\"yellow\">"
				+ outputStream + "</font>" + " )";
	}
}
