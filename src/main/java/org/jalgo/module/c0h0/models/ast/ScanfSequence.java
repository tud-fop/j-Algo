package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.ast.tools.Iterable;

/**
 * Sequence of all Scanf-calls made in the code
 *
 */
public class ScanfSequence extends Symbol implements Iterable {
	private ArrayList<Var> scanfs;

	public ScanfSequence() {
		scanfs = new ArrayList<Var>();
	}

	/**
	 * add Var which is to be added to the sequence
	 * @param s
	 * 		Var which is to be added to the sequence
	 */
	public void addScanf(Var s) {
		scanfs.add(s);
	}

	/**
	 * Returns the specific Var with the wanted index
	 * 
	 * @param index
	 * 			index of the wanted Var
	 * @return Var
	 * 			returns the Var
	 */
	public Var getScanfVar(int index) {
		return scanfs.get(index);
	}

	/**
	 * Returns all Vars which got a Scanf-Statement
	 * 
	 * @return scanfs
	 * 			ArrayList<Var> with all Variables in the sequence
	 */
	public ArrayList<Var> getScanfList() {
		return scanfs;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.tools.Iterable#getSequence()
	 */
	public ArrayList<Iterable> getSequence() {
		return new ArrayList<Iterable>(scanfs);
	}
}
