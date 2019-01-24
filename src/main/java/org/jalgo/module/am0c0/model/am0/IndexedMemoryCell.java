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

/**
 * This class represents a indexed memory cell which is used in the following
 * abstract machine element:<li>{@link Ram}</li>
 * 
 * @author Max Leuth&auml;user
 */
public class IndexedMemoryCell extends MemoryCell implements
		Comparable<IndexedMemoryCell> {
	private int index;

	public IndexedMemoryCell(int index, int value) {
		super(value);
		this.index = index;
	}

	/**
	 * @return the index for this cell
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Set a new index for this cell
	 * 
	 * @param index
	 */
	public void setIndex(final int index) {
		this.index = index;
	}

	@Override
	public int hashCode() {
		throw new AssertionError();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof IndexedMemoryCell
				&& ((IndexedMemoryCell) o).getIndex() == getIndex()
				&& ((IndexedMemoryCell) o).get() == get();
	}

	@Override
	public int compareTo(IndexedMemoryCell o) {
		return this.getIndex() - o.getIndex();
	}
}
