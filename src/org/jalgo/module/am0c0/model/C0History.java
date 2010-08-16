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
import java.util.ListIterator;

public class C0History<T> {
	private ListIterator <T>iterator;
	private List<T> list;
	
	public C0History(){
		list = new ArrayList<T>();
		iterator = list.listIterator();
	}
	
	public boolean hasNext() {
		return iterator.hasNext();
	}
	
	public T next() {
		return (T) iterator.next();
	}
	
	public boolean hasPrevious() {
		return iterator.hasPrevious();
	}
	
	public T previous() {
		return (T) iterator.previous();
	}
	
	public int getCount() {
		return list.size();
	}
	
	public int getCurrentIndex() {
		return iterator.nextIndex() - 1;
	}
	
	public void add(T entity) {
		// add the entity between the last returned element and the next element
		iterator.add(entity);
		
		// remove all following elements
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}
	
	public void remove(int index) {
		
	}
}
