/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 06.05.2004
 */

package org.jalgo.main.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Marco Zimmerling
 * @author Stephan Creutz
 * 
 * This class implements a stack with a wide range of functionalities in order to handle its elements.
 */
public class Stack implements Serializable {

	private LinkedList content;

	/**
	* default constructor, creates an empty stack 
	*/
	public Stack() {
		content = new LinkedList();
	}

	/**
	* @param c					elements to be initially put on the stack 
	* @param firstOnTop	indicates whether the first element of c will be the stack's top element (order)  
	* 
	* creates a stack which initially holds the elements passed by c  
	*/
	public Stack(Collection c, boolean firstOnTop) {
		content = new LinkedList(c);
		
		if (!firstOnTop)
			Collections.reverse(content);
	}
	
	/**
	 * @param item	element to put on the stack
	 * 
	 * puts item on the stack, thus it'll be the new top element
	 */
	public void push(Object item) {
		content.addFirst(item);				//content[0] is top element
	}

	/**
	 * @return	current top element or null in case of an empty stack
	 * 
	 * removes and returns the current top element
	 */
	public Object pop() {
		if (content.size() > 0)
			return content.removeFirst();
		
		return null;
	}

	/**
	 * @return Current top element or null in case of an empty stack.
	 * 
	 * It merely returns the current top element.
	 */
	public Object peak() {
		if (content.size() > 0)
			return content.getFirst();
		
		return null;
	}

	/**
	* @param c		new elements
	* 
	* Replaces the current elements with those of c.  
	*/
	public void setContent(Collection c) {
		content.clear();
		content.addAll(c);	
	}
		
	/**
	 * @return	a LinkedList containing the stack's elements    
	 * 
	 * Returns a LinkedList of the present stack's elements where the first element is the top element.           
	 */
	public LinkedList getContent() {
		return content;
	}

	/**
	* removes all elements
	*/
	public void clear() {
		content.clear();
	}

	/**
	 * @return	a boolean indicating wether the stack is empty
	 */
	public boolean isEmpty() {
		return content.isEmpty();
	}

	/**
	 * @return	number of elements on the stack
	 */
	public int size() {
		return content.size();
	}

	/**
	 * @param item	element whose position should be searched out
	 * @return			position of item from the top (top = 0), -1 in case of an unsuccessful search
	 * 
	 * Tries to search out the first occurrence of item. In case of a successful execution its position from the 
	 * top is returned. A return value of 0 indicates the top element. If item is not detectable, -1 will be returned.  
	 */
	public int search(Object item) {
		return content.indexOf(item);
	}

	/**
	 * @return	a string of the stack's elements
	 * 
	 * Returns a string of all present elements on the stack starting from the top.
	 * Note: will make only sence if the elements also provide a 'suitable' toString()-method  
	 */
	public String toString() {
		if (content.size() == 0)
			return new String();
			 
		String str = new String();
		ListIterator it = content.listIterator();
		
		str += it.toString();
		while (it.hasNext())
			str += it.next().toString();
		
		return str;		
	}
}