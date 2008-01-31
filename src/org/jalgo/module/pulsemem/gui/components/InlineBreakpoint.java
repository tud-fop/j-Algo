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

/* Created on 07.05.2007 */
package org.jalgo.module.pulsemem.gui.components;

import java.util.HashSet;
import java.io.Serializable;

/**
 * InlineBreakpoint.java
 * <p>
 * Stores the linenumbers of breakpoints the user set.
 * <p>
 *	Singleton-Pattern removed - Ebi
 *
 * @version $Revision: 1.5 $
 * @author Martin Brylski - TU Dresden, SWTP 2007
 */
public class InlineBreakpoint implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -400502530084377030L;

	/**
     * Stores the linnumbers wich are marked as breakpoints.
     */
    private HashSet<Integer> lineOfBreakpoint = new HashSet<Integer>();

    public InlineBreakpoint() {
        lineOfBreakpoint.add(0);
    }

    /**
     * @return the wohle Set <code>lineOfBreakpoint</code>
     */
    public HashSet<Integer> getLineOfBreakpoints() {
        return lineOfBreakpoint;
    }

    /**
     * Add a line to <code>lineOfBreakpoint</code>.
     *
     * @param lineOfBreakpoint
     *            the <code>lineOfBreakpoint</code> to set
     * @return wether it is added or not <br>
     *         in case of <code>false</code> it is already in set.
     */
    public boolean addLine(int line) {
        return this.lineOfBreakpoint.add(line);
    }

    /**
     * Checks wether the line is in the Set.
     *
     * @param line
     *            the linenumber to check.
     * @return true if the line is in the Set.
     */
    public boolean containsLine(int line) {
        return lineOfBreakpoint.contains(line);
    }

    /**
     * Removes a line from <code>lineOfBreakpoint</code>.
     *
     * @param lineOfBreakpoint
     *            the <code>lineOfBreakpoint</code> to remove
     * @return wether it is added or not <br>
     *         in case of <code>false</code> it is not in set.
     */
    public boolean removeLine(int line) {
        return this.lineOfBreakpoint.remove(line);

    }

    /**
     * Removes all breakpoints from the set.
     *
     */
    public void clear() {
    	lineOfBreakpoint.clear();
    }

    public String toString() {
    	return lineOfBreakpoint.toString();
    }
}
