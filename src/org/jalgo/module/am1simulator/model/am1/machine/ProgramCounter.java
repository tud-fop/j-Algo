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
