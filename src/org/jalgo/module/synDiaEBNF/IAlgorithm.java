/*
 * Created on 05.05.2004
 */
 
package org.jalgo.module.synDiaEBNF;

/**
 * This interface provides basic functionalities for all algorithms. The goal was to develop a common interface especially
 * to facilitate flow control which is the major task of the ModuleController.
 * 
 * @author Michael Pradel
 * @author Marco Zimmerling
 */
public interface IAlgorithm{

	/** 
	 * Returns information about whether there exists a further step or not. 
	 * @return 	true in case of an existing further step, otherwise false
	 */
	public boolean hasNextStep();

	/** 
	 * Performes the next step in case there exists one. 
	 * @exception IndexOutOfBoundsException 	There exists no further step.
	 */
	public void performNextStep() throws IndexOutOfBoundsException;

	/**
	 * Returns information about whether there exists a previous step.
	 * @return	true in case of an existing previous step, otherwise false
	 */
	public boolean hasPreviousHistStep();

	/**
	 * Restores the previous situation which means to go one step back in history.
	 * @throws IndexOutOfBoundsException	There exists no previous step.
	 */
	public void previousHistStep() throws IndexOutOfBoundsException;

	/**
	 * Returns information about whether there exists a further history step. A return value of false indicates
	 * that there is no saved step an performNextStep() should be executed to go on. 
	 * @return	true in case of an existing history step, otherwise false
	 */
	public boolean hasNextHistStep();

	/**
	 * Restores the next situation which means to go on step further in history.
	 * @throws IndexOutOfBoundsException	There exists no further history step.
	 */
	public void nextHistStep() throws IndexOutOfBoundsException;
}

