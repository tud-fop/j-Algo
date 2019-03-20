package org.jalgo.module.levenshtein.pattern;

public interface CellClickedObserver {

	public void cellClicked(int j, int i, boolean wasAlreadyFilled);
	
}
