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

import java.util.List;

/**
 * This class represents an input stream for an abstract machine.
 * 
 * @author Max Leuth&auml;user
 */
public class OutputStream extends Stream {

	/**
	 * Create a new empty output stream
	 */
	public OutputStream() {
		super();
	}

	/**
	 * Copyconstructor
	 * 
	 * @param i
	 *            {@link OutputStream} which should be copied.
	 */
	public OutputStream(OutputStream i) {
		super();
		for (MemoryCell m : i.stream) {
			this.stream.add(new MemoryCell(m.get()));
		}
	}

	/**
	 * Create a new output stream based on an already existing and filled list
	 * of values.
	 * 
	 * @param initList
	 */
	public OutputStream(final List<Integer> initList) {
		for (int i : initList) {
			stream.add(new MemoryCell(i));
		}
	}

	/**
	 * Write a new value on the output stream.
	 * 
	 * @param value
	 */
	public void write(final int value) {
		stream.add(new MemoryCell(value));
	}
}
