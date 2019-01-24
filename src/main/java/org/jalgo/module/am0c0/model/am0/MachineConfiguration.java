/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.am0c0.model.am0;


import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Configuration for an abstract machine as introduced in the lectures. Holds
 * the following elements:
 * 
 * <p>
 * <li>{@link ProgramCounter}</li>
 * <li>{@link Stack}</li>
 * <li>{@link Ram}</li>
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
	private Ram ram;
	private InputStream inputStream;
	private OutputStream outputStream;

	/**
	 * Creates an empty new machine configuration.
	 */
	public MachineConfiguration() {
		programCounter = new ProgramCounter();
		stack = new Stack();
		ram = new Ram();
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
			ram = new Ram(m.getRam());
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
	 * @param ram
	 * @param inputStream
	 * @param outputStream
	 */
	public MachineConfiguration(int programCounter, LinkedList<Integer> stack,
			TreeMap<Integer, Integer> ram, LinkedList<Integer> inputStream,
			LinkedList<Integer> outputStream) {
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

		if (ram != null) {
			this.ram = new Ram(ram);
		} else {
			this.ram = new Ram();
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

	public Ram getRam() {
		return ram;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}
}
