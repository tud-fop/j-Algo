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
