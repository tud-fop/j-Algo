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
 * This class represents a memory cell which is used in the following abstract
 * machine elements: <li>{@link Stack}</li> <li>{@link Stream}</li> <li>
 * {@link InputStream}</li> <li>{@link OutputStream}</li>
 * 
 * @author Max Leuth&auml;user
 */
public class MemoryCell {
	private int value;

	public MemoryCell(final int value) {
		set(value);
	}

	/**
	 * @return the value saved in this cell
	 */
	public int get() {
		return value;
	}

	/**
	 * Set a new value for this cell
	 * 
	 * @param value
	 */
	public void set(final int value) {
		this.value = value;
	}
}
