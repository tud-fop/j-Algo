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

package org.jalgo.module.am1simulator.model;

/**
 * Provides line-based addresses
 * 
 * @author Felix Schmitt
 * @author Max Leuth&auml;user
 */
public class LineAddress {
	private int address;

	/**
	 * Constructor
	 * 
	 * @param address
	 *            line number to initialize with
	 * @throws AddressException
	 */
	public LineAddress(int address) throws AddressException {
		setLine(address);
	}

	/**
	 * set the line number
	 * 
	 * @param address
	 *            new line number. must be >= 0
	 */
	public void setLine(int address) throws AddressException {
		if (address >= 0)
			this.address = address;
		else
			throw new AddressException("A line address must not be < 0");
	}

	/**
	 * returns string representation
	 * 
	 * @return string representation
	 */
	@Override
	public String toString() {
		return String.valueOf(address);
	}

	/**
	 * Returns the line number
	 * 
	 * @return line number
	 */
	public int getLine() {
		return address;
	}

	/**
	 * Compares two LineAddresses using their line numbers
	 */
	@Override
	public boolean equals(Object o) {
		return (o instanceof LineAddress)
				&& ((LineAddress) o).getLine() == getLine();
	}

	/**
	 * It is asserted that this method is never called.
	 */
	@Override
	public int hashCode() {
		throw new AssertionError();
	}
}
