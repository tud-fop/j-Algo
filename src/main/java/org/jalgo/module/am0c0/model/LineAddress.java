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
package org.jalgo.module.am0c0.model;

/**
 * provides line-based addresses
 * 
 * @author Felix Schmitt
 * 
 */
public class LineAddress extends Address {
	private int address;

	/**
	 * constructor
	 * 
	 * @param address
	 *            line number to initialize with
	 * @throws AddressException 
	 */
	public LineAddress(int address) throws AddressException {
		setLine(address);
		setVisible(true);
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
	 * returns the line number
	 * 
	 * @return line number
	 */
	public int getLine() {
		return address;
	}

	@Override
	/**
	 * compares two LineAddresses using their line numbers
	 */
	public boolean equals(Object o) {
		return (o instanceof LineAddress)
				&& ((LineAddress) o).getLine() == getLine();
	}

	/**
	 * It is asserted that this method is never called. The implementation of
	 * {@link TreeAddress#equals(Object)}, however requests that one must
	 * override {@link Object#hashCode()}.
	 */
	@Override
	public int hashCode() {
		throw new AssertionError();
	}
}
