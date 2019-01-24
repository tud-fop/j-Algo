package org.jalgo.module.c0h0.models.ast.tools;

import java.util.ArrayList;

/**
 * interface for iterable classes
 *
 */
public interface Iterable {
	/**
	 * returns the sequence
	 * 
	 * @return the sequence
	 */
	public ArrayList<Iterable> getSequence();
}
