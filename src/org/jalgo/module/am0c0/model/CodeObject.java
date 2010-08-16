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
 * Abstract basic code model which has an {@link Address} and code text
 * 
 * @author Felix Schmitt
 * 
 */
public abstract class CodeObject {

	protected Address address;

	/**
	 * constructor
	 * 
	 * @param address
	 *            the {@link Address} of this CodeObject
	 */
	public CodeObject(Address address) {
		setAddress(address);
	}

	/**
	 * returns the string representation (address + code text)
	 * 
	 * @return string representation
	 */
	@Override
	public String toString() {
		return getAddress().toString() + ": " + getCodeText();
	}

	/**
	 * returns the {@link Address} associated with this code
	 * 
	 * @return the {@link Address}
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * sets the {@link Address} associated with this code
	 * 
	 * @param address
	 *            new {@link Address}
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * returns the code text as a string
	 * 
	 * @return code text String
	 */
	public abstract String getCodeText();

	/**
	 * returns the number of lines this code spans over
	 * 
	 * @return number of lines
	 */
	public abstract int getLinesCount();
}
