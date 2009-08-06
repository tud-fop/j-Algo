package org.jalgo.module.lambda.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Observer;
import java.util.Set;

import org.jalgo.module.lambda.model.EAvailability;

public interface IController {
	
	void run();
	
	void loadData(ObjectInputStream in);
	
	void saveData(ObjectOutputStream out);
	
	LambdaException validateInputString(String s);
	
	LambdaException processInputString(String s, Observer o);
	
	boolean eliminateShortcut();

	boolean eliminateAllShortcuts();
	
	boolean matchShortcut();
	
	boolean makeAllShortcuts();

	boolean doAlphaConversion(String subst);
	
	EAvailability doBetaReduction();
	
	String doStep();
	
	String doShortcutStep();
	
	String doAllSteps();
	
	boolean isNormalized();
	
	boolean isUndoStepPossible();
	
	boolean isRedoStepPossible();
	
	String undoStep(Observer o);
	
	String undoAllSteps(Observer o);
	
	String redoStep(Observer o);
	
	String redoAllSteps(Observer o);
	
	void selectTerm(String pos);
	
	Set<String> getAllUnusedVars();
	
	List<String> getFreeVars();
	
	List<String> getBoundVars();		
}
