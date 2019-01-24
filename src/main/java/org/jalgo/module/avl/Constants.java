/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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

/* Created on 05.05.2005 */
package org.jalgo.module.avl;

/**
 * The interface <code>Constants</code> is a collection of several constant
 * values used in the core implementation of the AVL module.
 * 
 * @author Ulrike Fischer, Alexander Claus
 */
public interface Constants {

	/*----------------------------valid key range----------------------------*/
	/** The minimum integer value a key can have. */
	public static final int MIN_KEY = 1;
	/** The maximum integer value a key can have. */
	public static final int MAX_KEY = 99;

	/*------------------------------return codes-----------------------------*/
	public static final int FOUND = 1;
	public static final int WORKING = 2;
	public static final int NOTFOUND = 4;
	public static final int LEFT = 8;
	public static final int RIGHT = 16;
	public static final int DONE = 32;
	public static final int ROTATE = 64;
	public static final int ROOT = 128;
	public static final int LASTUNDO = 256;
	public static final int DOUBLEROTATE = 512;
}