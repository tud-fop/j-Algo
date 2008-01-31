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

/* Created on 18.06.2005 */
package org.jalgo.module.avl.gui;

/**
 * The interface <code>DisplayModeChangeable</code> has to be implemented by
 * all Components, whose display state should be dependent from a display mode
 * setting. Such setting could be a presentation mode for working on a beamer or
 * a normal display mode for working on a pc.<br>
 * Components, which implement this interface have to register themselves at the
 * instance, which controls the display mode setting, as observers.
 * 
 * @author Alexander Claus
 */
public interface DisplayModeChangeable {

	/**
	 * Invoked, if the display mode has changed.
	 */
	public void displayModeChanged();
}