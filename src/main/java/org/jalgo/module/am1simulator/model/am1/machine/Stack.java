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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a stack which is used in a {@link MachineConfiguration}
 * .
 * 
 * @author Max Leuth&auml;user
 */
public class Stack {
	private List<MemoryCell> values;

	/**
	 * Create a new empty stack.
	 */
	public Stack() {
		values = new LinkedList<MemoryCell>();
	}

	/**
	 * Copyconstructor
	 * 
	 * @param i
	 *            {@link Stack} which should be copied.
	 */
	public Stack(Stack i) {
		values = new LinkedList<MemoryCell>();

		for (MemoryCell c : i.getStackAsList()) {
			push(c.get());
		}
	}

	/**
	 * Create a new stack based on an already existing and filled list of
	 * values.
	 * 
	 * @param values
	 *            {@link LinkedList} which already holds some values.
	 */
	public Stack(final List<Integer> values) {
		this.values = new LinkedList<MemoryCell>();
		for (int i : values) {
			push(i);
		}
	}

	/**
	 * Adds a single element at the top of the stack.
	 * 
	 * @param value
	 */
	public void push(final int value) {
		values.add(new MemoryCell(value));
	}

	/**
	 * @return the current element from the top of the stack
	 */
	public int pop() {
		return values.remove(values.size() - 1).get();
	}

	/**
	 * @return the stack as list.
	 */
	public List<MemoryCell> getStackAsList() {
		return this.values;
	}

	/**
	 * @return the stack as String
	 */
	@Override
	public String toString() {
		if (values.isEmpty()) {
			return "Ɛ";
		}
		StringBuilder result = new StringBuilder();
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (MemoryCell i : values) {
			l.add(i.get());
		}
		Collections.reverse(l);
		for (int s : l) {
			result.append(s);
			result.append(":");
		}
		return result.toString().substring(0, result.length() - 1);
	}
}
