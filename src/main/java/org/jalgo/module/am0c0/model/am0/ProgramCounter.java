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


/**
 * Class which represents the program counter in an abstract machine.
 * 
 * @author Max Leuth&auml;user
 */
public class ProgramCounter {
	private int pc;

	/**
	 * Create a new program counter which is set to 1.
	 */
	public ProgramCounter() {
		pc = 1;
	}

	/**
	 * Copyconstructor
	 * 
	 * @param i
	 *            {@link ProgramCounter} which should be copied.
	 */
	public ProgramCounter(ProgramCounter i) {
		pc = i.get();
	}

	/**
	 * Create a new program counter which is set to the given value.
	 * 
	 * @param value
	 */
	public ProgramCounter(final int value) {
		pc = value;
	}

	/**
	 * @return the current program counter
	 */
	public int get() {
		return pc;
	}

	/**
	 * Set a new program counter.
	 * 
	 * @param value
	 */
	public void set(final int value) {
		pc = value;
	}

	/**
	 * Increase the program counter.
	 */
	public void inc() {
		pc++;
	}

	/**
	 * @return the program counter as String
	 */
	@Override
	public String toString() {
		return "" + pc;
	}
}
