/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * Created on 10.06.2004
 */
 
package org.jalgo.main.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Stephan Creutz
 */
public class Sets {
	/**
	 * proves whether two sets are disjoint
	 * @param set1
	 * @param set2
	 * @return true if the two sets are disjoint
	 */
	public static boolean disjoint(Set set1, Set set2) {
		Iterator it = set2.iterator();
		while (it.hasNext()) {
			if (set1.contains(it.next())) {
				return false;
			}
		}
		return true;
	}

	public static Object search(Set set, Object o) {
		for (Iterator it = set.iterator(); it.hasNext();) {
			Object setObject = it.next();
			if (o.equals(setObject)) {
				return setObject;
			}
		}
		return null;
	}
	
	public static Set union(Set set1, Set set2) {
		Set set = new HashSet();
		set.addAll(set1);
		set.addAll(set2);
		return set;
	}
}
