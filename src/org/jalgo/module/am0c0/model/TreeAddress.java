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

import java.util.ArrayList;
import java.util.List;

/**
 * provides tree structured addresses like 1.2.1.3
 * @author Felix Schmitt
 *
 */
public class TreeAddress extends Address {
	private List<Integer> address;
	
	/**
	 * constructor
	 */
	public TreeAddress() {
		this(null, false);
	}
	
	/**
	 * constructor
	 * default visibility is false
	 * @param base base TreeAddress to copy from. null is allowed
	 */
	public TreeAddress(TreeAddress base) {
		this(base, false);
	}
	
	/**
	 * constructor
	 * @param base base TreeAddress to copy from. null is allowed
	 * @param visible the visibility of this TreeAddress
	 */
	public TreeAddress(TreeAddress base, boolean visible) {
		setVisible(visible);
		
		if (base == null) {
			address = new ArrayList<Integer>();
			address.add(Integer.valueOf(1));
		} else {
			address = new ArrayList<Integer>(base.address);
		}
	}
	
	/**
	 * return string representation, e.g. 1.2.3
	 * @return string representation
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < address.size() - 1; i++) {
			result.append(address.get(i).toString());
			result.append(".");
		}

		result.append(address.get(address.size() - 1).toString());

		return result.toString();
	}

	/**
	 * adds new level in address tree
	 * (e.g. extends 1.2 to 1.2.1)
	 */
	public void extend() {
		address.add(Integer.valueOf(1));
	}

	/**
	 * increases the last (most bottom) level in address tree
	 * (e.g. increases 1.2 to 1.3)
	 */
	public void increase() {
		int i = address.remove(address.size() - 1);
		address.add(Integer.valueOf(i + 1));
	}

	@Override
	/**
	 * compares two TreeAddresses using their String representations
	 */
	public boolean equals(Object o) {
		return (o instanceof TreeAddress)
			&& ((TreeAddress) o).toString().equals(toString());
	}

	/**
	 * It is asserted that this method is never called. The implementation
	 * of {@link TreeAddress#equals(Object)}, however requests that one must
	 * override {@link Object#hashCode()}.
	 */
	@Override
	public int hashCode() {
		throw new AssertionError();
	}
}
