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
