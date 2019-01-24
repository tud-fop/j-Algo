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

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

/**
 * Class which represents the RAM in an abstract machine.
 * 
 * @author Max Leuth&auml;user
 */
public class Ram {
	private List<IndexedMemoryCell> values;

	/**
	 * Create a new empty ram.
	 */
	public Ram() {
		values = new LinkedList<IndexedMemoryCell>();
	}

	/**
	 * Copyconstructor
	 * 
	 * @param i
	 *            {@link Ram} which should be copied.
	 */
	public Ram(Ram i) {
		values = new LinkedList<IndexedMemoryCell>();

		for (IndexedMemoryCell c : i.getRamAsList()) {
			store(c.getIndex(), c.get());
		}
	}

	/**
	 * Create a new ram based on an already existing and filled map of values.
	 * 
	 * @param values
	 *            {@link Map} which already holds some values.
	 */
	public Ram(final Map<Integer, Integer> values) {
		this.values = new LinkedList<IndexedMemoryCell>();
		for (int i : values.keySet()) {
			store(i, values.get(i));
		}
	}

	/**
	 * Store a new entry in the ram.
	 * 
	 * @param index
	 * @param value
	 */
	public void store(final int index, final int value) {
		for (IndexedMemoryCell i : values) {
			if (i.getIndex() == index) {
				i.set(value);
				return;
			}
		}
		values.add(new IndexedMemoryCell(index, value));
	}

	/**
	 * Returns the value at this index in the ram.
	 * 
	 * @param index
	 * @return the value for the given index, <b>-1</b> if there is no entry at
	 *         this index.
	 */
	public int load(final int index) {
		for (IndexedMemoryCell i : values) {
			if (i.getIndex() == index) {
				return i.get();
			}
		}
		return -1;
	}

	/**
	 * @return the ram as list.
	 */
	public List<IndexedMemoryCell> getRamAsList() {
		return this.values;
	}

	public boolean cellIsDefined(int address) {
		for (IndexedMemoryCell i : values) {
			if (i.getIndex() == address) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the ram as String
	 */
	@Override
	public String toString() {
		if (values.isEmpty()) {
			char o = '\u00D8';
			return "<html>h<sub>" + o + "</sub>= [ ]";
		}
		Collections.sort(values);
		StringBuilder result = new StringBuilder();
		result.append("[");
		for (IndexedMemoryCell i : values) {
			result.append("," + i.getIndex() + "/" + i.get());
		}
		return result.toString().replaceFirst(",", "") + "]";
	}

}
