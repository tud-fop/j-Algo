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
