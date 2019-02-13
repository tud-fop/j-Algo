package org.jalgo.module.levenshtein.pattern;

public interface ToolbarObserver {

	public void performStep();
	
	public void performAllSteps();
	
	public void undoStep();
	
	public void undoAll();
	
}
